package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013J$\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J$\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J&\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u000b0\u000b*\b\u0012\u0004\u0012\u00020\u000e0\u000b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u0012\u0010\b\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005\u00a8\u0006\u0014"}, d2 = {"Lcom/catshi/bob/services/TeamDecisionLogic;", "", "chunkSize", "", "getChunkSize", "()I", "minimumCount", "getMinimumCount", "minimumTeamCount", "getMinimumTeamCount", "decide", "", "Lcom/catshi/bob/domain/BobTeam;", "tickets", "Lcom/catshi/bob/domain/BobTicket;", "teamSortStrategy", "Lcom/catshi/bob/services/TeamSortStrategy;", "getResult", "chunkedForTeams", "Companion", "bob"})
public abstract interface TeamDecisionLogic {
    @org.jetbrains.annotations.NotNull
    public static final com.catshi.bob.services.TeamDecisionLogic.Companion Companion = null;
    public static final int BIG_BIG_BIG_NUMBER = 1000;
    
    /**
     * 충분히 많이 시도해봤는데도, 팀이 안나오면 확률적으로 팀이 매칭될 수 없다고 보고
     * 예외를 날린다.
     */
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.bob.domain.BobTeam> decide(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.BobTicket> tickets, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamSortStrategy teamSortStrategy);
    
    public abstract int getMinimumCount();
    
    public abstract int getMinimumTeamCount();
    
    public abstract int getChunkSize();
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
        
        /**
         * 충분히 많이 시도해봤는데도, 팀이 안나오면 확률적으로 팀이 매칭될 수 없다고 보고
         * 예외를 날린다.
         */
        @org.jetbrains.annotations.NotNull
        public static java.util.List<com.catshi.bob.domain.BobTeam> decide(@org.jetbrains.annotations.NotNull
        com.catshi.bob.services.TeamDecisionLogic $this, @org.jetbrains.annotations.NotNull
        java.util.List<? extends com.catshi.bob.domain.BobTicket> tickets, @org.jetbrains.annotations.NotNull
        com.catshi.bob.services.TeamSortStrategy teamSortStrategy) {
            return null;
        }
        
        private static java.util.List<com.catshi.bob.domain.BobTeam> getResult(com.catshi.bob.services.TeamDecisionLogic $this, java.util.List<? extends com.catshi.bob.domain.BobTicket> tickets, com.catshi.bob.services.TeamSortStrategy teamSortStrategy) {
            return null;
        }
        
        private static java.util.List<java.util.List<com.catshi.bob.domain.BobTicket>> chunkedForTeams(com.catshi.bob.services.TeamDecisionLogic $this, java.util.List<? extends com.catshi.bob.domain.BobTicket> $this$chunkedForTeams, int chunkSize) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/catshi/bob/services/TeamDecisionLogic$Companion;", "", "()V", "BIG_BIG_BIG_NUMBER", "", "bob"})
    public static final class Companion {
        public static final int BIG_BIG_BIG_NUMBER = 1000;
        
        private Companion() {
            super();
        }
    }
}