package com.d106.arti.artwork;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QArtwork is a Querydsl query type for Artwork
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArtwork extends EntityPathBase<Artwork> {

    private static final long serialVersionUID = 298815008L;

    public static final QArtwork artwork = new QArtwork("artwork");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath img_url = createString("img_url");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QArtwork(String variable) {
        super(Artwork.class, forVariable(variable));
    }

    public QArtwork(Path<? extends Artwork> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArtwork(PathMetadata metadata) {
        super(Artwork.class, metadata);
    }

}

