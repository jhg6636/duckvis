package com.catshi.bob.ui;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0013J\b\u0010\r\u001a\u00020\fH\u0013J\b\u0010\u000e\u001a\u00020\fH\u0013J\b\u0010\u000f\u001a\u00020\fH\u0013J\b\u0010\u0010\u001a\u00020\fH\u0013R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0002"}, d2 = {"Lcom/catshi/bob/ui/BobScheduler;", "", "bob", "Lcom/catshi/bob/services/Bob;", "slackHandler", "Lcom/catshi/bob/ui/BobSlackHandler;", "(Lcom/catshi/bob/services/Bob;Lcom/catshi/bob/ui/BobSlackHandler;)V", "getBob", "()Lcom/catshi/bob/services/Bob;", "getSlackHandler", "()Lcom/catshi/bob/ui/BobSlackHandler;", "announceDinnerTicketingFinished", "", "announceDinnerTicketingStarted", "announceLunchTicketingFinished", "announceLunchTicketingStarted", "announceTeam"})
@org.springframework.stereotype.Component
public class BobScheduler {
    @org.jetbrains.annotations.NotNull
    private final com.catshi.bob.services.Bob bob = null;
    @org.jetbrains.annotations.NotNull
    private final com.catshi.bob.ui.BobSlackHandler slackHandler = null;
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 1 * * *")
    private void announceLunchTicketingStarted() {
    }
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 7 * * *")
    private void announceDinnerTicketingStarted() {
    }
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 2 * * *")
    private void announceLunchTicketingFinished() {
    }
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 8 * * *")
    private void announceDinnerTicketingFinished() {
    }
    
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 1 2,8 * * *")
    private void announceTeam() {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.services.Bob getBob() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.ui.BobSlackHandler getSlackHandler() {
        return null;
    }
    
    public BobScheduler(@org.jetbrains.annotations.NotNull
    com.catshi.bob.services.Bob bob, @org.jetbrains.annotations.NotNull
    com.catshi.bob.ui.BobSlackHandler slackHandler) {
        super();
    }
}