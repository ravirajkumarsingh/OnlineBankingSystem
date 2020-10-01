package com.cg.dto;

import java.time.LocalDate;
/**
 * @author raviraj
 *
 */
public class CustomerForm {

	
	private String customerName;
	
	private String password;
	
	private String role;
	
	private String customerAddress;
	
	private String customerAadhar;
	
	private String customerPan;
	
	private String customerContact;
	
	private String customerGender;
	
	private LocalDate customerDob;
	
	//private String branchCode;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getRole() 
	{  return role;  }  
	public void setRole(String role) 
	{  
		this.role = role;  }
	
	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerAadhar() {
		return customerAadhar;
	}

	public void setCustomerAadhar(String customerAadhar) {
		this.customerAadhar = customerAadhar;
	}

	public String getCustomerPan() {
		return customerPan;
	}

	public void setCustomerPan(String customerPan) {
		this.customerPan = customerPan;
	}

	public String getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getCustomerGender() {
		return customerGender;
	}

	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}

	public LocalDate getCustomerDob() {
		return customerDob;
	}

	public void setCustomerDob(LocalDate customerDob) {
		this.customerDob = customerDob;
	}

	
	
	
	
}




