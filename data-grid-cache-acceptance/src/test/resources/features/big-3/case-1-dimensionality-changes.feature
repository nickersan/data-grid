Feature: Case 1 - Dimensionality Changes
  Tests simple calculation that involves crossing dimensionality to obtain values

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

    When a line-item called sales is created against 2018
    When the following 1-dimensional integer values are added to the line-item sales:
    |Jan|Feb|Mar|Apr|May|Jun|
    |10 |11 |9  |12 |8  |13 |

    Then the a grid created with 2018 on the x-axis and sales on the y-axis contains:
    |2018|Q1|Jan|Feb|Mar|Q2|Apr|May|Jun|
    |63  |20|10 |11 |9  |33|12 |8  |13 |