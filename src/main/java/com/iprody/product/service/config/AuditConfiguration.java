package com.iprody.product.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * Audit Configuration. Add bean AuditingEntityListener for automatic set createdAt and updatedAt for entities.
 * Annotation @EnableJpaAuditing - enable listeners for auditing.
 */
@EnableJpaAuditing
@Configuration
public class AuditConfiguration {

    /**
     * Add bean in the context for auditing entity, especially for tracking columns updatedAt and createdAt.
     *
     * @return AuditingEntityListener The AuditingEntityListener.
     */
    @Bean
    public AuditingEntityListener auditingEntityListener() {
        return new AuditingEntityListener();
    }
}
