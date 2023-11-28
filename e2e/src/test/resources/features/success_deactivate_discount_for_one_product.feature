Feature: Deactivate discount for one product

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}/discounts" with http method "DELETE" available

    When a client wants deactivate discount for product with id 1

    Then response code is 200

    And response body contains:
      | discountValue |  |
