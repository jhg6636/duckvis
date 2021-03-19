package com.catshi.nuguri.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\t\b\u0017\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0016\u0010\b\u001a\u00020\u00068\u0016X\u0097D\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\u0004\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lcom/catshi/nuguri/domain/Project;", "", "name", "", "nickname", "teamId", "", "(Ljava/lang/String;Ljava/lang/String;J)V", "id", "getId", "()J", "getName", "()Ljava/lang/String;", "getNickname", "getTeamId", "nuguri"})
@javax.persistence.Entity
public class Project {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @javax.persistence.Id
    private final long id = -1L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String nickname = null;
    private final long teamId = 0L;
    
    public long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getNickname() {
        return null;
    }
    
    public long getTeamId() {
        return 0L;
    }
    
    public Project(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, long teamId) {
        super();
    }
}