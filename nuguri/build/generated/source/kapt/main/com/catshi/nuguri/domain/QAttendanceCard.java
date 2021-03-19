package com.catshi.nuguri.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAttendanceCard is a Querydsl query type for AttendanceCard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAttendanceCard extends EntityPathBase<AttendanceCard> {

    private static final long serialVersionUID = 177404548L;

    public static final QAttendanceCard attendanceCard = new QAttendanceCard("attendanceCard");

    public final DateTimePath<java.time.LocalDateTime> createdDateTime = createDateTime("createdDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> durationSeconds = createNumber("durationSeconds", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> loginDateTime = createDateTime("loginDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> logoutDateTime = createDateTime("logoutDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final EnumPath<AttendanceType> type = createEnum("type", AttendanceType.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAttendanceCard(String variable) {
        super(AttendanceCard.class, forVariable(variable));
    }

    public QAttendanceCard(Path<? extends AttendanceCard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttendanceCard(PathMetadata metadata) {
        super(AttendanceCard.class, metadata);
    }

}

