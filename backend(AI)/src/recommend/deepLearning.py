import os
import torch
import torch.nn as nn
import torch.nn.functional as F

os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"

class SEBlock(nn.Module):
    def __init__(self, in_channels, reduce_ratio=4):
        super(SEBlock, self).__init__()
        reduced_channels = in_channels // reduce_ratio
        self.fc1 = nn.Conv2d(in_channels, reduced_channels, kernel_size=1)
        self.fc2 = nn.Conv2d(reduced_channels, in_channels, kernel_size=1)

    def forward(self, x):
        se = F.adaptive_avg_pool2d(x, 1)
        se = torch.relu(self.fc1(se))
        se = torch.sigmoid(self.fc2(se))
        return x * se


class MBConv(nn.Module):
    def __init__(self, in_channels, out_channels, expansion, stride, se_ratio=0.25):
        super(MBConv, self).__init__()
        self.use_residual = stride == 1 and in_channels == out_channels
        mid_channels = in_channels * expansion

        self.expand_conv = nn.Conv2d(in_channels, mid_channels, kernel_size=1, bias=False)
        self.bn1 = nn.BatchNorm2d(mid_channels)
        self.deptwise_conv = nn.Conv2d(mid_channels, mid_channels, kernel_size=3, stride=stride, padding=1, groups=mid_channels, bias=False)
        self.bn2 = nn.BatchNorm2d(mid_channels)
        self.se = SEBlock(mid_channels, reduce_ratio=int(1 / se_ratio))
        self.project_conv = nn.Conv2d(mid_channels, out_channels, kernel_size=1, bias=False)
        self.bn3 = nn.BatchNorm2d(out_channels)
        self.act = nn.SiLU()

    def forward(self, x):
        residual = x
        x = self.act(self.bn1(self.expand_conv(x)))
        x = self.act(self.bn2(self.deptwise_conv(x)))
        x = self.se(x)
        x = self.bn3(self.project_conv(x))
        if self.use_residual:
            x += residual
        return x


class FusedMBConv(nn.Module):
    def __init__(self, in_channels, out_channels, expansion, stride):
        super(FusedMBConv, self).__init__()
        self.use_residual = stride == 1 and in_channels == out_channels
        mid_channels = in_channels * expansion

        self.expand_conv = nn.Conv2d(in_channels, mid_channels, kernel_size=3, stride=stride, padding=1, bias=False) if expansion != 1 else None
        self.bn1 = nn.BatchNorm2d(mid_channels if expansion != 1 else in_channels)
        self.project_conv = nn.Conv2d(mid_channels if expansion != 1 else in_channels, out_channels, kernel_size=1 if expansion != 1 else 3, stride=1, padding=1 if expansion == 1 else 0, bias=False)
        self.bn2 = nn.BatchNorm2d(out_channels)
        self.act = nn.SiLU()

    def forward(self, x):
        residual = x
        if self.expand_conv:
            x = self.act(self.bn1(self.expand_conv(x)))
        else:
            x = self.act(self.bn1(x))
        x = self.bn2(self.project_conv(x))
        if self.use_residual:
            x += residual
        return x


class EfficientNetV2(nn.Module):
    def __init__(self, num_classes=27):
        super(EfficientNetV2, self).__init__()

        self.stem = nn.Sequential(
            nn.Conv2d(3, 24, kernel_size=3, stride=2, padding=1, bias=False),
            nn.BatchNorm2d(24),
            nn.SiLU()
        )
        self.block_config = [
            (FusedMBConv, 2, 24, 24, 1, 1),
            (FusedMBConv, 4, 24, 48, 4, 2),
            (FusedMBConv, 4, 48, 64, 4, 2),
            (MBConv, 6, 64, 128, 4, 2),
            (MBConv, 9, 128, 160, 6, 1),
            (MBConv, 15, 160, 256, 6, 2)
        ]

        layers = []
        for block, repeats, in_channels, out_channels, expansion, stride in self.block_config:
            for i in range(repeats):
                if i == 0:
                    layers.append(block(in_channels, out_channels, expansion, stride))
                else:
                    layers.append(block(out_channels, out_channels, expansion, 1))
        self.blocks = nn.Sequential(*layers)

        self.head = nn.Sequential(
            nn.Conv2d(256, 1280, kernel_size=1, bias=False),
            nn.BatchNorm2d(1280),
            nn.SiLU(),
            nn.AdaptiveAvgPool2d(1),
            nn.Flatten(),
            nn.Linear(1280, num_classes)
        )

    def forward(self, x):
        x = self.stem(x)
        x = self.blocks(x)
        x = self.head(x)
        return x