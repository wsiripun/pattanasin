package com.erp.pattanasin.db.model;

public class Customer {
	private static final long serialVersionUID = 1L;
	
	private int customerID;
	private String firstName = null;
	private String lastName =  null;
	private boolean	active = true;
	private String email =  null;
	private String phoneNumber =  null;
	private float outstandingMoneyBalance = 0;
	private int outstandingBagBalance = 0;
	
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public float getOutstandingMoneyBalance() {
		return outstandingMoneyBalance;
	}
	public void setOutstandingMoneyBalance(float outstandingMoneyBalance) {
		this.outstandingMoneyBalance = outstandingMoneyBalance;
	}
	public int getOutstandingBagBalance() {
		return outstandingBagBalance;
	}
	public void setOutstandingBagBalance(int outstandingBagBalance) {
		this.outstandingBagBalance = outstandingBagBalance;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", active=" + active + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", phoneNumber=" + phoneNumber + "]";
	}
}
