/**
 * 
 */
package it.polito.latazza.data;

import it.polito.latazza.exceptions.EmployeeException;

/**
 * @author pauli
 *
 */
public class Employee {
	int id;
	String name;
	String surname;
	double credit;
	
	public Employee(int id, String name, String surname, double d) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.credit = d;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public double getCredit() {
		return credit;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void updateCredit(double amount) throws EmployeeException{
		// TODO Auto-generated method stub
		if(this.credit+amount<0) {
			throw new EmployeeException();
		}
		this.credit+=amount;
	}
	
	
}
