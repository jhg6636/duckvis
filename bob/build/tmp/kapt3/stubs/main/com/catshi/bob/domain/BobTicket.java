package com.catshi.bob.domain;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\b\u0017\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u000bH\u0016J\u0010\u00105\u001a\u0002032\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u00106\u001a\u00020&2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u00107\u001a\u00020&2\u0006\u00104\u001a\u00020\u000bH\u0016J\b\u00108\u001a\u000203H\u0016J\b\u00109\u001a\u00020:H\u0016R\u001e\u0010\n\u001a\u00020\u000b8\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001e\u0010\b\u001a\u00020\t8\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001e\u0010\f\u001a\u00020\r8\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u001e\u0010\u001f\u001a\u00020 8\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0014\u0010%\u001a\u00020&8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b%\u0010\'R\u0014\u0010(\u001a\u00020&8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b(\u0010\'R\u0014\u0010)\u001a\u00020&8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b)\u0010\'R\u001e\u0010\u000e\u001a\u00020\u000f8\u0016@\u0016X\u0097\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0016\u0010\u0002\u001a\u00020\u00038\u0016X\u0097\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101\u00a8\u0006;"}, d2 = {"Lcom/catshi/bob/domain/BobTicket;", "", "user", "Lcom/catshi/core/domain/User;", "date", "Ljava/time/LocalDate;", "time", "Ljava/time/LocalTime;", "bobTimeType", "Lcom/catshi/bob/types/BobTimeType;", "bobStyle", "Lcom/catshi/bob/types/BobStyleType;", "city", "Lcom/catshi/core/types/CityType;", "issuedOrder", "Lcom/catshi/bob/types/IssuedOrderType;", "(Lcom/catshi/core/domain/User;Ljava/time/LocalDate;Ljava/time/LocalTime;Lcom/catshi/bob/types/BobTimeType;Lcom/catshi/bob/types/BobStyleType;Lcom/catshi/core/types/CityType;Lcom/catshi/bob/types/IssuedOrderType;)V", "getBobStyle", "()Lcom/catshi/bob/types/BobStyleType;", "setBobStyle", "(Lcom/catshi/bob/types/BobStyleType;)V", "getBobTimeType", "()Lcom/catshi/bob/types/BobTimeType;", "setBobTimeType", "(Lcom/catshi/bob/types/BobTimeType;)V", "getCity", "()Lcom/catshi/core/types/CityType;", "setCity", "(Lcom/catshi/core/types/CityType;)V", "getDate", "()Ljava/time/LocalDate;", "id", "", "getId", "()J", "setId", "(J)V", "isAnything", "", "()Z", "isFirst", "isVegetarian", "getIssuedOrder", "()Lcom/catshi/bob/types/IssuedOrderType;", "setIssuedOrder", "(Lcom/catshi/bob/types/IssuedOrderType;)V", "getTime", "()Ljava/time/LocalTime;", "getUser", "()Lcom/catshi/core/domain/User;", "changeBobStyleType", "", "styleType", "changeCity", "isSameCity", "isSameStyle", "setFirst", "tagString", "", "bob"})
@javax.persistence.Table(uniqueConstraints = {@javax.persistence.UniqueConstraint(columnNames = {"user_id", "date", "bob_time_type"})})
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"com.catshi.core"})
@javax.persistence.Entity
public class BobTicket {
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Id
    private long id = -1L;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.JoinColumn(nullable = false)
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private final com.catshi.core.domain.User user = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDate date = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalTime time = null;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private com.catshi.bob.types.BobTimeType bobTimeType;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private com.catshi.bob.types.BobStyleType bobStyle;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private com.catshi.core.types.CityType city;
    @org.jetbrains.annotations.NotNull
    @javax.persistence.Enumerated(value = javax.persistence.EnumType.STRING)
    private com.catshi.bob.types.IssuedOrderType issuedOrder;
    
    public long getId() {
        return 0L;
    }
    
    public void setId(long p0) {
    }
    
    public void changeCity(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city) {
    }
    
    public boolean isSameCity(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city) {
        return false;
    }
    
    public boolean isVegetarian() {
        return false;
    }
    
    public boolean isAnything() {
        return false;
    }
    
    public void changeBobStyleType(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobStyleType styleType) {
    }
    
    public boolean isSameStyle(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobStyleType styleType) {
        return false;
    }
    
    public boolean isFirst() {
        return false;
    }
    
    public void setFirst() {
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String tagString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.core.domain.User getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.time.LocalDate getDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.time.LocalTime getTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.types.BobTimeType getBobTimeType() {
        return null;
    }
    
    public void setBobTimeType(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.types.BobStyleType getBobStyle() {
        return null;
    }
    
    public void setBobStyle(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobStyleType p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.core.types.CityType getCity() {
        return null;
    }
    
    public void setCity(@org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public com.catshi.bob.types.IssuedOrderType getIssuedOrder() {
        return null;
    }
    
    public void setIssuedOrder(@org.jetbrains.annotations.NotNull
    com.catshi.bob.types.IssuedOrderType p0) {
    }
    
    public BobTicket(@org.jetbrains.annotations.NotNull
    com.catshi.core.domain.User user, @org.jetbrains.annotations.NotNull
    java.time.LocalDate date, @org.jetbrains.annotations.NotNull
    java.time.LocalTime time, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobTimeType bobTimeType, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.BobStyleType bobStyle, @org.jetbrains.annotations.NotNull
    com.catshi.core.types.CityType city, @org.jetbrains.annotations.NotNull
    com.catshi.bob.types.IssuedOrderType issuedOrder) {
        super();
    }
}