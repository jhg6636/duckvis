package com.catshi.bob.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u0017J\u001f\u0010\u000b\u001a\u0004\u0018\u00010\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0017\u00a2\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\rH\u0017R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0013"}, d2 = {"Lcom/catshi/bob/services/UserDao;", "", "userRepository", "Lcom/catshi/core/domain/UserRepository;", "(Lcom/catshi/core/domain/UserRepository;)V", "getUserRepository", "()Lcom/catshi/core/domain/UserRepository;", "getUserById", "Lcom/catshi/core/domain/User;", "userId", "", "getUserIdByUserCodeAndPath", "stringId", "", "pathType", "Lcom/catshi/core/types/UserPathType;", "(Ljava/lang/String;Lcom/catshi/core/types/UserPathType;)Ljava/lang/Long;", "saveNewUser", "name", "bob"})
@org.springframework.stereotype.Service
public class UserDao {
    @org.jetbrains.annotations.NotNull
    private final com.catshi.core.domain.UserRepository userRepository = null;
    
    @org.jetbrains.annotations.Nullable
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public java.lang.Long getUserIdByUserCodeAndPath(@org.jetbrains.annotations.NotNull
    java.lang.String stringId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserPathType pathType) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.core.domain.User getUserById(long userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @org.springframework.transaction.annotation.Transactional
    public com.catshi.core.domain.User saveNewUser(@org.jetbrains.annotations.NotNull
    java.lang.String stringId, @org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.core.domain.UserRepository getUserRepository() {
        return null;
    }
    
    public UserDao(@org.jetbrains.annotations.NotNull
    com.catshi.core.domain.UserRepository userRepository) {
        super();
    }
}