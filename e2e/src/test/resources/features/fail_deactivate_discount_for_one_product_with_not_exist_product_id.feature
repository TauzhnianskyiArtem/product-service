Feature: Client error while deactivating discount for one product with not exist product id

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}/discounts" with http method "DELETE" available

    When a client wants deactivate discount for product with id 1000

    Then response code is 404

    And response body contains:
      | status  | 404                             |
      | message | Resource was not found          |
      | details | Product not found with id: 1000 |