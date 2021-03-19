package com.catshi.bob.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u0011\u0010\u0006\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u0014\u0010\t\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\bR\u0011\u0010\n\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0012"}, d2 = {"Lcom/catshi/bob/domain/BobTeam;", "", "tickets", "", "Lcom/catshi/bob/domain/BobTicket;", "(Ljava/util/List;)V", "isAvailable", "", "()Z", "isNotAvailable", "leaderTicket", "getLeaderTicket", "()Lcom/catshi/bob/domain/BobTicket;", "memberTickets", "getMemberTickets", "()Ljava/util/List;", "toString", "", "bob"})
public final class BobTeam {
    @org.jetbrains.annotations.NotNull
    private final com.catshi.bob.domain.BobTicket leaderTicket = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.catshi.bob.domain.BobTicket> memberTickets = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.catshi.bob.domain.BobTicket getLeaderTicket() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.catshi.bob.domain.BobTicket> getMemberTickets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    private final boolean isNotAvailable() {
        return false;
    }
    
    public final boolean isAvailable() {
        return false;
    }
    
    public BobTeam(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.BobTicket> tickets) {
        super();
    }
}