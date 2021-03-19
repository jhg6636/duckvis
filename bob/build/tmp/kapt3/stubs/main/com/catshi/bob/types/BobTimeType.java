package com.catshi.bob.types;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0001\u0018\u0000 \u00112\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0011B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003H\u0002J\b\u0010\f\u001a\u00020\rH\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007j\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010\u00a8\u0006\u0012"}, d2 = {"Lcom/catshi/bob/types/BobTimeType;", "", "startHour", "", "endHour", "(Ljava/lang/String;III)V", "getEndHour", "()I", "getStartHour", "isBobTimeIn", "", "nowHour", "toString", "", "BREAKFAST", "LUNCH", "DINNER", "Companion", "bob"})
public enum BobTimeType {
    /*public static final*/ BREAKFAST /* = new BREAKFAST(0, 0) */,
    /*public static final*/ LUNCH /* = new LUNCH(0, 0) */,
    /*public static final*/ DINNER /* = new DINNER(0, 0) */;
    private final int startHour = 0;
    private final int endHour = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.catshi.bob.types.BobTimeType.Companion Companion = null;
    
    private final boolean isBobTimeIn(int nowHour) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public final int getStartHour() {
        return 0;
    }
    
    public final int getEndHour() {
        return 0;
    }
    
    BobTimeType(int startHour, int endHour) {
    }
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/catshi/bob/types/BobTimeType$Companion;", "", "()V", "of", "Lcom/catshi/bob/types/BobTimeType;", "now", "Ljava/time/LocalDateTime;", "bob"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull
        public final com.catshi.bob.types.BobTimeType of(@org.jetbrains.annotations.NotNull
        java.time.LocalDateTime now) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}