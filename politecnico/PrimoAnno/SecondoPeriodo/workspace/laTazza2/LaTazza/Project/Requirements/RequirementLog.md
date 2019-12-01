

# Inspection log

# Inspection Log
| | |
| ---------- | :----------: |
| Date | 10/04/2019 |
| Place | Polito |
| Object of inspection | Requirements document and UI prototype |
| Start time | 11.00 |
| End time | 12.30 |

## Participants and roles
| Role | Name |
| :--------: | :------: |
| Moderator | Simone Brigante |
| Author | Riccardo Mereu |
| Reader | Riccardo Mamone |
| Reader | Alberto Pellitteri |
| Reader | Emanuele Munafò |
| Reader | Vittorio Di Leo |

## Problems in requirements document
| Problem ID | Location | Description | Status | Type | Gravity |
| ----------- | :-------: | :-----------: | :-------: | :---: | :-------: |
| 1 | Context Diagram, Interfaces | No need of a visitor's interface to the system | Closed | Inconsistency | Major | 
| 2 | Interfaces | Add Manager as an actor | Closed | Omission | Normal |
| 3 | Glossary | Add payment type in class "Sale" | Closed | Omission | Normal | 
| 4 | Glossary | Fix constraints among Stakeholders "nb" specification and glossary class "Capsule Type" | Close | Inconsistency | Normal | 
| 5 | Glossary | Add possibility of checking and updating the total balance | Close | Omission | Normal | 
| 6 | Functional Requirements | FR6: Possibility of buying boxes for Employees/Visitors has not been considered elsewhere | Close | Incosistency | Minor | 
| 7 | Functional Requirements/Non Functional Requirements | FR8: payment method "retained on the pay slip" not considered in other parts of the document, NFR14 needs to be modified too | Close | Inconsistency| Minor|
| 8 | Non Functional Requirements| Delete NFR1 | Close | Overspecification | Minor |
| 9 | Functional Requirements/Non Functional Requirements | FR4 not coherent with the definition of credit, NFR7 needs to be modified too | Close | Inconsistency | Normal | 
| 10 | Non Functional Requirements | NFR12 is redoundant with respect to NFR13,NFR12 type isn't "domain" | Close | Ambiguity | Minor |
| 11 | Use Case Diagram | Actors: Employee/Visitors shouldn't be connected with "sell capsule" or "mange credit and debt" use cases | Close | Incorrect fact | Major |
| 12 | Use Case | Missing a use case for authentication to the system | Close | Incompleteness | Normal |
| 13 | Use Case | Use case 7: inconsistency in payment history and last log in date, these actions aren't handled in other parts of this document | Close | Inconsistency | Minor |
| 14 | Scenarios | Scenario 1: Missing balance update in postcondition | Close | Omission | Minor|

## Problems in UI prototype

| Problem ID | Location | Description | Status | Type | Gravity |
| ----------- | :-------: | :-----------: | :-------: | :---: | :-------: |
| 1 | Administrative's page | Recharge should specify that it is connected to a box order | Close | Ambiguity | Minor |
| 2 | Administrative's page | Missing button to manage order status | Close | Omission | Normal |
