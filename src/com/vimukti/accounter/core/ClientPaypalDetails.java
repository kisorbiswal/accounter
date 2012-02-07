package com.vimukti.accounter.core;

public class ClientPaypalDetails {
	
	private String first_name;
	private String last_name;
	private int payment_status;
	private String payer_email;
	private String payment_gross;
	private String mc_currency;
	private String address_country;
	private String clinetEmailId;
	
	public ClientPaypalDetails() {

	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(int payment_status) {
		this.payment_status = payment_status;
	}

	public String getPayer_email() {
		return payer_email;
	}

	public void setPayer_email(String payer_email) {
		this.payer_email = payer_email;
	}

	public String getPayment_gross() {
		return payment_gross;
	}

	public void setPayment_gross(String payment_gross) {
		this.payment_gross = payment_gross;
	}

	public String getMc_currency() {
		return mc_currency;
	}

	public void setMc_currency(String mc_currency) {
		this.mc_currency = mc_currency;
	}

	public String getAddress_country() {
		return address_country;
	}

	public void setAddress_country(String address_country) {
		this.address_country = address_country;
	}

	public String getClinetEmailId() {
		return clinetEmailId;
	}

	public void setClinetEmailId(String clinetEmailId) {
		this.clinetEmailId = clinetEmailId;
	}
	

}
