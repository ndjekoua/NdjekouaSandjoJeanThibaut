package it.polito.latazza.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

import it.polito.latazza.exceptions.BeverageException;
import it.polito.latazza.exceptions.DateException;
import it.polito.latazza.exceptions.EmployeeException;
import it.polito.latazza.exceptions.NotEnoughBalance;
import it.polito.latazza.exceptions.NotEnoughCapsules;

public interface DataInterface {

	// --------------- FR1 --------------- //
	
	/**
	 * Record that an employee has used some capsules of a certain beverage.
	 * 
	 * @param employeeId       the identifier of the employee
	 * @param beverageId       the identifier of the beverage
	 * @param numberOfCapsules the number of consumed capsules
	 * @param fromAccount      if true the employee pays with her account, if false
	 *                         she pays with cash
	 * @return the updated amount of the account in cents
	 * @throws EmployeeException if the identifier of the employee is not valid
	 * @throws BeverageException if the identifier of the beverage is not valid
	 * @throws NotEnoughCapsules if the number of available capsules is insufficient
	 */
	public Integer sellCapsules(Integer employeeId, Integer beverageId, Integer numberOfCapsules, Boolean fromAccount)
			throws EmployeeException, BeverageException, NotEnoughCapsules;
    //TODO: create object transaction and add it to the DB
	// --------------- FR2 --------------- //
	/**
	 * Record that a visitor has used some capsules of a certain beverage.
	 * 
	 * @param beverageId       the identifier of the beverage
	 * @param numberOfCapsules the number of consumed capsules
	 * @throws BeverageException if the identifier of the beverage is not valid
	 * @throws NotEnoughCapsules if the number of available capsules is insufficient
	 */
	public void sellCapsulesToVisitor(Integer beverageId, Integer numberOfCapsules)
			throws BeverageException, NotEnoughCapsules;
   //TODO: create an object transaction with the attribute isEmployye==false and employeeId==-1
	// --------------- FR3 --------------- //
	/**
	 * Record that an employee has recharged on her account a certain amount in
	 * cents.
	 * 
	 * @param id            the identifier of the employee
	 * @param amountInCents the amount of the transaction in cents
	 * @return the updated amount of the account in cents
	 * @throws EmployeeException if the identifier is not valid
	 */
	public Integer rechargeAccount(Integer id, Integer amountInCents) throws EmployeeException;

	// --------------- FR4 --------------- //
	/**
	 * Record that some boxes of a beverage have been received and paid for.
	 * 
	 * @param beverageId  the identifier of the beverage
	 * @param boxQuantity the number of boxes
	 * @throws BeverageException if the identifier is not valid
	 * @throws NotEnoughBalance  if the shared balance is insufficient
	 */
	public void buyBoxes(Integer beverageId, Integer boxQuantity) throws BeverageException, NotEnoughBalance;
      //TODO: update balance.
	// --------------- FR5 --------------- //
	/**
	 * Produce a report about consumption and recharges of an employee.
	 * 
	 * The string format for a consumption payed by cash is "[datetime] CASH
	 * [employee] [beverageName] [numberOfCapsules]".
	 * 
	 * The string format for a consumption payed with the balance is "[datetime]
	 * BALANCE [employee] [beverageName] [numberOfCapsules]".
	 * 
	 * The string format for a recharge is "[datetime] RECHARGE [employee]
	 * [amount]".
	 * 
	 * The string format for [datetime] is "yyyy-MM-dd HH:mm:ss". The string format
	 * for [employee] is "[employeeName] [employeeSurname]". The string format for
	 * [amount] is "%.2f \u20ac".
	 * 
	 * @param employeeId the identifier of the employee
	 * @param startDate  the start date of the report
	 * @param endDate    the end date of the report
	 * @return a list of strings
	 * @throws EmployeeException if the identifier of the employee is not valid
	 * @throws DateException     if the dates are not valid
	 */
	public List<String> getEmployeeReport(Integer employeeId, Date startDate, Date endDate)
			throws EmployeeException, DateException;

	// --------------- FR6 --------------- //
	/**
	 * Produce a report about all consumption, recharges, and capsules purchases.
	 * 
	 * The string format for a consumption payed by cash is "[datetime] CASH
	 * [employee] [beverageName] [numberOfCapsules]".
	 * 
	 * The string format for a consumption payed with the balance is "[datetime]
	 * BALANCE [employee] [beverageName] [numberOfCapsules]".
	 * 
	 * The string format for a consumption of a visitor is "[datetime] VISITOR
	 * [beverageName] [numberOfCapsules]".
	 * 
	 * The string format for a recharge is "[datetime] RECHARGE [employee]
	 * [amount]".
	 * 
	 * The string format for a box purchase is "[datetime] BUY [beverageName]
	 * [boxQuantity]".
	 * 
	 * The string format for [datetime] is "yyyy-MM-dd HH:mm:ss". The string format
	 * for [employee] is "[employeeName] [employeeSurname]". The string format for
	 * [amount] is "%.2f \u20ac".
	 * 
	 * @param startDate the start date of the report
	 * @param endDate   the end date of the report
	 * @return a list of strings
	 * @throws DateException if the dates are not valid
	 */
	public List<String> getReport(Date startDate, Date endDate) throws DateException;

	// --------------- FR7 --------------- //
	/**
	 * Create a new beverage, given its name, the number of capsules per box and the
	 * price of the box.
	 * 
	 * @param name           the name of the beverage
	 * @param capsulesPerBox the number of capsules per box
	 * @param boxPrice       the price of a box in cents
	 * @return a unique identifier of the beverage
	 * @throws BeverageException if the beverage cannot be created
	 */
	public Integer createBeverage(String name, Integer capsulesPerBox, Integer boxPrice) throws BeverageException;
     //TODO: we should initialize all the others atributes
	/**
	 * Update the name, the number of capsules per box, and the box price of a
	 * beverage.
	 * 
	 * @param id             the identifier of the beverage
	 * @param name           the name of the beverage
	 * @param capsulesPerBox the number of capsules per box
	 * @param boxPrice       the price of a box in cents
	 * @throws BeverageException if the identifier is not valid
	 */
	public void updateBeverage(Integer id, String name, Integer capsulesPerBox, Integer boxPrice)
			throws BeverageException;
      //TODO: throws exption if price is greater or equal to 0 or the beverage does not exist. if in the db exist another beverage with the same name exception
	/**
	 * Get the name of a beverage.
	 * 
	 * @param id the identifier of the beverage
	 * @return the name of the beverage
	 * @throws BeverageException if the identifier is not valid
	 */
	public String getBeverageName(Integer id) throws BeverageException;

	/**
	 * Get the number of capsules per box of a beverage.
	 * 
	 * @param id the identifier of the beverage
	 * @return the number of capsules per box
	 * @throws BeverageException if the identifier is not valid
	 */
	public Integer getBeverageCapsulesPerBox(Integer id) throws BeverageException;

	/**
	 * Get the price of a box in cents.
	 * 
	 * @param id the identifier of the beverage
	 * @return the price of a box in cents
	 * @throws BeverageException if the identifier is not valid
	 */
	public Integer getBeverageBoxPrice(Integer id) throws BeverageException;

	/**
	 * Get a list containing the identifiers of the beverages.
	 * 
	 * @return a list of identifiers
	 */
	public List<Integer> getBeveragesId();

	/**
	 * Get a map with the identifiers of the beverages as keys and their names as
	 * values.
	 * 
	 * @return a map of identifiers and their corresponding strings
	 */
	public Map<Integer, String> getBeverages();

	/**
	 * Get the number of available capsules of a beverage. The initial number of
	 * capsules is zero.
	 * 
	 * @param id the identifier of the beverage
	 * @return the number of available capsules
	 * @throws BeverageException if the identifier is not valid
	 */
	public Integer getBeverageCapsules(Integer id) throws BeverageException;

	// --------------- FR8 --------------- //
	/**
	 * Create a new employee, given her name and surname.
	 * 
	 * @param name    the name of the employee
	 * @param surname the surname of the employee
	 * @return a unique identifier of the employee
	 * @throws EmployeeException if the employee cannot be created
	 */
	public Integer createEmployee(String name, String surname) throws EmployeeException;

	/**
	 * Update the name and the surname of an employee.
	 * 
	 * @param id      the identifier of the employee
	 * @param name    the name of the employee
	 * @param surname the surname of the employee
	 * @throws EmployeeException if the identifier is not valid
	 */
	public void updateEmployee(Integer id, String name, String surname) throws EmployeeException;

	/**
	 * Get the name of an employee.
	 * 
	 * @param id the identifier of the employee
	 * @return the name of the employee
	 * @throws EmployeeException if the identifier is not valid
	 */
	public String getEmployeeName(Integer id) throws EmployeeException;

	/**
	 * Get the surname of an employee.
	 * 
	 * @param id the identifier of the employee
	 * @return the surname of the employee
	 * @throws EmployeeException if the identifier is not valid
	 */
	public String getEmployeeSurname(Integer id) throws EmployeeException;

	/**
	 * Get the balance of an employee in cents. The balance of a new employee is
	 * zero.
	 * 
	 * @param id the identifier of the employee
	 * @return the balance of the employee in cents
	 * @throws EmployeeException if the identifier is not valid
	 */
	public Integer getEmployeeBalance(Integer id) throws EmployeeException;
     //TODO: we read the object inside the class Employye by calling method getEmployeeData() and then we return the balance;
	/**
	 * Get a list containing the identifiers of the employees.
	 * 
	 * @return a list of identifiers
	 */
	public List<Integer> getEmployeesId();

	/**
	 * Get a map with the identifiers of the employees as keys and the concatenation
	 * of their names and surnames separated by a space as values.
	 * 
	 * @return a map of identifiers and their corresponding strings
	 */
	public Map<Integer, String> getEmployees();

	/**
	 * Get the balance of the shared account in cents. The initial balance is zero.
	 * 
	 * @return the shared balance in cents
	 */
	public Integer getBalance();
      //TODO: get the value directly from the DB
	/**
	 * Clear all data structures and restore the initial status of the application,
	 * with no beverages and no employees. The shared balance must be zero and the
	 * log empty. This method is only used for testing purposes.
	 */
	public void reset();
    //TODO: Truncate table
}
