package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class MakeDeposit extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Account depositTo;

	List<TransactionDepositItem> transactionDepositItems;

	private Set<Estimate> estimates = new HashSet<Estimate>();

	public MakeDeposit() {
		super();
		setType(Transaction.TYPE_MAKE_DEPOSIT);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		if (this.depositTo != null) {
			w.put(messages.depositTo(), this.depositTo.name);
		}
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		return null;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_BANKING;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_MAKE_DEPOSIT;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	protected void updatePayee(boolean onCreate) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (isDraftOrTemplate()) {
			return false;
		}

		if (getCompany().getPreferences()
				.isProductandSerivesTrackingByCustomerEnabled()
				&& getCompany().getPreferences()
						.isBillableExpsesEnbldForProductandServices()) {
			createAndSaveEstimates(this.transactionDepositItems, session);
		}

		depositTo.updateCurrentBalance(this, -this.total, this.currencyFactor);
		session.save(depositTo);

		return false;
	}

	private void createAndSaveEstimates(
			List<TransactionDepositItem> transactionDepositItems,
			Session session) {
		this.getEstimates().clear();

		Set<Estimate> estimates = new HashSet<Estimate>();
		for (TransactionDepositItem transactionItem : transactionDepositItems) {
			if (transactionItem.isBillable()
					&& transactionItem.getCustomer() != null) {
				TransactionItem newTransactionItem = new TransactionItem();
				newTransactionItem.setId(0);
				newTransactionItem.setOnSaveProccessed(false);
				newTransactionItem.setLineTotal(transactionItem.getTotal()
						* getCurrencyFactor());
				newTransactionItem.setUnitPrice(transactionItem.getTotal()
						* getCurrencyFactor());
				newTransactionItem.setBillable(transactionItem.isBillable());
				newTransactionItem.setAccounterClass(transactionItem
						.getAccounterClass());
				newTransactionItem.setTransaction(transactionItem
						.getTransaction());
				newTransactionItem.setDescription(transactionItem
						.getDescription());
				newTransactionItem.setAccount(transactionItem.getAccount());
				newTransactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				newTransactionItem.setCustomer(transactionItem.getCustomer());

				Estimate estimate = getCustomerEstimate(estimates,
						newTransactionItem.getCustomer().getID());
				if (estimate == null) {
					estimate = new Estimate();
					estimate.setCompany(getCompany());
					estimate.setCustomer(newTransactionItem.getCustomer());
					estimate.setTransactionItems(new ArrayList<TransactionItem>());
					estimate.setEstimateType(Estimate.DEPOSIT_EXPENSES);
					estimate.setType(Transaction.TYPE_ESTIMATE);
					estimate.setDate(new FinanceDate());
					estimate.setExpirationDate(new FinanceDate());
					estimate.setDeliveryDate(new FinanceDate());
					estimate.setNumber(NumberUtils.getNextTransactionNumber(
							Transaction.TYPE_ESTIMATE, getCompany()));
				}
				List<TransactionItem> transactionItems2 = estimate
						.getTransactionItems();

				transactionItems2.add(newTransactionItem);
				estimate.setTransactionItems(transactionItems2);
				estimates.add(estimate);
			}
		}

		for (Estimate estimate : estimates) {
			session.save(estimate);
		}

		this.setEstimates(estimates);
	}

	private Estimate getCustomerEstimate(Set<Estimate> estimates, long customer) {
		for (Estimate clientEstimate : estimates) {
			if (clientEstimate.getCustomer().getID() == customer) {
				return clientEstimate;
			}
		}
		return null;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		return super.onUpdate(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (!UserUtils.canDoThis(EnterBill.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		for (Estimate estimate : this.getEstimates()) {
			if (estimate.getUsedInvoice() != null) {
				throw new AccounterException(AccounterException.USED_IN_INVOICE);
			}
		}
		return super.canEdit(clientObject);
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		Session session = HibernateUtil.getCurrentSession();
		MakeDeposit deposit = (MakeDeposit) clonedObject;

		if (isDraftOrTemplate()) {
			super.onEdit(deposit);
			return;
		}
		if (this.isVoid() && !deposit.isVoid()) {
			this.doVoidEffect(session);

		} else {
			if (this.depositTo.getID() != deposit.depositTo.getID()
					|| !DecimalUtil.isEquals(this.total, deposit.total)
					|| isCurrencyFactorChanged()) {
				Account depositInAccount = (Account) session.get(Account.class,
						deposit.depositTo.getID());
				depositInAccount.updateCurrentBalance(this, deposit.total,
						deposit.currencyFactor);
				depositInAccount.onUpdate(session);
				session.saveOrUpdate(depositInAccount);

				this.depositTo.updateCurrentBalance(this, -this.total,
						this.currencyFactor);
				this.depositTo.onUpdate(session);
				session.saveOrUpdate(this.depositTo);
			}
			for (Estimate estimate : this.estimates) {
				session.delete(estimate);
			}
			this.estimates.clear();
			this.createAndSaveEstimates(this.transactionDepositItems, session);
		}
		super.onEdit(deposit);
	}

	private void doVoidEffect(Session session) {
		depositTo.updateCurrentBalance(this, this.total, this.currencyFactor);
		session.save(depositTo);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session);
		}
		for (Estimate estimate : this.getEstimates()) {
			session.delete(estimate);
		}
		this.estimates.clear();
		return super.onDelete(session);
	}

	public List<TransactionDepositItem> getTransactionDepositItems() {
		return transactionDepositItems;
	}

	public void setTransactionDepositItems(
			List<TransactionDepositItem> transactionDepositItems) {
		this.transactionDepositItems = transactionDepositItems;
	}

	public Set<Estimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(Set<Estimate> estimates) {
		this.estimates = estimates;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		// TODO Auto-generated method stub
		super.onLoad(session, arg1);
	}
}
