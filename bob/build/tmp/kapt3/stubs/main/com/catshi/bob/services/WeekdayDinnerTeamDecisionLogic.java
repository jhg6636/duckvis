package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006\u00a8\u0006\u000b"}, d2 = {"Lcom/catshi/bob/services/WeekdayDinnerTeamDecisionLogic;", "Lcom/catshi/bob/services/TeamDecisionLogic;", "()V", "chunkSize", "", "getChunkSize", "()I", "minimumCount", "getMinimumCount", "minimumTeamCount", "getMinimumTeamCount", "bob"})
public final class WeekdayDinnerTeamDecisionLogic implements com.catshi.bob.services.TeamDecisionLogic {
    private final int minimumCount = 4;
    private final int minimumTeamCount = 4;
    private final int chunkSize = 4;
    
    @java.lang.Override
    public int getMinimumCount() {
        return 0;
    }
    
    @java.lang.Override
    public int getMinimumTeamCount() {
        return 0;
    }
    
    @java.lang.Override
    public int getChunkSize() {
        return 0;
    }
    
    public WeekdayDinnerTeamDecisionLogic() {
        super();
    }
    
    /**
     * 충분히 많이 시도해봤는데도, 팀이 안나오면 확률적으로 팀이 매칭될 수 없다고 보고
     * 예외를 날린다.
     */
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.catshi.bob.domain.BobTeam> decide(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.BobTicket> tickets, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamSortStrategy teamSortStrategy) {
        return null;
    }
}