package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0017R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\t"}, d2 = {"Lcom/catshi/bob/services/TicketDao;", "", "bobTicketRepository", "Lcom/catshi/bob/domain/BobTicketRepository;", "(Lcom/catshi/bob/domain/BobTicketRepository;)V", "getBobTicketRepository", "()Lcom/catshi/bob/domain/BobTicketRepository;", "isFirst", "", "bob"})
@org.springframework.stereotype.Service
public class TicketDao {
    @org.jetbrains.annotations.NotNull
    private final com.catshi.bob.domain.BobTicketRepository bobTicketRepository = null;
    
    @org.springframework.transaction.annotation.Transactional
    public boolean isFirst() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.domain.BobTicketRepository getBobTicketRepository() {
        return null;
    }
    
    public TicketDao(@org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobTicketRepository bobTicketRepository) {
        super();
    }
}