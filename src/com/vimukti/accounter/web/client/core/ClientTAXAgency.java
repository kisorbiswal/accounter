/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Murali
 * 
 */
public class ClientTAXAgency extends ClientPayee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Payment Term for this TaxAgency
	 * 
	 * @see
	 */
	long paymentTerm;

	/**
	 * Liability Account for this TaxAgency
	 * 
	 * @see
	 */
	long purchaseLiabilityAccount;

	long salesLiabilityAccount;

	long filedLiabilityAccount;

	public final static int RETURN_TYPE_NONE = 0;
	public final static int RETURN_TYPE_UK_VAT = 1;
	public final static int RETURN_TYPE_IRELAND_VAT = 2;

	public final static int TAX_TYPE_SALESTAX = 1;
	public final static int TAX_TYPE_VAT = 2;
	public final static int TAX_TYPE_SERVICETAX = 3;
	public final static int TAX_TYPE_TDS = 4;
	public final static int TAX_TYPE_OTHER = 5;

	public final static int TAX_RETURN_FREQUENCY_MONTHLY = 0;
	public final static int TAX_RETURN_FREQUENCY_QUARTERLY = 1;
	public final static int TAX_RETURN_FREQUENCY_HALF_YEARLY = 2;
	public final static int TAX_RETURN_FREQUENCY_YEARLY = 3;

	int VATReturn;

	int taxType;

	int tAXFilingFrequency;

	public int getTaxType() {
		return taxType;
	}

	public void setTaxType(int taxType) {
		this.taxType = taxType;
	}

	/**
	 * 
	 */
	public ClientTAXAgency() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getClientClassSimpleName
	 * ()
	 */
	@Override
	public String getClientClassSimpleName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAXAGENCY;
	}

	public long getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the purchaseLiabilityAccount
	 */
	public long getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	/**
	 * @param purchaseLiabilityAccount
	 *            the purchaseLiabilityAccount to set
	 */
	public void setPurchaseLiabilityAccount(long purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	public long getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	public void setSalesLiabilityAccount(long salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	public int getVATReturn() {
		return VATReturn;
	}

	public void setVATReturn(int vATReturn) {
		VATReturn = vATReturn;
	}

	public ClientTAXAgency clone() {
		ClientTAXAgency taxAgency = (ClientTAXAgency) this.clone();

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		for (ClientAddress clientAddress : this.address) {
			addresses.add(clientAddress.clone());
		}
		taxAgency.address = address;

		Set<ClientContact> contacts = new HashSet<ClientContact>();
		for (ClientContact clientContact : this.contacts) {
			contacts.add(clientContact.clone());
		}
		taxAgency.contacts = contacts;

		Set<ClientEmail> emails = new HashSet<ClientEmail>();
		for (ClientEmail clientEmail : this.emails) {
			emails.add(clientEmail.clone());
		}
		taxAgency.emails = emails;

		Set<ClientFax> faxes = new HashSet<ClientFax>();
		for (ClientFax clientFax : this.faxNumbers) {
			faxes.add(clientFax.clone());
		}
		taxAgency.faxNumbers = faxes;

		Set<ClientPhone> phones = new HashSet<ClientPhone>();
		for (ClientPhone clientPhone : this.phoneNumbers) {
			phones.add(clientPhone.clone());
		}
		taxAgency.phoneNumbers = phones;

		return taxAgency;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientTAXAgency) {
			ClientTAXAgency taxAgency = (ClientTAXAgency) obj;
			return this.getID() == taxAgency.getID() ? true : false;
		}
		return false;
	}

	/**
	 * @return the tAXReturnfrequency
	 */
	public int getTAXFilingFrequency() {
		return tAXFilingFrequency;
	}

	/**
	 * @param tAXReturnfrequency
	 *            the tAXReturnfrequency to set
	 */
	public void setTAXFilingFrequency(int tAXReturnfrequency) {
		this.tAXFilingFrequency = tAXReturnfrequency;
	}

	/**
	 * @return the filedLiabilityAccount
	 */
	public long getFiledLiabilityAccount() {
		return filedLiabilityAccount;
	}

	/**
	 * @param filedLiabilityAccount
	 *            the filedLiabilityAccount to set
	 */
	public void setFiledLiabilityAccount(long filedLiabilityAccount) {
		this.filedLiabilityAccount = filedLiabilityAccount;
	}
}
