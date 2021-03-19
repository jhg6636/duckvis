package com.catshi.bob.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0000H\u0016R\u0016\u0010\u0005\u001a\u00020\u00068\u0016X\u0097D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000e"}, d2 = {"Lcom/catshi/bob/domain/Menu;", "", "name", "", "(Ljava/lang/String;)V", "id", "", "getId", "()J", "getName", "()Ljava/lang/String;", "equals", "", "other", "bob"})
@javax.persistence.Entity
public class Menu {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Id
    private final long id = -1L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    
    public long getId() {
        return 0L;
    }
    
    public boolean equals(@org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.Menu other) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getName() {
        return null;
    }
    
    public Menu(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
        super();
    }
}