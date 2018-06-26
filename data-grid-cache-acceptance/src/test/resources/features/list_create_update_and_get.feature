Feature: List Create, Update and Get
  Tests creating and updating a basic flat list and getting that list

  Scenario:

    When a new list called people is created
    When Nick is added to the list people
    When Sarah is added to the list people
    When Mia is added to the list people
    When Nina is added to the list people

    Then the list people contains Nick, Sarah, Mia, Nina
    Then the list people at version 3 contains Nick, Sarah

    When Nick is updated to Dad

    Then the list people contains Dad, Sarah, Mia, Nina