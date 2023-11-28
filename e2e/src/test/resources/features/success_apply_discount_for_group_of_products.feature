Feature: Apply discount for group of products

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/discounts" with http method "POST" available

    When a client wants apply discount for group of products with ids:
      | productIds | 2, 3                |
      | value      | 10                  |
      | from       | 2023-11-28 11:00:00 |
      | until      | 2023-12-31 11:00:00 |

    Then response code is 200

    And response body contains:
      | discountValue | 10                  |
      | from          | 2023-11-28 11:00:00 |
      | until         | 2023-12-31 11:00:00 |
