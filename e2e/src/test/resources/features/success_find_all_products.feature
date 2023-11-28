Feature: Find all Products

  Scenario:
    Given Product Service is up and running

    And Product endpoint "/api/v1/products" with http method "GET" available

    When a client wants find products by filter:
      | name          | Test |
      | active        | true |
      | sortBy        | NAME |
      | directionSort | DESC |
      | pageNumber    | 0    |
      | pageSize      | 10   |


    Then response code is 200

    And response body contains:
      | totalPages    | 1    |
      | totalElements | 2    |
      | sorted        | true |
      | pageSize      | 10   |
      | pageNumber    | 0    |