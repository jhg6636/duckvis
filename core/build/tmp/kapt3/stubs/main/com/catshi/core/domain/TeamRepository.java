package com.catshi.core.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0012\u0010\b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\t"}, d2 = {"Lcom/catshi/core/domain/TeamRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/catshi/core/domain/Team;", "", "existsByName", "", "name", "", "findByName", "core"})
@org.springframework.data.jpa.repository.config.EnableJpaRepositories
@org.springframework.stereotype.Repository
public abstract interface TeamRepository extends org.springframework.data.jpa.repository.JpaRepository<com.catshi.core.domain.Team, java.lang.Long> {
    
    public abstract boolean existsByName(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    @org.jetbrains.annotations.Nullable
    public abstract com.catshi.core.domain.Team findByName(@org.jetbrains.annotations.NotNull
    java.lang.String name);
}