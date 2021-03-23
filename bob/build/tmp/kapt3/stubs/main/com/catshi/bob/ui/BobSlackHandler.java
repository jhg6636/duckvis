package com.catshi.bob.ui;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0010\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\t\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0012J\b\u0010\u0015\u001a\u00020\u0013H\u0017J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0012J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0013H\u0012J\u0016\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020\u0013H\u0016J\u0012\u0010!\u001a\u00020\u00132\b\b\u0001\u0010\u001c\u001a\u00020\u0013H\u0017J\"\u0010\"\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\b\b\u0002\u0010$\u001a\u00020%H\u0016J\u0012\u0010&\u001a\u0004\u0018\u00010\'2\u0006\u0010#\u001a\u00020\u0013H\u0016R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0018\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/catshi/bob/ui/BobSlackHandler;", "", "bobService", "Lcom/catshi/bob/services/BobService;", "ticketRepository", "Lcom/catshi/bob/domain/BobTicketQueryDslRepository;", "userRepository", "Lcom/catshi/core/domain/UserRepository;", "commandParsers", "", "Lcom/catshi/bob/ui/command/CommandParser;", "(Lcom/catshi/bob/services/BobService;Lcom/catshi/bob/domain/BobTicketQueryDslRepository;Lcom/catshi/core/domain/UserRepository;Ljava/util/List;)V", "getBobService", "()Lcom/catshi/bob/services/BobService;", "getTicketRepository", "()Lcom/catshi/bob/domain/BobTicketQueryDslRepository;", "getUserRepository", "()Lcom/catshi/core/domain/UserRepository;", "getUserName", "", "userCode", "hello", "isBot", "", "userId", "", "jsonBodyToDto", "Lcom/catshi/bob/dtos/SlackRequestDto;", "body", "postMessageRequest", "Lorg/springframework/http/ResponseEntity;", "Lkotlin/String$Companion;", "message", "slackEvents", "slackResponse", "text", "pathType", "Lcom/catshi/core/types/UserPathType;", "textToCommand", "Lcom/catshi/bob/ui/command/BobCommand;", "bob"})
@org.springframework.stereotype.Controller
@org.springframework.web.bind.annotation.RestController
public class BobSlackHandler {
    @org.jetbrains.annotations.NotNull
    private final com.catshi.bob.services.BobService bobService = null;
    @org.jetbrains.annotations.NotNull
    private final com.catshi.bob.domain.BobTicketQueryDslRepository ticketRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.catshi.core.domain.UserRepository userRepository = null;
    private final java.util.List<com.catshi.bob.ui.command.CommandParser<?>> commandParsers = null;
    
    @org.jetbrains.annotations.NotNull
    @org.springframework.web.bind.annotation.ResponseBody
    @org.springframework.web.bind.annotation.GetMapping(value = {"/hello"})
    public java.lang.String hello() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @org.springframework.web.bind.annotation.PostMapping(value = {"slack/event"})
    @org.springframework.transaction.annotation.Transactional
    public java.lang.String slackEvents(@org.jetbrains.annotations.NotNull
    @org.springframework.web.bind.annotation.RequestBody
    java.lang.String body) {
        return null;
    }
    
    private com.catshi.bob.dtos.SlackRequestDto jsonBodyToDto(java.lang.String body) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public com.catshi.bob.ui.command.BobCommand textToCommand(@org.jetbrains.annotations.NotNull
    java.lang.String text) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String slackResponse(@org.jetbrains.annotations.NotNull
    java.lang.String text, @org.jetbrains.annotations.NotNull
    java.lang.String userCode, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserPathType pathType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<kotlin.jvm.internal.StringCompanionObject> postMessageRequest(@org.jetbrains.annotations.NotNull
    java.lang.String message) {
        return null;
    }
    
    private java.lang.String getUserName(java.lang.String userCode) {
        return null;
    }
    
    private boolean isBot(long userId) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.services.BobService getBobService() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.domain.BobTicketQueryDslRepository getTicketRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.core.domain.UserRepository getUserRepository() {
        return null;
    }
    
    public BobSlackHandler(@org.jetbrains.annotations.NotNull
    com.catshi.bob.services.BobService bobService, @org.jetbrains.annotations.NotNull
    com.catshi.bob.domain.BobTicketQueryDslRepository ticketRepository, @org.jetbrains.annotations.NotNull
    com.catshi.core.domain.UserRepository userRepository, @org.jetbrains.annotations.NotNull
    java.util.List<? extends com.catshi.bob.ui.command.CommandParser<?>> commandParsers) {
        super();
    }
}