package it.polito.latazza.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polito.latazza.exceptions.BeverageException;
import it.polito.latazza.exceptions.DateException;
import it.polito.latazza.exceptions.EmployeeException;
import it.polito.latazza.exceptions.NotEnoughBalance;
import it.polito.latazza.exceptions.NotEnoughCapsules;

public class TestDataImplOfficial {

	private static final String MARCO = "Marco";
	private static final String ROSSI = "Rossi";
	private static final String GIOVANNI = "Giovanni";
	private static final String BIANCHI = "Bianchi";
	private static final String COFFEE = "Coffee";
	private static final String TEA = "Tea";

	private DataInterface data;

	@BeforeEach
	public void init() {
		data = new DataImpl();
		data.reset();
	}

	@Test
	public void testCreateEmployee() throws EmployeeException {
		Integer id = data.createEmployee(MARCO, ROSSI);
		assertEquals(MARCO, data.getEmployeeName(id));
		assertEquals(ROSSI, data.getEmployeeSurname(id));
		assertEquals(Integer.valueOf(0), data.getEmployeeBalance(id));
	}

	@Test
	public void testCreateEmployeeId() throws EmployeeException {
		Integer firstId = data.createEmployee(MARCO, ROSSI);
		Integer secondId = data.createEmployee(GIOVANNI, BIANCHI);
		assertNotEquals(firstId, secondId);
	}

	@Test
	public void testCreateEmployeeNull() {
		assertThrows(EmployeeException.class, () -> data.createEmployee(null, null));
		assertThrows(EmployeeException.class, () -> data.createEmployee(MARCO, null));
		assertThrows(EmployeeException.class, () -> data.createEmployee(null, ROSSI));
	}

	@Test
	public void testCreateEmployeeEmpty() {
		assertThrows(EmployeeException.class, () -> data.createEmployee("", ""));
		assertThrows(EmployeeException.class, () -> data.createEmployee(MARCO, ""));
		assertThrows(EmployeeException.class, () -> data.createEmployee("", ROSSI));
	}

	@Test
	public void testGetEmployeeNull() {
		assertThrows(EmployeeException.class, () -> data.getEmployeeName(null));
		assertThrows(EmployeeException.class, () -> data.getEmployeeSurname(null));
		assertThrows(EmployeeException.class, () -> data.getEmployeeBalance(null));
	}

	@Test
	public void testGetEmployeeInvalid() {
		assertThrows(EmployeeException.class, () -> data.getEmployeeName(-1));
		assertThrows(EmployeeException.class, () -> data.getEmployeeSurname(-1));
		assertThrows(EmployeeException.class, () -> data.getEmployeeBalance(-1));
	}

	@Test
	public void testGetEmployeesId() throws EmployeeException {
		Integer firstId = data.createEmployee(MARCO, ROSSI);
		Integer secondId = data.createEmployee(GIOVANNI, BIANCHI);
		List<Integer> expected = new ArrayList<>();
		expected.add(firstId);
		expected.add(secondId);
		assertEquals(expected, data.getEmployeesId());
	}

	@Test
	public void testGetEmployeesIdEmpty() {
		List<Integer> expected = new ArrayList<>();
		assertEquals(expected, data.getEmployeesId());
	}

	@Test
	public void testGetEmployees() throws EmployeeException {
		Integer firstId = data.createEmployee(MARCO, ROSSI);
		Integer secondId = data.createEmployee(GIOVANNI, BIANCHI);
		Map<Integer, String> expected = new HashMap<>();
		expected.put(firstId, "Marco Rossi");
		expected.put(secondId, "Giovanni Bianchi");
		assertEquals(expected, data.getEmployees());
	}

	@Test
	public void testGetEmployeesEmpty() {
		Map<Integer, String> expected = new HashMap<>();
		assertEquals(expected, data.getEmployees());
	}

	@Test
	public void testUpdateEmployee() throws EmployeeException {
		Integer id = data.createEmployee(MARCO, ROSSI);
		data.updateEmployee(id, GIOVANNI, BIANCHI);
		assertEquals(GIOVANNI, data.getEmployeeName(id));
		assertEquals(BIANCHI, data.getEmployeeSurname(id));
	}

	@Test
	public void testUpdateEmployeeNull() throws EmployeeException {
		Integer id = data.createEmployee(MARCO, ROSSI);
		assertThrows(EmployeeException.class, () -> data.updateEmployee(null, GIOVANNI, BIANCHI));
		assertThrows(EmployeeException.class, () -> data.updateEmployee(id, null, null));
		assertThrows(EmployeeException.class, () -> data.updateEmployee(id, GIOVANNI, null));
		assertThrows(EmployeeException.class, () -> data.updateEmployee(id, null, BIANCHI));
		assertEquals(MARCO, data.getEmployeeName(id));
		assertEquals(ROSSI, data.getEmployeeSurname(id));
	}

	@Test
	public void testUpdateEmployeeEmpty() throws EmployeeException {
		Integer id = data.createEmployee(MARCO, ROSSI);
		assertThrows(EmployeeException.class, () -> data.updateEmployee(id, "", ""));
		assertThrows(EmployeeException.class, () -> data.updateEmployee(id, GIOVANNI, ""));
		assertThrows(EmployeeException.class, () -> data.updateEmployee(id, "", BIANCHI));
		assertEquals(MARCO, data.getEmployeeName(id));
		assertEquals(ROSSI, data.getEmployeeSurname(id));
	}

	@Test
	public void testUpdateEmployeeInvalid() {
		assertThrows(EmployeeException.class, () -> data.updateEmployee(-1, GIOVANNI, BIANCHI));
	}

	@Test
	public void testCreateBeverage() throws BeverageException {
		Integer id = data.createBeverage(COFFEE, 20, 500);
		assertEquals(COFFEE, data.getBeverageName(id));
		assertEquals(Integer.valueOf(20), data.getBeverageCapsulesPerBox(id));
		assertEquals(Integer.valueOf(500), data.getBeverageBoxPrice(id));
		assertEquals(Integer.valueOf(0), data.getBeverageCapsules(id));
	}

	@Test
	public void testCreateBeverageId() throws BeverageException {
		Integer firstId = data.createBeverage(COFFEE, 20, 500);
		Integer secondId = data.createBeverage(TEA, 50, 800);
		assertNotEquals(firstId, secondId);
	}

	@Test
	public void testCreateBeverageNull() {
		assertThrows(BeverageException.class, () -> data.createBeverage(null, null, null));
		assertThrows(BeverageException.class, () -> data.createBeverage(COFFEE, null, null));
		assertThrows(BeverageException.class, () -> data.createBeverage(null, 20, null));
		assertThrows(BeverageException.class, () -> data.createBeverage(null, null, 500));
		assertThrows(BeverageException.class, () -> data.createBeverage(COFFEE, 20, null));
		assertThrows(BeverageException.class, () -> data.createBeverage(null, 20, 500));
		assertThrows(BeverageException.class, () -> data.createBeverage(COFFEE, null, 500));
	}

	@Test
	public void testCreateBeverageEmpty() {
		assertThrows(BeverageException.class, () -> data.createBeverage("", 20, 500));
	}

	@Test
	public void testCreateBeverageInvalid() {
		assertThrows(BeverageException.class, () -> data.createBeverage(COFFEE, -1, 500));
		assertThrows(BeverageException.class, () -> data.createBeverage(COFFEE, 20, -1));
	}

	@Test
	public void testGetBeverageNull() {
		assertThrows(BeverageException.class, () -> data.getBeverageName(null));
		assertThrows(BeverageException.class, () -> data.getBeverageCapsulesPerBox(null));
		assertThrows(BeverageException.class, () -> data.getBeverageBoxPrice(null));
		assertThrows(BeverageException.class, () -> data.getBeverageCapsules(null));
	}

	@Test
	public void testGetBeverageInvalid() {
		assertThrows(BeverageException.class, () -> data.getBeverageName(-1));
		assertThrows(BeverageException.class, () -> data.getBeverageCapsulesPerBox(-1));
		assertThrows(BeverageException.class, () -> data.getBeverageBoxPrice(-1));
		assertThrows(BeverageException.class, () -> data.getBeverageCapsules(-1));
	}

	@Test
	public void testGetBeveragesId() throws BeverageException {
		Integer firstId = data.createBeverage(COFFEE, 20, 500);
		Integer secondId = data.createBeverage(TEA, 50, 800);
		List<Integer> expected = new ArrayList<>();
		expected.add(firstId);
		expected.add(secondId);
		assertEquals(expected, data.getBeveragesId());
	}

	@Test
	public void testGetBeveragesIdEmpty() {
		List<Integer> expected = new ArrayList<>();
		assertEquals(expected, data.getBeveragesId());
	}

	@Test
	public void testGetBeverages() throws BeverageException {
		Integer firstId = data.createBeverage(COFFEE, 20, 500);
		Integer secondId = data.createBeverage(TEA, 50, 800);
		Map<Integer, String> expected = new HashMap<>();
		expected.put(firstId, COFFEE);
		expected.put(secondId, TEA);
		assertEquals(expected, data.getBeverages());
	}

	@Test
	public void testGetBeveragesEmpty() {
		Map<Integer, String> expected = new HashMap<>();
		assertEquals(expected, data.getBeverages());
	}

	@Test
	public void testUpdateBeverage() throws BeverageException {
		Integer id = data.createBeverage(COFFEE, 20, 500);
		data.updateBeverage(id, TEA, 50, 800);
		assertEquals(TEA, data.getBeverageName(id));
		assertEquals(Integer.valueOf(50), data.getBeverageCapsulesPerBox(id));
		assertEquals(Integer.valueOf(800), data.getBeverageBoxPrice(id));
	}

	@Test
	public void testUpdateBeverageNull() throws BeverageException {
		Integer id = data.createBeverage(COFFEE, 20, 500);
		assertThrows(BeverageException.class, () -> data.updateBeverage(null, TEA, 50, 800));
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, null, null, null));
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, null, 50, 800));
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, TEA, null, null));
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, TEA, null, 800));
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, TEA, 50, null));
		assertEquals(COFFEE, data.getBeverageName(id));
		assertEquals(Integer.valueOf(20), data.getBeverageCapsulesPerBox(id));
		assertEquals(Integer.valueOf(500), data.getBeverageBoxPrice(id));
	}

	@Test
	public void testUpdateBeverageEmpty() throws BeverageException {
		Integer id = data.createBeverage(COFFEE, 20, 500);
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, "", 50, 800));
		assertEquals(COFFEE, data.getBeverageName(id));
		assertEquals(Integer.valueOf(20), data.getBeverageCapsulesPerBox(id));
		assertEquals(Integer.valueOf(500), data.getBeverageBoxPrice(id));
	}

	@Test
	public void testUpdateBeverageInvalid() throws BeverageException {
		Integer id = data.createBeverage(COFFEE, 20, 500);
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, TEA, -1, 800));
		assertThrows(BeverageException.class, () -> data.updateBeverage(id, TEA, 50, -1));
		assertThrows(BeverageException.class, () -> data.updateBeverage(-1, TEA, 50, 800));
	}

	@Test
	public void testRechargeAccount() throws EmployeeException {
		assertEquals(Integer.valueOf(0), data.getBalance());
		Integer id = data.createEmployee(MARCO, ROSSI);
		Integer amount = data.rechargeAccount(id, 100);
		assertEquals(Integer.valueOf(100), amount);
		assertEquals(Integer.valueOf(100), data.getEmployeeBalance(id));
		assertEquals(Integer.valueOf(100), data.getBalance());
		amount = data.rechargeAccount(id, 50);
		assertEquals(Integer.valueOf(150), amount);
		assertEquals(Integer.valueOf(150), data.getEmployeeBalance(id));
		assertEquals(Integer.valueOf(150), data.getBalance());
		id = data.createEmployee(GIOVANNI, BIANCHI);
		data.rechargeAccount(id, 25);
		assertEquals(Integer.valueOf(175), data.getBalance());
	}

	@Test
	public void testRechargeAccountNull() throws EmployeeException {
		assertThrows(EmployeeException.class, () -> data.rechargeAccount(null, null));
		assertThrows(EmployeeException.class, () -> data.rechargeAccount(null, 100));
	}

	@Test
	public void testRechargeAccountInvalid() throws EmployeeException {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		assertThrows(EmployeeException.class, () -> data.rechargeAccount(employee, -1));
		assertThrows(EmployeeException.class, () -> data.rechargeAccount(-1, -1));
		assertThrows(EmployeeException.class, () -> data.rechargeAccount(-1, 100));
	}

	@Test
	public void testBuyBoxes() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testBuyBoxesZero() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 0);
		assertEquals(Integer.valueOf(1500), data.getBalance());
		assertEquals(Integer.valueOf(0), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testBuyBoxesBalance() throws EmployeeException, BeverageException {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		assertThrows(NotEnoughBalance.class, () -> data.buyBoxes(beverage, 2));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(0), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testBuyBoxesNull() throws BeverageException {
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		assertThrows(BeverageException.class, () -> data.buyBoxes(null, 2));
		assertEquals(Integer.valueOf(0), data.getBalance());
		assertEquals(Integer.valueOf(0), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testBuyBoxesInvalid() throws BeverageException {
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		assertThrows(BeverageException.class, () -> data.buyBoxes(-1, 2));
		assertEquals(Integer.valueOf(0), data.getBalance());
		assertEquals(Integer.valueOf(0), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesToVisitor()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		data.sellCapsulesToVisitor(beverage, 10);
		assertEquals(Integer.valueOf(750), data.getBalance());
		assertEquals(Integer.valueOf(30), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesToVisitorZero()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		data.sellCapsulesToVisitor(beverage, 0);
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesToVisitorNotEnough() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsulesToVisitor(beverage, 100));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesToVisitorNull() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(BeverageException.class, () -> data.sellCapsulesToVisitor(null, 10));
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsulesToVisitor(beverage, null));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesToVisitorInvalid() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(BeverageException.class, () -> data.sellCapsulesToVisitor(-1, 10));
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsulesToVisitor(beverage, -1));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsules() throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		Integer balance = data.sellCapsules(employee, beverage, 10, false);
		assertEquals(Integer.valueOf(1500), balance);
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(750), data.getBalance());
		assertEquals(Integer.valueOf(30), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesNotEnough() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsules(employee, beverage, 100, false));
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesNegativeBalance()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		Integer secondEmployee = data.createEmployee(GIOVANNI, BIANCHI);
		Integer balance = data.sellCapsules(secondEmployee, beverage, 10, true);
		assertEquals(Integer.valueOf(-250), balance);
		assertEquals(Integer.valueOf(-250), data.getEmployeeBalance(secondEmployee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(30), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesZero()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		Integer balance = data.sellCapsules(employee, beverage, 0, false);
		assertEquals(Integer.valueOf(1500), balance);
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesNull()
			throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(EmployeeException.class, () -> data.sellCapsules(null, beverage, 0, false));
		assertThrows(BeverageException.class, () -> data.sellCapsules(employee, null, 0, false));
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsules(employee, beverage, null, false));
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesInvalid()
			throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(EmployeeException.class, () -> data.sellCapsules(-1, beverage, 0, false));
		assertThrows(BeverageException.class, () -> data.sellCapsules(employee, -1, 0, false));
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsules(employee, beverage, -1, false));
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesFromAccount()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		Integer balance = data.sellCapsules(employee, beverage, 10, true);
		assertEquals(Integer.valueOf(1250), balance);
		assertEquals(Integer.valueOf(1250), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(30), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesFromAccountNotEnough() throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsules(employee, beverage, 100, true));
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesFromAccountZero()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		Integer balance = data.sellCapsules(employee, beverage, 0, true);
		assertEquals(Integer.valueOf(1500), balance);
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesFromAccountNull()
			throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(EmployeeException.class, () -> data.sellCapsules(null, beverage, 0, true));
		assertThrows(BeverageException.class, () -> data.sellCapsules(employee, null, 0, true));
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsules(employee, beverage, null, true));
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testSellCapsulesFromAccountInvalid()
			throws EmployeeException, BeverageException, NotEnoughBalance {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		assertThrows(EmployeeException.class, () -> data.sellCapsules(-1, beverage, 0, true));
		assertThrows(BeverageException.class, () -> data.sellCapsules(employee, -1, 0, true));
		assertThrows(NotEnoughCapsules.class, () -> data.sellCapsules(employee, beverage, -1, true));
		assertEquals(Integer.valueOf(1500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(500), data.getBalance());
		assertEquals(Integer.valueOf(40), data.getBeverageCapsules(beverage));
	}

	@Test
	public void testGetReport()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules, DateException {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		data.sellCapsules(employee, beverage, 1, false);
		data.sellCapsulesToVisitor(beverage, 2);
		data.sellCapsules(employee, beverage, 3, true);
		Date date = new Date();
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, -1);
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(date);
		tomorrow.add(Calendar.DATE, 1);
		List<String> report = data.getReport(yesterday.getTime(), tomorrow.getTime());
		System.out.println(report.get(0));
		assertEquals(5, report.size());
		assertTrue(report.get(0).contains("RECHARGE Marco Rossi 15.00 \u20ac"));
		assertTrue(report.get(1).contains("BUY Coffee 2"));
		assertTrue(report.get(2).contains("CASH Marco Rossi Coffee 1"));
		assertTrue(report.get(3).contains("VISITOR Coffee 2"));
		assertTrue(report.get(4).contains("BALANCE Marco Rossi Coffee 3"));
	}

	@Test
	public void testGetReportEmpty() throws DateException {
		Date date = new Date();
		List<String> report = new ArrayList<>();
		assertEquals(report, data.getReport(date, date));
	}

	@Test
	public void testGetReportNull() {
		Date date = new Date();
		assertThrows(DateException.class, () -> data.getReport(null, null));
		assertThrows(DateException.class, () -> data.getReport(date, null));
		assertThrows(DateException.class, () -> data.getReport(null, date));
	}

	@Test
	public void testGetReportInvalid() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		assertThrows(DateException.class, () -> data.getReport(date, calendar.getTime()));
	}

	@Test
	public void testGetEmployeeReport()
			throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules, DateException {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		data.sellCapsules(employee, beverage, 1, false);
		data.sellCapsulesToVisitor(beverage, 2);
		data.sellCapsules(employee, beverage, 3, true);
		Date date = new Date();
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.DATE, -1);
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(date);
		tomorrow.add(Calendar.DATE, 1);
		List<String> report = data.getEmployeeReport(employee, yesterday.getTime(), tomorrow.getTime());
		System.out.println(report);
		assertEquals(3, report.size());
		System.out.println(report.size());
		assertTrue(report.get(0).contains("RECHARGE Marco Rossi 15.00 \u20ac"));
		assertTrue(report.get(1).contains("CASH Marco Rossi Coffee 1"));
		assertTrue(report.get(2).contains("BALANCE Marco Rossi Coffee 3"));
		
	}

	@Test
	public void testGetEmployeeReportEmpty() throws DateException, EmployeeException {
		Date date = new Date();
		Integer employee = data.createEmployee(MARCO, ROSSI);
		List<String> report = new ArrayList<>();
		assertEquals(report, data.getEmployeeReport(employee, date, date));
	}

	@Test
	public void testGetEmployeeReportNull() throws EmployeeException {
		Date date = new Date();
		Integer employee = data.createEmployee(MARCO, ROSSI);
		assertThrows(DateException.class, () -> data.getEmployeeReport(employee, null, null));
		assertThrows(DateException.class, () -> data.getEmployeeReport(employee, date, null));
		assertThrows(DateException.class, () -> data.getEmployeeReport(employee, null, date));
		assertThrows(EmployeeException.class, () -> data.getEmployeeReport(null, date, date));
	}

	@Test
	public void testGetEmployeeReportInvalid() throws EmployeeException {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		Integer employee = data.createEmployee(MARCO, ROSSI);
		assertThrows(DateException.class, () -> data.getEmployeeReport(employee, date, calendar.getTime()));
		assertThrows(EmployeeException.class, () -> data.getEmployeeReport(-1, date, date));
	}
	
	@Test
	public void testPersistence() throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 20, 500);
		data.buyBoxes(beverage, 2);
		data.sellCapsules(employee, beverage, 10, false);

		DataInterface other = new DataImpl();
		assertEquals(Integer.valueOf(1500), other.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(750), other.getBalance());
		assertEquals(Integer.valueOf(30), other.getBeverageCapsules(beverage));
	}

	@Test
	public void testPriceVariation() throws EmployeeException, BeverageException, NotEnoughBalance, NotEnoughCapsules {
		Integer employee = data.createEmployee(MARCO, ROSSI);
		data.rechargeAccount(employee, 1500);
		Integer beverage = data.createBeverage(COFFEE, 5, 500);
		data.buyBoxes(beverage, 1);
		data.updateBeverage(beverage, COFFEE, 20, 1000);
		data.buyBoxes(beverage, 1);
		Integer balance = data.sellCapsules(employee, beverage, 15, true);
 		assertEquals(Integer.valueOf(500), data.getEmployeeBalance(employee));
		assertEquals(Integer.valueOf(0), data.getBalance());
		assertEquals(Integer.valueOf(10), data.getBeverageCapsules(beverage));
	}

}