# Requirements Document Template

**Authors:**  
Elia Migliore @s228279,  
Jean Thibaut Ndjekoua Sandjo @s256770,  
Riccardo Mereu @s265599,  
Roy Paulin Justo Nguetsop Kenfack Djouaka @s257855

**Date:**

**Version:**

# Contents

- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces)

- [Stories and personas](#stories-and-personas)

- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)

# Stakeholders
| Stakeholder name  | Description |
| ----------------- |:-----------:|
|	Employee	| They are the employees of the company and they can use the application LaTazza to manage their credit in order to buy capsules and check the inventory|
|	Visitor	    | They may be either clients of the company or friends or family of any employee which can buy a capsule to make coffee |
|	Manager	    | He is also an employee of the company but have been designated by the colleagues to manage the sale and supply of capsules |
|	Supply Company	| Need an access to the application in order to monitor orders of boxes of capsules from the Manager's company |

# Context Digram and interfaces

## Context Diagram

```plantuml
left to right direction
skinparam packageStyle rectangle

actor Employee as e
actor "Supply Company" as sc
actor Bank as b
actor Manager as m

rectangle system{
  (LaTazza) as lt
  e--lt
  m--lt
  lt--b
  lt--sc
}
```

## Interfaces
| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
| Employee      | GUI to give the possibility to the customer to check availabily of the capsules, view the prices and to manage his account (view remaining credit and charge it, view list of expenses) | Touch screen |
| Bank|API to interact with the bank in order to perform the debit in case the customer buy by credit card;Date should be sent using a defined format(either json or xml are good choices but xml is more easy to understand and reduces the errors due to the xmlsd that validates the data before the processing) |not needed
| Supply company | Assuming the supply company has it own system to handle supplies,we should provide some API to interact with that system in order to facilitate supply activities. | not needed

# Stories and personas

### Persona 1  
**Paolo Capaldo**  
53, Software Architect in the company, father of six children

As the oldest employee of the company, Paolo has been chosen by his colleagues to monitor and manage the finance related to the coffee maker.  
Each employee has a different taste on coffee, in Italy coffee is very important and a developer without coffee cannot produce valid and correct code.  
Each year some money related to the capsules go lost and Paolo must pay from his own pocket. Also, the supply process is now handled by email and Paolo loses so much time in doing this. He also needs a way to reuse the previous order requests in order to reduce the time spent doing this job and increase the efficiency.

### Persona 2
**Franco Ruggieri**        
 26,Developer in the company,bachelor and has nor children

As a developer in the company,Franco drinks a coffee every morning in order to be awake during the day.He also takes another cup of coffee after the break in the afternoon for a better digestion and may be another one in the evening to fight against the tiredness.The only way to buy capsule now in order to make coffee is by cash and doing this way,Franco is not able to control how much he spends on coffee in a month. Futhermore since capsules are bougth to the manager,sometimes it happens that Franco want to buy a capsule but they are finished.So he would like to have a way to control his coffee's expenses and also to check the avilability of capules before going to the manager in order to not waste his time if thre are not available.
### Persona 3
**Gianfranco Fantozzi**  
37, Employee at CoffeeHouse's sales department

Gianfranco works at the sales department of CoffeeHouse, and his main task is to manage the orders from small companies who want to buy their capsules. Right now the company manages the orders through emails, so he has to check frequently the email to process every single order.
The clients usually are not very detailed in their orders so he has to reply with other emails in order to clarify the items in the order or even call them.

**Goal:**  
* Wants to check all the orders in a single easy to use interface.  
* Wants an unambiguous order with the real names of the products.  
* Wants to avoid to use email and phone calls as much as possible

**Story**
It's Monday morning and Gianfranco logs in the system. In the LaTazza interface, he is able to see the pending orders to prepare. Then, he begins to process the first order and, in the same interface, he can read which and how many boxes prepare. He collects all the capsules' boxes and prepares the shipment. When all the boxes are ready to be sent, he can close the shipping package and mark the order as sent.

# Functional and non functional requirements

## Functional Requirements
| 	ID		|	Description	|
| ------------- |:-------------|
|	FR1		|	Agent Manager sell capsules to agent employee or to agent visitor |  
|	FR2		|	Agent Manager order boxes of capsule from supply company |
|	FR3		|	Agent Manager increment or decrement credit of an Employee |
|	FR4		| 	Agent Manager increment or decrement the debt of an Employee |
|	FR5		|	Agent Manager check the inventory (product availability and product price) |
|	FR6		| 	Agent Employee or Agent Visitor buy Capsule  |
|	FR7		|	Agent Employee get his/her balance |
|	FR8		|	Employee buy credit by cash, credit card |
|   FR9     |   Agent Manager update the status of an order|
|   FR10    |   Agent Manager logs in and check pending orders|
|   FR11    |   Agent Manager Create or delete a user|


## Non Functional Requirements


| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| :-----:|
|  NFR1     | Efficiency | F1, the interaction with the db in this function must be less than 1sec  | FR1 |
|  NFR2     | Reliability | the number of capsule must never be less than 0 | FR1 |
|  NFR3     | Usability | the action to be done must require less than 3 screen changes  | FR1,FR2,FR3,FR4,FR5,FR6,FR7,FR8 |
|  NFR4     | Maintainability | the interaction with the bank for the credit card must me done with REST paradigm | FR8 |
|  NFR5     | Portability | the code has to be written in java, so it is independent from the machine  | FR1,FR2,FR3,FR4,FR5,FR6,FR7,FR8 |
|  NFR6     | Efficiency  | F3 in less than 0.5sec  | FR3 |
|  NFR7     | Efficiency | F4 in less than 0.5sec | FR4 |
|  NFR8     | Efficiency | F5 in less than 0.5sec | FR5 |
|  NFR9     | Efficiency | F7 in less than 0.5sec  | FR7 |
|  NFR10    | Efficiency | F8 in less than 10sec (time more hight because the interaction with the bank api| FR8 |
|  NFR11    | Domain | the Currency is Euro € | FR1,FR2,FR3,FR4,FR5,FR6,FR7,FR8 |
|  NFR12    | Reiability | the credit must be always >= 0€  | FR1,FR2,FR3,FR4,FR5,FR6,FR7,FR8 |
|  NFR13    | Reliability | the software must check if at the end of buy the credit is >=-10€ | FR1 |
|  NFR14    | Reliability | if the Employee buy credit throw credit card the system must check that the card is valid  | FR8 |
| NFR15 | Performance| if for conclude a transaction the credit is not enought the manager can force the transaction making a dept for the account | FR4 |
| NFR16 | Reliability | before increment the credit the system must check that the debt is 0€ | FR3 |
| NFR17 | Domain | the debt must be always >=0€ and always <=10€ | FR4 |
| NFR18 | Usability | the debt must be shown in the UI as negative number in the same place where the credit is shown  | FR1,FR2,FR3,FR4,FR5,FR6,FR7,FR8 | 

# Use case diagram and use cases

## Use case diagram
```plantuml

actor Manager as m
actor Employee as e
actor "supply company" as s
actor visitor as v
actor bank as b


(Sell capsule) as sc
(manage credit and debt) as mcd
(check cash account) as cca
(buy boxes of capsule) as bbc
(check inventory) as ci
(select employee) as se
(choose beverage type) as cbt
(select number of capsule) as snc
(check order) as co
(check debt) as cd
(check balance) as cb
(supply capsules ) as sca
(send deliverer) as sd
(check local account) as cla
(buy credits from account) as bcfa

m -- sc
m -- bbc
m -- mcd
e -- mcd
b -- bcfa
e -- bcfa

e <|-- m
sc .> se : include
sc .> cbt : include
sc .> snc : include
bbc .> cca : include
bbc .> ci : include
sc .> ci : include
sc .> cb : include
sca .> co : include
sca .> sd : include
cb .> cd : extend
cb .> cca : extend

sc -- e
bbc -- s
sc -- v
b -- bbc
s -- sca
sca -- b
e -- cla
```

## Use Cases

### Use case 1, Sell capsule
| Actors Involved   | Manager, Employee, Visitor |
| ------------- |:-------------:|
|  Nominal Scenario | The system shows details about the Sale, the Manager fill the empty fields with the information given by the client (Employee/Visitor), he/she clicks 'Sell' button and the system automatically updates the number of capsules and the balance  |
|  Variants         | An Employee initially wants to pay using Credit, but after he changes idea / A Visitor wants to buy some capsules, but notice after he/she does not have cash, so the Manager must cancel the operation/ The debt threshold is reached so the operation is stopped |

### Use case 2, Buy boxes of capsules
| Actors Involved        | Manager, Supply Company |
| ------------- |:-------------:|
|  Nominal Scenario | The system shows a summary about the inventory and LaTazza balance. The manager fills a small form with details about the quantity and the type of capsule to buy, then he clicks on 'Buy' to send the order to the Supply Company database and the Bank handles the money transaction |
|  Variants         | There is not enough money on LaTazza balance to do the order, so the system cancel the order and the manager must put some money in the bank account. |


### Use case 3, Manage credit and debt
| Actors Involved        | manager, employee |
| ------------- |:-------------:|
|  Nominal Scenario     | the employee wants to buy credits by cash , the manager takes the cash , fills a form with the employee's data, put the amount to add to the balance and when he has finished the system updates the employee's data   |
|  Variants     | the manager chose the wrong employee or the employee does not have enough money  |

### Use case 4, Supply capsules
| Actors Involved        | supply company, bank,manager |
| ------------- |:-------------:|
|  Nominal Scenario     |the supply company  has its own LaTazza interface, logs in, checks if capsule boxes have been ordered.Supply company send a deliverer to the manager accordingly to the order after receiving the payment notification from the bank.The supply company changes the state of the order request from "in process" to "sent".The manager receives the order. The order state passes from "sent" to "received". The Manager checks if everything is ok and changes the order state from "received" to "completed" |
|  Variants     |  The supply company sends an order which is not compliant with the manager request,so we should start the recorvery procedure. The Supply company did not received the payment so the order should be canceled.

### Use case 5, Check local account
| Actors Involved        | employee |
| ------------- |:-------------:|
|  Nominal Scenario     |the employee can have a personal account on LaTazza and can access through an interface, watch the updated details about his credits, his payments history, and the last log in  date   |
|  Variants     |  the supply company sends the wrong order, so it has to cancel the previous delivery, and  rechecks the order |

### Use case 6, Buy credits from account
| Actors Involved        | employee,bank |
| ------------- |:-------------:|
|  Nominal Scenario     |the employee wants to buy some credits online,he accesses his local account , fill a form directly connected to the bank with his card number.the bank handles the payment and notifies the system which updates the employee's balance  watch the updated details about his credits, his payments history, and the last log in  date   |
|  Variants     |  the employee put the wrong card number so needs to go back |


# Relevant scenarios
## Scenario 1, Successful sale of Capsule to an Employee
| Scenario ID: SC1        | Corresponds to UC: Sell capsule |
| ------------- |:-------------:|
| Step#        | Description  |
|  Precondition     | At least one capsule is available for the requested type and the credit and debt are updated (balance) |  
|  Post condition   | The number of capsules for the selected type is updated and also the credit and the debt (balance) |
|  1     | the customer orders some capsules to the manager |  
|  2     | the manager clicks on "start a sell operation"  |
|  3     | the manager select  "employee" as customer's type |
|  4     | the system shows the inventory, the local  account data and a form to fill  |
|  5     | the manager checks the inventory |
|  6     | the manager selects the employee |
|  7     | the manager selects number of capsules|
|  8     | the manager selects beverage type|
|  9     | the manager selects the payment method  |
|  10     | the manager clicks on "sell" |
|  11     | system considers the capsules as sold, updates the remaining number and the credit and debt aswell |

## Scenario 2, Successful sale of Capsule to a Visitor

| Scenario ID: SC2        | Corresponds to UC: Sell capsule |
| ------------- |:-------------:|
| Step#        | Description  |
|  Precondition     | At least one capsule is available for the requested type |  
|  Post condition   | The number of capsules for the selected type is updated |
|  1     | the customer orders some capsules to the manager |  
|  2     | the manager clicks on "Manage Account and Sell Capsule"  |
|  3     | the manager select  "visitor" as customer's type |
|  3     | the system shows the inventory and a form to fill  |
|  4     | the manager checks the inventory |
|  7     | the manager selects number of capsules|
|  8     | the manager selects beverage type|
|  9     | the manager takes the cash from the visitor  |
|  10     | the manager clicks on "sell" |
|  11     | system considers the capsules as sold and updates the remaining number |


## Scenario 3, Successful order from Supply Company done by Manager

| Scenario ID: SC3        | Corresponds to UC: Buy boxes of capsules |
| ------------- |:-------------:|
| Step#        | Description  |
|  Precondition     | At least one capsule type with less than one remaining box (less than 50 capsules) |  
|  Post condition   | The order is sent to the Supply Company |
|  1     | the manager clicks on "Buy New Capsules for Inventory"  |
|  2     | the manager checks the inventory |
|  3     | the system shows the inventory and the cash account |
|  4     | the manager checks the cash account |
|  5     | the manager checks the inventory |
|  6     | the manager puts the number of boxes he wants to buy and the type of beverage |
|  7     | the manager clicks on "buy"|
|  8     | the bank handles the payment to the supply company|
|  9     |the system shows the order's summary |
|  10     | the  order is sent to the supply company |


## Scenario 4, Successful Order dispatch

| Scenario ID: SC4        | Corresponds to UC: Supply capsules |
| ------------- |:-------------:|
| Step#        | Description  |
|  Precondition     |the company has an account |  
|  Post condition     | the order is done |
|  1     | the supply company manager clicks on "show orders"  |
|  2     | the system displays the orders |
|  3     | the supply company manager chooses one |
|  4     | the system shows details about the chosen order |
|  5     | the bank handles the payment |
|  6     | the order  is set as "ongoing" |
|  7     | a deliverer is sent|
|  8     | the manager checks the delevery|
|  9     | the manager updates the order's state to completed|
|  10     | the system notifies the supply company|
|  11    | the order is set as  done in the supply company side|

## Scenario 5, Successful Employee's Credit update with cash

| Scenario ID: SC5        | Corresponds to UC: Manage credits and debt|
| ------------- |:-------------:|
| Step#        | Description  |
|  Precondition     | employee has an account |  
|  Post condition     | the employee's balance is updated |
|  1     | the manager clicks on "charge credits" |
|  2     | the manager takes the cash from the employee |
|  3     | the manager selects the client |
|  4     | the manager puts the amount |
|  5     | the manager clicks on "charge" |
|  6     | the system shows the charging operation details  |
|  7     | the system updates the employee's balance|

## Scenario 6, Successful Employee's Credit update from account 

| Scenario ID: SC6        | Corresponds to UC: Buy credit from account|
| ------------- |:-------------:|
| Step#        | Description  |
|  Precondition     |the employee has a local account |  
|  Post condition     | the employee balance is updated |
|  1     | the employee clicks on "charge credits" |
|  2     | the employee fills the relevant fields with his credit card details |
|  3     | the employee puts the amount |
|  4     | the employee clicks on "charge" |
|  5     | the bank handles the payment |
|  6     | the system shows the charging operation details  |
|  7     | the system updates the employee's balance|

# Glossary

```plantuml
class Visitor{
first name
last name
}

class Employee{
ID
first name
last surname
}

class Client{
}

class "Supply Company"{
ID
companyName
address
}

class Manager{
}

class Credit{
ID
lastUpdate
balance
}
class Capsule{
ID
price
}

class "Capsule Type"{
ID
name
}
class Box {
ID
price
}


class Inventory{
}

class Sale{
ID
typePayment
date
}
class Account {
    ID
    creationDate
    userName
    password
}

class Order{
ID
requestDate
sentDate
receiveDate
completedDate
state
}

Order "*" -- "1" Manager: performs
Order "1" -- "1..*" Box
"Supply Company" "1" -- "*" Order

Sale "1..*" -- "1" Manager: performs <
Sale "1..*" -- "1" Client: to >
Sale "1..*" -- "1..*" Capsule

Credit "1" -- "1" Employee: has <
Box -- “50” Capsule
Inventory -- "*" Capsule
Client <|-- Employee
Client <|-- Visitor

Employee <|-- Manager
Account -- Employee: has <
Manager "1" -- "0..*" Credit: manage

Capsule "0..*" -- "1" "Capsule Type": is of

note top of Inventory: It keeps track of the currently available Capsules

```

# System Design
```plantuml
class LaTazza{

}

class Server{
/' management Capsule '/
+sellCapsule()
/' SupplyCompany related functions '/
+checkPendingOrder()
+changeOrderStatus()
+makeOrder()
/' management Credit '/
+buyCredit()
+chargeCredit()
+showBalanceEmployee()
/' LaTazza balance'/
+showBalance()
/' BankingSystem related functions '/
+processPayment()
+login()
+logout()
+updateTotalBalance()
+createUser()
+deleteUser()
+updateCapsule()
+showInventory()
+accessSupplyHistory()
+changeOrderStatus()
}

class BankGateway{
+processPayment()
}

class SupplyCompanyInterface{
+checkPendingOrder()
+changeOrderStatus()
}

class ManagerInterface{
+sellCapsule()
+updateTotalBalance()
+chargeCredit()
+showBalance()
+showBalanceEmployee()
+showInventory()
+makeOrder()
+createUser()
+deleteUser()
+showSupplyHistory()
+changeOrderStatus()
}

class EmployeeInterface{
+buyCredit()
+showBalanceEmployee()
+showInventory()
}

class Database{
+updateCapsule()
+showCapsule()
+showSupplyHistory()
}


LaTazza o--  Server
Server -- BankGateway
Server -- ManagerInterface
Server -- EmployeeInterface
Server -- SupplyCompanyInterface
Server -- Database

```
