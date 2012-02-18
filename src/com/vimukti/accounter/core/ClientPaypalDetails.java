package com.vimukti.accounter.core;

public class ClientPaypalDetails {

	long id;

	private String firstname;
	private String lastname;
	private String paymentStatus;
	private String payerEmail;
	private double paymentGross;
	private String mcCurrency;
	private String addressCountry;
	private String clinetEmailId;

	public ClientPaypalDetails() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public double getPaymentGross() {
		return paymentGross;
	}

	public void setPaymentGross(double paymentGross) {
		this.paymentGross = paymentGross;
	}

	public String getMcCurrency() {
		return mcCurrency;
	}

	public void setMcCurrency(String mcCurrency) {
		this.mcCurrency = mcCurrency;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getClinetEmailId(){
		return clinetEmailId;
	}

	public void setClinetEmailId(String clinetEmailId) {
		this.clinetEmailId = clinetEmailId;
	}

}
