package com.catshi.bob.ui.command;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005R\u001a\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/catshi/bob/ui/command/ResponseRecommendMenu;", "Lcom/catshi/bob/ui/command/BobCommand;", "exceptingMenus", "", "Lcom/catshi/bob/domain/Menu;", "(Ljava/util/List;)V", "getExceptingMenus", "()Ljava/util/List;", "bob"})
public class ResponseRecommendMenu implements com.catshi.bob.ui.command.BobCommand {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.catshi.bob.domain.Menu> exceptingMenus = null;
    
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.catshi.bob.domain.Menu> getExceptingMenus() {
        return null;
    }
    
    public ResponseRecommendMenu(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.Menu> exceptingMenus) {
        super();
    }
}