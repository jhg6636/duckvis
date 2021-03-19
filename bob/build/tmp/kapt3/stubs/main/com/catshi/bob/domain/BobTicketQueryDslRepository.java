package com.catshi.bob.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0017J\u001a\u0010\n\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tH\u0017J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\tH\u0017J\u0010\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\tH\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/catshi/bob/domain/BobTicketQueryDslRepository;", "", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "findThisMealAllTickets", "", "Lcom/catshi/bob/domain/BobTicket;", "mealType", "Lcom/catshi/bob/types/BobTimeType;", "getThisMealTicket", "userId", "", "thisMeal", "hasMoreThanOneTicket", "", "isFirst", "bob"})
@org.springframework.stereotype.Component
public class BobTicketQueryDslRepository {
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    @org.jetbrains.annotations.Nullable
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.BobTicket getThisMealTicket(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType thisMeal) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.bob.domain.BobTicket> findThisMealAllTickets(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType mealType) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional
    public boolean isFirst(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType mealType) {
        return false;
    }
    
    @org.springframework.transaction.annotation.Transactional
    public boolean hasMoreThanOneTicket(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType mealType) {
        return false;
    }
    
    public BobTicketQueryDslRepository(@org.jetbrains.annotations.NotNull
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
}