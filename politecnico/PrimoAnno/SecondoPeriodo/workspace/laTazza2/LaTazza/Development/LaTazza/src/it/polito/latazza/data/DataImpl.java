package it.polito.latazza.data;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import it.polito.latazza.exceptions.BeverageException;
import it.polito.latazza.exceptions.DateException;
import it.polito.latazza.exceptions.EmployeeException;
import it.polito.latazza.exceptions.NotEnoughBalance;
import it.polito.latazza.exceptions.NotEnoughCapsules;
import it.polito.latazza.data.Beverage;
import it.polito.latazza.data.Database;
import it.polito.latazza.data.Transaction;

public class DataImpl implements DataInterface {
     Database database = new Database();
	@Override
	/* @author roy paulin */
	/***is it correct to throw the EmployeeException when the employee balance is not  enough to buy capsules?
	 * there are also some other cases where it's not clear if to throw exception or not, here in the code for those cases we just printed the stackTace
	 ****how do we set or update laTazza balance from the GUI? it looks like there is not a method to update laTazza balance in DataImpl class??
	 ****The amount of the recharge is in cent but in the report it should be printed in euros, is it correct to assume that all them operations related to amount
	 *like(GetEmployeeBalance, buyCapsules ect..) are made in cent and only when we want to produce the report we have to divide by 100 to cnvert in euros rigth??
	 ***** 
	 * of the error*/
	public Integer sellCapsules(Integer employeeId, Integer beverageId, Integer numberOfCapsules, Boolean fromAccount)
			throws EmployeeException, BeverageException, NotEnoughCapsules {
		Employee emp;
		Beverage be;
		try {
			emp=database.getEmployeeData(employeeId);
		
		}catch(Exception e) {
			throw new EmployeeException();
		}
		try {
			be=database.getBeverageData(beverageId);
		
		}catch(Exception e) {
			throw new BeverageException();
		}
		if(numberOfCapsules>0) {
		double d=(numberOfCapsules*(be.getBoxPrice()/be.getCapsulePerBox()));
		if(fromAccount) {
			emp.updateCredit(-1.0*d);
		}
		be.updateCapsuleQuantity(-1*numberOfCapsules);
		try {
			database.updateBeverage(be);
			
		}catch(Exception e) {
			throw new BeverageException();
		}
		if(fromAccount) {
			try {
				database.updateEmployee(emp);
			}catch(Exception e) {
				throw new EmployeeException();
			}
			
		}else {
			 try {
				 double balance=database.getBalance();
			       database.updateBalance(balance+d);
		       } catch (Exception e1) {
			      // TODO Auto-generated catch block
		    	   System.out.println("unable to update la tazza balance");
			    e1.printStackTrace();
		      }
		}
		Transaction tr=new Transaction(-1,new Date(),'C',-1,employeeId,beverageId,numberOfCapsules,-1,fromAccount);
		try {
			database.registerTransaction(tr);
		}catch(Exception e) {
			System.out.println("Unable to regsiter the transaction");
		}
	}
		
		
		
		return Math.round((float)emp.getCredit());
	}

	@Override
	/* @author roy paulin */
	public void sellCapsulesToVisitor(Integer beverageId, Integer numberOfCapsules)
			throws BeverageException, NotEnoughCapsules {
		Beverage be;
		try {
			be=database.getBeverageData(beverageId);
		
		}catch(Exception e) {
			throw new BeverageException();
		}
		if(numberOfCapsules>0) {
		be.updateCapsuleQuantity(-1*numberOfCapsules);
		try {
			database.updateBeverage(be);
			
		}catch(Exception e) {
			throw new BeverageException();
		}
		
		
		double d=(numberOfCapsules*(be.getBoxPrice()/be.getCapsulePerBox()));
		 try {
			 double balance=database.getBalance();
		       database.updateBalance(balance+d);
	       } catch (Exception e1) {
		      // TODO Auto-generated catch block
	    	   System.out.println("unable to update la tazza balance");
		    e1.printStackTrace();
	      }
		Transaction tr=new Transaction(-1,new Date(),'C',-1,-1,beverageId,numberOfCapsules,-1,false);
		try {
			database.registerTransaction(tr);
		}catch(Exception e) {
			System.out.println("Unable to regsiter the transaction");
		}
	}
		
	}

	@Override
	/* @author roy paulin */
	public Integer rechargeAccount(Integer id, Integer amountInCents) throws EmployeeException {
		
		Employee emp;
		try {
			emp=database.getEmployeeData(id);
			
		}catch(Exception e) {
			throw new EmployeeException();
		}
		if(amountInCents>0) {
		try {
			emp.updateCredit(amountInCents);
			database.updateEmployee(emp);
		}catch(Exception e) {
			throw new EmployeeException();
		}
		try {
			 double balance=database.getBalance();
		       database.updateBalance(balance+amountInCents);
	       } catch (Exception e1) {
		      // TODO Auto-generated catch block
	    	   System.out.println("unable to update la tazza balance");
		    e1.printStackTrace();
	      }
		Date date = new Date();
		Transaction tr= new Transaction(-1,date,'R',-1,id,-1,-1,amountInCents, false);
		
		try {
		database.registerTransaction(tr);
	}catch(Exception e) {
		System.out.println("Unable to regsiter the transaction");
	}
	}
		return Math.round((float)emp.getCredit());
		
	}

	
	@Override
	// @author jean thibaut 
	
	
	public void buyBoxes(Integer beverageId, Integer boxQuantity) throws BeverageException, NotEnoughBalance {
		Beverage bev ;
		float amount;
		try {
			 bev = database.getBeverageData(beverageId);
			bev.updateCapsuleQuantity(boxQuantity*bev.getCapsulePerBox());
			
		}catch(Exception be) {
			
			throw new BeverageException();
		}
		//then i update latazza account
			float boxPrice = (float)bev.getBoxPrice();
			 amount = boxPrice * boxQuantity;
			 float balance = 0;
			 try {
				 balance = (float)database.getBalance();
			 }catch(Exception e){
				 System.out.println("Unable to get la laTazza balance");
				//throw new NotEnoughBalance();
				//these are specifics errors: is it correct to throw this exception??o or it's better to do nothing
			 }
			 
			 if((balance-amount)< 0) {
				 throw new NotEnoughBalance();
			 }
			 
			 try {
					database.updateBeverage(bev);
			 }catch(Exception e) {
				 //throw new BeverageException();
			 }
	
            try {
		       database.updateBalance(balance-amount);
	       } catch (Exception e1) {
		     
	    	   System.out.println("unable to update la tazza balance");
		    e1.printStackTrace();
	      }
		//i create the object transaction
		Date date = new Date();
		// for transactions of Type P, the attribute numberOfCapsules is irrelevant
		int numberOfCapsules = boxQuantity*bev.getCapsulePerBox();
		Transaction transaction = new Transaction(-1,date,'P',boxQuantity,-1,beverageId,numberOfCapsules, amount,false);
		try {
		database.registerTransaction(transaction);
		}catch(Exception e) {
			System.out.println("Unable to regsiter the transaction");
		}
		return ;
	}

	@Override
	// @author jean thibaut 
	public List<String> getEmployeeReport(Integer employeeId, Date startDate, Date endDate)
			throws EmployeeException, DateException {
		
		if( startDate==null | endDate==null) {throw new DateException();}
		if(startDate.compareTo(endDate) > 0) {
			throw new DateException();
		}
		List<Transaction> transactionList = new ArrayList<>();
		List<String> returnList = new ArrayList<>();
		Employee emp;
		Beverage bev = null;
		String s;
		// i first get the list of transactions for this employee
		try {
			transactionList = database.getEmployeeReport(employeeId,startDate, endDate);
		} catch (Exception e) {
			throw new EmployeeException();
		}
		// i get the employee data 
		try {
			emp= database.getEmployeeData(employeeId);
		} catch (Exception e) {
			throw new EmployeeException();
		}
		
		// i now build the returnList
		for(Transaction t : transactionList) {
			 s="";
			 
			 if(t.getType()=='C') {
				 s=this.convDate(t.getTransactionDate());
				 try {
						bev = database.getBeverageData(t.getBeverageID());
					} catch (Exception e) {
						//if this happens, it's an internal error which means we did not well registered the transaction
						System.out.println("unable to get beverage data for id"+t.getBeverageID());
					}
				 if(t.isFromAccount()==true) {
					 s= s+" BALANCE";
				 }
				 else {
					 s=s+" CASH";
				 }
				 s=s+" "+emp.getName()+" "+emp.getSurname()+" "+bev.getName()+" "+t.getNumberOfCapsules();
			 }
			 else {//the transaction is for sure of type R=recharge
				 s=this.convDate(t.getTransactionDate())+" RECHARGE"+" "+emp.getName()+" "+emp.getSurname()+" "+this.convAmountWithCurrency(t.getAmount());
			 }
			returnList.add(s);
		}
		return returnList;
	}

	@Override
	//@author jean thibaut
	
	public List<String> getReport(Date startDate, Date endDate) throws DateException {
		// TODO Auto-generated method stub

		/*getReport() //which returns a list of transactions to be formated
		 */
		if( startDate==null | endDate==null) {throw new DateException();}
		if(startDate.compareTo(endDate) > 0) {
			throw new DateException();
		}
		List<Transaction> transactionList = new ArrayList<>();
		List<String> returnList = new ArrayList<>();
		Employee emp = null;
		Beverage bev = null;
		String s;
		
       try {
		transactionList = database.getReport(startDate, endDate);
	  } catch (Exception e) {
		e.printStackTrace();
	  }
       
       
    // i now build the returnList
    		for(Transaction t : transactionList) {
    			 s=new String();
    			 //s.replaceAll(" ","");
    			 if(t.getType()=='C') {
    				 //get the beverage object
    				 try {
    						bev = database.getBeverageData(t.getBeverageID());
    					} catch (Exception e) {
    						//if this happens, it's an internal error which means we did not well registered the transaction
    						System.out.println("unable to get beverage data for id"+t.getBeverageID());
    					}
    				 
    				 s= this.convDate(t.getTransactionDate());
    				   if(t.getEmployeeID() != -1) {//this transaction is related to an employee
    					   try {
    							emp= database.getEmployeeData(t.getEmployeeID());
    						} catch (Exception e) {
    							//this should never happen,otherwise we did an error creating the transaction
    							System.out.println("unable to get employee data from getReport");
    						}
    					   if(t.isFromAccount()==true) {
    	    					 s= s+" BALANCE";
    	    				 }
    	    				 else {
    	    					 s=s+" CASH";
    	    				 }
    					   s=s+" "+emp.getName()+" "+emp.getSurname()+" "+bev.getName()+" "+t.getNumberOfCapsules();
    				   }
    				   else {//the transaction is related to a visitor
    					   s = s+" VISITOR"+" "+bev.getName()+" "+t.getNumberOfCapsules();
    				   }
    			 }
    			 
    			 if(t.getType() == 'R') {
    				 try {
							emp= database.getEmployeeData(t.getEmployeeID());
						} catch (Exception e) {
							//this should never happen,otherwise we did an error creating the transaction
							System.out.println("unable to get employee data from getReport");
						}
    				 s= this.convDate(t.getTransactionDate())+" RECHARGE"+" "+emp.getName()+" "+emp.getSurname()+" "+this.convAmountWithCurrency(t.getAmount());
    			 }
    			 
    			 if(t.getType() == 'P') {
    				 try {
 						bev = database.getBeverageData(t.getBeverageID());
 					} catch (Exception e) {
 						//if this happens, it's an internal error which means we did not well registered the transaction
 						System.out.println("unable to get beverage data for id"+t.getBeverageID());
 					}
    				 s=this.convDate(t.getTransactionDate())+" BUY"+" "+bev.getName()+" "+t.getBoxQuantity();
    				 
    			 }
    			 
    			returnList.add(s);
    		}
    		return returnList;
	}

	@Override
	/* @author jean thibaut */
	public Integer createBeverage(String name, Integer capsulesPerBox, Integer boxPrice) throws BeverageException {
	
		Integer id=-1;
		Beverage b= new Beverage(-1,0,boxPrice,capsulesPerBox,name);
		try{
			id=database.addBeverage(b);
			}catch( Exception e) {
				throw new BeverageException();
			}
		return id;
	}

	@Override
	/* @author jean thibaut */
	public void updateBeverage(Integer id, String name, Integer capsulesPerBox, Integer boxPrice)
			throws BeverageException {
		try {
		 Beverage bev = database.getBeverageData(id);
         bev.setName(name);
		 bev.setCapsulePerBox(capsulesPerBox);
		 bev.setBoxPrice(boxPrice);
		 database.updateBeverage(bev);
		}catch(Exception be) {
			throw new BeverageException() ;
			
		}
	}

	@Override
	/* @author jean thibaut */
	public String getBeverageName(Integer id) throws BeverageException {
		String name;
		try {
		    name = database.getBeverageData(id).getName();
		}
		catch(Exception be) {
			
			throw new BeverageException() ;
		}
		
		return name;
	}

	@Override
	/* @author jean thibaut */
	public Integer getBeverageCapsulesPerBox(Integer id) throws BeverageException {
		Integer capsulesPerBox;
		try {
		   capsulesPerBox = database.getBeverageData(id).getCapsulePerBox();
		}
		catch(Exception be) {
			
			throw new BeverageException() ;
		}
		return capsulesPerBox;
	}

	@Override
	/* @author jean thibaut */
	public Integer getBeverageBoxPrice(Integer id) throws BeverageException {
		float price;
		try {
		    price = (float)database.getBeverageData(id).getBoxPrice();
		}
		catch(Exception be) {
			
			throw new BeverageException() ;
		}
		return Math.round(price);
	}

	@Override
	/* @author jean thibaut */
	public List<Integer> getBeveragesId() {
		
		List<Integer> beveragesId = new ArrayList<>();
		List<Beverage> beverages = new ArrayList<>();
		try{
			beverages = database.getListOfBeverages();
		}
		catch(Exception e){
			System.out.println("cannot get the list of beverages");
		}
		for(Beverage b : beverages) {
			beveragesId.add(b.getId());
		}
		return beveragesId;
	}

	@Override
	/* @author jean thibaut */
	public Map<Integer, String> getBeverages() {
		
		Map<Integer, String> mapBeverages = new HashMap<>();
		List<Beverage> beverages = new ArrayList<>();
		try{
			beverages = database.getListOfBeverages();
		}catch(Exception e) {
			
		}
		for(Beverage b : beverages) {
			mapBeverages.put(b.getId(),b.getName());
		}
		return mapBeverages;
	}

	@Override
	/* @author jean thibaut */
	public Integer getBeverageCapsules(Integer id) throws BeverageException {
		
		Integer quantityAvailable;	
		 try {
			   quantityAvailable = database.getBeverageData(id).getQuantityAvailable();
			}
			catch(Exception e) {
				
				throw new BeverageException() ;
			}
		return quantityAvailable;
	}

	@Override
	/* @author roy paulin */
	public Integer createEmployee(String name, String surname) throws EmployeeException {
		
		Integer id=0;
		if((name!="") && (surname!="") ) { 
		Employee em= new Employee(-1,name,surname,0.0);
		try{
			id=database.addEmployee(em);
			}catch( Exception e) {
				throw new EmployeeException();
			}
	} else {
		return -1;
	}
		return id;
		
	}

	@Override
	/* @author roy paulin */
	public void updateEmployee(Integer id, String name, String surname) throws EmployeeException {
		if((name!="") && (surname!="") ) { 
		try {
			Employee emp = database.getEmployeeData(id);
	         emp.setName(name);
			 emp.setSurname(surname);
			 database.updateEmployee(emp);
			}catch(Exception e) {
				throw new EmployeeException() ;
				
			}
		}
	}

	@Override
	/* @author roy paulin */
	public String getEmployeeName(Integer id) throws EmployeeException {
		// TODO Auto-generated method stub
		/*getEmployeeData().getName()
		 */
		Employee emp;
		try {
		 emp = database.getEmployeeData(id);
		
	}catch( Exception e) {
		throw new EmployeeException();
	}
		return emp.getName();
	}

	@Override
	/* @author roy paulin */
	public String getEmployeeSurname(Integer id) throws EmployeeException {
		Employee emp;
		try {
		 emp = database.getEmployeeData(id);
		
	}catch( Exception e) {
		throw new EmployeeException();
	}
		// TODO Auto-generated method stub
		/*getEmployeeData().getSurname()
		 */
		return emp.getSurname();
	}

	@Override
	/* @author roy paulin */
	public Integer getEmployeeBalance(Integer id) throws EmployeeException {
		Employee emp;
		try {
		 emp = database.getEmployeeData(id);
		
	}catch( Exception e) {
		throw new EmployeeException();
	}
		// TODO Auto-generated method stub
	   /*getEmployeeData().getEmployeecredit()
		 */
		return (int)Math.round(emp.getCredit());
	}

	@Override
	/* @author roy paulin */
	public List<Integer> getEmployeesId() {
		List<Integer> employeesId = new ArrayList<>();
		List<Employee>  employees= new ArrayList<>();
		try{
			employees = database.getListEmployee();
		}
		catch(Exception e){
			System.out.println("cannot get the list of Employees");
		}
		for(Employee emp : employees) {
			employeesId.add(emp.getId());
		}
		// TODO Auto-generated method stub
		  /*getEmployeeData().getEmployeeID()
		 */
		return employeesId;
	}

	@Override
	/* @author roy paulin */
	public Map<Integer, String> getEmployees() {
		Map<Integer, String> employeesMap=new HashMap<Integer, String>();
		List<Employee> employees = new ArrayList<>();
		try{
			employees = database.getListEmployee();
		}catch(Exception e) {
			
		}
		for(Employee emp : employees) {
			employeesMap.put(emp.getId(),emp.getName()+" "+emp.getSurname());
		}
		// TODO Auto-generated method stub
		/*getListOfEmployee() //then transforms in Map
		 */
		return employeesMap;
	}

	@Override
	/* @author roy paulin */
	public Integer getBalance() {
		double  d=0;
		try {
		d=database.getBalance();
		}catch(Exception e) {
			System.out.println("cannot get the Latazza account");
		}
		// TODO Auto-generated method stub
		// getBalance()
		return Math.round((float)d);
	}

	@Override
	/* @author jean thibaut */
	public void reset() {
		// TODO Auto-generated method stub
		/*truncateTables()
		 */
		try {
			database.truncateTables();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    //This method is mainly usefull to have the right date format.
	public  String convDate(Date date_init) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = dateFormat.format(date_init);
		//System.out.println("created: " + newDate);
		return newDate;
	}
	
	public String convAmountWithCurrency(double amount) {
		DecimalFormat df = new DecimalFormat("#.##");
		double a = Double.valueOf(df.format(amount/100));
		return DecimalFormat.getCurrencyInstance(Locale.GERMANY).format(a);
	}

}
