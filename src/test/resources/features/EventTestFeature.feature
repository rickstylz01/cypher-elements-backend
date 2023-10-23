Feature: Event API Endpoints

  Scenario: Create a new event
    Given The create event endpoint is "/api/events/create/"
    When the client makes a POST request with event details
    Then the response status code should be 201
    And the response should contain the created event details