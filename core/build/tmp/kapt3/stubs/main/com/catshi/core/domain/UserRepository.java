package com.catshi.core.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0003H&J\u001a\u0010\u0007\u001a\u0004\u0018\u00010\u00022\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\'\u00a8\u0006\f"}, d2 = {"Lcom/catshi/core/domain/UserRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/catshi/core/domain/User;", "", "countByTeamId", "", "teamId", "findByUserCodeAndPath", "stringId", "", "pathType", "Lcom/catshi/core/types/UserPathType;", "core"})
@org.springframework.data.jpa.repository.config.EnableJpaRepositories
@org.springframework.stereotype.Repository
public abstract interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<com.catshi.core.domain.User, java.lang.Long> {
    
    @org.jetbrains.annotations.Nullable
    @org.springframework.transaction.annotation.Transactional
    public abstract com.catshi.core.domain.User findByUserCodeAndPath(@org.jetbrains.annotations.NotNull
    java.lang.String stringId, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.UserPathType pathType);
    
    public abstract int countByTeamId(long teamId);
}