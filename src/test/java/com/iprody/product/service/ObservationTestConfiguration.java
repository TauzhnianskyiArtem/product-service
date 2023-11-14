package com.iprody.product.service;

import io.micrometer.observation.tck.TestObservationRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Test Configuration for adding TestObservationRegistry.
 */
@TestConfiguration
public class ObservationTestConfiguration {

    /**
     * @return  TestObservationRegistry are responsible for managing state of an Observation.
     */
    @Bean
    public TestObservationRegistry observationRegistry() {
        return TestObservationRegistry.create();
    }

}
