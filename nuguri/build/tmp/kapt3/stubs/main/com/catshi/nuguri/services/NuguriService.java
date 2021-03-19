package com.catshi.nuguri.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0017\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0012J \u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0012J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00100\u0018H\u0012J\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0012J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u0010H\u0012J\u0010\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u0010H\u0012J \u0010\u001c\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0012J\u0010\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\u000eH\u0017J \u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020%2\u0006\u0010\'\u001a\u00020\u0010H\u0017J\u0018\u0010(\u001a\u00020)2\u0006\u0010$\u001a\u00020%2\u0006\u0010*\u001a\u00020\u0010H\u0017J\u0018\u0010+\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\'\u001a\u00020\u0010H\u0017J\u000e\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0018H\u0017J\u0010\u0010-\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u0010H\u0017J\u0010\u0010.\u001a\u00020\u00162\u0006\u0010$\u001a\u00020%H\u0017J\u0018\u0010/\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\'\u001a\u00020\u0010H\u0017J\u0010\u00100\u001a\u00020%2\u0006\u00101\u001a\u000202H\u0016J\u0010\u00103\u001a\u0002042\u0006\u0010\u000f\u001a\u00020\u0010H\u0017J\u0018\u00105\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0017J\u0010\u00106\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0017J \u00107\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J\u000e\u00108\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0018H\u0017J\u000e\u00109\u001a\b\u0012\u0004\u0012\u00020#0\u0018H\u0017J \u0010:\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0017J\u000e\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00100\u0018H\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006<"}, d2 = {"Lcom/catshi/nuguri/services/NuguriService;", "Lcom/catshi/nuguri/services/Nuguri;", "attendanceCardRepository", "Lcom/catshi/nuguri/domain/AttendanceCardRepository;", "attendanceCardQueryDslRepository", "Lcom/catshi/nuguri/domain/AttendanceCardQueryDslRepository;", "projectRepository", "Lcom/catshi/nuguri/domain/ProjectRepository;", "teamRepository", "Lcom/catshi/core/domain/TeamRepository;", "userRepository", "Lcom/catshi/core/domain/UserRepository;", "(Lcom/catshi/nuguri/domain/AttendanceCardRepository;Lcom/catshi/nuguri/domain/AttendanceCardQueryDslRepository;Lcom/catshi/nuguri/domain/ProjectRepository;Lcom/catshi/core/domain/TeamRepository;Lcom/catshi/core/domain/UserRepository;)V", "createAttendanceCard", "Lcom/catshi/nuguri/domain/AttendanceCard;", "userId", "", "projectId", "createMistakeCard", "durationMinutes", "", "endCoreTime", "", "getNotWorkingOnCoreTime", "", "getNowWorkingCard", "getThisWeekWorkingSeconds", "getTodayWorkingSeconds", "getTotalDurationSecondsBetweenDates", "startDate", "Ljava/time/LocalDate;", "endDate", "logOut", "attendanceCard", "responseAddProject", "Lcom/catshi/nuguri/domain/Project;", "name", "", "nickname", "teamId", "responseAddTeam", "Lcom/catshi/core/domain/Team;", "managerId", "responseAddTeamMember", "responseAdminStatistics", "responseDeleteProject", "responseDeleteTeam", "responseDeleteTeamMember", "responseHelp", "userLevel", "Lcom/catshi/core/types/UserLevelType;", "responseHowLongIWorked", "Lcom/catshi/nuguri/dtos/HowLongIWorkedResponse;", "responseLogin", "responseLogout", "responseMistake", "responseNow", "responseShowAllProjects", "responseStatistics", "startCoreTime", "nuguri"})
@org.springframework.stereotype.Service
public class NuguriService implements com.catshi.nuguri.services.Nuguri {
    private final com.catshi.nuguri.domain.AttendanceCardRepository attendanceCardRepository = null;
    private final com.catshi.nuguri.domain.AttendanceCardQueryDslRepository attendanceCardQueryDslRepository = null;
    private final com.catshi.nuguri.domain.ProjectRepository projectRepository = null;
    private final com.catshi.core.domain.TeamRepository teamRepository = null;
    private final com.catshi.core.domain.UserRepository userRepository = null;
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.nuguri.domain.AttendanceCard responseLogin(long userId, long projectId) {
        return null;
    }
    
    private com.catshi.nuguri.domain.AttendanceCard createAttendanceCard(long userId, long projectId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.nuguri.domain.AttendanceCard responseLogout(long userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.nuguri.domain.AttendanceCard logOut(@org.jetbrains.annotations.NotNull
    com.catshi.nuguri.domain.AttendanceCard attendanceCard) {
        return null;
    }
    
    private com.catshi.nuguri.domain.AttendanceCard getNowWorkingCard(long userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.nuguri.domain.AttendanceCard responseMistake(long userId, long projectId, int durationMinutes) {
        return null;
    }
    
    private com.catshi.nuguri.domain.AttendanceCard createMistakeCard(long userId, long projectId, int durationMinutes) {
        return null;
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public int responseStatistics(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDate startDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate endDate) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.nuguri.domain.AttendanceCard> responseAdminStatistics() {
        return null;
    }
    
    private int getTotalDurationSecondsBetweenDates(long userId, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.nuguri.domain.AttendanceCard> responseNow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.nuguri.dtos.HowLongIWorkedResponse responseHowLongIWorked(long userId) {
        return null;
    }
    
    private int getTodayWorkingSeconds(long userId) {
        return 0;
    }
    
    private int getThisWeekWorkingSeconds(long userId) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<com.catshi.nuguri.domain.Project> responseShowAllProjects() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.nuguri.domain.Project responseAddProject(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, long teamId) {
        return null;
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public void responseDeleteProject(long projectId) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.core.domain.Team responseAddTeam(@org.jetbrains.annotations.NotNull
    java.lang.String name, long managerId) {
        return null;
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public void responseDeleteTeam(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public void responseAddTeamMember(long userId, long teamId) {
    }
    
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public void responseDeleteTeamMember(long userId, long teamId) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @org.springframework.transaction.annotation.Transactional
    public java.util.List<java.lang.Long> startCoreTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String responseHelp(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserLevelType userLevel) {
        return null;
    }
    
    private java.util.List<java.lang.Long> getNotWorkingOnCoreTime() {
        return null;
    }
    
    @java.lang.Override
    public void endCoreTime() {
    }
    
    public NuguriService(@org.jetbrains.annotations.NotNull
    com.catshi.nuguri.domain.AttendanceCardRepository attendanceCardRepository, @org.jetbrains.annotations.NotNull
    com.catshi.nuguri.domain.AttendanceCardQueryDslRepository attendanceCardQueryDslRepository, @org.jetbrains.annotations.NotNull
    com.catshi.nuguri.domain.ProjectRepository projectRepository, @org.jetbrains.annotations.NotNull
    com.catshi.core.domain.TeamRepository teamRepository, @org.jetbrains.annotations.NotNull
    com.catshi.core.domain.UserRepository userRepository) {
        super();
    }
}