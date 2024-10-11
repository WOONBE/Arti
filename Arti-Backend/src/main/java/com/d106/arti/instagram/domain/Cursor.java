package com.d106.arti.instagram.domain;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class Cursor {

    private String before;
    private String after;
}
