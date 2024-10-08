package com.d106.arti.instagram.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonResponse {

    private List<Media> data;
    private Paging paging;
}
