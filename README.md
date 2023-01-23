# CreditCardRewardsCalculator
Capital One Technical Assessment.

Program takes in a list of transactions for the month and outputs the maximum reward points for each transaction as well as the maximum total points for the month.

The following rules are used to calculate points:

● Rule 1: 500 points for every $75 spend at Sport Check, $25 spend at Tim Hortons
and $25 spend at Subway

● Rule 2: 300 points for every $75 spend at Sport Check and $25 spend at Tim
Hortons

● Rule 3: 200 points for every $75 spend at Sport Check

● Rule 4: 150 points for every $25 spend at Sport Check, $10 spend at Tim Hortons
and $10 spend at Subway

● Rule 5: 75 points for every $25 spend at Sport Check and $10 spend at Tim
Hortons
● Rule 6: 75 point for every $20 spend at Sport Check

● Rule 7: 1 points for every $1 spend for all other purchases (including leftover
amount)

Note, some rules combine multiple transactions to total a larger sum of points (eg. Rule 1 requires at least 3 transactions), in this solution rewards calculated at the transaction level are considered in isolation, but the total rewards for the month will utilize combinations of transactions to maximize points.

Sample input:

{
"T01": {"date": "2021-05-01", "merchant_code" : "sportcheck", "amount_cents": 21000},
"T02": {"date": "2021-05-02", "merchant_code" : "sportcheck", "amount_cents": 8700},
"T03": {"date": "2021-05-03", "merchant_code" : "tim_hortons", "amount_cents": 323},
"T04": {"date": "2021-05-04", "merchant_code" : "tim_hortons", "amount_cents": 1267},
"T05": {"date": "2021-05-05", "merchant_code" : "tim_hortons", "amount_cents": 2116},
"T06": {"date": "2021-05-06", "merchant_code" : "tim_hortons", "amount_cents": 2211},
"T07": {"date": "2021-05-07", "merchant_code" : "subway", "amount_cents": 1853},
"T08": {"date": "2021-05-08", "merchant_code" : "subway", "amount_cents": 2153},
"T09": {"date": "2021-05-09", "merchant_code" : "sportcheck", "amount_cents": 7326},
"T10": {"date": "2021-05-10", "merchant_code" : "tim_hortons", "amount_cents": 1321}
}

