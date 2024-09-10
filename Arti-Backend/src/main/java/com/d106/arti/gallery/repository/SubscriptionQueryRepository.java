package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Subscription;
import com.d106.arti.gallery.domain.SubscriptionSearchFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

public class SubscriptionQueryRepository {
    private final JPAQueryFactory query;
    public SubscriptionQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em); // EntityManager를 사용하여 쿼리 팩토리 생성
    }
}

/**
 * DB에서 조건에 맞는 Conference들을 검색하고 해당하는 객체 리스트를 반환합니다.
 * @param filter DB 검색에 사용되는 필터 객체
 * @return 조건에 맞는 Conference 리스트를 반환합니다.
 */
public List<Subscription> getSubscriptionList(SubscriptionSearchFilter filter) {
    return query.select(subscription)
            .from(subscription)
            .where(
                    eqMemberId(filter.getMemberId()),
                    eqGalleryId(filter.getGalleryId())
            )
            .fetch();
    }

    BooleanExpression eqMemberId(Long memberId) {
        return memberId != null ? QSubscription.subscription.memberId.eq(memberId) : null;
    }

    BooleanExpression eqGalleryId(Long galleryId) {
        return galleryId != null ? QSubscription.subscription.galleryId.eq(galleryId) : null;
    }