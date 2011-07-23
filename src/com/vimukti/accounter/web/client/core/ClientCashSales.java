package com.vimukti.accounter.web.client.core;

import java.util.List;

@SuppressWarnings("serial")
public class ClientCashSales extends ClientTransaction {

	String customer;

	ClientContact contact;

	ClientAddress billingAddress;

	ClientAddress shippingAdress;

	String phone;

	String salesPerson;

	String depositIn;

	String shippingTerm;

	String shippingMethod;

	long deliverydate;

	String priceLevel;

	double salesTax = 0D;

	double discountTotal = 0D;
	

	@Override
	public double getAllNonTaxableLineTotal() {
		return totalNonTaxableAmount;
	}

	@Override
	public void setAllNonTaxableLineTotal(double allNonTaxableLineTotal) {
		this.totalNonTaxableAmount = allNonTaxableLineTotal;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the contact
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public ClientAddress getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(ClientAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public ClientAddress getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
	public void setShippingAdress(ClientAddress shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the salesPerson
	 */

	public String getSalesPerson() {
		return this.salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the depositIn
	 */
	public String getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(String depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the shippingTerm
	 */
	public String getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(String shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the deliverydate
	 */
	public long getDeliverydate() {
		return deliverydate;
	}

	/**
	 * @param deliverydate
	 *            the deliverydate to set
	 */
	public void setDeliverydate(long deliverydate) {
		this.deliverydate = deliverydate;
	}

	/**
	 * @return the priceLevel
	 */
	public String getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the salesTax
	 */
	public double getSalesTax() {
		return salesTax;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	public void setSalesTax(double salesTax) {
		this.salesTax = salesTax;
	}

	/**
	 * @return the allLineTotal
	 */
	@Override
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @param allLineTotal
	 *            the allLineTotal to set
	 */
	@Override
	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	/**
	 * @return the allTaxableLineTotal
	 */
	@Override
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @param allTaxableLineTotal
	 *            the allTaxableLineTotal to set
	 */
	@Override
	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *            the discountTotal to set
	 */
	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	@Override
	public void setTransactionItems(List<ClientTransactionItem> transactionItems) {
		super.setTransactionItems(transactionItems);

		this.subTotal = getLineTotalSum();
		this.totalTaxableAmount = getTaxableLineTotalSum();
		this.discountTotal = getDiscountTotalSum();

	}

	@Override
	public String getDisplayName() {

		return this.getName();
	}

	@Override
	public String getName() {

		return Utility.getTransactionName(getType());
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientCashSales";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CASHSALES;
	}
}
