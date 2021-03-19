package com.catshi.core.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\u0016R\u0016\u0010\u0007\u001a\u00020\u00058\u0016X\u0097D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/catshi/core/domain/Team;", "", "name", "", "managerId", "", "(Ljava/lang/String;J)V", "id", "getId", "()J", "setManager", "", "core"})
@javax.persistence.Entity
public class Team {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Id
    private final long id = -1L;
    private final java.lang.String name = null;
    private long managerId;
    
    public long getId() {
        return 0L;
    }
    
    public void setManager(long managerId) {
    }
    
    public Team(@org.jetbrains.annotations.NotNull
    java.lang.String name, long managerId) {
        super();
    }
}