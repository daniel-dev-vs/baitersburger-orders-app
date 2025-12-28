Feature: Get Order

  Scenario: Get an order by id successfully
    Given That exists an order registered with ID "13c42ec8-4786-4e3b-8b34-f7a7bde2aea5"
    When I send a GET request for "/orders"
    Then The response status should be 200
    Then The response body should be validated against "schemas/getOrder200Schema.json"

  Scenario: Get an order by id that does not exist
    Given That does not exist an order registered with ID "33c42ec8-4786-4e3b-8b34-f7a7bde2aea"
    When I send a GET request for "/orders"
    Then The response status should be 404
    Then The response body should be validated against "schemas/getOrder404Schema.json"

  Scenario: Get an order by an invalid request
    When I send a GET request for "orderss/invalid-id"
    Then The response status should be 500
    Then The response body should be validated against "schemas/getOrder500Schema.json"
