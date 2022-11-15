Feature: Security mechanism
  As security officer, I want a security mechanism in the system,
  so the services are only reachable once 3rd party has been authenticated

  Scenario: 3rd party authenticates with correct credentials
    Given 3rd party provides correct credentials
    Then 3rd party get authenticated

  Scenario: 3rd party authenticates with incorrect credentials
    Given 3rd party provides incorrect credentials
    Then 3rd party doesn't get authenticated
