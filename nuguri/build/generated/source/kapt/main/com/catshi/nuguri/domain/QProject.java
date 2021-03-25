package com.catshi.nuguri.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.duckvis.nuguri.domain.Project;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = 696674446L;

    public static final QProject project = new QProject("project");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isFinished = createBoolean("isFinished");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}

