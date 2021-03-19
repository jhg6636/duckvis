package com.catshi.bob;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0013\u00a8\u0006\u0005"}, d2 = {"Lcom/catshi/bob/BobApplication;", "", "()V", "started", "", "bob"})
@org.springframework.scheduling.annotation.EnableScheduling
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"com.catshi"})
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = {"com.catshi.bob", "com.catshi.core"})
@org.springframework.boot.autoconfigure.SpringBootApplication
public class BobApplication {
    
    @javax.annotation.PostConstruct
    private void started() {
    }
    
    public BobApplication() {
        super();
    }
}