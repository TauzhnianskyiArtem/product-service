package com.iprody.product.service.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Observed aspect configuration. Add bean ObservedAspect for support @Observed.
 */
@Configuration
public class ObservedAspectConfiguration {

    /**
     * @param observationRegistry are responsible for managing state of an Observation.
     * @return ObservedAspect needs for supporting annotation @Observed.
     */
    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
