Feature: Client error while applying discount for one product with not exist product id

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}/discounts" with http method "POST" available

    When a client wants apply discount for product with id 1000:
      | value | 10                  |
      | from  | 2023-11-28 11:00:00 |
      | until | 2023-12-31 11:00:00 |

    Then response code is 404

    And response body contains:
      | status  | 404                             |
      | message | Resource was not found          |
      | details | Product not found with id: 1000 |