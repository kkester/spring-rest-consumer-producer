@integration
Feature: Integration Account is credited with amount

  Scenario: Credit amount
    Given account balance is 10.0
    When the account is credited with 20.0
    Then account should have a balance of 30.0