/**
 * 
 */
package it.polito.latazza.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import it.polito.latazza.exceptions.BeverageException;
import it.polito.latazza.exceptions.EmployeeException;

/**
 * @author elia
 *
 */

public class Database {
	
	Connection connection = null;
	
	private void connect() throws Exception {
		try {
			if (connection != null) {
				connection.close();
				//String msg = "Exception, called connect on a non void connect object";
				//throw new Exception(msg);
			}
				
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:./db/db_se");
			connection.createStatement().execute("PRAGMA foreign_keys=ON");
			//System.out.println("Database connection opened.");
		} catch(SQLException | ClassNotFoundException e) {
			System.err.println("erroreeeeee");
			throw new Exception();
		}
	}
	
	private void closeConnection() throws Exception {
		connection.close();
		connection = null;
		//System.out.println("Database connection closed.");
	}
	
	public List<Employee> getListEmployee() throws Exception{
		connect();
		
		List<Employee> returnList = new LinkedList<Employee>();
		
		Statement stmt = connection.createStatement();
		String sql = "SELECT * FROM Employee";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String surname = rs.getString("surname");
			float credit = rs.getFloat("credit");
			
			returnList.add(new Employee(id,name,surname,credit));
			
		}
		
		rs.close();
		stmt.close();
		
		closeConnection();
		
		return returnList;
	}
	
	
	/* for security purpose */
	@Override
	protected void finalize() throws Throwable {
		connection.close();
		connection = null;
		super.finalize();
	}

	public Employee getEmployeeData(int i) throws Exception {
		connect();
		
		String sql = "SELECT * FROM Employee WHERE id = ?";
		
		PreparedStatement prep = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		prep.setInt(1, i);
		ResultSet rs = prep.executeQuery();
		
		int id = 0;
		String name = null,surname = null;
		float credit = 0;
		boolean result = false;
		
		if (rs.next()) {
			id = rs.getInt("id");
			name = rs.getString("name");
			surname = rs.getString("surname");
			credit = rs.getFloat("credit");
			result = true;
		}
		
		closeConnection();
		
		if (!result)
			throw new EmployeeException();
		
		return new Employee(id,name,surname,credit);
	}


	public void updateBalance(double d) throws Exception {
		String sql = "update LaTazza set balance=?";
		connect();
		
		PreparedStatement prep = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		prep.setDouble(1, d);
		prep.execute();
		
		closeConnection();
	}

	public List<Beverage> getListOfBeverages() throws Exception {
		List<Beverage> returnList = new ArrayList<Beverage>();
		
		connect();
		
		String sql = "SELECT * FROM Beverage";
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int capsulePerBox = rs.getInt("capsulePerBox");
			int quantityAvaiable = rs.getInt("quantityAvaiable");
			float price = rs.getFloat("price");
			
			returnList.add(new Beverage(id,quantityAvaiable,price,capsulePerBox,name));
			
		}
		
		rs.close();
		stmt.close();
		
		closeConnection();
		
		return returnList;
	}

	public Beverage getBeverageData(int id) throws Exception {
		connect();
		
		String sql = "SELECT * FROM Beverage WHERE id = ?";
		
		PreparedStatement prep = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		

		int id_u = 0;
		String name = null;
		int capsulePerBox = 0;
		float credit;
		int quantityAvaiable = 0;
		float price = 0;
		boolean result=false;
		
		if (rs.next()) {
			id_u = rs.getInt("id");
			name = rs.getString("name");
			capsulePerBox = rs.getInt("capsulePerBox");
			quantityAvaiable = rs.getInt("quantityAvaiable");
			price = rs.getFloat("price");
			
			result=true;
		}
		
		closeConnection();
		
		if (!result)
			throw new BeverageException();
		
		return new Beverage(id_u,quantityAvaiable,price,capsulePerBox,name);
	}

	public double getBalance() throws Exception {
		connect();
		
		String sql = "SELECT * FROM LaTazza";
		
		PreparedStatement prep = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = prep.executeQuery();
		
		double balance = 0;
		
		while (rs.next()) {
			balance = rs.getDouble("balance");
		}
		
		closeConnection();
		
		return balance;

	}

	public List<Transaction> getReport(Date date_init, Date date_final) throws Exception {
		String sql = "SELECT * FROM Transactions WHERE transactionDate BETWEEN ? and ?";
		
		List<Transaction> returnList = new ArrayList<Transaction>();
		
		connect();
		
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, convDate(date_init));
		stmt.setString(2, convDate(date_final));
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next()) {
			int id_m = rs.getInt("id");
			String transactionDate = rs.getString("transactionDate");
			Date myDate = parseDate(transactionDate);
			char type = rs.getString("type").charAt(0);
			int boxQuantity = rs.getInt("boxQuantity");
			int employeeID = rs.getInt("employeeID");
			int beverageID = rs.getInt("beverageID");
			float amount = rs.getFloat("amount");
			int fr_account = rs.getInt("fromAccount");
			int numberOfCapsules = rs.getInt("numberOfCapsules");
			boolean from_account = fr_account==1;
			
			returnList.add(new Transaction(id_m, myDate, type, boxQuantity, employeeID, beverageID,numberOfCapsules, amount, from_account));
			
		}
		
		rs.close();
		stmt.close();
		
		closeConnection();
		
		return returnList;
	}

	public List<Transaction> getEmployeeReport(int id, Date date_init, Date date_final) throws Exception {
		String sql = "SELECT * FROM Transactions WHERE employeeID=? AND transactionDate BETWEEN ? and ?";
		
		List<Transaction> returnList = new ArrayList<Transaction>();
		
		connect();
		
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		stmt.setString(2, convDate(date_init));
		stmt.setString(3, convDate(date_final));
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next()) {
			int id_m = rs.getInt("id");
			String transactionDate = rs.getString("transactionDate");
			Date myDate = parseDate(transactionDate);
			char type = rs.getString("type").charAt(0);
			int boxQuantity = rs.getInt("boxQuantity");
			int employeeID = rs.getInt("employeeID");
			int beverageID = rs.getInt("beverageID");
			float amount = rs.getFloat("amount");
			int fr_account = rs.getInt("fromAccount");
			int numberOfCapsules = rs.getInt("numberOfCapsules");
			boolean from_account = fr_account==1;
			
			returnList.add(new Transaction(id_m, myDate, type, boxQuantity, employeeID, beverageID,numberOfCapsules, amount, from_account));
			
		}
		
		rs.close();
		stmt.close();
		
		closeConnection();
		
		return returnList;
	}
	
	public static Date parseDate(String datePassed) throws ParseException {
		//System.out.println("converting: " + datePassed);
		
		java.util.Date temp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .parse(datePassed);
		
		return temp;	
	}
	
	public static String convDate(Date date_init) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String newDate = dateFormat.format(date_init);
		//System.out.println("created: " + newDate);
		return newDate;
	}

	public int registerTransaction(Transaction transaction) throws Exception {
		// TODO Auto-generated method stub
		connect();
		
		int last_inserted_id = -1;
		
		String sql = "INSERT INTO Transactions VALUES (NULL,?,?,?,?,?,?,?,?);";
		
		PreparedStatement prep = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, convDate(transaction.getTransactionDate()));
		prep.setString(2, String.valueOf(transaction.getType()));
		prep.setInt(3, transaction.getBoxQuantity());
		prep.setInt(4, transaction.getEmployeeID());
		prep.setInt(5, transaction.getBeverageID());
		prep.setDouble(6, transaction.getNumberOfCapsules());
		prep.setDouble(7, transaction.getAmount());
		prep.setBoolean(8, transaction.isFromAccount());
		
		
		prep.executeUpdate();
		
		ResultSet rs = prep.getGeneratedKeys();
        if(rs.next())
        {
            last_inserted_id = rs.getInt(1);
        }
		
		prep.close();
		
		closeConnection();
		
		return last_inserted_id;
	}



	public int addBeverage(Beverage beverage) throws Exception {

		connect();
		int last_inserted_id = -1;
		String sql = "INSERT INTO Beverage VALUES (NULL,?,?,?,?);";
		

		
		PreparedStatement prep = connection.prepareStatement(sql);
		prep.setInt(1, beverage.getQuantityAvailable());
		prep.setDouble(2, beverage.getBoxPrice());
		prep.setInt(3, beverage.getCapsulePerBox());
		prep.setString(4, beverage.getName());
		prep.executeUpdate();

		ResultSet rs = prep.getGeneratedKeys();
        if(rs.next())
        {
            last_inserted_id = rs.getInt(1);
        }
        
        
		prep.close();
		
		closeConnection();
		
		return last_inserted_id;

	}

	public int addEmployee(Employee employee) throws Exception {
		connect();
		
		int last_inserted_id=-1;
		
		String sql = "INSERT INTO Employee VALUES (NULL,?,?,?);";
		
		PreparedStatement prep = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		prep.setString(1, employee.getName());
		prep.setString(2, employee.getSurname());
		prep.setDouble(3, employee.getCredit());
		prep.executeUpdate();
		
		ResultSet rs = prep.getGeneratedKeys();
        if(rs.next())
        {
            last_inserted_id = rs.getInt(1);
        }
		
		prep.close();
		
		closeConnection();
		
		return last_inserted_id;
	}

	public void truncateTables() throws Exception {
		// TODO Auto-generated method stub
		String sql_create_1 = "CREATE TABLE IF NOT EXISTS `Transactions` (\n" + 
				"	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" + 
				"	`transactionDate`	TEXT NOT NULL ,\n" + // CHECK(date ( transactionDate ) IS NOT NULL),\n" + 
				"	`type`	CHAR NOT NULL CHECK(type = 'R' OR type = 'C' OR type = 'P'),\n" + 
				"	`boxQuantity`	INTEGER NOT NULL,\n" + 
				"	`employeeID`	INTEGER NOT NULL,\n" + 
				"	`beverageID`	INTEGER NOT NULL,\n" + 
				"	`numberOfCapsules`	INTEGER NOT NULL," +
				"	`amount`	REAL NOT NULL,\n" + 
				"	`fromAccount`	NUMERIC NOT NULL CHECK(fromAccount = 0 OR fromAccount = 1)\n" + 
				");";
				
		String sql_create_2 = "CREATE TABLE IF NOT EXISTS `Employee` (\n" + 
				"	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,\n" + 
				"	`name`	TEXT NOT NULL,\n" + 
				"	`surname`	TEXT NOT NULL,\n" + 
				"	`credit`	REAL NOT NULL DEFAULT 0 CHECK(credit >= 0)\n" + 
				");";
	    String sql_create_3 = "CREATE TABLE IF NOT EXISTS `Beverage` (\n" + 
				"	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" + 
				"	`quantityAvaiable`	INTEGER NOT NULL DEFAULT 0 CHECK(quantityAvaiable >= 0),\n" + 
				"	`price`	REAL NOT NULL CHECK(price > 0),\n" + 
				"	`capsulePerBox`	INTEGER NOT NULL CHECK(capsulePerBox > 0),\n" + 
				"	`name`	TEXT NOT NULL UNIQUE\n" + 
				");";
	    
	    String sql_create_4 = "insert into LaTazza values(0.0)";
		
		String sqlDelete_1 = "drop table IF EXISTS `Transactions`;";
		String sqlDelete_2 = "drop table IF EXISTS `Employee`;";
		String sqlDelete_3 = "drop table IF EXISTS `Beverage`;";
		
		connect();

		Statement stmt_drop_tables = connection.createStatement();
		stmt_drop_tables.addBatch(sqlDelete_1);
		stmt_drop_tables.addBatch(sqlDelete_2);
		stmt_drop_tables.addBatch(sqlDelete_3);
		stmt_drop_tables.executeBatch();
		
		Statement stmt_create_tables = connection.createStatement();
		stmt_create_tables.addBatch(sql_create_1);
		stmt_create_tables.addBatch(sql_create_2);
		stmt_create_tables.addBatch(sql_create_3);
		stmt_create_tables.addBatch(sql_create_4);
		stmt_create_tables.executeBatch();
		
		
		closeConnection();
		
	}

	public void updateBeverage(Beverage beverage) throws Exception {
		connect();
		int last_inserted_id = -1;
		String sql = "UPDATE `Beverage` SET `quantityAvaiable`=?,`price`=?,`capsulePerBox`=?,`name`=? WHERE id=?;";

		
		PreparedStatement prep = connection.prepareStatement(sql);
		prep.setInt(1, beverage.getQuantityAvailable());
		prep.setDouble(2, beverage.getBoxPrice());
		prep.setInt(3, beverage.getCapsulePerBox());
		prep.setString(4, beverage.getName());
		prep.setInt(5, beverage.getId());
		int count = prep.executeUpdate();
        
		prep.close();
		
		closeConnection();
		
		if (count <= 0)
			throw new BeverageException();
	}

	public void updateEmployee(Employee employee) throws Exception {
		connect();
		
		int last_inserted_id=-1;
		
		String sql = "UPDATE `Employee` SET `name`=?,`surname`=?,`credit`=? WHERE id=?;";
		
		PreparedStatement prep = connection.prepareStatement(sql);
		prep.setString(1, employee.getName());
		prep.setString(2, employee.getSurname());
		prep.setDouble(3, employee.getCredit());
		prep.setDouble(4, employee.getId());
		int count = prep.executeUpdate();
		
		prep.close();
		
		closeConnection();
		

		if (count <= 0)
			throw new EmployeeException();
	}
	

	
}