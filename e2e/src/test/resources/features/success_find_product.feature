Feature: Find Product by id

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}" with http method "GET" available

    When a client wants find product with id 3

    Then response code is 200

    And response body contains:
      | id                   | 3              |
      | name                 | Test product 3 |
      | active               | true           |
      | price.value          | 250.0          |
      | price.currency.value | USD            |