/* table Transitions definitions */
/* table Transitions definitions */
CREATE TABLE `Transactions` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`transactionDate`	TEXT NOT NULL CHECK(date(transactionDate) IS NOT NULL),
	`type`	CHAR NOT NULL CHECK(type='R' OR type='C' OR type='P'),
	`boxQuantity`	INTEGER NOT NULL,
	`employeeID`	INTEGER NOT NULL,
	`beverageID`	INTEGER NOT NULL,
	`amount`	REAL NOT NULL,
	`fromAccount`	NUMERIC NOT NULL CHECK(fromAccount=0 OR fromAccount=1)
);/* table Employee definitions */
CREATE TABLE `Employee` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL,
	`surname`	TEXT NOT NULL,
	`credit`	REAL NOT NULL DEFAULT 0 CHECK(credit >= 0)
);
/* table Beverage definitions */
CREATE TABLE `Beverage` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`quantityAvaiable`	INTEGER NOT NULL DEFAULT 0 CHECK(quantityAvaiable>=0),
	`price`	REAL NOT NULL CHECK(price>0),
	`capsulePerBox`	INTEGER NOT NULL CHECK(capsulePerBox > 0),
	`name`	TEXT NOT NULL UNIQUE
);
/* table LaTazza definitions */
CREATE TABLE `LaTazza` (
	`balance`	REAL NOT NULL DEFAULT 0 CHECK(balance>=0)
);
<<<<<<< HEAD

=======
>>>>>>> 1416d90b65cf74039ccc56d1b9607d945b5745d8
