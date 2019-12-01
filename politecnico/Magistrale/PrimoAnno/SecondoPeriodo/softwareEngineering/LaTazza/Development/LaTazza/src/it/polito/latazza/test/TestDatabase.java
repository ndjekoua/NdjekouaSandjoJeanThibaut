
/**
 * 
 */
package it.polito.latazza.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.polito.latazza.data.Beverage;
import it.polito.latazza.data.DataImpl;
import it.polito.latazza.data.Database;
import it.polito.latazza.data.Employee;
import it.polito.latazza.data.Transaction;
import it.polito.latazza.exceptions.BeverageException;

/**
 * @author jean thibaut
 *
 */
class TestDatabase {

	Database database = new Database();
	DataImpl dataImpl = new DataImpl();
	public Date getDate(int year,int month,int day,int hour,int minute,int sec) {
		LocalDate d = LocalDateTime.of(year, month, day, hour, minute, sec).toLocalDate();
		Date date = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
		date.setHours(hour);
		date.setMinutes(minute);
		date.setSeconds(sec);
		return date;
	}
	
	@Test
	public void testDatabase() throws ClassNotFoundException, SQLException, Exception {
		
		database.truncateTables();
		
		int id = database.addEmployee(new Employee(-1,"Morisio","Maurizio",1000000.99));
		
		assertEquals(id>=0, true);
		
		List<Employee> list = null;
		
		list = database.getListEmployee();
		
		Employee emp = database.getEmployeeData(1);
		assertNotEquals(null, emp);
		
		database.updateBalance(1.1);
		double balance = database.getBalance();
		assertEquals(1.1, balance);
		

		id = database.addBeverage(new Beverage(-1,10,10.1,50,"do you wanna a Kaffè"));
		
		ArrayList<Beverage> bev = (ArrayList<Beverage>) database.getListOfBeverages();
		assertNotEquals(null, bev);
		assertEquals(bev.size()>=1, true);
		
		Beverage bevan = database.getBeverageData(1);
		assertNotEquals(bevan, null);
		
		id = database.registerTransaction(new Transaction(1,getDate(2010, 8, 21, 10, 5, 3),'P',1,1,1,1, 1.0,true));
		assertEquals(id>=0, true);
		
		try {
			database.updateBeverage(new Beverage(-1,0,1.4,50,"do you want a Kaffè!?!!!"));
		} catch(BeverageException e) {
			System.out.println("works correctly, it launch an exception if id is wrong!");
		}
		
		
		database.updateEmployee(new Employee(1,"Morisio","Maurizio",1.99));
		
		List<Transaction> trans = database.getEmployeeReport(1,getDate(2009, 8, 21, 10, 5, 3),getDate(2011, 8, 21, 10, 5, 3));
		assertEquals(trans.size()>0, true);
		assertEquals(trans.get(0).getTransactionDate().toLocaleString(),getDate(2010, 8, 21, 10, 5, 3).toLocaleString());
		
		trans = database.getReport(getDate(2011, 8, 21, 10, 5, 3),getDate(2013, 8, 21, 10, 5, 3));
		assertEquals(trans.size()==0, true);
		
		assertNotEquals(null, list);

	}

}
