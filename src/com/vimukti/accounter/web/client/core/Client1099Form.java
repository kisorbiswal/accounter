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
	double[] boxes = new double[12];
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

	public double getBox(int i) {
		return boxes[i];
	}

	public void setBox1(int i, double box) {
		this.boxes[i] = box;
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
