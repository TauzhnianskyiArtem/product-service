Feature: Client error while finding a product with not exists id

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}" with http method "GET" available

    When a client wants find product with id 1000

    Then response code is 404

    And response body contains:
      | status  | 404                             |
      | message | Resource was not found          |
      | details | Product not found with id: 1000 |