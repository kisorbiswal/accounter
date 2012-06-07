package com.vimukti.accounter.core.migration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.InventoryPurchase;
import com.vimukti.accounter.core.ItemUpdate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionEffectsImpl;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class Migrator11 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {

		log.info("Started Migrator11.");
		Session session = HibernateUtil.getCurrentSession();
		List<Long> transactionIds = getAllTransactions(company);
		for (Long id : transactionIds) {
			try {
				Transaction transaction = (Transaction) session.load(
						Transaction.class, id);
				transaction = HibernateUtil.initializeAndUnproxy(transaction);
				migrate(transaction);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		log.info("Finished Migrator11.");
	}

	private void migrate(Transaction transaction) throws Exception {
		log.info("***Migrating Transaction ID: " + transaction.getID()
				+ " Number: " + transaction.getNumber() + " " + transaction);

		Set<ItemUpdate> oldItemUpdates = new HashSet<ItemUpdate>(
				transaction.getItemUpdates());
		Set<AccountTransaction> oldATs = new HashSet<AccountTransaction>(
				transaction.getAccountTransactionEntriesList());

		Set<AccountTransaction> missingAts = new HashSet<AccountTransaction>();
		for (TransactionItem tItem : transaction.getTransactionItems()) {
			if (tItem.getType() == TransactionItem.TYPE_ITEM
					&& tItem.getItem() != null && tItem.getItem().isInventory()) {
				matchATandIP(oldATs, tItem.getPurchases(), tItem.getItem()
						.getAssestsAccount(), missingAts);
			}
		}
		TransactionEffectsImpl tEffects = new TransactionEffectsImpl(
				transaction);
		transaction.getEffects(tEffects);
		List<AccountTransaction> newATs = tEffects.getNewATs();
		tEffects.checkTrailBalance();
		if (!missingAts.isEmpty()) {
			newATs.addAll(missingAts);
		}

		// Finding Intersection
		findOutIntersectionAT(oldATs, newATs);

		if (!oldATs.isEmpty() || !newATs.isEmpty()) {
			log.warn("Removing ATs : " + oldATs);
			transaction.getAccountTransactionEntriesList().removeAll(oldATs);

			log.warn("Creating ATs : " + newATs);
			transaction.getAccountTransactionEntriesList().addAll(newATs);
		}

		List<ItemUpdate> newIUs = tEffects.getNewIUs();

		findOutIntersectionIUs(oldItemUpdates, newIUs);
		if (!oldItemUpdates.isEmpty() || !newIUs.isEmpty()) {
			log.warn("Removing IUs : " + oldItemUpdates);
			transaction.getItemUpdates().removeAll(oldItemUpdates);

			log.warn("Creating IUs : " + newIUs);
			transaction.getItemUpdates().addAll(newIUs);
		}
		getSession().save(transaction);
	}

	private void findOutIntersectionIUs(Set<ItemUpdate> oldIUs,
			List<ItemUpdate> newIUs) {
		Iterator<ItemUpdate> oldIUsIterator = oldIUs.iterator();
		while (oldIUsIterator.hasNext()) {
			ItemUpdate oldAT = oldIUsIterator.next();
			Iterator<ItemUpdate> newIUsIterator = newIUs.iterator();
			while (newIUsIterator.hasNext()) {
				ItemUpdate newAT = newIUsIterator.next();
				if (oldAT.getWarehouse().getID() == newAT.getWarehouse()
						.getID()
						&& oldAT.getItem().getID() == newAT.getItem().getID()
						&& DecimalUtil.isEquals(oldAT.getUnitPrice(),
								newAT.getUnitPrice())
						&& oldAT.getQuantity().equals(newAT.getQuantity())) {
					newIUsIterator.remove();
					oldIUsIterator.remove();
					break;
				}
			}
		}
	}

	/**
	 * It will check the Suitable AccountTrannsaction for this Purchases and
	 * removes it from List
	 * 
	 * @param oldATs
	 * @param purchases
	 * @param assetAccount
	 * @param missingAts
	 * @return
	 */
	private double matchATandIP(Set<AccountTransaction> oldATs,
			Set<InventoryPurchase> purchases, Account assetAccount,
			Set<AccountTransaction> missingAts) {
		if (purchases.isEmpty()) {
			return 0.0D;
		}
		double assetChange = 0.00D;
		for (InventoryPurchase purchase : purchases) {
			double purchaseValue = purchase.getQuantity().calculatePrice(
					purchase.getCost());
			assetChange += purchaseValue;
			Account effectingAccount = purchase.getEffectingAccount();
			purchaseValue = (effectingAccount.isIncrease() ? 1 : -1)
					* purchaseValue;
			checkAndRemoveAT(oldATs, purchase.getEffectingAccount(),
					-purchaseValue, missingAts);
		}
		assetChange = (assetAccount.isIncrease() ? 1 : -1) * assetChange;
		checkAndRemoveAT(oldATs, assetAccount, assetChange, missingAts);
		return assetChange;
	}

	/**
	 * It will check the AccountTransaction with same account and amount. If
	 * exists removes that from list and Make the VAR 'updateAccount' false;
	 * 
	 * @param ats
	 * @param account
	 * @param amount
	 * @param missingAts
	 * @return
	 */
	private AccountTransaction checkAndRemoveAT(Set<AccountTransaction> ats,
			Account account, double amount, Set<AccountTransaction> missingAts) {
		Iterator<AccountTransaction> iterator = ats.iterator();
		Transaction tr = null;
		while (iterator.hasNext()) {
			AccountTransaction next = iterator.next();
			tr = next.getTransaction();
			if (next.getAccount().getID() == account.getID()
					&& DecimalUtil.isEquals(next.getAmount(), amount)) {
				log.info("Found the AT match for Purchase : " + next);
				next.setUpdateAccount(false);
				getSession().saveOrUpdate(next);
				iterator.remove();
				return next;
			}
		}
		if (tr != null) {
			missingAts.add(new AccountTransaction(account, tr, amount, false));
		}
		return null;
	}

	private void findOutIntersectionAT(Set<AccountTransaction> oldATs,
			List<AccountTransaction> newATs) {
		Iterator<AccountTransaction> oldAtsIterator = oldATs.iterator();
		while (oldAtsIterator.hasNext()) {
			AccountTransaction oldAT = oldAtsIterator.next();
			Iterator<AccountTransaction> newATsIterator = newATs.iterator();
			while (newATsIterator.hasNext()) {
				AccountTransaction newAT = newATsIterator.next();
				if (oldAT.getTransaction().getID() == newAT.getTransaction()
						.getID()
						&& oldAT.getAccount().getID() == newAT.getAccount()
								.getID()
						&& DecimalUtil.isEquals(oldAT.getAmount(),
								newAT.getAmount())) {
					newATsIterator.remove();
					oldAtsIterator.remove();
					break;
				}
			}
		}
	}

	private List<Long> getAllTransactions(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		List<Long> list = session
				.getNamedQuery("get.all.inventory.items.transactions")
				.setLong("companyId", company.getId()).list();
		return list;
	}

	@Override
	public int getVersion() {
		return 11;
	}

}
