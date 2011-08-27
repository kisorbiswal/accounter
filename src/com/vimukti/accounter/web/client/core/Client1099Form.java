package com.vimukti.accounter.web.client.core;

public class Client1099Form implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	ClientVendor vendor;
	double[] boxes = new double[15];
	double total1099Payments;
	double totalAllPayments;
	boolean isSelected;

	private int version;

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

	public ClientVendor getVendor() {
		return vendor;
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public double getBox(int i) {

		return boxes[i];
	}

	public void setBox(int i, double box) {
		this.boxes[i] = box;
	}

	public double getTotal1099Payments() {
		for (double box : boxes) {
			total1099Payments += box;
		}
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
		information.append(vendor.getName()).append("\n");
		if (address != null) {
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
		}
		String taxId = this.vendor.getTaxId();
		if (taxId != null && !taxId.equals(""))
			information.append("Tax ID: ").append(taxId);
		return information.toString();
	}

	private ClientAddress getAddress() {
		for (ClientAddress address : vendor.address) {
			if (address != null && address.type == ClientAddress.TYPE_BILL_TO) {
				return address;
			}
		}
		for (ClientAddress address : vendor.address) {
			if (address != null) {
				return address;
			}
		}
		return null;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

}
