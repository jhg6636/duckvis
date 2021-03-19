package com.catshi.nuguri.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J&\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/catshi/nuguri/domain/AttendanceCardQueryDslRepository;", "", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "getAllMistakeCards", "", "Lcom/catshi/nuguri/domain/AttendanceCard;", "getMyAttendanceCardsBetween", "userId", "", "startTime", "Ljava/time/LocalDateTime;", "endTime", "getMyWorkingCard", "getNowWorkingCards", "nuguri"})
@org.springframework.stereotype.Component
public class AttendanceCardQueryDslRepository {
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    @org.jetbrains.annotations.Nullable
    public com.catshi.nuguri.domain.AttendanceCard getMyWorkingCard(long userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.catshi.nuguri.domain.AttendanceCard> getMyAttendanceCardsBetween(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime startTime, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime endTime) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.catshi.nuguri.domain.AttendanceCard> getAllMistakeCards() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.catshi.nuguri.domain.AttendanceCard> getNowWorkingCards() {
        return null;
    }
    
    public AttendanceCardQueryDslRepository(@org.jetbrains.annotations.NotNull
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
}