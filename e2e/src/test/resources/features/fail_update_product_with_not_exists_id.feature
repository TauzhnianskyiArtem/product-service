Feature: Client error while updating a product with not exists id

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}" with http method "PUT" available

    When a client wants update product with id 1000:
      | id       | 1000    |
      | name     | NewName |
      | active   | true    |
      | price    | 100.0   |
      | currency | USD     |

    Then response code is 404

    And response body contains:
      | status  | 404                             |
      | message | Resource was not found          |
      | details | Product not found with id: 1000 |