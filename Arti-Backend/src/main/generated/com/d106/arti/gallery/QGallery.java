package com.d106.arti.gallery;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.d106.arti.gallery.domain.Gallery;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGallery is a Querydsl query type for Gallery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGallery extends EntityPathBase<Gallery> {

    private static final long serialVersionUID = -818180388L;

    public static final QGallery gallery = new QGallery("gallery");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> view = createNumber("view", Integer.class);

//    // Member 엔티티와의 ManyToOne 관계인 owner 필드를 추가
//    public final EntityPathBase<Member> owner = createEntity("owner", Member.class);

    public QGallery(String variable) {
        super(Gallery.class, forVariable(variable));
    }

    public QGallery(Path<? extends Gallery> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGallery(PathMetadata metadata) {
        super(Gallery.class, metadata);
    }

}

