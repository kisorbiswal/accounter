package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientTransactionData implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	String transactionNumber;

	List<ClientPaymentTerms> paymentTerms;

	int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber
	 *            the transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) throws Exception {
		if (transactionNumber == null)
			throw new Exception(Accounter.messages().transactionNumberCouldNotBeInitiated());
		this.transactionNumber = transactionNumber;

	}

	/**
	 * @return the paymentTerms
	 */
	public List<ClientPaymentTerms> getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * @param paymentTerms
	 *            the paymentTerms to set
	 */
	public void setPaymentTerms(List<ClientPaymentTerms> paymentTerms)
			throws Exception {
		if (paymentTerms == null)
			throw new Exception(
					Accounter.messages().ListofPaymentTermsCouldNotBeInitiated());
		this.paymentTerms = paymentTerms;
	}

	/**
	 * @return the accounts
	 */
	public List<ClientAccount> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 *            the accounts to set
	 */
	public void setAccounts(List<ClientAccount> accounts) throws Exception {
		if (accounts == null)
			throw new Exception(
					Accounter.messages().ListofPaymentTermsCouldNotBeInitiated());
		this.accounts = accounts;
	}

	/**
	 * @return the items
	 */
	public List<ClientItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<ClientItem> items) throws Exception {
		if (items == null)
			throw new Exception(Accounter.messages().listofItemsCouldNotBeInitiated());

		this.items = items;
	}

	/**
	 * @return the shippingTerms
	 */
	public List<ClientShippingTerms> getShippingTerms() {
		return shippingTerms;
	}

	/**
	 * @param shippingTerms
	 *            the shippingTerms to set
	 */
	public void setShippingTerms(List<ClientShippingTerms> shippingTerms)
			throws Exception {
		if (shippingTerms == null)
			throw new Exception(
					Accounter.messages().listofShippingTermsCouldNotBeInitiated());
		this.shippingTerms = shippingTerms;
	}

	/**
	 * @return the shippingMethods
	 */
	public List<ClientShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	/**
	 * @param shippingMethods
	 *            the shippingMethods to set
	 */
	public void setShippingMethods(List<ClientShippingMethod> shippingMethods)
			throws Exception {
		if (shippingMethods == null)
			throw new Exception(
					Accounter.messages().listofShippingMethodsCouldNotBeInitiated());
		this.shippingMethods = shippingMethods;
	}

	/**
	 * @return the salesPersons
	 */
	public List<ClientSalesPerson> getSalesPersons() {
		return salesPersons;
	}

	/**
	 * @param salesPersons
	 *            the salesPersons to set
	 */
	public void setSalesPersons(List<ClientSalesPerson> salesPersons)
			throws Exception {
		if (salesPersons == null) {
			throw new Exception(
					Accounter.messages().listofPaymentMethodsCouldNotBeInitiated());
		}
		this.salesPersons = salesPersons;
	}

	/**
	 * @return the taxGroups
	 */
	public List<ClientTAXGroup> getTaxGroups() {
		return taxGroups;
	}

	/**
	 * @param taxGroups
	 *            the taxGroups to set
	 */
	public void setTaxGroups(List<ClientTAXGroup> taxGroups) throws Exception {
		if (taxGroups == null) {
			throw new Exception(Accounter.messages().listofTaxGroupsCouldNotBeInitiated());

		}
		this.taxGroups = taxGroups;
	}

	/**
	 * @return the priceLevels
	 */
	public List<ClientPriceLevel> getPriceLevels() {
		return priceLevels;
	}

	/**
	 * @param priceLevels
	 *            the priceLevels to set
	 */
	public void setPriceLevels(List<ClientPriceLevel> priceLevels)
			throws Exception {
		if (priceLevels == null)
			throw new Exception(Accounter.messages().priceLevelsCouldNotBeInitiated());
		this.priceLevels = priceLevels;
	}

	/**
	 * @return the taxCodes
	 */
	// public List<ClientTaxCode> getTaxCodes() {
	// return taxCodes;
	// }
	//
	// /**
	// * @param taxCodes
	// * the taxCodes to set
	// */
	// public void setTaxCodes(List<ClientTaxCode> taxCodes) throws Exception {
	// if (taxCodes == null)
	// throw new Exception("Tax Codes Could Not Be Initiated....");
	// this.taxCodes = taxCodes;
	// }

	/**
	 * @return the taxAgencies
	 */
	// public List<ClientTaxAgency> getTaxAgencies() {
	// return taxAgencies;
	// }

	/**
	 * @param taxAgencies
	 *            the taxAgencies to set
	 */
	// public void setTaxAgencies(List<ClientTaxAgency> taxAgencies)
	// throws Exception {
	// if (taxAgencies == null)
	// throw new Exception("TaxAgency List Could not be Initiated...");
	// this.taxAgencies = taxAgencies;
	// }

	/**
	 * @return the payee
	 */
	public List<ClientPayee> getPayee() {
		return payee;
	}

	/**
	 * @param payee
	 *            the payee to set
	 */
	public void setPayee(List<ClientPayee> payee) throws Exception {
		if (payee == null) {
			throw new Exception(Accounter.messages().payeeListCouldBeInitiated());
		}
		this.payee = payee;
	}

	/**
	 * @return the customers
	 */
	public List<ClientCustomer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers
	 *            the customers to set
	 */
	public void setCustomers(List<ClientCustomer> customers) throws Exception {
		if (customers == null)
			throw new Exception(Accounter.messages().customerListCouldBeInitiated());
		this.customers = customers;
	}

	/**
	 * @return the vendors
	 */
	public List<ClientVendor> getVendors() {
		return vendors;
	}

	/**
	 * @param vendors
	 *            the vendors to set
	 */
	public void setVendors(List<ClientVendor> vendors) throws Exception {
		if (vendors == null)
			throw new Exception(Accounter.messages().customerListCouldBeInitiated());
		this.vendors = vendors;
	}

	List<ClientAccount> accounts;

	List<ClientItem> items;

	List<ClientShippingTerms> shippingTerms;

	List<ClientShippingMethod> shippingMethods;

	List<ClientSalesPerson> salesPersons;

	List<ClientTAXGroup> taxGroups;

	List<ClientPriceLevel> priceLevels;

	// List<ClientTaxCode> taxCodes;

	// List<ClientTaxAgency> taxAgencies;

	List<ClientPayee> payee;

	List<ClientCustomer> customers;

	List<ClientVendor> vendors;

	private long id;

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public ClientTransactionData clone() {
		ClientTransactionData clientTransactionDataClone = (ClientTransactionData) this
				.clone();
		List<ClientPaymentTerms> paymentTerms = new ArrayList<ClientPaymentTerms>();
		for (ClientPaymentTerms clientPaymentTerm : this.paymentTerms) {
			paymentTerms.add(clientPaymentTerm.clone());
		}
		clientTransactionDataClone.paymentTerms = paymentTerms;

		return clientTransactionDataClone;
	}
}
