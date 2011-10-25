package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * A type of {@link Payee} which deals with the filing and paying of 'VAT' owed.
 * It contains two separate accounts; one for purchase transactions and the
 * other for sales.
 * 
 * @author Chandan
 * 
 */
public class TAXAgency extends Payee {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3306854477062491595L;

	/**
	 * Payment Term for this VATAgency
	 * 
	 * @see PaymentTerms
	 */
	PaymentTerms paymentTerm;

	/**
	 * Liability Account for this VATAgency
	 * 
	 * @see Account
	 */
	@ReffereredObject
	Account purchaseLiabilityAccount;

	@ReffereredObject
	Account salesLiabilityAccount;

	public final static int RETURN_TYPE_NONE = 0;
	public final static int RETURN_TYPE_UK_VAT = 1;
	public final static int RETURN_TYPE_IRELAND_VAT = 2;

	public final static int TAX_TYPE_SALESTAX = 1;
	public final static int TAX_TYPE_VAT = 2;
	public final static int TAX_TYPE_SERVICETAX = 3;
	public final static int TAX_TYPE_TDS = 4;
	public final static int TAX_TYPE_OTHER = 5;

	int VATReturn;
	int taxType;

	public int getTaxType() {
		return taxType;
	}

	public void setTaxType(int taxType) {
		this.taxType = taxType;
	}

	public TAXAgency() {
		setType(Payee.TYPE_TAX_AGENCY);
	}

	/**
	 * @return the purchaseLiabilityAccount
	 */
	public Account getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	/**
	 * @param purchaseLiabilityAccount
	 *            the purchaseLiabilityAccount to set
	 */
	public void setPurchaseLiabilityAccount(Account purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public Account getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	public void setSalesLiabilityAccount(Account salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	/**
	 * @return the vATReturn
	 */
	public int getVATReturn() {
		return VATReturn;
	}

	/**
	 * @param return1
	 *            the vATReturn to set
	 */
	public void setVATReturn(int return1) {
		VATReturn = return1;
	}

	/**
	 * @return the address
	 */
	@Override
	public Set<Address> getAddress() {
		return address;
	}

	/**
	 * @return the webPageAddress
	 */
	@Override
	public String getWebPageAddress() {
		return webPageAddress;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * Deals with updating the VATAgency's liability Account's balance. If it is
	 * called through a {@link Customer} transaction, the account to be
	 * considered is its salesLiabilityAccount else if it is {@link Vendor}
	 * Transaction, then the account is purchaseLiabilityAccount.
	 * 
	 * @param session
	 * @param transaction
	 * @param transactionCategory
	 * @param amount
	 */
	public void updateVATAgencyAccount(Session session,
			Transaction transaction, int transactionCategory, double amount) {

		Account account = null;
		if (transactionCategory == Transaction.CATEGORY_CUSTOMER) {
			account = this.salesLiabilityAccount;
		} else if (transactionCategory == Transaction.CATEGORY_VENDOR) {
			account = this.purchaseLiabilityAccount;
		}
		if (account != null) {
			account.updateCurrentBalance(transaction, amount);
			session.update(account);
			account.onUpdate(session);
		}

	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.TAXAGENCY);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		setType(TYPE_TAX_AGENCY);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		super.onUpdate(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public long getID() {

		return this.id;
	}

	@Override
	public Account getAccount() {

		return null;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		TAXAgency taxAgency = (TAXAgency) clientObject;
		Query query = session.getNamedQuery("getTaxAgency.by.Name")
				.setString("name", taxAgency.name)
				.setEntity("company", taxAgency.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			TAXAgency newTaxAgency = (TAXAgency) list.get(0);
			if (taxAgency.id != newTaxAgency.id) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A TAXAgency already exists with this name");
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	protected JournalEntry createJournalEntry(Payee payee) {
		// TODO Auto-generated method stub
		return null;
	}

}
