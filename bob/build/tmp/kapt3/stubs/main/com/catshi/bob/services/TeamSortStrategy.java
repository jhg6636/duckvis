package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&\u00a8\u0006\u0006"}, d2 = {"Lcom/catshi/bob/services/TeamSortStrategy;", "", "sort", "", "Lcom/catshi/bob/domain/BobTicket;", "originalList", "bob"})
public abstract interface TeamSortStrategy {
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.bob.domain.BobTicket> sort(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.BobTicket> originalList);
}