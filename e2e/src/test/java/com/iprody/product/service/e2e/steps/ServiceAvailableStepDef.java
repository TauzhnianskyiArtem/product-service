package com.iprody.product.service.e2e.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.iprody.product.service.e2e.generated.api.ActuatorApi;
import com.iprody.product.service.e2e.util.JsonSerializationHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public final class ServiceAvailableStepDef {

    /**
     * Injection of HTTP client working with actuator endpoints.
     */
    private final ActuatorApi actuatorApi;
    /**
     * Helper bean for json (de)serialization.
     */
    private final JsonSerializationHelper jsonHelper;

    /**
     * Checking if health of service is good.
     */
    @Given("Product Service is up and running")
    public void upServiceIsUpAndRunning() {
        final var health = actuatorApi.health();
        final var actualStatus = jsonHelper.getObjectAsNode(health).get("status").asText();

        assertThat(actualStatus).isEqualTo("UP");
    }

    /**
     * Checking if specific user endpoint available.
     *
     * @param url        of tested endpoint e.g. "/users", "/users/{id}" ...
     * @param httpMethod of tested endpoint e.g. "POST", "put", "PUT", "GET"... (case insensitive)
     */
    @And("Product endpoint {string} with http method {string} available")
    public void userEndpointAvailable(String url, String httpMethod) {
        final var mappings = actuatorApi.mappings();
        final var actualEndpoints = jsonHelper.getObjectAsNode(mappings)
                .get("contexts")
                .get("product-service")
                .get("mappings")
                .get("dispatcherServlets")
                .get("dispatcherServlet")
                .findValues("predicate")
                .stream()
                .map(JsonNode::asText)
                .toList();
        final var expectedEndpoint = "{%s [%s]}".formatted(httpMethod.toUpperCase(), url.toLowerCase());

        assertThat(actualEndpoints).contains(expectedEndpoint);
    }
}