package com.catshi.bob.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBobTicket is a Querydsl query type for BobTicket
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBobTicket extends EntityPathBase<BobTicket> {

    private static final long serialVersionUID = 548107091L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBobTicket bobTicket = new QBobTicket("bobTicket");

    public final EnumPath<com.catshi.bob.types.BobStyleType> bobStyle = createEnum("bobStyle", com.catshi.bob.types.BobStyleType.class);

    public final EnumPath<com.catshi.bob.types.BobTimeType> bobTimeType = createEnum("bobTimeType", com.catshi.bob.types.BobTimeType.class);

    public final EnumPath<com.catshi.core.types.CityType> city = createEnum("city", com.catshi.core.types.CityType.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.catshi.bob.types.IssuedOrderType> issuedOrder = createEnum("issuedOrder", com.catshi.bob.types.IssuedOrderType.class);

    public final TimePath<java.time.LocalTime> time = createTime("time", java.time.LocalTime.class);

    public final com.catshi.core.domain.QUser user;

    public QBobTicket(String variable) {
        this(BobTicket.class, forVariable(variable), INITS);
    }

    public QBobTicket(Path<? extends BobTicket> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBobTicket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBobTicket(PathMetadata metadata, PathInits inits) {
        this(BobTicket.class, metadata, inits);
    }

    public QBobTicket(Class<? extends BobTicket> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.catshi.core.domain.QUser(forProperty("user")) : null;
    }

}

