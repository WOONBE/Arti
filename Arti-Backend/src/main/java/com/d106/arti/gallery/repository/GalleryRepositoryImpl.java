//package com.d106.arti.gallery.repository;
//
//import com.d106.arti.gallery.domain.Gallery;
//import com.d106.arti.gallery.domain.QGallery;
//import com.d106.arti.gallery.domain.QTheme;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class GalleryRepositoryImpl implements GalleryRepositoryCustom {
//
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @Override
//    public List<Gallery> search(String keyword) {
//        QGallery gallery = QGallery.gallery;
//        QTheme theme = QTheme.theme;  // QTheme 사용하여 Theme 엔티티 조인
//
//        return jpaQueryFactory.selectFrom(gallery)
//                .leftJoin(gallery.themes, theme)  // themes 필드를 통해 Theme과 조인
//                .where(containsKeyword(keyword, gallery, theme))
//                .fetch();
//    }
//
//    private BooleanExpression containsKeyword(String keyword, QGallery gallery, QTheme theme) {
//        if (keyword == null || keyword.isEmpty()) {
//            return null;
//        }
//        return gallery.name.containsIgnoreCase(keyword)
//                .or(gallery.description.containsIgnoreCase(keyword))
//                .or(theme.name.containsIgnoreCase(keyword));  // Theme의 name 필드를 사용하여 검색
//    }
//}
