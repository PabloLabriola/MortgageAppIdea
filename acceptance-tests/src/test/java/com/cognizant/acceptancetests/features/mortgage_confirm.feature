Feature: Mortgage confirm
  The service has to validate the mortgage id .
  If id does not exist , the service will return an error.
  Once the mortgage is confirmed , the status of the mortgage will changed to confirmed .
  Other mortgages linked to the same request will be cancelled
  If the customer tries to confirm a cancelled mortgage
  The system will return an error

Background: Company is authenticated
Given 3rd party provides correct credentials
Then 3rd party get authenticated


Scenario: A third party has an existing mortgage id tries to confirm a mortgage
        the status of mortgage changes to confirmed
        and other mortgages linked to the same request will be cancelled
  Given A third party has an existing mortgage id
  When Tries to confirm that mortgage
  Then Status of mortgage changes to confirmed
  Then Other mortgages linked to the same request will be cancelled

Scenario: A third party has a cancelled mortgage id tries to confirm a mortgage the system
        returns a 409 error
  Given A third party has a cancelled mortgage id
  When Tries to confirm that mortgage
  Then The system returns a 409 error


Scenario: A third party has a non existing mortgage id tries to confirm a mortgage
          the service will return a 404 error
  Given A third party has a non existing mortgage id
  When Tries to confirm that mortgage
  Then The service will return a 404 error

Scenario: A third party has an invalid mortgage id tries to confirm that mortgage the service will
          return a 400 error

  Given A third party has an invalid mortgage id
  When Tries to confirm that mortgage
  Then The service will return a 400 error
