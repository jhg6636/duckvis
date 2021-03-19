package com.catshi.nuguri.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J \u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH&J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\nH&J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\nH&J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H&J\u0010\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\nH&J\u0010\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0018\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\nH&J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u0019H&J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000f\u001a\u00020\nH&J\u0018\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\nH&J\u0010\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\nH&J \u0010\u001e\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020 H&J\u000e\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H&J\u000e\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011H&J$\u0010#\u001a\u00020 2\u0006\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020%H&J\u000e\u0010\'\u001a\b\u0012\u0004\u0012\u00020\n0\u0011H&\u00a8\u0006("}, d2 = {"Lcom/catshi/nuguri/services/Nuguri;", "", "endCoreTime", "", "responseAddProject", "Lcom/catshi/nuguri/domain/Project;", "name", "", "nickname", "teamId", "", "responseAddTeam", "Lcom/catshi/core/domain/Team;", "managerId", "responseAddTeamMember", "userId", "responseAdminStatistics", "", "Lcom/catshi/nuguri/domain/AttendanceCard;", "responseDeleteProject", "projectId", "responseDeleteTeam", "responseDeleteTeamMember", "responseHelp", "userLevel", "Lcom/catshi/core/types/UserLevelType;", "responseHowLongIWorked", "Lcom/catshi/nuguri/dtos/HowLongIWorkedResponse;", "responseLogin", "responseLogout", "responseMistake", "durationMinutes", "", "responseNow", "responseShowAllProjects", "responseStatistics", "startDate", "Ljava/time/LocalDate;", "endDate", "startCoreTime", "nuguri"})
@org.springframework.stereotype.Service
public abstract interface Nuguri {
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.AttendanceCard responseLogin(long userId, long projectId);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.AttendanceCard responseLogout(long userId);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.AttendanceCard responseMistake(long userId, long projectId, int durationMinutes);
    
    public abstract int responseStatistics(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDate startDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate endDate);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.nuguri.domain.AttendanceCard> responseAdminStatistics();
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.nuguri.domain.AttendanceCard> responseNow();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.dtos.HowLongIWorkedResponse responseHowLongIWorked(long userId);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.nuguri.domain.Project> responseShowAllProjects();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.Project responseAddProject(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, long teamId);
    
    public abstract void responseDeleteProject(long projectId);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.core.domain.Team responseAddTeam(@org.jetbrains.annotations.NotNull
    java.lang.String name, long managerId);
    
    public abstract void responseDeleteTeam(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    public abstract void responseAddTeamMember(long userId, long teamId);
    
    public abstract void responseDeleteTeamMember(long userId, long teamId);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.lang.String responseHelp(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserLevelType userLevel);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<java.lang.Long> startCoreTime();
    
    public abstract void endCoreTime();
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
    }
}