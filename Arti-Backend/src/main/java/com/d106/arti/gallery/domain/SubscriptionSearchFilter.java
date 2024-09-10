package com.d106.arti.gallery.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionSearchFilter {
    private Integer memberId;
    private Integer galleryId;
//    private Boolean isDeleted;
//    private Boolean isSubscribed;
}
