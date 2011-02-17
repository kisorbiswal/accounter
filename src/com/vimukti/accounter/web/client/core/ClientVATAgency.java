/**
 * 
 */
package com.vimukti.accounter.web.client.core;


/**
 * @author Murali
 * 
 */
@SuppressWarnings("serial")
public class ClientVATAgency extends ClientPayee {

	/**
	 * Payment Term for this TaxAgency
	 * 
	 * @see
	 */
	String paymentTerm;

	/**
	 * Liability Account for this TaxAgency
	 * 
	 * @see
	 */
	String purchaseLiabilityAccount;

	String salesLiabilityAccount;

	public final static int RETURN_TYPE_NONE = 0;
	public final static int RETURN_TYPE_UK_VAT = 1;
	public final static int RETURN_TYPE_IRELAND_VAT = 2;

	int VATReturn;

	/**
	 * 
	 */
	public ClientVATAgency() {
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
		// TODO Auto-generated method stub
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

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the purchaseLiabilityAccount
	 */
	public String getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	/**
	 * @param purchaseLiabilityAccount
	 *            the purchaseLiabilityAccount to set
	 */
	public void setPurchaseLiabilityAccount(String purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	public String getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	public void setSalesLiabilityAccount(String salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	public int getVATReturn() {
		return VATReturn;
	}

	public void setVATReturn(int vATReturn) {
		VATReturn = vATReturn;
	}

}
