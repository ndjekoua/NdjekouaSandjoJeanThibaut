# Unit Testing Documentation template

Authors:

Date:

Version:



# Contents

- [Black Box Unit Tests](#black-box-unit-tests)



- [White Box Unit Tests](#white-box-unit-tests)

  

# Black Box Unit Tests

```
<Define here criteria, predicates and the combination of predicates for each function of each class.
Define test cases to cover all equivalence classes and boundary conditions.
In the table, report the description of the black box test case and the correspondence with the JUnit black box test case name/number>
```



### Class EventsQueue 



**Criteria for method push:**
	

- Sign of timeTag

- Type of timeTag

- There are equal time tags

- Number of time tags

  

**Predicates for method push:**

| Criteria                  | Predicate    |
| ------------------------- | ------------ |
| Sign of time tag          | > 0          |
|                           | <=0          |
| Type of time tag          | Integer      |
|                           | Char         |
|                           | String       |
|                           | Float        |
| There are equal time tags | Yes          |
|                           | No           |
| Number of time tags       | 0 to 100.000 |
|                           | > 100.000    |



**Boundaries for method push**:

| Criteria            | Boundary values             |
| ------------------- | --------------------------- |
| Sign of time tag    | Minint, 0, maxint           |
| Number of time tags | 0, 1, 99999, 100000, 100001 |





 **Combination of predicates for method push**

| Type of time tag | Sign of time tag | There are equal time tags | Number of time tags | Valid/Invalid | Description of the test case                                 | JUnit test case                              |
| ---------------- | ---------------- | ------------------------- | ------------------- | ------------- | ------------------------------------------------------------ | -------------------------------------------- |
| Integer          | Positive         | no                        | 0 to 100000         | V             | push(10)<br />pop() -> 10                                    | com.polito.converter.<br />blackboxtests.tc1 |
|                  |                  |                           | more than 100.000   | I             | for (100.000 times) { push(i); i++ }<br />push(20) -> QueueOverflow | com.polito.converter.<br />blackboxtests.tc2 |
|                  |                  | yes                       | 0 to 100000         | V             | push(10)<br />push(10)<br />push(1000)<br />push(1000)<br />pop()  -> 10<br />pop() -> 1000 | ...                                          |
|                  |                  |                           | more than 100.000   | I             | for (100.000 times) { push(20; i++ }<br />push(20) -> QueueOverflow | ...                                          |
|                  | Negative         | no                        | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
| Char             | Positive         | no                        | 0 to 100000         | V             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | V             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  | negative         | no                        | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
| string           | positive         | no                        | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  | negative         | no                        | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
| float            | positive         | no                        | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | no                        | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |
|                  |                  | yes                       | 0 to 100000         | I             | ...                                                          | ...                                          |
|                  |                  |                           | more than 100.000   | I             | ...                                                          | ...                                          |



# White Box Unit Tests

### Test cases definition

```
<Report here all the created JUnit test cases, and the units/classes they test >
```

| Unit name | JUnit test case                              |
| --------- | -------------------------------------------- |
| Converter | com.polito.converter.<br />whiteboxtests.tc1 |
| Converter | com.polito.converter.<br />blackboxtests.tc2 |
|           |                                              |

### Code coverage report

```
<Add here the screenshot report of the code and branch coverage obtained using
the Jacoco tool. >
```

### Loop coverage analysis

```
<Identify significant loops in the units and reports the test cases
developed to cover zero, one or multiple iterations >
```

| Unit name | Loop rows | Number of iterations | JUnit test case                              |
| --------- | --------- | -------------------- | -------------------------------------------- |
| Converter | 7-11      | 0                    | com.polito.converter.<br />whiteboxtests.tc1 |
|           |           | 1                    | com.polito.converter.<br />whiteboxtests.tc2 |
|           |           | 2+                   | com.polito.converter.<br />whiteboxtests.tc4 |
| ...       | ...       | ...                  | ...                                          |



