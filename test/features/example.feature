Feature: Example cucumber feature

  Documents how testing could look like in an ideal world where Clojurians
  actually were pro-tests.

  Background:
    Given the following currency prices in dollars
      | EUR | 1.08 |
      | CHF | 1.12 |

  Scenario: Getting shortchanged
    When I order a 19.00 EUR coffee from Ben Rahim
    Then I have wasted $20.52
