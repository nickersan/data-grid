Feature: Tree Create and Get
  Tests creating and updating a tree and getting that list

  Scenario:

    When a new list called 2018 is created

    When Q1 is added to the list 2018
    When Jan is added to the list Q1
    When Feb is added to the list Q1
    When Mar is added to the list Q1

    When Q2 is added to the list 2018
    When Apr is added to the list Q2
    When May is added to the list Q2
    When Jun is added to the list Q2

    When Q3 is added to the list 2018
    When Jul is added to the list Q3
    When Aug is added to the list Q3
    When Sep is added to the list Q3

    When Q4 is added to the list 2018
    When Oct is added to the list Q4
    When Nov is added to the list Q4
    When Dec is added to the list Q4

    Then the flattened list 2018 contains Q1, Jan, Feb, Mar, Q2, Apr, May, Jun, Q3, Jul, Aug, Sep, Q4, Oct, Nov, Dec