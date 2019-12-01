# Design Document Template

Authors:

Date:

Version:

# Contents

- [Package diagram](#package-diagram)
- [Class diagram](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design document has to comply with:
1. [Official Requirement Document](../Official\ Requirements\ Document.md)
2. [DataInterface.java](../src/main/java/it/polito/latazza/data/DataInterface.java)

UML diagrams **MUST** be written using plantuml notation.

# Package diagram

\<define UML package diagram >

\<explain rationales for choices> 

\<mention architectural patterns used, if any>
```plantuml
class latazza {


}

class data{

}

class exeptions {

}

class jdbc {
    
    
}
latazza -- data
latazza -- exeptions
latazza -- jdbc
```


# Class diagram

\<for each package define class diagram with classes defined in the package>

\<mention design patterns used, if any>
```plantuml
class DataImpl [[java:it.polito.latazza.data.DataImpl]] {
    +Integer sellCapsules(Integer employeeId, Integer beverageId, Integer numberOfCapsules, Boolean fromAccount)
    +void sellCapsulesToVisitor(Integer beverageId, Integer numberOfCapsules)
    +Integer rechargeAccount(Integer id, Integer amountInCents)
    +void buyBoxes(Integer beverageId, Integer boxQuantity)
    +List<String> getEmployeeReport(Integer employeeId, Date startDate, Date endDate)
    +List<String> getReport(Date startDate, Date endDate)
    +Integer createBeverage(String name, Integer capsulesPerBox, Integer boxPrice)
    +void updateBeverage(Integer id, String name, Integer capsulesPerBox, Integer boxPrice)
    +String getBeverageName(Integer id)
    +Integer getBeverageCapsulesPerBox(Integer id)
    +Integer getBeverageBoxPrice(Integer id)
    +List<Integer> getBeveragesId()
    +Map<Integer,String> getBeverages()
    +Integer getBeverageCapsules(Integer id)
    +Integer createEmployee(String name, String surname)
    +void updateEmployee(Integer id, String name, String surname)
    +String getEmployeeName(Integer id)
    +String getEmployeeSurname(Integer id)
    +Integer getEmployeeBalance(Integer id)
    +List<Integer> getEmployeesId()
    +Map<Integer,String> getEmployees()
    +Integer getBalance()
    +void reset()
}
interface DataInterface [[java:it.polito.latazza.data.DataInterface]] {
}
DataInterface <|.. DataImpl
interface DataInterface [[java:it.polito.latazza.data.DataInterface]] {
    Integer sellCapsules(Integer employeeId, Integer beverageId, Integer numberOfCapsules, Boolean fromAccount)
    void sellCapsulesToVisitor(Integer beverageId, Integer numberOfCapsules)
    Integer rechargeAccount(Integer id, Integer amountInCents)
    void buyBoxes(Integer beverageId, Integer boxQuantity)
    List<String> getEmployeeReport(Integer employeeId, Date startDate, Date endDate)
    List<String> getReport(Date startDate, Date endDate)
    Integer createBeverage(String name, Integer capsulesPerBox, Integer boxPrice)
    void updateBeverage(Integer id, String name, Integer capsulesPerBox, Integer boxPrice)
    String getBeverageName(Integer id)
    Integer getBeverageCapsulesPerBox(Integer id)
    Integer getBeverageBoxPrice(Integer id)
    List<Integer> getBeveragesId()
    Map<Integer,String> getBeverages()
    Integer getBeverageCapsules(Integer id)
    Integer createEmployee(String name, String surname)
    void updateEmployee(Integer id, String name, String surname)
    String getEmployeeName(Integer id)
    String getEmployeeSurname(Integer id)
    Integer getEmployeeBalance(Integer id)
    List<Integer> getEmployeesId()
    Map<Integer,String> getEmployees()
    Integer getBalance()
    void reset()
}
class LaTazza [[java:it.polito.latazza.LaTazza]] {
        - float balance
        + void updateBalance(float amount) throws NotEnoughBalance
    +{static}void main(String[] args)
}
class BeverageException [[java:it.polito.latazza.exceptions.BeverageException]] {
    -{static}long serialVersionUID
}
class Exception [[java:com.sun.tools.jdi.JDWP$Event$Composite$Events$Exception]] {
}
Exception <|-- BeverageException
class DateException [[java:it.polito.latazza.exceptions.DateException]] {
    -{static}long serialVersionUID
}
class Exception [[java:com.sun.tools.jdi.JDWP$Event$Composite$Events$Exception]] {
}
Exception <|-- DateException
class EmployeeException [[java:it.polito.latazza.exceptions.EmployeeException]] {
    -{static}long serialVersionUID
}
class Exception [[java:com.sun.tools.jdi.JDWP$Event$Composite$Events$Exception]] {
}
Exception <|-- EmployeeException
class NotEnoughBalance [[java:it.polito.latazza.exceptions.NotEnoughBalance]] {
    -{static}long serialVersionUID
}
class Exception [[java:com.sun.tools.jdi.JDWP$Event$Composite$Events$Exception]] {
}
Exception <|-- NotEnoughBalance
class NotEnoughCapsules [[java:it.polito.latazza.exceptions.NotEnoughCapsules]] {
    -{static}long serialVersionUID
}
class Exception [[java:com.sun.tools.jdi.JDWP$Event$Composite$Events$Exception]] {
}
Exception <|-- NotEnoughCapsules
class Employee {
   - int id
   - String name
   - String surname
   - float credit
   + float getEmployeecredit()
   + int getEmployeeID()
   + String getName()
   + String getSurname()
   + void updateCredit(float amount) throw EmployeeException /' the amount can be eighter positive (recharge credit) and negative (buy capsules) '/
}
class Beverage {
   - int id
   - int quantityAvaiable
   - float boxPrice
   + int getNumberOfBoxes()
   + int getQuantityAvaiable()
   - int capsulePerBox
   - String name
   + Beverage(float price,String name,int capsulePerBox)
   + void setPrice(float price) throws BeverageException
   + String getName()
   + void setName(String name) throws BeverageException
   + void setCapsulesPerBox(int caps)
   + int getCapsulesPerBox()
   + String getBeverageName()
   + float getBevaragePrice();
   + float getBeverageBoxPrice()
   + int getQuantityAvaiable()
   + void updateCapsuleQuantity(int quantity) throws BeverageException /' the number can be eighter positive (buy capsules) and negative (sell capsules) '/
}
class Transaction {
   - int id
   - Date transationDate
   -char type /*can be P=Purchase C=consumption R=Recharge*/
   -int boxQuantity
   -int beverageId
   -int employeeId
   -float amount
   -boolean fromAccount
}

/'class Purchase {
   -int id
   -int boxQuantity
   +int beverageId
}

class Consumption{
   -int id
   -boolean fromAccount
   -int employeeId
   -int BeverageId
   -fromAccount
}

class Recharge {
   -int id
   -float amount
   -int employeeId
}'/
class Database {
   + List<Employee> getListEmployee()
   + Employee getEmployeeData(int id) throws EmployeeException
   + List<Beverage> getListOfBeverage()
   + Beverage getBeverageData(int id) throws BeverageException
   + float getBalance()
   + void updateBalance(float balance)
   + List<Transaction> getEmployeeReport(int idEmployee,Date startDate,Date endDate)
   + List<Transaction> getReport(Date startDate,Date endDate)
   + void registerTransaction(Transaction transaction)
   + void addBeverage(Beverage bev) throw BeverageException
   + void addEmployee(Employee emp) throw EmployeeException
   + void truncateTables()
   + void updateBeverage(Beverage bev) throws BeverageException
   + void updateEmployee(Employee emp) throws EmployeeException
		 
}
LaTazza -- DataImpl
Exception -- DataImpl
DataImpl -- Database
DataImpl -- Transaction
DataImpl -- Beverage
DataImpl -- Employee
```


# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>


|  | Beverage | Transaction  | Employee  | DataImpl | Database   | LaTazza |  Exception|
| ------------- |:-------------:| -----:| -----:| -------:|-------:|-------:| -------:|
| Functional requirement 1  | x | x  | x | x| x| x | x|
| Functional requirement 2  |  x|  x| |x |x |x | x|
| Functional requirement 3  |  | x |x |x |x | |x |
| Functional requirement 4  |  x| x | | x|x |x |x |
| Functional requirement 5  |  |  x| x| x| x| | x|
| Functional requirement 6  |  | x | | x| x| | x|
| Functional requirement 7  | x |  | |x |x | |x |
| Functional requirement 8  |  |  |x | x| x| | x|


# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

## Sequence Diagram 1
```plantuml
skinparam sequenceArrowThickness 2
skinparam roundcorner 20
skinparam maxmessagesize 60
skinparam sequenceParticipant underline
actor Employee as em
actor Administrator as ad
participant "LaTazza" as tazza
participant "DataImpl" as di
participant "Database" as db
participant "Employee" as emp
participant "Beverage" as bev
em -> ad : request buy capsule
ad -> tazza : sellCapsule(id,beverageID,numberofcapsule,contant)
tazza -> di : sellCapsule(id,beverageID,numberofcapsule,contant)
di -> db : getEmployee(id)
db -> di : Employee
di -> db : getBeverage(id)
db -> di : Beverage
di -> emp : updateBalance(-cost)
di -> bev : updateCapsule(-buyed)
di -> db : updateEmployee(employee)
di -> db : updateBeverage(beverage)
di -> db : updateBalance(updatedBalance)
di -> tazza : creditUpdated
tazza -> ad : creditUpdated
ad -> em : creditUpdated
```
## Sequence Diagram 2
```plantuml
skinparam sequenceArrowThickness 2
skinparam roundcorner 20
skinparam maxmessagesize 60
skinparam sequenceParticipant underline
actor Employee as em
actor Administrator as ad
participant "LaTazza" as tazza
participant "DataImpl" as di
participant "Database" as db
participant "Employee" as emp
participant "Beverage" as bev
em -> ad : request buy capsule
ad -> tazza : sellCapsule(id,beverageID,numberofcapsule,contant)
tazza -> di : sellCapsule(id,beverageID,numberofcapsule,contant)
di -> db : getEmployee(id)
db -> di : Employee
di -> db : getBeverage(id)
db -> di : Beverage
di -> emp : updateBalance(-cost)
db -> di : EmployeeException, ROLLBACK
di -> tazza : EmployeeException
tazza -> ad : EmployeeException
ad -> em : Not enought credit
```
