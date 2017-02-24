# PayPal Income Totaler

This program totals up payments that were copy and pasted from PayPal's activity section into a file. This makes it quick and efficient to total up how much was received from each person over a period of time. Income is sorted in decending order. Amounts under $50 from a single sender are totaled into "Other Income" and then displayed one by one at the bottom. The program converts GBP and EUR into USD for "Other Income" and total. Any others payments are displayed but ignored in the total and "Other Income". 

# Sample Output

![Output Example](https://github.com/LostInVelvet/PayPal-Income-Totaler/blob/master/output.JPG)

# Sample Input

TwoPayment Received<br>
DEC292016<br>
TwoPayment Received<br>
Loading transaction details forTwoPayment Received<br>
\+ $10.00<br>
OnePayment Received<br>
DEC292016<br>
OnePayment Received<br>
Loading transaction details forOnePayment Received<br>
\+ $15.00<br>
ThreePayment Received<br>
DEC292016<br>
ThreePayment Received<br>
Loading transaction details forThreePayment Received<br>
\+ $10.00<br>
.<br>
.<br>
.<br>

# Notable Points

* Compiles and runs in Netbeans
* Reads from a file /src/paypal-income.txt (/src/ was required for it to run properly in NetBeans
* Outputs to file Income-Report.txt
* Sorts income from largest to smallest
* All income amounts under $50 per sender are totalled into "Other Income".
