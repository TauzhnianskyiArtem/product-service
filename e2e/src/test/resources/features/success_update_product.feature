Feature: Update Product

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}" with http method "PUT" available

    When a client wants update product with id 1:
      | id       | 1       |
      | name     | NewName |
      | active   | true    |
      | price    | 100.0   |
      | currency | USD     |

    Then response code is 200

    And response body contains:
      | name                 | NewName |
      | active               | true    |
      | price.value          | 100.0   |
      | price.currency.value | USD     |