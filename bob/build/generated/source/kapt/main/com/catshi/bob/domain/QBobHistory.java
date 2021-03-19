package com.catshi.bob.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBobHistory is a Querydsl query type for BobHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBobHistory extends EntityPathBase<BobHistory> {

    private static final long serialVersionUID = 2061362477L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBobHistory bobHistory = new QBobHistory("bobHistory");

    public final NumberPath<Integer> bobTeamNumber = createNumber("bobTeamNumber", Integer.class);

    public final QBobTicket bobTicket;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBobLeader = createBoolean("isBobLeader");

    public QBobHistory(String variable) {
        this(BobHistory.class, forVariable(variable), INITS);
    }

    public QBobHistory(Path<? extends BobHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBobHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBobHistory(PathMetadata metadata, PathInits inits) {
        this(BobHistory.class, metadata, inits);
    }

    public QBobHistory(Class<? extends BobHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bobTicket = inits.isInitialized("bobTicket") ? new QBobTicket(forProperty("bobTicket"), inits.get("bobTicket")) : null;
    }

}

