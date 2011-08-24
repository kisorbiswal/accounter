package com.vimukti.accounter.web.client.core;

public class Client1099Form implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String vendorName;
	ClientAddress address;
	String taxId;
	double box1;
	double box2;
	double box3;
	double box4;
	double box5;
	double box6;
	double box7;
	double box8;
	double box9;
	double box10;
	double box11;
	double box12;
	double box13;
	double box14;
	double total1099Payments;
	double totalAllPayments;
	boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public double getBox1() {
		return box1;
	}

	public void setBox1(double box1) {
		this.box1 = box1;
	}

	public double getBox2() {
		return box2;
	}

	public void setBox2(double box2) {
		this.box2 = box2;
	}

	public double getBox3() {
		return box3;
	}

	public void setBox3(double box3) {
		this.box3 = box3;
	}

	public double getBox4() {
		return box4;
	}

	public void setBox4(double box4) {
		this.box4 = box4;
	}

	public double getBox5() {
		return box5;
	}

	public void setBox5(double box5) {
		this.box5 = box5;
	}

	public double getBox6() {
		return box6;
	}

	public void setBox6(double box6) {
		this.box6 = box6;
	}

	public double getBox7() {
		return box7;
	}

	public void setBox7(double box7) {
		this.box7 = box7;
	}

	public double getBox8() {
		return box8;
	}

	public void setBox8(double box8) {
		this.box8 = box8;
	}

	public double getBox9() {
		return box9;
	}

	public void setBox9(double box9) {
		this.box9 = box9;
	}

	public double getBox10() {
		return box10;
	}

	public void setBox10(double box10) {
		this.box10 = box10;
	}

	public double getBox11() {
		return box11;
	}

	public void setBox11(double box11) {
		this.box11 = box11;
	}

	public double getBox12() {
		return box12;
	}

	public void setBox12(double box12) {
		this.box12 = box12;
	}

	public double getBox13() {
		return box13;
	}

	public void setBox13(double box13) {
		this.box13 = box13;
	}

	public double getBox14() {
		return box14;
	}

	public void setBox14(double box14) {
		this.box14 = box14;
	}

	public double getTotal1099Payments() {
		return total1099Payments;
	}

	public void setTotal1099Payments(double total1099Payments) {
		this.total1099Payments = total1099Payments;
	}

	public double getTotalAllPayments() {
		return totalAllPayments;
	}

	public void setTotalAllPayments(double totalAllPayments) {
		this.totalAllPayments = totalAllPayments;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "1099Form";
	}

	public String getVendorInformation() {
		StringBuffer information = new StringBuffer();
		ClientAddress address = this.getAddress();
		information.append(this.getName()).append("\n");
		String address1 = address.getAddress1();
		if (address1 != null && !address.equals(""))
			information.append(address1).append(", ");
		String street = address.getStreet();
		if (street != null && !street.equals(""))
			information.append(address.getStreet()).append("\n");
		String city = address.getCity();
		if (city != null && !city.equals(""))
			information.append(city).append(", ");
		String state = address.getStateOrProvinence();
		if (state != null && !state.equals(""))
			information.append(state).append(" ");
		String zip = address.getZipOrPostalCode();
		if (zip != null && !zip.equals(""))
			information.append(zip).append("\n");
		String country = address.getCountryOrRegion();
		if (country != null && !country.equals(""))
			information.append(country).append("\n");
		String taxId = this.getTaxId();
		if (taxId != null && !taxId.equals(""))
			information.append("Tax ID: ").append(taxId);
		return information.toString();
	}

}
