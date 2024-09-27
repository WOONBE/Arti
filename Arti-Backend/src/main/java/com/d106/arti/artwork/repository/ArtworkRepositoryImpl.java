package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.domain.QNormalArtWork;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArtworkRepositoryImpl extends QuerydslRepositorySupport implements ArtworkRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    public ArtworkRepositoryImpl() {
        super(NormalArtWork.class);
    }

//    @Override
//    public List<NormalArtWork> search(String keyword) {
//        QNormalArtWork normalArtWork = QNormalArtWork.normalArtWork;
//
//        return jpaQueryFactory.selectFrom(normalArtWork)
//                .where(containsKeyword(keyword))
//                .fetch();
//    }

    @Override
    public Page<NormalArtWork> search(String keyword, Pageable pageable) {
        QNormalArtWork normalArtWork = QNormalArtWork.normalArtWork;

        // Fetch the filtered list of artworks with pagination
        List<NormalArtWork> artworks = jpaQueryFactory.selectFrom(normalArtWork)
            .where(containsKeyword(keyword))
            .offset(pageable.getOffset()) // Starting point for the page
            .limit(pageable.getPageSize()) // Size of the page
            .fetch();

        // Fetch total count of artworks for pagination
        long totalCount = jpaQueryFactory.selectFrom(normalArtWork)
            .where(containsKeyword(keyword))
            .fetchCount();

        return new PageImpl<>(artworks, pageable, totalCount);
    }


    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return QNormalArtWork.normalArtWork.title.containsIgnoreCase(keyword)
                .or(QNormalArtWork.normalArtWork.description.containsIgnoreCase(keyword));
    }
}
