package com.catshi.nuguri.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH&J\"\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H&J\u0010\u0010\u0011\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH&J*\u0010\u0012\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010H&J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\r0\u0016H&J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0016H&\u00a8\u0006\u0018"}, d2 = {"Lcom/catshi/nuguri/services/Attendance;", "", "endCoreTime", "", "responseHelp", "", "userLevel", "Lcom/catshi/core/types/UserLevelType;", "responseHowLongIWorked", "Lcom/catshi/nuguri/dtos/HowLongIWorkedResponse;", "userId", "", "responseLogin", "Lcom/catshi/nuguri/domain/AttendanceCard;", "projectId", "attendanceOption", "Lcom/catshi/nuguri/types/AttendanceOption;", "responseLogout", "responseMistake", "durationMinutes", "", "responseNow", "", "startCoreTime", "nuguri"})
public abstract interface Attendance {
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.AttendanceCard responseLogin(long userId, long projectId, @org.jetbrains.annotations.NotNull
    com.catshi.nuguri.types.AttendanceOption attendanceOption);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.AttendanceCard responseLogout(long userId);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.AttendanceCard responseMistake(long userId, long projectId, int durationMinutes, @org.jetbrains.annotations.NotNull
    com.catshi.nuguri.types.AttendanceOption attendanceOption);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.nuguri.domain.AttendanceCard> responseNow();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.dtos.HowLongIWorkedResponse responseHowLongIWorked(long userId);
    
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