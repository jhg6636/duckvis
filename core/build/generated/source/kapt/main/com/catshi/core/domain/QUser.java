package com.catshi.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -431953815L;

    public static final QUser user = new QUser("user");

    public final EnumPath<com.catshi.core.types.CityType> city = createEnum("city", com.catshi.core.types.CityType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath multiTeam = createString("multiTeam");

    public final StringPath name = createString("name");

    public final EnumPath<com.catshi.core.types.UserPathType> path = createEnum("path", com.catshi.core.types.UserPathType.class);

    public final NumberPath<Long> teamId = createNumber("teamId", Long.class);

    public final StringPath userCode = createString("userCode");

    public final EnumPath<com.catshi.core.types.UserLevelType> userLevel = createEnum("userLevel", com.catshi.core.types.UserLevelType.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

