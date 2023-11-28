Feature: Deactivate discount for group of products

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products/discounts" with http method "DELETE" available

    When a client wants deactivate discount for group of products with ids:
      | productIds | 2, 3 |

    Then response code is 200

    And response body contains:
      | discountValue |  |
