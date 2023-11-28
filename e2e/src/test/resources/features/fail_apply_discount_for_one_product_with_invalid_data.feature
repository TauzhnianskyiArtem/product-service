Feature: Client error while applying discount for one product with invalid parameters

  Scenario Outline:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/{id}/discounts" with http method "POST" available

    When a client wants apply discount for product with id 1:
      | value | <value> |
      | from  | <from>  |
      | until | <until> |

    Then response code is 400

    And response body contains:
      | status  | <status>                          |
      | message | Request validation error occurred |
      | details | <details>                         |

    Examples:
      | value | from                | until               | status | details                                  |
      | 110   | 2023-11-28 11:00:00 | 2023-11-29 11:00:00 | 400    | value must be less than or equal to 100  |
      | -10   | 2023-11-28 11:00:00 | 2023-11-29 11:00:00 | 400    | value must be greater than or equal to 0 |
      | 10    |                     | 2023-11-28 11:00:00 | 400    | from must not be null                    |
      | 10    | 2023-11-28 11:00:00 |                     | 400    | until must not be null                   |