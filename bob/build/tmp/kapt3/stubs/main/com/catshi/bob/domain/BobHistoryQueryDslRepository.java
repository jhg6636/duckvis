package com.catshi.bob.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0017J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\rH\u0017J\u0016\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\n\u001a\u00020\tH\u0017R\u0014\u0010\u0005\u001a\u00020\u00068RX\u0092\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/catshi/bob/domain/BobHistoryQueryDslRepository;", "", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "isThisMonth", "Lcom/querydsl/core/types/dsl/BooleanExpression;", "()Lcom/querydsl/core/types/dsl/BooleanExpression;", "bobLeaderCount", "", "userId", "countThisMealHistory", "bobTimeType", "Lcom/catshi/bob/types/BobTimeType;", "teammates", "", "Lcom/catshi/core/domain/User;", "bob"})
@org.springframework.stereotype.Component
public class BobHistoryQueryDslRepository {
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    @org.springframework.transaction.annotation.Transactional
    public long bobLeaderCount(long userId) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.core.domain.User> teammates(long userId) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional
    public long countThisMealHistory(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType bobTimeType) {
        return 0L;
    }
    
    private com.querydsl.core.types.dsl.BooleanExpression isThisMonth() {
        return null;
    }
    
    public BobHistoryQueryDslRepository(@org.jetbrains.annotations.NotNull
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
}