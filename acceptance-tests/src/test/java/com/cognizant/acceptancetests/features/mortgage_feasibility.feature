Feature: Mortgage feasibility
  As a 3rd party, I want to be able to request the feasibility study
  for a new mortgage by providing the country, city, home price, down
  payment, length of loan and monthly incomes, so that I can capture
  the data from my clients and send the request.
  Only allowed subscribed parties
  All requests with monthly incomes  lower 100 euros are rejected.
  Requests with monthly incomes  upper 100 euros are approved.
  Requests with monthly incomes  equals 100 euros on-hold , to be manually reviewed.

  Background: Company is authenticated
    Given 3rd party provides correct credentials
    Then 3rd party get authenticated

  Scenario Outline: Mortgage feasibility study with monthly income upper 100 euros and home price lower 100000
    Given A client with monthly incomes <monthlyIncomes> and home price <homePrice>
    When A 3rd party request the feasibility study
    Then mortgage is approved and provides fixed and variable mortgage details

    Examples:
      | monthlyIncomes | homePrice |
      | 101.0          | 99999.9   |
      | 130.1          | 0.0       |


  Scenario Outline: Mortgage feasibility study with monthly income upper 100 euros and home price equal or higher 100000
    Given A client with monthly incomes <monthlyIncomes> and home price <homePrice>
    When A 3rd party request the feasibility study
    Then mortgage is approved and provides fixed mortgage details

    Examples:
      | monthlyIncomes | homePrice |
      | 101.1          | 100000.0  |
      | 130.1          | 100000.1  |

  Scenario Outline: Mortgage feasibility study with monthly income down 100 euros
    Given A client with monthly incomes <monthlyIncomes> and home price <homePrice>
    When A 3rd party request the feasibility study
    Then mortgage is rejected

    Examples:
      | monthlyIncomes | homePrice |
      | 0.0            | 100000.0  |
      | 50.0           | 100000.0  |
      | 99.0           | 100000.0  |

  Scenario Outline: Mortgage feasibility study with monthly income of 100 euros
    Given A client with monthly incomes <monthlyIncomes> and home price <homePrice>
    When A 3rd party request the feasibility study
    Then mortgage is on hold

    Examples:
      | monthlyIncomes | homePrice |
      | 100.0          | 100000.0  |
