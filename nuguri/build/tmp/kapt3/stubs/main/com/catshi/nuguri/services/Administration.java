package com.catshi.nuguri.services;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0000\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH&J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\bH&J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\bH&J\u0010\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH&J\u000e\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H&\u00a8\u0006\u0015"}, d2 = {"Lcom/catshi/nuguri/services/Administration;", "", "responseAddProject", "Lcom/catshi/nuguri/domain/Project;", "name", "", "nickname", "teamId", "", "responseAddTeam", "Lcom/catshi/core/domain/Team;", "managerId", "responseAddTeamMember", "", "userId", "responseDeleteProject", "projectId", "responseDeleteTeam", "responseDeleteTeamMember", "responseShowAllProjects", "", "nuguri"})
public abstract interface Administration {
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.catshi.nuguri.domain.Project> responseShowAllProjects();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.nuguri.domain.Project responseAddProject(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, long teamId);
    
    public abstract void responseDeleteProject(long projectId);
    
    @org.jetbrains.annotations.NotNull
    public abstract com.catshi.core.domain.Team responseAddTeam(@org.jetbrains.annotations.NotNull
    java.lang.String name, long managerId);
    
    public abstract void responseDeleteTeam(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    public abstract void responseAddTeamMember(long userId, long teamId);
    
    public abstract void responseDeleteTeamMember(long userId, long teamId);
}