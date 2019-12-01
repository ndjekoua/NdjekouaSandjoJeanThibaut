package it.polito.latazza.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.polito.latazza.data.Beverage;
import it.polito.latazza.data.DataImpl;
import it.polito.latazza.data.Database;
import it.polito.latazza.data.Employee;
import it.polito.latazza.data.Transaction;
import it.polito.latazza.exceptions.BeverageException;
import it.polito.latazza.exceptions.DateException;
import it.polito.latazza.exceptions.EmployeeException;
import it.polito.latazza.exceptions.NotEnoughBalance;
import it.polito.latazza.exceptions.NotEnoughCapsules;

public class TestDataImpl {
	/*can be used by other developper, no need to redefine thm again*/
	Database database = new Database();
	DataImpl dataImpl = new DataImpl();
	@Test
	public void testExample() {
		assertEquals(2, 1 + 1);

	}
	
		
	@Test
	public void testCreateBeverage() throws Exception{
		dataImpl.reset();
		int id=-1;
		id=dataImpl.createBeverage("coffee",10, 100);
		assertNotEquals(id,-1);
		Beverage bev = database.getBeverageData(id);
		assertEquals("coffee",bev.getName());
		assertEquals(10,bev.getCapsulePerBox());
		assertEquals(100,bev.getBoxPrice());
	}
	
	
	@Test
	public void testCreateBeverageWrongAttributes() throws Exception{
		dataImpl.reset();
		int id=-1;
		try {
		id=dataImpl.createBeverage("coffee",10,-100);
		fail();
		}catch(BeverageException be) {
			//the id should not be updated so it's value should be -1
			assertEquals(id,-1);
		}
		
	}
	@Test
	public void testUpdateBeverageWithSuccess() throws Exception {
		dataImpl.reset();
		int id =-1;
		id=dataImpl.createBeverage("Tea",10, 100);
	    Beverage bev = database.getBeverageData(id);
	    bev.setBoxPrice(200);
	    dataImpl.updateBeverage(id, bev.getName(),bev.getCapsulePerBox() , (int)Math.round(bev.getBoxPrice()));
	    //everything is correct so the object should be updated
	    assertEquals(200,database.getBeverageData(id).getBoxPrice());
	}
	
	@Test
	public void testUpdateBeverageWrongId() throws Exception {
		dataImpl.reset();
	    //i put an id that does not exist => i should catch the exception
	    try {
	    	dataImpl.updateBeverage(-1,"coffee",10,100);
	    	fail();
	    }catch(Exception e) {
	    	assertTrue(true);
	    }
	}
	
	@Test
	public void testUpdateBeverageAttributes() throws Exception {
		dataImpl.reset();
		int id =-1;
		id=dataImpl.createBeverage("Tea",10, 100);
	    Beverage bev = database.getBeverageData(id);
	    bev.setBoxPrice(200);
	    dataImpl.updateBeverage(id, bev.getName(),bev.getCapsulePerBox() , (int)Math.round(bev.getBoxPrice()));
	   
	    //the price and CapsulesPerBox are negative so i should catch a BeverageException
        
	    try {
	    	dataImpl.updateBeverage(id, bev.getName(),-10,-2);
	    	fail();
	    }catch(BeverageException e) {
	    	//assertTrue(true);
	    	assertTrue(e instanceof BeverageException);
	    	assertEquals(bev.getCapsulePerBox(),10);
	    	assertEquals(bev.boxPrice,200);
	    }
	}
	@Test
	public void testReset() throws Exception {
		
			database.addBeverage(new Beverage(10, 0, 10, 100, "Lemon"));
			database.addEmployee(new Employee(10,"ndjekoua","sandjo",0));
			database.updateBalance(10);
		
		//After calling this function, the databse should be empty
            dataImpl.reset();
		
		    List<Employee> empList = null;
		    List<Beverage> bevList = null;
		    double balance =-1;
	        empList=database.getListEmployee();
		    bevList =database.getListOfBeverages();
		    balance = database.getBalance();
		
		assertEquals(true,bevList.isEmpty());
		assertEquals(true,empList.isEmpty());
		assertEquals(balance,0);
	}
	@Test
	public void testGetBeverageNameSuccess() throws BeverageException {
		dataImpl.reset();// used to clear everything before starting the Test
		int id=-1;
		id=dataImpl.createBeverage("coffee",10, 100);
		//i get the beverageName of the created string
		String bevName = dataImpl.getBeverageName(id);
		assertEquals(bevName,"coffee");
	}
	
	@Test
	public void testGetBeverageNameWrongId() throws BeverageException {
		dataImpl.reset();// used to clear everything before starting the Test
		
		//this should throw a BeverageException because the Beverage does not exist
		try {
			dataImpl.getBeverageName(-1);
			fail();
		}catch(BeverageException be){
			assertTrue(true);
		}
	}
	@Test
	public void testGetBeverageCapsulesPerBoxSuccess() throws BeverageException {
		dataImpl.reset();
		int id=-1;
		id=dataImpl.createBeverage("coffee",10, 100);
		int capsulesPerBox = dataImpl.getBeverageCapsulesPerBox(id);
		assertEquals(capsulesPerBox,10);
	}
	
	@Test
	public void testGetBeverageCapsulesPerBoxWrongId() throws BeverageException {
		dataImpl.reset();
		
		//this should throw a BeverageException because the Beverage does not exist
		try {
			dataImpl.getBeverageCapsulesPerBox(-1);
			fail();
		}catch(BeverageException be){
			assertTrue(true);		}
	}
	@Test
	public void getBeverageBoxPriceSuccess() throws BeverageException {
		dataImpl.reset();
		int id=-1;
		id=dataImpl.createBeverage("coffee",10, 100);
		Integer boxPrice = dataImpl.getBeverageBoxPrice(id);
		assertEquals(boxPrice,100,0.0000000001);
	}
	
	@Test
	public void getBeverageBoxPriceWrongId() throws BeverageException {
		dataImpl.reset();
		
		//this should throw a BeverageException because the Beverage does not exist
		try {
			dataImpl.getBeverageBoxPrice(-1);
			fail();
		}catch(BeverageException be){
			assertTrue(true);
		}
	}
	@Test
	public void testGetBeveragesId() throws BeverageException {
		dataImpl.reset();
		List<Integer> expectedList = new ArrayList<>();
		List<Integer> returnList;
		int id,id1,id2;
		id=dataImpl.createBeverage("coffee",10, 100);
		id1=dataImpl.createBeverage("Tea",10, 100);
		id2=dataImpl.createBeverage("Lemon", 20,150);
		expectedList.add(id);
		expectedList.add(id1);
		expectedList.add(id2);
		returnList= dataImpl.getBeveragesId();
		assertEquals(returnList,expectedList);
	}

    @Test
    public void testGetBeverages() throws BeverageException {
    	dataImpl.reset();
    	Map<Integer, String> returnMap = new HashMap<>();
    	Map<Integer, String> expectedMap = new HashMap<>();
    	int id,id1,id2;
		id=dataImpl.createBeverage("coffee",10, 100);
		id1=dataImpl.createBeverage("Tea",10, 100);
		id2=dataImpl.createBeverage("Lemon", 20,150);
		expectedMap.put(id, "coffee");
		expectedMap.put(id1, "Tea");
		expectedMap.put(id2, "Lemon");
		
		returnMap= dataImpl.getBeverages();
		assertEquals(expectedMap,returnMap);
    }
    
    @Test
    public void testGetBeveragesEmptyList() throws BeverageException {
    	dataImpl.reset();
    	Map<Integer, String> returnMap = new HashMap<>();
		returnMap= dataImpl.getBeverages();
		assertTrue(returnMap.isEmpty());
    }
    @Test
    public void testGetBeverageCapsulesZeroBoundary() throws Exception {
    	dataImpl.reset();
    	int id;
		id=dataImpl.createBeverage("coffee",10, 100);
		Beverage bev = database.getBeverageData(id);
		//when we create a beverage, the nitial quantity is always 0
		Integer capsulesQuantity = bev.getQuantityAvailable();
		assertEquals(capsulesQuantity,0,0.0000000001);
    }
    @Test
    public void testGetBeverageCapsules() throws Exception {
    	dataImpl.reset();
    	int id;
		id=dataImpl.createBeverage("coffee",10, 100);
		Beverage bev = database.getBeverageData(id);
		bev.setQuantityAvailable(10);
		database.updateBeverage(bev);
		Integer capsulesQuantity = bev.getQuantityAvailable();
		assertEquals(capsulesQuantity,10,0.0000000001);
    }
    @Test
    public void testGetBeverageCapsulesSuccess() throws Exception {
    	dataImpl.reset();
    	int id;
		id=dataImpl.createBeverage("coffee",10, 100);
		Beverage bev = database.getBeverageData(id);
		bev.setQuantityAvailable(10);
		database.updateBeverage(bev);
		Integer capsulesQuantity = dataImpl.getBeverageCapsules(id);
		assertEquals(capsulesQuantity,10,0.0000000001);
    }
    @Test
    public void testGetBeverageCapsulesWrongId() throws Exception {
    	dataImpl.reset();
    	int id;
		id=dataImpl.createBeverage("coffee",10, 100);
		Beverage bev = database.getBeverageData(id);
		bev.setQuantityAvailable(10);
		database.updateBeverage(bev);
		try {
		 dataImpl.getBeverageCapsules(-2);
		fail();
		}catch(BeverageException be) {
			assertTrue(true);
		}
    }
    
    @Test
    public void testBuyBoxesSuccess() throws Exception {
    	dataImpl.reset();
    	database.updateBalance(500);
    	int id;
		id=dataImpl.createBeverage("coffee",10, 100);
		dataImpl.buyBoxes(id,3);// so i will spend 3*100cent to buy 3*10 capsules
		
		// check laTazza balance have been updated
		double balance = database.getBalance();
		assertEquals(balance,500-300);
		
		// check the Quantity available for this Beverage has been correctly updated
		Integer quantityAvailable = database.getBeverageData(id).getQuantityAvailable();
		assertEquals(quantityAvailable,0+30,0.0000000001);
		
		 //check the Transaction has been created
		 Date date = new Date();
		   List<Transaction> transactionList = database.getReport(date,new Date());
		   assertEquals(1,transactionList.size());
	  }
     
    
    @Test
    public void testBuyBoxesWrongBeverageId() throws Exception {
    	dataImpl.reset();
    	database.updateBalance(500);
		dataImpl.createBeverage("coffee",10, 100);
		//now try to buyBoxes for an identifier which is not valid so i should catch a BeverageException
		       try{
		         	dataImpl.buyBoxes(-1,3);
		         	fail();
		       }catch(BeverageException be) {
		    	 assertTrue(true);
			   }
	  }
    
    
    @Test
    public void testBuyBoxesNotEnoughBalance() throws Exception {
    	dataImpl.reset();
    	database.updateBalance(100);
    	int id;
		id=dataImpl.createBeverage("coffee",10, 100);     
		 // now consider the case i do not have enough balance so i should throw notEnoughBalnceException
		       
		        try {
				   dataImpl.buyBoxes(id,3);// so i will spend 3*100cent to buy 3*10 capsules
				   fail();
		        }catch(NotEnoughBalance  e) {
		        	assertTrue(true);
		        }
	  }
    
    @Test
    public void testSellCapsules()
			throws Exception{
    	dataImpl.reset();
      int emp1=dataImpl.createEmployee("john","doe");
      int emp2=dataImpl.createEmployee("jane","roberts");
      int bev1=dataImpl.createBeverage("tea", 10, 10);
      //int bev2=dataImpl.createBeverage("lemon", 20, 15);
      
  	  database.updateBalance(400);
      dataImpl.rechargeAccount(emp1, 10);
      
      dataImpl.buyBoxes(bev1, 1);
      dataImpl.sellCapsules(emp1, bev1, 1, true);
      
      // check LaTazza balance
      double balance=database.getBalance();
      assertEquals(balance,400 -10 +10);
      dataImpl.sellCapsules(emp2, bev1, 1, false);
      balance=database.getBalance();
      assertEquals(balance,400 -10 +10 +1);
      
      // check the beverage available quantity
      Beverage bev=database.getBeverageData(bev1);
      int q=bev.getQuantityAvailable();
      assertEquals(q,10-1-1);
      
      //check the employee account
      Employee emp=database.getEmployeeData(emp1);
      double d=emp.getCredit();
      assertEquals(d,10-1);
      
      //check that a transaction has been created for the employee
      List<Transaction> transactionList=database.getReport(shiftDate(-1), shiftDate(1));
      assertEquals(transactionList.size(),4);
      
      //dataImpl.rechargeAccount(emp2, 20);
      //check that the employee account has not been updated given that he pays by cash
      dataImpl.sellCapsules(emp1, bev1, 1, false);
      emp=database.getEmployeeData(emp1);
      d=emp.getCredit();
      assertEquals(d,10-1);
      bev=database.getBeverageData(bev1);
      q=bev.getQuantityAvailable();
      assertEquals(q,10-1-1-1);
      
    }
    @Test
    public void testSellCapsulesNotEnoughCapsules() throws Exception{
    	dataImpl.reset();
        int id1=dataImpl.createEmployee("john","doe");
        int id2=dataImpl.createBeverage("tea", 10, 10);
        
       dataImpl.rechargeAccount(id1, 10);
        
	    try {
	        dataImpl.sellCapsules(id1, id2, 1, true);
	        fail();
        } catch(NotEnoughCapsules e) {
        	assertTrue(true);
        }
        
        try {
	            dataImpl.sellCapsules(id1, id2, 1, false);
	            fail();
            } catch(NotEnoughCapsules e) {
            	assertTrue(true);
            }
    }
    
    @Test
    public void testSellCapsulesNotEnoughMoney() throws Exception{
    	dataImpl.reset();
        int id1=dataImpl.createEmployee("john","doe");
        int id2=dataImpl.createBeverage("tea", 10, 10);
    	database.updateBalance(400);
        dataImpl.buyBoxes(id2, 1);
        try {
        	 dataImpl.sellCapsules(id1, id2, 1, true);
        	 fail();
        }catch(Exception e) {
        	 assertTrue(true);
        }
    }
    @Test
    public void testSellCapsulesWrongAttributes() throws Exception {
    	
    	dataImpl.reset();
        int id1=dataImpl.createEmployee("john","doe");
        int id2=dataImpl.createBeverage("tea", 10, 10);
        database.updateBalance(400);
        dataImpl.buyBoxes(id2, 1);
        dataImpl.rechargeAccount(id1, 10);
        try {
        	 dataImpl.sellCapsules(-1, id2, 1, true);
        	 fail();
        }catch(EmployeeException em) {
        	assertTrue(true);
        }
        
        try {
        	 dataImpl.sellCapsules(id1, -1, 1, true);
        	 fail();
        }catch(BeverageException be) {
        	assertTrue(true);
        }
    	
        dataImpl.sellCapsules(id1, id2, -1, true);
        Employee emp=database.getEmployeeData(id1);
        Beverage bev=database.getBeverageData(id2);
        assertEquals(emp.getCredit(),10);
       assertEquals(bev.getQuantityAvailable(),10);
       
        dataImpl.sellCapsules(id1, id2, -1, false);
        emp=database.getEmployeeData(id1);
        bev=database.getBeverageData(id2);
        assertEquals(emp.getCredit(),10);
        assertEquals(bev.getQuantityAvailable(),10);
    }
    @Test 
     public void testSellCapsulesToVisitor() throws Exception {
    	dataImpl.reset();
    	
    	int bev1=dataImpl.createBeverage("tea", 20, 40);
    	database.updateBalance(100);
    	dataImpl.buyBoxes(bev1, 2);
    	dataImpl.sellCapsulesToVisitor(bev1, 2);
    	
    	//check available quantity
    	Beverage bev=database.getBeverageData(bev1);
    	assertEquals(bev.getQuantityAvailable(),38);
    	
    	// check the LaTazza Account
    	assertEquals(database.getBalance(),100-80+4);
    	
    	//check transactions have been created
    	 List<Transaction> transactionList=database.getReport(shiftDate(-1), shiftDate(1));
         assertEquals(transactionList.size(),2);
        
    	
    }
	
    @Test 
    public void testSellCapsulesToVisitorNotEnoughCapsules() throws Exception {
   	dataImpl.reset();
   	
    int id1=dataImpl.createBeverage("tea", 10, 10);
   	
    try {
        dataImpl.sellCapsulesToVisitor(id1,1);
        fail();
        } catch(NotEnoughCapsules e) {
        	assertTrue(true);
        }
    }
    
    @Test 
    public void testSellCapsulesToVisitorWrongAttributes() throws Exception {
   	dataImpl.reset();
   	int id1=dataImpl.createBeverage("tea", 10, 10);
   	database.updateBalance(100);
   	dataImpl.buyBoxes(id1, 1);
    try {
    	 dataImpl.sellCapsulesToVisitor(-1,1);
    	 fail();
   }catch(BeverageException be) {
   	assertTrue(true);
   }
    
    try {
   	 dataImpl.sellCapsulesToVisitor(-1,-1);
   	 fail();
  }catch(BeverageException be) {
  	assertTrue(true);
  }
    
    dataImpl.sellCapsulesToVisitor(id1, -1);
    Beverage bev=database.getBeverageData(id1);
   assertEquals(bev.getQuantityAvailable(),10);
    }
    
    @Test
    public void testRechargeAccount() throws Exception{
    	dataImpl.reset();
    	int emp1=dataImpl.createEmployee("john","doe");
    	
    	int credit=dataImpl.rechargeAccount(emp1, 10);
    	
    	//check employee credit
    	Employee emp=database.getEmployeeData(emp1);
    	assertEquals(emp.getCredit(),10);
    	assertEquals(credit,10);
    	//check that transactions have been created 
        List<Transaction> transactionList=database.getReport(shiftDate(-1), shiftDate(1));
        assertEquals(transactionList.size(),1);
        
    	
    }
	   
    @Test
    public void testRechargeAccountWrongAttributes() throws Exception{
    	dataImpl.reset();
    	int emp1=dataImpl.createEmployee("john","doe");
    	
    	//try recharge account with an invalid employee Id
        try {
        	dataImpl.rechargeAccount(-1, 1);
        	fail();
        }catch(EmployeeException e) {
        	assertTrue(true);
        }
      //try recharge account with an invalid employee Id
        try {
        	dataImpl.rechargeAccount(-1, -1);
        	fail();
        }catch(EmployeeException e) {
        	assertTrue(true);
        }
      //try recharge account with a negative amount
        int credit=dataImpl.rechargeAccount(emp1, -1);
        assertEquals(credit,0);
    }
    @Test
	public void testCreateEmployee() throws Exception{
		dataImpl.reset();
		int id=-1;
		id=dataImpl.createEmployee("john", "doe");
		assertNotEquals(id,-1);
		Employee emp = database.getEmployeeData(id);
		assertEquals("john",emp.getName());
		assertEquals("doe",emp.getSurname());
		assertEquals(0,emp.getCredit());
		
	}
    
    
    @Test
   	public void testCreateEmployeeMissingParameters() throws Exception{
   		dataImpl.reset();
   		int id=-1;
   		id=dataImpl.createEmployee("john", "");
   		assertEquals(id,-1);
   		dataImpl.createEmployee("", "doe");
   		dataImpl.createEmployee("", "");
   		
   		assertEquals(dataImpl.getEmployeesId().size(),0);
   		
   	}
    
    @Test
	public void testUpdateEmployee() throws Exception {
		dataImpl.reset();
		int id =-1;
		id=dataImpl.createEmployee("john","doe");
	    
	  
	    dataImpl.updateEmployee(id, "jane", "mary");
	    
	    //check if the object has been updated
	    Employee emp = database.getEmployeeData(id);
	    assertEquals("jane",emp.getName());
	    assertEquals("mary",emp.getSurname());
	    
	   
	}
    
    @Test
   	public void testUpdateEmployeeMissingParameters() throws Exception {
   		dataImpl.reset();
   		int id =-1;
   		id=dataImpl.createEmployee("john","doe");
   		
   	//check that the object has not been updated
   		dataImpl.updateEmployee(id, "", "");
   	 Employee emp = database.getEmployeeData(id);
	    assertEquals("john",emp.getName());
	    assertEquals("doe",emp.getSurname());
	    
	    dataImpl.updateEmployee(id, "john", "");
	    emp = database.getEmployeeData(id);
	    assertEquals("john",emp.getName());
	    assertEquals("doe",emp.getSurname());
	    
	    dataImpl.updateEmployee(id, "", "doe");
	    emp = database.getEmployeeData(id);
	    assertEquals("john",emp.getName());
	    assertEquals("doe",emp.getSurname());
    }
    
    @Test
  	public void testUpdateEmployeeWrongId() throws Exception {
  		dataImpl.reset();
  		
  	// try with an invalid id
        try {
	    	
	    	dataImpl.updateEmployee(-1, "james", "roberts");
	    	fail();
	    	
	    }catch(Exception e) {
	    	assertTrue(true);
	    }
    }
    
    @Test
	public void testGetEmployeeName() throws EmployeeException {
		dataImpl.reset();// used to clear everything before starting the Test
		int id=-1;
		id=dataImpl.createEmployee("john","doe");
		//i get the name of the created string
		String empName = dataImpl.getEmployeeName(id);
		assertEquals(empName,"john");
         
		// throw an EmployeeException because the id is invalid
		try {
			empName=dataImpl.getEmployeeName(-1);
			fail();
		}catch(EmployeeException e){
			assertTrue(true);
		}
	}
    
    @Test
	public void testGetEmployeeSurName() throws EmployeeException {
		dataImpl.reset();// used to clear everything before starting the Test
		int id=-1;
		id=dataImpl.createEmployee("john","doe");
		//i get the Surname of the created string
		String empSurname = dataImpl.getEmployeeSurname(id);
		assertEquals(empSurname,"doe");
         
		// throw an EmployeeException because the id is invalid
		try {
			empSurname=dataImpl.getEmployeeSurname(-1);
			fail();
		}catch(EmployeeException e){
			assertTrue(true);
		}
	}
    
    @Test
    public void testGetEmployeeBalance() throws EmployeeException {
    	dataImpl.reset();// used to clear everything before starting the Test
		int id=-1;
		id=dataImpl.createEmployee("john","doe");
		
		// check employee balance
		dataImpl.rechargeAccount(id, 10);
		assertEquals(10,dataImpl.getEmployeeBalance(id),0.0000000001);
		
		// throw an EmployeeException because the id is invalid
		try {
			dataImpl.getEmployeeBalance(-1);
			fail();
		}catch(EmployeeException e) {
			assertTrue(true);
		}
    }
    
    @Test
    public void testGetEmployeeBalanceZeroBoundary() throws Exception {
    	dataImpl.reset();
    	int id;
		id=dataImpl.createEmployee("john","doe");
		Employee emp = database.getEmployeeData(id);
		//check that the initial employee balance is zero
	
		double credit = emp.getCredit();
		assertEquals(credit,0);
    }
    @Test
    public void testGetEmployeesId() throws EmployeeException {
    	dataImpl.reset();
		List<Integer> expectedList = new ArrayList<>();
		List<Integer> returnList;
		int id1,id2,id3;
		id1=dataImpl.createEmployee("john","doe");
		id2=dataImpl.createEmployee("chris","paul");
		id3=dataImpl.createEmployee("steven","adams");
		expectedList.add(id1);
		expectedList.add(id2);
		expectedList.add(id3);
		returnList= dataImpl.getEmployeesId();
		assertEquals(returnList,expectedList);
    	
    }
    
    @Test
    public void testGetEmployeesIdEmptyList() throws EmployeeException {
    	dataImpl.reset();
    	List<Integer> returnList;
		returnList= dataImpl.getEmployeesId();
		assertTrue(returnList.isEmpty());
    }
    @Test
    public void testGetEmployeesIdOneElement() throws EmployeeException {
    	dataImpl.reset();
		List<Integer> expectedList = new ArrayList<>();
		List<Integer> returnList;
		int id1;
		id1=dataImpl.createEmployee("john","doe");
		expectedList.add(id1);
		returnList= dataImpl.getEmployeesId();
		assertEquals(returnList,expectedList);
    	
    }
    @Test
    public void testGetEmployees() throws EmployeeException {
    	dataImpl.reset();
    	Map<Integer, String> returnMap = new HashMap<>();
    	Map<Integer, String> expectedMap = new HashMap<>();
    	int id1,id2,id3;
    	id1=dataImpl.createEmployee("john","doe");
		id2=dataImpl.createEmployee("chris","paul");
		id3=dataImpl.createEmployee("steven","adams");
		expectedMap.put(id1, "john "+"doe" );
		expectedMap.put(id2, "chris "+"paul");
		expectedMap.put(id3, "steven "+"adams");
		
		returnMap= dataImpl.getEmployees();
		assertEquals(expectedMap,returnMap);
    }
    
    @Test
    public void testGetEmployeesEmptyMap() throws EmployeeException {
    	dataImpl.reset();
    	Map<Integer, String> returnMap = new HashMap<>();
		returnMap= dataImpl.getEmployees();
		assertTrue(returnMap.isEmpty());
    }
    @Test
    public void testGetEmployeesOneElement() throws EmployeeException {
    	dataImpl.reset();
    	Map<Integer, String> returnMap = new HashMap<>();
    	Map<Integer, String> expectedMap = new HashMap<>();
    	int id1=dataImpl.createEmployee("john","doe");
    	expectedMap.put(id1, "john "+"doe" );
		returnMap= dataImpl.getEmployees();
		assertEquals(expectedMap,returnMap);
    }
    @Test
    public void testGetBalance() throws Exception {
    	assertEquals(dataImpl.getBalance(),0,0.0000000001);
    	database.updateBalance(100.50);
    	assertEquals(dataImpl.getBalance(),Math.round((float)100.50),0.0000000001);
    }
    @Test
    public void TestGetReport() throws Exception {
    	dataImpl.reset();
    	List<String> returnReport = new ArrayList<>();
    	List<String> excpectedReport = new ArrayList<>();
    	List<Transaction> transactionList;
    	String s;
    	int id,empId;
    	database.updateBalance(500);
    	empId= dataImpl.createEmployee("ndjekoua", "sandjo");
    	id=dataImpl.createBeverage("coffee",10, 100);
    	
    	//i make 5 transactions
    	dataImpl.buyBoxes(id,3);// so i will spend 2*100cent to buy 2*10 capsules; Transaction of TYPE= P
    	dataImpl.rechargeAccount(empId,500);// this should create a transaction of TYPE=R
		dataImpl.sellCapsules(empId,id,10,true);//TransactionType=C fromAccount=yes
		dataImpl.sellCapsules(empId,id,10,false);//TransactionType=C fromAccount=flase
		dataImpl.sellCapsulesToVisitor(id,10);//TransactionType=C TO VISITOR
    	
		//get all the 5 transactions from DB
    	transactionList = database.getReport(shiftDate(-1), shiftDate(1));
    	assertEquals(transactionList.size(),5); // to be uncommented after pasty has defined useful methods
    	Collections.sort(transactionList, new sortById());//i order to  be sure that strings are as i expect in order to build expected list
    	
    	//build now the excpected list of strings
    	s=dataImpl.convDate(transactionList.get(0).getTransactionDate())+" BUY"+" coffee"+" 3";
        excpectedReport.add(s);
        s=dataImpl.convDate(transactionList.get(1).getTransactionDate())+" RECHARGE"+" ndjekoua"+" sandjo"+" "+dataImpl.convAmountWithCurrency(500);
        excpectedReport.add(s);
        s=dataImpl.convDate(transactionList.get(2).getTransactionDate())+" BALANCE"+" ndjekoua"+" sandjo"+" coffee"+" "+10;
        excpectedReport.add(s);
        s=dataImpl.convDate(transactionList.get(3).getTransactionDate())+" CASH"+" ndjekoua"+" sandjo"+" coffee"+" "+10;
        excpectedReport.add(s);
        s=dataImpl.convDate(transactionList.get(4).getTransactionDate())+" VISITOR"+" coffee"+" "+10;
        excpectedReport.add(s);//will be uncommented when pasty will terminate methods like RechargeAccoun, sellCapsules, ect....
    	returnReport = dataImpl.getReport(shiftDate(-1), shiftDate(+1));
    	//System.out.println("returnreport "+returnReport+"  excpected report"+excpectedReport);
    	assertEquals(excpectedReport,returnReport);
    }
    
    @Test
    public void TestGetReportemptyList() throws Exception {
    	dataImpl.reset();
    	//for loop testing: i do no transaction so the returnList should contain not transaction.
    		List<String> returnList=dataImpl.getReport(shiftDate(-1), shiftDate(+1));
    		assertTrue(returnList.isEmpty());
    	
    }
    public void TestGetReportOneElement() throws Exception {
    	dataImpl.reset();
    	database.updateBalance(500);
    	Integer id=dataImpl.createBeverage("coffee",10, 100);
    	
    	//i make 1 transactions
    	dataImpl.buyBoxes(id,3);// so i will spend 2*100cent to buy 2*10 capsules; Transaction of TYPE= P
    	//for loop testing: i do 1 transaction so the returnList should contain not transaction.
    		List<String> returnList=dataImpl.getReport(shiftDate(-1), shiftDate(+1));
    		assertEquals(returnList.size(),1);
    	
    }
    @Test
    public void TestGetReportWrongDates() throws Exception {
    	dataImpl.reset();
    	//pass wrong dates so i should catch an exception: startDate > endDate
    	
    	try {
    		dataImpl.getReport(shiftDate(1), shiftDate(-1));
    		fail();
    	}catch(DateException de) {
    		assertTrue(true);
    	}
    }
    @Test
    public void TestGetReportNullDates() throws Exception {
    	dataImpl.reset();
    	//pass wrong dates so i should catch an exception: startDate > endDate
    	
    	try {
    		dataImpl.getReport(null, shiftDate(-1));
    		fail();
    	}catch(DateException de) {
    		assertTrue(true);
    		
    	}
    }
    @Test
    public void TestGetReportEmployee() throws Exception {
    	dataImpl.reset();
    	List<String> returnReport = new ArrayList<>();
    	List<String> excpectedReport = new ArrayList<>();
    	List<Transaction> transactionList;
    	String s;
    	int id,empId;
    	database.updateBalance(500);
    	empId= dataImpl.createEmployee("ndjekoua", "sandjo");
    	id=dataImpl.createBeverage("coffee",10, 100);
    	
    	//i make 5 transactions
    	dataImpl.buyBoxes(id,3);// so i will spend 2*100cent to buy 2*10 capsules; Transaction of TYPE= P
    	dataImpl.rechargeAccount(empId,500);// this should create a transaction of TYPE=R
		dataImpl.sellCapsules(empId,id,10,true);//TransactionType=C fromAccount=yes
		dataImpl.sellCapsules(empId,id,10,false);//TransactionType=C fromAccount=flase
		dataImpl.sellCapsulesToVisitor(id,10);//TransactionType=C TO VISITOR
    	
		//get all the 5 transactions from DB
    	transactionList = database.getEmployeeReport(empId, shiftDate(-1), shiftDate(1)); 
    	assertEquals(transactionList.size(),3); // to be uncommented after pasty has defined useful methods
    	Collections.sort(transactionList, new sortById());//i order to  be sure that strings are as i expect in order to build expected list
    	
    	//build now the excpected list of strings
    	
        s=dataImpl.convDate(transactionList.get(0).getTransactionDate())+" RECHARGE"+" ndjekoua"+" sandjo"+" "+dataImpl.convAmountWithCurrency(500);
        excpectedReport.add(s);
        s=dataImpl.convDate(transactionList.get(1).getTransactionDate())+" BALANCE"+" ndjekoua"+" sandjo"+" coffee"+" "+10;
        excpectedReport.add(s);
        s=dataImpl.convDate(transactionList.get(2).getTransactionDate())+" CASH"+" ndjekoua"+" sandjo"+" coffee"+" "+10;
        excpectedReport.add(s);
    
    	returnReport = dataImpl.getEmployeeReport(empId,shiftDate(-1),shiftDate(1));
    	//System.out.println("returnreport "+returnReport+"  excpected report"+excpectedReport);
    	assertEquals(excpectedReport,returnReport);
    }
    
    @Test
    public void TestGetReportEmployeeEmptyList() throws Exception {
    	dataImpl.reset();
    	Integer empId= dataImpl.createEmployee("ndjekoua", "sandjo");
    	//for loop testing: i do no transaction so the returnList should contain not transaction.
    		List<String> returnList=dataImpl.getEmployeeReport(empId, shiftDate(-1), shiftDate(+1));
    		assertTrue(returnList.isEmpty());
    }
    
    @Test
    public void TestGetReportEmployeeOneElement() throws Exception {
    	dataImpl.reset();
    	Integer empId= dataImpl.createEmployee("ndjekoua", "sandjo");
    	dataImpl.rechargeAccount(empId,500);// this should create a transaction of TYPE=R
    	//for loop testing: i do no transaction so the returnList should contain not transaction.
    		List<String> returnList=dataImpl.getEmployeeReport(empId, shiftDate(-1), shiftDate(+1));
    		assertEquals(returnList.size(),1);
    }
    @Test
    public void TestGetEmployeeReportWrongEmployee() throws Exception {
    	dataImpl.reset();
    	//pass wrong employeeId so i should catch an exception
    	
    	try {
    		dataImpl.getEmployeeReport(-1,shiftDate(-1), shiftDate(1));
    		fail();
    	}catch(EmployeeException e) {
    		assertTrue(true);
    	}
    }
    
    @Test
    public void TestGetEmployeeReportNullDate() throws Exception {
    	dataImpl.reset();
    	//pass null dates so i should catch an exception: startDate==null
    	
    	try {
    		dataImpl.getEmployeeReport(-1,null, shiftDate(1));
    		fail();
    	}catch(DateException e) {
    		assertTrue(true);
    	}
    }

    @Test
    public void TestGetEmployeeReportWrongDate() throws Exception {
    	dataImpl.reset();
    	Integer empId= dataImpl.createEmployee("ndjekoua", "sandjo");
    	dataImpl.rechargeAccount(empId,500);// this should create a transaction of TYPE=R
    	//: startDate > endDate
    	
    	try {
    		dataImpl.getEmployeeReport(empId,shiftDate(+2), shiftDate(-1));
    		fail();
    	}catch(DateException e) {
    		assertTrue(true);
    	}
    }
	private class sortById implements Comparator<Transaction>{

		@Override
		public int compare(Transaction t1, Transaction t2) {
			// TODO Auto-generated method stub
			return t1.getId() -t2.getId();
		}
		
	}
	/*@param dayNumber can be positive or negati and represent how much we want to shift
	 * eg : shiftDate(-1) return the date of yesterday
	 *       shiftDate(+1) return the date of tomorrow*/
	public static Date shiftDate(int dayNumber) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, dayNumber); // number represents number of days
		Date shiftedDate = cal.getTime();
		return shiftedDate;
	}

}


