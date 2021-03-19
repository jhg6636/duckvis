package com.catshi.bob.exceptions;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0012\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0006J\u0014\u0010\b\u001a\u00020\u00042\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a8\u0006\n"}, d2 = {"Lcom/catshi/bob/exceptions/NeverMatchThem;", "", "()V", "isPossibleChunkedList", "", "chunkedList", "", "Lcom/catshi/bob/domain/BobTicket;", "isPossibleTeam", "originalList", "bob"})
public final class NeverMatchThem {
    @org.jetbrains.annotations.NotNull
    public static final com.catshi.bob.exceptions.NeverMatchThem INSTANCE = null;
    
    public final boolean isPossibleTeam(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.domain.BobTicket> originalList) {
        return false;
    }
    
    public final boolean isPossibleChunkedList(@org.jetbrains.annotations.NotNull
    java.util.List<? extends java.util.List<? extends com.catshi.bob.domain.BobTicket>> chunkedList) {
        return false;
    }
    
    private NeverMatchThem() {
        super();
    }
}