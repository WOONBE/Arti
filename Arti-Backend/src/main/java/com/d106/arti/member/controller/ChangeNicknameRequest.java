package com.d106.arti.member.controller;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeNicknameRequest {
    private String newNickname;
}
