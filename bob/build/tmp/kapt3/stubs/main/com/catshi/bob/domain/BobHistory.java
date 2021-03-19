package com.catshi.bob.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\b\b\u0017\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u001a\u0010\u0006\u001a\u00020\u0007X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0016\u0010\u0002\u001a\u00020\u00038\u0016X\u0097\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0004\u001a\u00020\u0005X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0015\"\u0004\b\u0016\u0010\u0017\u00a8\u0006\u0018"}, d2 = {"Lcom/catshi/bob/domain/BobHistory;", "", "bobTicket", "Lcom/catshi/bob/domain/BobTicket;", "isBobLeader", "", "bobTeamNumber", "", "(Lcom/catshi/bob/domain/BobTicket;ZI)V", "getBobTeamNumber", "()I", "setBobTeamNumber", "(I)V", "getBobTicket", "()Lcom/catshi/bob/domain/BobTicket;", "id", "", "getId", "()J", "setId", "(J)V", "()Z", "setBobLeader", "(Z)V", "bob"})
@javax.persistence.Entity
public class BobHistory {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Id
    private long id = -1L;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.JoinColumn(nullable = false)
    @javax.persistence.OneToOne(fetch = javax.persistence.FetchType.LAZY)
    private final com.catshi.bob.domain.BobTicket bobTicket = null;
    private boolean isBobLeader;
    private int bobTeamNumber;
    
    public long getId() {
        return 0L;
    }
    
    public void setId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.domain.BobTicket getBobTicket() {
        return null;
    }
    
    public boolean isBobLeader() {
        return false;
    }
    
    public void setBobLeader(boolean p0) {
    }
    
    public int getBobTeamNumber() {
        return 0;
    }
    
    public void setBobTeamNumber(int p0) {
    }
    
    public BobHistory(@org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobTicket bobTicket, boolean isBobLeader, int bobTeamNumber) {
        super();
    }
}