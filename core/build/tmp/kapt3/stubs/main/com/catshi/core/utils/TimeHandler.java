package com.catshi.core.utils;

import java.lang.System;

@lombok.NoArgsConstructor
@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\u0005\u001a\u00020\u0004J\u0006\u0010\u0006\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\t\u00a8\u0006\n"}, d2 = {"Lcom/catshi/core/utils/TimeHandler;", "", "()V", "isTodayHoliday", "", "isTodayWeekend", "nowDate", "Ljava/time/LocalDate;", "nowDateTime", "Ljava/time/LocalDateTime;", "core"})
public final class TimeHandler {
    @org.jetbrains.annotations.NotNull
    public static final com.catshi.core.utils.TimeHandler INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime nowDateTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate nowDate() {
        return null;
    }
    
    public final boolean isTodayWeekend() {
        return false;
    }
    
    public final boolean isTodayHoliday() {
        return false;
    }
    
    private TimeHandler() {
        super();
    }
}