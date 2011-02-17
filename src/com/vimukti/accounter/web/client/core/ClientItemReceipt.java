package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientItemReceipt extends ClientTransaction {

	boolean toBePrinted;
	boolean toBeEmailed;

	String vendor;

	ClientAccount shipTo;

	long purchaseOrderDate;

	ClientContact contact;

	ClientAddress vendorAddress;

	String phone;

	String paymentTerm;

	long deliveryDate;

	ClientAddress shippingAddress;

	String shippingTerm;

	String shippingMethod;

	private String purchaseOrder;

	boolean isBilled;

	public boolean isToBePrinted() {
		return toBePrinted;
	}

	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public ClientAccount getShipTo() {
		return shipTo;
	}

	public void setShipTo(ClientAccount shipTo) {
		this.shipTo = shipTo;
	}

	public long getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	public void setPurchaseOrderDate(long purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public ClientContact getContact() {
		return contact;
	}

	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	public ClientAddress getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public long getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public ClientAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ClientAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingTerm() {
		return shippingTerm;
	}

	public void setShippingTerm(String shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public boolean isBilled() {
		return isBilled;
	}

	public void setBilled(boolean isBilled) {
		this.isBilled = isBilled;
	}

	public boolean isToBeEmailed() {
		return toBeEmailed;
	}

	public void setToBeEmailed(boolean toBeEmailed) {
		this.toBeEmailed = toBeEmailed;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientItemReceipt";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ITEMRECEIPT;
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

}
