package com.catshi.nuguri.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0018\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0017\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000eJ\b\u0010$\u001a\u00020%H\u0016J\u0010\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\bH\u0016J\u0010\u0010)\u001a\u00020\'2\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010*\u001a\u00020%H\u0016J\u0010\u0010+\u001a\u00020%2\u0006\u0010\r\u001a\u00020\nH\u0016R\u0014\u0010\u000f\u001a\u00020\nX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0096\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0016\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0016\u0010\u0017\u001a\u00020\f8\u0016X\u0097D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\t\u001a\u00020\nX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0004\u0018\u00010\nX\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0011\"\u0004\b\u001b\u0010\u001cR\u0016\u0010\u0007\u001a\u00020\b8\u0016X\u0097\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0014\u0010\u0004\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0016\u0010\u0005\u001a\u00020\u00068\u0016X\u0097\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010 \u00a8\u0006,"}, d2 = {"Lcom/catshi/nuguri/domain/AttendanceCard;", "", "userId", "", "projectId", "type", "Lcom/catshi/nuguri/domain/CardType;", "option", "Lcom/catshi/nuguri/types/AttendanceOption;", "loginDateTime", "Ljava/time/LocalDateTime;", "durationSeconds", "", "logoutDateTime", "(JJLcom/catshi/nuguri/domain/CardType;Lcom/catshi/nuguri/types/AttendanceOption;Ljava/time/LocalDateTime;Ljava/lang/Integer;Ljava/time/LocalDateTime;)V", "createdDateTime", "getCreatedDateTime", "()Ljava/time/LocalDateTime;", "getDurationSeconds", "()Ljava/lang/Integer;", "setDurationSeconds", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "id", "getId", "()I", "getLogoutDateTime", "setLogoutDateTime", "(Ljava/time/LocalDateTime;)V", "getOption", "()Lcom/catshi/nuguri/types/AttendanceOption;", "getProjectId", "()J", "getType", "()Lcom/catshi/nuguri/domain/CardType;", "getUserId", "durationCalculate", "", "isSameAttendanceOption", "", "attendanceOption", "isSameProjectId", "logOut", "saveLogoutTime", "nuguri"})
@javax.persistence.Entity
public class AttendanceCard {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @javax.persistence.Id
    private final int id = -1;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime createdDateTime = null;
    private final long userId = 0L;
    private final long projectId = 0L;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private final com.catshi.nuguri.domain.CardType type = null;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private final com.catshi.nuguri.types.AttendanceOption option = null;
    private final java.time.LocalDateTime loginDateTime = null;
    @org.jetbrains.annotations.Nullable
    private java.lang.Integer durationSeconds;
    @org.jetbrains.annotations.Nullable
    private java.time.LocalDateTime logoutDateTime;
    
    public int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.time.LocalDateTime getCreatedDateTime() {
        return null;
    }
    
    public void saveLogoutTime(@org.jetbrains.annotations.NotNull
    java.time.LocalDateTime logoutDateTime) {
    }
    
    public void logOut() {
    }
    
    public void durationCalculate() {
    }
    
    public boolean isSameProjectId(long projectId) {
        return false;
    }
    
    public boolean isSameAttendanceOption(@org.jetbrains.annotations.NotNull
    com.catshi.nuguri.types.AttendanceOption attendanceOption) {
        return false;
    }
    
    public long getUserId() {
        return 0L;
    }
    
    public long getProjectId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.nuguri.domain.CardType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.nuguri.types.AttendanceOption getOption() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public java.lang.Integer getDurationSeconds() {
        return null;
    }
    
    public void setDurationSeconds(@org.jetbrains.annotations.Nullable
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public java.time.LocalDateTime getLogoutDateTime() {
        return null;
    }
    
    public void setLogoutDateTime(@org.jetbrains.annotations.Nullable
    java.time.LocalDateTime p0) {
    }
    
    public AttendanceCard(long userId, long projectId, @org.jetbrains.annotations.NotNull
    com.catshi.nuguri.domain.CardType type, @org.jetbrains.annotations.NotNull
    com.catshi.nuguri.types.AttendanceOption option, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime loginDateTime, @org.jetbrains.annotations.Nullable
    java.lang.Integer durationSeconds, @org.jetbrains.annotations.Nullable
    java.time.LocalDateTime logoutDateTime) {
        super();
    }
}