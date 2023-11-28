Feature: Client error while applying discount for group of products with invalid parameters

  Scenario Outline:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/discounts" with http method "POST" available

    When a client wants apply discount for group of products with ids:
      | productIds | 2, 3    |
      | value      | <value> |
      | from       | <from>  |
      | until      | <until> |

    Then response code is 400

    And response body contains:
      | status  | <status>                          |
      | message | Request validation error occurred |
      | details | <details>                         |

    Examples:
      | value | from                | until               | status | details                                                     |
      | 110   | 2023-11-28 11:00:00 | 2023-11-29 11:00:00 | 400    | discountRequestDto.value must be less than or equal to 100  |
      | -10   | 2023-11-28 11:00:00 | 2023-11-29 11:00:00 | 400    | discountRequestDto.value must be greater than or equal to 0 |
      | 10    |                     | 2023-11-28 11:00:00 | 400    | discountRequestDto.from must not be null                    |
      | 10    | 2023-11-28 11:00:00 |                     | 400    | discountRequestDto.until must not be null                   |