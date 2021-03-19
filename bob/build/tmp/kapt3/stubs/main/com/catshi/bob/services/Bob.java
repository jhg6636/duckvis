package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J,\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH&J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H&J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003H&J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\tH&J\b\u0010\u001a\u001a\u00020\u0013H&J\u001a\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u001d\u001a\u00020\u001eH&J\"\u0010\u001f\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u001d\u001a\u00020\u001eH&J\u001a\u0010 \u001a\u00020!2\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u001d\u001a\u00020\u001eH&J\u0016\u0010\"\u001a\u00020\u00112\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00130\u0003H&J\u0010\u0010$\u001a\u00020!2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0018\u0010%\u001a\u00020&2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020(H&J\u001a\u0010)\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u001d\u001a\u00020\u001eH&J\"\u0010*\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u001d\u001a\u00020\u001eH&\u00a8\u0006+"}, d2 = {"Lcom/catshi/bob/services/Bob;", "", "archiveBobTeam", "", "Lcom/catshi/bob/domain/BobHistory;", "bobTeams", "Lcom/catshi/bob/domain/ThisMealBobTeams;", "determineBobTeam", "city", "Lcom/catshi/core/types/CityType;", "bobTimeType", "Lcom/catshi/bob/types/BobTimeType;", "decisionLogic", "Lcom/catshi/bob/services/TeamDecisionLogic;", "teamSortStrategy", "Lcom/catshi/bob/services/TeamSortStrategy;", "responseAddMenu", "Lcom/catshi/bob/domain/Menu;", "name", "", "responseAllMenu", "responseChangeLivingPlace", "Lcom/catshi/core/domain/User;", "userId", "", "livingCity", "responseHelp", "responseMe", "Lcom/catshi/bob/domain/BobTicket;", "now", "Ljava/time/LocalDateTime;", "responseMeSpecificPlace", "responseNotMe", "", "responseRecommendMenu", "exceptedMenus", "responseRemoveMenu", "responseStatistics", "Lcom/catshi/bob/dtos/StatisticsDto;", "option", "Lcom/catshi/bob/types/StatisticsOption;", "responseVegetarian", "responseVegetarianSpecificPlace", "bob"})
public abstract interface Bob {
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.BobTicket responseMe(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.BobTicket responseVegetarian(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.BobTicket responseMeSpecificPlace(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.BobTicket responseVegetarianSpecificPlace(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now);
    
    public abstract void responseNotMe(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.core.domain.User responseChangeLivingPlace(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType livingCity);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.Menu responseRecommendMenu(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> exceptedMenus);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.bob.domain.Menu> responseAllMenu();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.Menu responseAddMenu(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    public abstract void responseRemoveMenu(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.domain.ThisMealBobTeams determineBobTeam(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType bobTimeType, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamDecisionLogic decisionLogic, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamSortStrategy teamSortStrategy);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.bob.domain.BobHistory> archiveBobTeam(@org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.ThisMealBobTeams bobTeams);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.lang.String responseHelp();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.bob.dtos.StatisticsDto responseStatistics(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.StatisticsOption option);
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
    }
}