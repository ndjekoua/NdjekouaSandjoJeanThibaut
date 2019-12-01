/**
 * 
 */
package it.polito.latazza.data;



import it.polito.latazza.exceptions.NotEnoughCapsules;

/**
 * @author elia
 *
 */
public class Beverage {
	int id,quantityAvailable;
	double boxPrice;
	int capsulePerBox;
	
	public int getId() {
		return id;
	}
	public int getQuantityAvailable() {
		return quantityAvailable;
	}
	public double getBoxPrice() {
		return boxPrice;
	}
	public int getCapsulePerBox() {
		return capsulePerBox;
	}
	public String getName() {
		return name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public void setBoxPrice(Integer boxPrice) {
		this.boxPrice = boxPrice;
	}
	public void setCapsulePerBox(int capsulePerBox) {
		this.capsulePerBox = capsulePerBox;
	}
	public void setName(String name) {
		this.name = name;
	}

	String name;
	public Beverage(int id, int quantityAvailable, double boxPrice, int capsulePerBox, String name) {
		super();
		this.id = id;
		this.quantityAvailable = quantityAvailable;
		this.boxPrice = boxPrice;
		this.capsulePerBox = capsulePerBox;
		this.name = name;
	}

    /*The Quantity can be either positive buying capsules or negative selling*/
	public void updateCapsuleQuantity(int quantity)throws NotEnoughCapsules {
		if((this.quantityAvailable +quantity)< 0) {
			throw new NotEnoughCapsules() ;
		}
		 this.quantityAvailable += quantity ;
		 return ;
	}
}
