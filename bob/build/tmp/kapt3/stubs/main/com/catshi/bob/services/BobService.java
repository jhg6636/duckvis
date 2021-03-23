package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00b4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0017\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0017J\u001c\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010H\u0012J\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0017J(\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0012J\"\u0010&\u001a\u00020\u00112\u0006\u0010\'\u001a\u00020\u001d2\b\b\u0002\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u001bH\u0012J(\u0010+\u001a\u00020\u00132\u0006\u0010,\u001a\u00020#2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u000200H\u0017J\b\u00101\u001a\u00020\u001bH\u0012J\u0010\u00102\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0017J\b\u00103\u001a\u000204H\u0012J\u0010\u00105\u001a\u0002042\u0006\u00106\u001a\u000207H\u0017J\u000e\u00108\u001a\b\u0012\u0004\u0012\u0002040\u0010H\u0017J\u0018\u00109\u001a\u00020\u001f2\u0006\u0010:\u001a\u00020;2\u0006\u0010,\u001a\u00020#H\u0017J\b\u0010<\u001a\u000207H\u0016J\u0018\u0010=\u001a\u00020\u001d2\u0006\u0010:\u001a\u00020;2\u0006\u0010$\u001a\u00020%H\u0017J \u0010>\u001a\u00020\u001d2\u0006\u0010:\u001a\u00020;2\u0006\u0010,\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0017J\u0018\u0010?\u001a\u00020\u00172\u0006\u0010:\u001a\u00020;2\u0006\u0010$\u001a\u00020%H\u0017J\u0016\u0010@\u001a\u0002042\f\u0010A\u001a\b\u0012\u0004\u0012\u0002070\u0010H\u0017J\u0010\u0010B\u001a\u00020\u00172\u0006\u00106\u001a\u000207H\u0017J\u0018\u0010C\u001a\u00020D2\u0006\u0010:\u001a\u00020;2\u0006\u0010E\u001a\u00020FH\u0017J\u0018\u0010G\u001a\u00020\u001d2\u0006\u0010:\u001a\u00020;2\u0006\u0010$\u001a\u00020%H\u0017J \u0010H\u001a\u00020\u001d2\u0006\u0010:\u001a\u00020;2\u0006\u0010,\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0017J(\u0010I\u001a\u00020\u001d2\u0006\u0010:\u001a\u00020;2\u0006\u0010J\u001a\u00020!2\u0006\u0010,\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0012R\u000e\u0010\n\u001a\u00020\u000bX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2 = {"Lcom/catshi/bob/services/BobService;", "Lcom/catshi/bob/services/Bob;", "bobTicketRepository", "Lcom/catshi/bob/domain/BobTicketRepository;", "bobTicketQueryDslRepository", "Lcom/catshi/bob/domain/BobTicketQueryDslRepository;", "userRepository", "Lcom/catshi/core/domain/UserRepository;", "bobHistoryRepository", "Lcom/catshi/bob/domain/BobHistoryRepository;", "bobHistoryQueryDslRepository", "Lcom/catshi/bob/domain/BobHistoryQueryDslRepository;", "menuRepository", "Lcom/catshi/bob/domain/MenuRepository;", "(Lcom/catshi/bob/domain/BobTicketRepository;Lcom/catshi/bob/domain/BobTicketQueryDslRepository;Lcom/catshi/core/domain/UserRepository;Lcom/catshi/bob/domain/BobHistoryRepository;Lcom/catshi/bob/domain/BobHistoryQueryDslRepository;Lcom/catshi/bob/domain/MenuRepository;)V", "archiveBobTeam", "", "Lcom/catshi/bob/domain/BobHistory;", "bobTeams", "Lcom/catshi/bob/domain/ThisMealBobTeams;", "bobTeamArchiver", "Lcom/catshi/bob/domain/BobTeam;", "checkBobTeamMatched", "", "bobTimeType", "Lcom/catshi/bob/types/BobTimeType;", "waitingTicketNumber", "", "createBobTicket", "Lcom/catshi/bob/domain/BobTicket;", "user", "Lcom/catshi/core/domain/User;", "bobStyle", "Lcom/catshi/bob/types/BobStyleType;", "bobCity", "Lcom/catshi/core/types/CityType;", "now", "Ljava/time/LocalDateTime;", "createNewBobHistory", "bobTicket", "isBobLeader", "", "bobTeamNumber", "determineBobTeam", "city", "decisionLogic", "Lcom/catshi/bob/services/TeamDecisionLogic;", "teamSortStrategy", "Lcom/catshi/bob/services/TeamSortStrategy;", "getPresentMaxBobTeamNumber", "markFirstTicket", "pickOneMenu", "Lcom/catshi/bob/domain/Menu;", "responseAddMenu", "name", "", "responseAllMenu", "responseChangeLivingPlace", "userId", "", "responseHelp", "responseMe", "responseMeSpecificPlace", "responseNotMe", "responseRecommendMenu", "exceptedMenus", "responseRemoveMenu", "responseStatistics", "Lcom/catshi/bob/dtos/StatisticsDto;", "option", "Lcom/catshi/bob/types/StatisticsOption;", "responseVegetarian", "responseVegetarianSpecificPlace", "saveTicket", "styleType", "bob"})
@org.springframework.stereotype.Service
public class BobService implements com.catshi.bob.services.Bob {
    private final com.catshi.bob.domain.BobTicketRepository bobTicketRepository = null;
    private final com.catshi.bob.domain.BobTicketQueryDslRepository bobTicketQueryDslRepository = null;
    private final com.catshi.core.domain.UserRepository userRepository = null;
    private final com.catshi.bob.domain.BobHistoryRepository bobHistoryRepository = null;
    private final com.catshi.bob.domain.BobHistoryQueryDslRepository bobHistoryQueryDslRepository = null;
    private final com.catshi.bob.domain.MenuRepository menuRepository = null;
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.BobTicket responseMe(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.BobTicket responseVegetarian(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.BobTicket responseMeSpecificPlace(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.BobTicket responseVegetarianSpecificPlace(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now) {
        return null;
    }
    
    private com.catshi.bob.domain.BobTicket saveTicket(long userId, com.catshi.bob.types.BobStyleType styleType, com.catshi.core.types.CityType city, java.time.LocalDateTime now) {
        return null;
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public void responseNotMe(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime now) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.core.domain.User responseChangeLivingPlace(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.Menu responseRecommendMenu(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> exceptedMenus) {
        return null;
    }
    
    private com.catshi.bob.domain.Menu pickOneMenu() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.Menu responseAddMenu(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.domain.ThisMealBobTeams determineBobTeam(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType bobTimeType, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamDecisionLogic decisionLogic, @org.jetbrains.annotations.NotNull
    com.catshi.bob.services.TeamSortStrategy teamSortStrategy) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional
    public void checkBobTeamMatched(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType bobTimeType, int waitingTicketNumber) {
    }
    
    @org.springframework.transaction.annotation.Transactional
    public void markFirstTicket(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType bobTimeType) {
    }
    
    private com.catshi.bob.domain.BobHistory createNewBobHistory(com.catshi.bob.domain.BobTicket bobTicket, boolean isBobLeader, int bobTeamNumber) {
        return null;
    }
    
    private com.catshi.bob.domain.BobTicket createBobTicket(com.catshi.core.domain.User user, com.catshi.bob.types.BobStyleType bobStyle, com.catshi.core.types.CityType bobCity, java.time.LocalDateTime now) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.bob.domain.BobHistory> archiveBobTeam(@org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.ThisMealBobTeams bobTeams) {
        return null;
    }
    
    private java.util.List<com.catshi.bob.domain.BobHistory> bobTeamArchiver(java.util.List<com.catshi.bob.domain.BobTeam> bobTeams) {
        return null;
    }
    
    private int getPresentMaxBobTeamNumber() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String responseHelp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.bob.dtos.StatisticsDto responseStatistics(long userId, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.StatisticsOption option) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.bob.domain.Menu> responseAllMenu() {
        return null;
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public void responseRemoveMenu(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
    }
    
    public BobService(@org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobTicketRepository bobTicketRepository, @org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobTicketQueryDslRepository bobTicketQueryDslRepository, @org.jetbrains.annotations.NotNull
    com.catshi.core.domain.UserRepository userRepository, @org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobHistoryRepository bobHistoryRepository, @org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobHistoryQueryDslRepository bobHistoryQueryDslRepository, @org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.MenuRepository menuRepository) {
        super();
    }
}