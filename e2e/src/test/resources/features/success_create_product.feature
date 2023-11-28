Feature: Create Product with valid parameters

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products" with http method "POST" available

    When a client wants create product with parameters:
      | name     | ProductName |
      | active   | true        |
      | price    | 10.0        |
      | currency | USD         |

    Then response code is 200

    And response body contains:
      | name                 | ProductName |
      | active               | true        |
      | price.value          | 10.0        |
      | price.currency.value | USD         |