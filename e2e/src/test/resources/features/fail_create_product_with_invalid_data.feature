Feature: Client error while creating a product with invalid parameters

  Scenario Outline:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products" with http method "POST" available

    When a client wants create product with parameters:
      | name     | <name>     |
      | active   | <active>   |
      | price    | <price>    |
      | currency | <currency> |

    Then response code is 400

    And response body contains:
      | status  | <status>                          |
      | message | Request validation error occurred |
      | details | <details>                         |

    Examples:
      | name        | active | price | currency | status | details                   |
      | ProductName | true   |       | USD      | 400    | price must not be null    |
      |             | true   | 10.0  | USD      | 400    | name must not be null     |
      | ProductName | true   | 10.0  |          | 400    | currency must not be null |
