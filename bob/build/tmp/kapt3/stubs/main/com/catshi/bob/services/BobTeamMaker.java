package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001b\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\rR\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/catshi/bob/services/BobTeamMaker;", "", "allTickets", "", "Lcom/catshi/bob/domain/BobTicket;", "teamSortStrategy", "Lcom/catshi/bob/services/TeamSortStrategy;", "(Ljava/util/List;Lcom/catshi/bob/services/TeamSortStrategy;)V", "anythingTickets", "vegetarianTickets", "checkTeamAvailability", "Lcom/catshi/bob/types/TeamAvailabilityType;", "decisionLogic", "Lcom/catshi/bob/services/TeamDecisionLogic;", "make", "Lcom/catshi/bob/domain/ThisMealBobTeams;", "bob"})
public final class BobTeamMaker {
    private final java.util.List<com.catshi.bob.domain.BobTicket> vegetarianTickets = null;
    private final java.util.List<com.catshi.bob.domain.BobTicket> anythingTickets = null;
    private final java.util.List<com.catshi.bob.domain.BobTicket> allTickets = null;
    private final com.catshi.bob.services.TeamSortStrategy teamSortStrategy = null;
    
    private final com.catshi.bob.types.TeamAvailabilityType checkTeamAvailability(com.catshi.bob.services.TeamDecisionLogic decisionLogic) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.catshi.bob.domain.ThisMealBobTeams make(@org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamDecisionLogic decisionLogic) {
        return null;
    }
    
    public BobTeamMaker(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.BobTicket> allTickets, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamSortStrategy teamSortStrategy) {
        super();
    }
}