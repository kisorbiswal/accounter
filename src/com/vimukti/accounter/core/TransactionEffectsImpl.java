package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionEffectsImpl implements ITransactionEffects {

	/** New Account Transaction */
	Set<AccountTransaction> newATs = new HashSet<AccountTransaction>();

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
		if (account == null || amount == null
				|| DecimalUtil.isEquals(amount, 0.00D)) {
			return;
		}
		double atAamount = (account.isIncrease() ? 1 : -1) * amount
				* transaction.getCurrencyFactor();
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
		addIU(new ItemUpdate(item, quantity, unitPrice, wareHouse));
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

		}
	}

	private void addAT(AccountTransaction at) {
		newATs.add(at);
	}

	private void addPU(PayeeUpdate payeeUpdate) {
		newPUs.add(payeeUpdate);
	}

	private void addIU(ItemUpdate itemUpdate) {
		newIUs.add(itemUpdate);
	}

	public void saveOrUpdate() {
		Session session = HibernateUtil.getCurrentSession();
		mergeAccountTransactions(session);
		mergePayeeUpdates(session);
		mergeTAXRateCalculation(session);

		for (TAXRateCalculation trc : newTRCs) {
			session.save(trc);
		}

	}

	private void mergeAccountTransactions(Session session) {
		Set<AccountTransaction> oldAT = new HashSet<AccountTransaction>(
				transaction.getAccountTransactionEntriesList());
		Collection<?> intersection = CollectionUtils
				.intersection(newATs, oldAT);
		newATs.removeAll(intersection);
		oldAT.removeAll(intersection);
		for (AccountTransaction at : newATs) {
			session.saveOrUpdate(at);
		}
		for (AccountTransaction at : oldAT) {
			transaction.getAccountTransactionEntriesList().remove(at);
			session.delete(at);
		}

	}

	private void mergePayeeUpdates(Session session) {
		Set<PayeeUpdate> oldPUs = new HashSet<PayeeUpdate>(
				transaction.getPayeeUpdates());
		Collection<?> intersection = CollectionUtils.intersection(newPUs,
				oldPUs);
		newPUs.removeAll(intersection);
		oldPUs.removeAll(intersection);
		for (PayeeUpdate pu : newPUs) {
			session.saveOrUpdate(pu);
		}
		for (PayeeUpdate pu : oldPUs) {
			transaction.getPayeeUpdates().remove(pu);
			session.delete(pu);
		}
	}

	private void mergeTAXRateCalculation(Session session) {
		Set<TAXRateCalculation> oldTRCs = new HashSet<TAXRateCalculation>(
				transaction.getTaxRateCalculationEntriesList());
		Collection<?> intersection = CollectionUtils.intersection(newTRCs,
				oldTRCs);
		newTRCs.removeAll(intersection);
		oldTRCs.removeAll(intersection);
		for (TAXRateCalculation trc : newTRCs) {
			session.saveOrUpdate(trc);
		}
		for (TAXRateCalculation trc : oldTRCs) {
			transaction.getTaxRateCalculationEntriesList().remove(trc);
			session.delete(trc);
		}
	}

	public void doVoid() {
		Session session = HibernateUtil.getCurrentSession();
		for (AccountTransaction at : transaction
				.getAccountTransactionEntriesList()) {
			session.delete(at);
		}
		for (PayeeUpdate pu : transaction.getPayeeUpdates()) {
			session.delete(pu);
		}

		for (TAXRateCalculation trc : transaction
				.getTaxRateCalculationEntriesList()) {
			session.delete(trc);
		}

	}

}
