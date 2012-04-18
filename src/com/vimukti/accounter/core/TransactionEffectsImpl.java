package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionEffectsImpl implements ITransactionEffects {

	Logger log = Logger.getLogger(TransactionEffectsImpl.class);

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
		newATs.add(at);
	}

	private void addPU(PayeeUpdate payeeUpdate) {
		newPUs.add(payeeUpdate);
	}

	private void addIU(ItemUpdate itemUpdate) {
		newIUs.add(itemUpdate);
	}

	private void addTRC(TAXRateCalculation trc) {
		newTRCs.add(trc);
	}

	public void saveOrUpdate() throws AccounterException {
		checkTrailBalance();
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

		findOutIntersectionAT(oldAT, newATs);

		transaction.getAccountTransactionEntriesList().removeAll(oldAT);
		transaction.getAccountTransactionEntriesList().addAll(newATs);

	}

	private void findOutIntersectionAT(Set<AccountTransaction> oldATs,
			List<AccountTransaction> newATs) {
		Iterator<AccountTransaction> oldAtsIterator = oldATs.iterator();
		while (oldAtsIterator.hasNext()) {
			AccountTransaction oldAT = oldAtsIterator.next();
			Iterator<AccountTransaction> newATsIterator = newATs.iterator();
			while (newATsIterator.hasNext()) {
				AccountTransaction newAT = newATsIterator.next();
				if (oldAT.getTransaction() == newAT.getTransaction()
						&& oldAT.getAccount() == newAT.getAccount()

						&& DecimalUtil.isEquals(oldAT.getAmount(),
								newAT.getAmount())) {
					newATsIterator.remove();
					oldAtsIterator.remove();
					break;
				}
			}
		}
	}

	private void mergePayeeUpdates(Session session) {
		Set<PayeeUpdate> oldPUs = new HashSet<PayeeUpdate>(
				transaction.getPayeeUpdates());
		findOutIntersectionPU(oldPUs, newPUs);
		transaction.getPayeeUpdates().removeAll(oldPUs);
		transaction.getPayeeUpdates().addAll(newPUs);
	}

	private void findOutIntersectionPU(Set<PayeeUpdate> oldPUs,
			List<PayeeUpdate> newPUs) {
		Iterator<PayeeUpdate> oldPUsIterator = oldPUs.iterator();
		while (oldPUsIterator.hasNext()) {
			PayeeUpdate oldPU = oldPUsIterator.next();
			Iterator<PayeeUpdate> newPUIterator = newPUs.iterator();
			while (newPUIterator.hasNext()) {
				PayeeUpdate newPU = newPUIterator.next();
				if (oldPU.getTransaction() == newPU.getTransaction()
						&& oldPU.getPayee() == newPU.getPayee()
						&& DecimalUtil.isEquals(oldPU.getAmount(),
								newPU.getAmount())) {
					newPUIterator.remove();
					oldPUsIterator.remove();
					break;
				}
			}
		}
	}

	private void mergeItemUpdates(Session session) {
		Set<ItemUpdate> oldIUs = new HashSet<ItemUpdate>(
				transaction.getItemUpdates());
		findOutIntersectionIU(oldIUs, newIUs);
		transaction.getItemUpdates().removeAll(oldIUs);
		transaction.getItemUpdates().addAll(newIUs);
	}

	private void findOutIntersectionIU(Set<ItemUpdate> oldIUs,
			List<ItemUpdate> newIUs) {
		Iterator<ItemUpdate> oldPUsIterator = oldIUs.iterator();
		while (oldPUsIterator.hasNext()) {
			ItemUpdate oldIU = oldPUsIterator.next();
			Iterator<ItemUpdate> newPUIterator = newIUs.iterator();
			while (newPUIterator.hasNext()) {
				ItemUpdate newIU = newPUIterator.next();
				if (oldIU.getTransaction() == newIU.getTransaction()
						&& oldIU.getItem() == newIU.getItem()
						&& oldIU.getWarehouse() == newIU.getWarehouse()
						&& oldIU.getQuantity().equals(newIU.getQuantity())
						&& DecimalUtil.isEquals(oldIU.getUnitPrice(),
								newIU.getUnitPrice())) {
					newPUIterator.remove();
					oldPUsIterator.remove();
					break;
				}
			}
		}
	}

	private void mergeTAXRateCalculation(Session session) {
		Set<TAXRateCalculation> oldTRCs = new HashSet<TAXRateCalculation>(
				transaction.getTaxRateCalculationEntriesList());
		findOutIntersectionTRC(oldTRCs, newTRCs);
		transaction.getTaxRateCalculationEntriesList().removeAll(oldTRCs);
		transaction.getTaxRateCalculationEntriesList().addAll(newTRCs);
	}

	private void findOutIntersectionTRC(Set<TAXRateCalculation> oldTRCs,
			List<TAXRateCalculation> newTRCs) {
		Iterator<TAXRateCalculation> oldPUsIterator = oldTRCs.iterator();
		while (oldPUsIterator.hasNext()) {
			TAXRateCalculation oldTRC = oldPUsIterator.next();
			Iterator<TAXRateCalculation> newPUIterator = newTRCs.iterator();
			while (newPUIterator.hasNext()) {
				TAXRateCalculation newTRC = newPUIterator.next();
				if (oldTRC.getTransaction() == newTRC.getTransaction()
						&& oldTRC.getTaxItem() == newTRC.getTaxItem()
						&& oldTRC.getTaxAgency() == newTRC.getTaxAgency()
						&& oldTRC.getSalesLiabilityAccount() == newTRC
								.getSalesLiabilityAccount()
						&& oldTRC.getPurchaseLiabilityAccount() == newTRC
								.getPurchaseLiabilityAccount()
						&& DecimalUtil.isEquals(oldTRC.getRate(),
								newTRC.getRate())
						&& DecimalUtil.isEquals(oldTRC.getLineTotal(),
								newTRC.getLineTotal())
						&& DecimalUtil.isEquals(oldTRC.getTAXAmount(),
								newTRC.getTAXAmount())
						&& oldTRC.isVATGroupEntry() == newTRC.isVATGroupEntry()) {
					newPUIterator.remove();
					oldPUsIterator.remove();
					break;
				}
			}
		}
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

	public void checkTrailBalance() throws AccounterException {
		double total = 0.00D;
		for (AccountTransaction at : newATs) {
			total += (at.getAccount().isIncrease() ? -1 : 1) * at.getAmount();
		}
		if (!DecimalUtil.isEquals(total, 0.00D)) {
			log.info("New ATs : " + newATs);
			log.info("Old ATs :"
					+ transaction.getAccountTransactionEntriesList());
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT);
		}
	}

}
