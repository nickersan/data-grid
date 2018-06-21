Feature: List Create and Get
  Tests creating a basic flat list and getting that list

  Scenario:

    When a new list called people is created
    When Nick is added to the list people
    When Sarah is added to the list people
    When Mia is added to the list people
    When Nina is added to the list people

    Then the list people contains Nick, Sarah, Mia, Nina
