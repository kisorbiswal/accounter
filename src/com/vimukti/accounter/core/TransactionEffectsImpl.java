package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionEffectsImpl implements ITransactionEffects {

	/** New Account Transaction */
	List<AccountTransaction> newATs = new ArrayList<AccountTransaction>();

	/** New Payee Updates */
	List<PayeeUpdate> newPUs = new ArrayList<PayeeUpdate>();

	List<ItemUpdate> newIUs = new ArrayList<ItemUpdate>();

	/** New TAX Rate Calculations */
	List<TAXRateCalculation> newTRCs = new ArrayList<TAXRateCalculation>();

	private Transaction transaction;

	public TransactionEffectsImpl(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public void add(Account account, Double amount) {
		add(account, amount, transaction.getCurrencyFactor());
	}

	@Override
	public void add(Account account, Double amount, double currencyFactor) {
		if (account == null || amount == null
				|| DecimalUtil.isEquals(amount, 0.00D)) {
			return;
		}
		double atAamount = (account.isIncrease() ? 1 : -1) * amount
				* currencyFactor;
		addAT(new AccountTransaction(account, transaction, atAamount));
	}

	@Override
	public void add(Payee payee, Double amount) {
		if (payee == null || amount == null
				|| DecimalUtil.isEquals(amount, 0.00D)) {
			return;
		}
		addPU(new PayeeUpdate(payee, transaction, amount));
		Account account = payee.getAccount();
		add(account, amount);
	}

	@Override
	public void add(Item item, Quantity quantity, Double unitPrice,
			Warehouse wareHouse) {
		addIU(new ItemUpdate(transaction, item, quantity, unitPrice, wareHouse));
	}

	@Override
	public void add(TAXItemGroup taxItemGroup, Double amount) {
		if (taxItemGroup == null || amount == null
				|| DecimalUtil.isEquals(amount, 0.00D)) {
			return;
		}
		List<TAXItem> taxitems = new ArrayList<TAXItem>();
		boolean isTAXGroup;
		if (taxItemGroup instanceof TAXItem) {
			taxitems.add((TAXItem) taxItemGroup);
			isTAXGroup = false;
		} else {
			taxitems.addAll(((TAXGroup) taxItemGroup).getTAXItems());
			isTAXGroup = true;
		}
		for (TAXItem item : taxitems) {
			TAXRateCalculation trc = new TAXRateCalculation(item, transaction,
					amount);
			trc.setVATGroupEntry(isTAXGroup);

			addPU(new PayeeUpdate(trc.getTaxAgency(), transaction,
					trc.getTAXAmount()));

			Account account = null;
			if (transaction.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {
				account = trc.salesLiabilityAccount;
			} else if (transaction.getTransactionCategory() == Transaction.CATEGORY_VENDOR) {
				account = trc.purchaseLiabilityAccount;
			}
			add(account, trc.getTAXAmount() / transaction.getCurrencyFactor());
			addTRC(trc);

		}
	}

	private void addAT(AccountTransaction at) {
		int index = newATs.indexOf(at);
		if (index >= 0) {
			newATs.get(index).add(at);
		} else {
			newATs.add(at);
		}
	}

	private void addPU(PayeeUpdate payeeUpdate) {
		int index = newPUs.indexOf(payeeUpdate);
		if (index >= 0) {
			newPUs.get(index).add(payeeUpdate);
		} else {
			newPUs.add(payeeUpdate);
		}
	}

	private void addIU(ItemUpdate itemUpdate) {
		int index = newIUs.indexOf(itemUpdate);
		if (index >= 0) {
			newIUs.get(index).add(itemUpdate);
		} else {
			newIUs.add(itemUpdate);
		}
	}

	private void addTRC(TAXRateCalculation trc) {
		int index = newTRCs.indexOf(trc);
		if (index >= 0) {
			newTRCs.get(index).add(trc);
		} else {
			newTRCs.add(trc);
		}
	}

	public void saveOrUpdate() {
		Session session = HibernateUtil.getCurrentSession();
		mergeAccountTransactions(session);
		mergePayeeUpdates(session);
		mergeTAXRateCalculation(session);
		mergeItemUpdates(session);

		for (TAXRateCalculation trc : newTRCs) {
			session.save(trc);
		}

	}

	private void mergeAccountTransactions(Session session) {
		Set<AccountTransaction> oldAT = new HashSet<AccountTransaction>();
		for (AccountTransaction at : transaction
				.getAccountTransactionEntriesList()) {
			if (at.isUpdateAccount()) {
				oldAT.add(at);
			}
		}
		Collection<?> intersection = CollectionUtils
				.intersection(newATs, oldAT);
		newATs.removeAll(intersection);
		oldAT.removeAll(intersection);
		transaction.getAccountTransactionEntriesList().removeAll(oldAT);
		transaction.getAccountTransactionEntriesList().addAll(newATs);

	}

	private void mergePayeeUpdates(Session session) {
		Set<PayeeUpdate> oldPUs = new HashSet<PayeeUpdate>(
				transaction.getPayeeUpdates());
		Collection<?> intersection = CollectionUtils.intersection(newPUs,
				oldPUs);
		newPUs.removeAll(intersection);
		oldPUs.removeAll(intersection);

		transaction.getPayeeUpdates().removeAll(oldPUs);
		transaction.getPayeeUpdates().addAll(newPUs);
	}

	private void mergeItemUpdates(Session session) {
		Set<ItemUpdate> oldIUs = new HashSet<ItemUpdate>(
				transaction.getItemUpdates());
		Collection<?> intersection = CollectionUtils.intersection(newIUs,
				oldIUs);
		newIUs.removeAll(intersection);
		oldIUs.removeAll(intersection);

		transaction.getItemUpdates().removeAll(oldIUs);
		transaction.getItemUpdates().addAll(newIUs);
	}

	private void mergeTAXRateCalculation(Session session) {
		Set<TAXRateCalculation> oldTRCs = new HashSet<TAXRateCalculation>(
				transaction.getTaxRateCalculationEntriesList());
		Collection<?> intersection = CollectionUtils.intersection(newTRCs,
				oldTRCs);
		newTRCs.removeAll(intersection);
		oldTRCs.removeAll(intersection);

		transaction.getTaxRateCalculationEntriesList().removeAll(oldTRCs);
		transaction.getTaxRateCalculationEntriesList().addAll(newTRCs);
	}

	public void doVoid() {
		Iterator<AccountTransaction> iterator = transaction
				.getAccountTransactionEntriesList().iterator();
		while (iterator.hasNext()) {
			AccountTransaction next = iterator.next();
			if (next.isUpdateAccount()) {
				iterator.remove();
			}
		}
		transaction.getPayeeUpdates().clear();
		transaction.getTaxRateCalculationEntriesList().clear();

	}

	public boolean isEmpty() {
		if (!newATs.isEmpty() || !newPUs.isEmpty() || !newTRCs.isEmpty()
				|| !newIUs.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * @return the newATs
	 */
	public List<AccountTransaction> getNewATs() {
		return newATs;
	}

	/**
	 * @param newATs
	 *            the newATs to set
	 */
	public void setNewATs(List<AccountTransaction> newATs) {
		this.newATs = newATs;
	}

}
