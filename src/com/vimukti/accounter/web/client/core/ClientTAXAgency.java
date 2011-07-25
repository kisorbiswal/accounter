/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author Murali
 * 
 */
@SuppressWarnings("serial") 
public class ClientTAXAgency extends ClientPayee {

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

	public final static int RETURN_TYPE_NONE = 0;
	public final static int RETURN_TYPE_UK_VAT = 1;
	public final static int RETURN_TYPE_IRELAND_VAT = 2;

	int VATReturn;

	/**
	 * 
	 */
	public ClientTAXAgency() {
		// TODO Auto-generated constructor stub
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

}
