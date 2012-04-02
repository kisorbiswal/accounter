package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * Any transaction which is liable to VAT must be recorded in this
 * VATRateCalculation. It contains all the information about the transaction
 * item, the vat item used, the return box of the vat item, vat item's purchase
 * and sales liabilities accounts, the date of the transaction, the rate by
 * which this vat item effects the transaction item, the vat agency related to
 * that vat item and the {@link VATReturn} reference. All these references are
 * backed up here so as to maintain the record's references as they may get
 * changed on time.
 * 
 * @author Chandan
 * 
 */
public class TAXRateCalculation implements IAccounterServerCore, Lifecycle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9008613714032328895L;
	long id;
	private boolean isVATGroupEntry;
	double vatAmount;
	double lineTotal;
	Transaction transaction;
	TAXItem taxItem;
	FinanceDate transactionDate;
	private int version;
	TAXAgency taxAgency;
	double rate;
	VATReturnBox vatReturnBox;
	Account purchaseLiabilityAccount;
	Account salesLiabilityAccount;

	/**
	 * This reference to VATReturn is used to indicate whether this entry of
	 * VATRateCalculation is filed to any VATReturn.
	 */
	TAXReturn taxReturn;

	/**
	 * This taxDue is used to know how much Vat Amount is remains to pay
	 */
	double taxDue;

	public TAXRateCalculation() {
	}

	public TAXRateCalculation(TAXItem taxItem, Transaction transacton,
			double lineTotal) {

		this.transaction = transacton;
		this.taxItem = taxItem;
		this.transactionDate = transacton.getDate();
		this.taxAgency = taxItem.getTaxAgency();
		this.rate = taxItem.getTaxRate();
		this.vatReturnBox = taxItem.getVatReturnBox();
		this.purchaseLiabilityAccount = this.taxAgency
				.getPurchaseLiabilityAccount();
		this.salesLiabilityAccount = this.taxAgency.getSalesLiabilityAccount();

		this.lineTotal = lineTotal * transacton.getCurrencyFactor();
		this.vatAmount = this.getCeilValueofTAX();
		this.taxDue = this.vatAmount;
	}

	/**
	 * @return the isVATGroupEntry
	 */
	public boolean isVATGroupEntry() {
		return isVATGroupEntry;
	}

	/**
	 * @param isVATGroupEntry
	 *            the isVATGroupEntry to set
	 */
	public void setVATGroupEntry(boolean isVATGroupEntry) {
		this.isVATGroupEntry = isVATGroupEntry;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the vatReturnBox
	 */
	public VATReturnBox getVatReturnBox() {
		return vatReturnBox;
	}

	/**
	 * @param vatReturnBox
	 *            the vatReturnBox to set
	 */
	public void setVatReturnBox(VATReturnBox vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
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
	 * @return the salesLiabilityAccount
	 */
	public Account getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	/**
	 * @param salesLiabilityAccount
	 *            the salesLiabilityAccount to set
	 */
	public void setSalesLiabilityAccount(Account salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	/**
	 * @return the vatReturn
	 */
	public TAXReturn getTAXReturn() {
		return taxReturn;
	}

	/**
	 * @param vatReturn
	 *            the vatReturn to set
	 */
	public void setTAXReturn(TAXReturn vatReturn) {
		this.taxReturn = vatReturn;
	}

	/**
	 * @return the vatAmount
	 */
	public double getVatAmount() {
		return vatAmount;
	}

	/**
	 * @param vatAmount
	 *            the vatAmount to set
	 */
	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	/**
	 * 
	 * @return the lineTotal
	 */
	public double getLineTotal() {
		return lineTotal;
	}

	/**
	 * @param lineTotal
	 */
	public void setlineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	/**
	 * @return the vatItem
	 */
	public TAXItem getTaxItem() {
		return taxItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	/**
	 * @return the transactionDate
	 */
	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionItem
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionItem
	 *            the transactionItem to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		this.taxAgency.updateTAXAgencyAccount(s, transaction, -this.vatAmount);
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {

	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		this.taxAgency.updateTAXAgencyAccount(s, transaction, this.vatAmount);
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}

		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	private double getCeilValueofTAX() {
		return this.lineTotal * this.rate / 100;
	}

	/**
	 * @return the taxDue
	 */
	public double getTaxDue() {
		return taxDue;
	}

	/**
	 * @param taxDue
	 *            the taxDue to set
	 */
	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
