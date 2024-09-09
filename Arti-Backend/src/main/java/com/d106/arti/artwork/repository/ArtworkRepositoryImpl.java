package com.d106.arti.artwork.repository;

import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.domain.QNormalArtWork;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<NormalArtWork> search(String keyword) {
        QNormalArtWork normalArtWork = QNormalArtWork.normalArtWork;

        return jpaQueryFactory.selectFrom(normalArtWork)
                .where(containsKeyword(keyword))
                .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return QNormalArtWork.normalArtWork.title.containsIgnoreCase(keyword)
                .or(QNormalArtWork.normalArtWork.description.containsIgnoreCase(keyword));
    }
}
