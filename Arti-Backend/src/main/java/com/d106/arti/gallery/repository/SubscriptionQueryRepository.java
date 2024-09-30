//package com.d106.arti.gallery.repository;
//
//import com.d106.arti.gallery.domain.QSubscription;
//import com.d106.arti.gallery.domain.Subscription;
//import com.d106.arti.gallery.domain.SubscriptionSearchFilter;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class SubscriptionQueryRepository {
//
//    private final JPAQueryFactory query;
//
//    public SubscriptionQueryRepository(EntityManager em) {
//        this.query = new JPAQueryFactory(em);
//    }
//
//    public List<Subscription> getSubscriptionList(SubscriptionSearchFilter filter) {
//        return query.select(QSubscription.subscription)
//                .from(QSubscription.subscription)
//                .where(
//                        eqMemberId(filter.getMemberId()),
//                        eqGalleryId(filter.getGalleryId())
//                )
//                .fetch();
//    }
//
//    private BooleanExpression eqMemberId(Integer memberId) {
//        return memberId != null ? QSubscription.subscription.id.memberId.eq(memberId) : null;
//    }
//
//    private BooleanExpression eqGalleryId(Integer galleryId) {
//        return galleryId != null ? QSubscription.subscription.id.galleryId.eq(galleryId) : null;
//    }
//}
