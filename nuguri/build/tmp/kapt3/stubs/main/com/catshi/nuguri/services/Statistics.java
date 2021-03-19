package com.catshi.nuguri.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J$\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nH&\u00a8\u0006\f"}, d2 = {"Lcom/catshi/nuguri/services/Statistics;", "", "responseAdminStatistics", "", "Lcom/catshi/nuguri/domain/AttendanceCard;", "responseStatistics", "", "userId", "", "startDate", "Ljava/time/LocalDate;", "endDate", "nuguri"})
public abstract interface Statistics {
    
    public abstract int responseStatistics(long userId, @org.jetbrains.annotations.NotNull
    java.time.LocalDate startDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate endDate);
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.nuguri.domain.AttendanceCard> responseAdminStatistics();
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
    }
}