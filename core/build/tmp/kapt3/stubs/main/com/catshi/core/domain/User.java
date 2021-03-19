package com.catshi.core.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u001a\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0017\u0018\u00002\u00020\u0001BI\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010&\u001a\u00020\'2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010(\u001a\u00020\'2\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u0010\u0010)\u001a\u00020\'2\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u0010\u0010*\u001a\u00020+2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016R\u001e\u0010\u0005\u001a\u00020\u00068\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0013\u001a\u00020\f8\u0016X\u0097D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u001a\u0010\r\u001a\u00020\u0003X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u0004\u001a\u00020\u0003X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0017\"\u0004\b\u001b\u0010\u0019R\u0010\u0010\u0007\u001a\u00020\b8\u0012X\u0093\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0096\u000e\u00a2\u0006\u0010\n\u0002\u0010 \u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017R\u001e\u0010\t\u001a\u00020\n8\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%\u00a8\u0006,"}, d2 = {"Lcom/catshi/core/domain/User;", "", "userCode", "", "name", "city", "Lcom/catshi/core/types/CityType;", "path", "Lcom/catshi/core/types/UserPathType;", "userLevel", "Lcom/catshi/core/types/UserLevelType;", "teamId", "", "multiTeam", "(Ljava/lang/String;Ljava/lang/String;Lcom/catshi/core/types/CityType;Lcom/catshi/core/types/UserPathType;Lcom/catshi/core/types/UserLevelType;Ljava/lang/Long;Ljava/lang/String;)V", "getCity", "()Lcom/catshi/core/types/CityType;", "setCity", "(Lcom/catshi/core/types/CityType;)V", "id", "getId", "()J", "getMultiTeam", "()Ljava/lang/String;", "setMultiTeam", "(Ljava/lang/String;)V", "getName", "setName", "getTeamId", "()Ljava/lang/Long;", "setTeamId", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getUserCode", "getUserLevel", "()Lcom/catshi/core/types/UserLevelType;", "setUserLevel", "(Lcom/catshi/core/types/UserLevelType;)V", "changeCity", "", "changeName", "checkSameName", "isSameCity", "", "core"})
@org.springframework.context.annotation.ComponentScan(basePackages = {"com.catshi"})
@javax.persistence.Entity
public class User {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Id
    private final long id = -1L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String userCode = null;
    @org.jetbrains.annotations.NotNull
    private java.lang.String name;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private com.catshi.core.types.CityType city;
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private final com.catshi.core.types.UserPathType path = null;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private com.catshi.core.types.UserLevelType userLevel;
    @org.jetbrains.annotations.Nullable
    private java.lang.Long teamId;
    @org.jetbrains.annotations.NotNull
    private java.lang.String multiTeam;
    
    public long getId() {
        return 0L;
    }
    
    public void changeCity(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city) {
    }
    
    public void changeName(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
    }
    
    public void checkSameName(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
    }
    
    public boolean isSameCity(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getUserCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getName() {
        return null;
    }
    
    public void setName(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.core.types.CityType getCity() {
        return null;
    }
    
    public void setCity(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.core.types.UserLevelType getUserLevel() {
        return null;
    }
    
    public void setUserLevel(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserLevelType p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public java.lang.Long getTeamId() {
        return null;
    }
    
    public void setTeamId(@org.jetbrains.annotations.Nullable
    java.lang.Long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getMultiTeam() {
        return null;
    }
    
    public void setMultiTeam(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public User(@org.jetbrains.annotations.NotNull
    java.lang.String userCode, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserPathType path, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserLevelType userLevel, @org.jetbrains.annotations.Nullable
    java.lang.Long teamId, @org.jetbrains.annotations.NotNull
    java.lang.String multiTeam) {
        super();
    }
}