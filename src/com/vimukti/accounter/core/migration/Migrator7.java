package com.vimukti.accounter.core.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.InventoryPurchase;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionEffectsImpl;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class Migrator7 extends AbstractMigrator {
	Logger log = Logger.getLogger(Migrator6.class);

	@Override
	public int getVersion() {
		return 7;
	}

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator7");
		Query query = getSession().getNamedQuery("getAllTransactionOfCompany")
				.setEntity("company", company);
		List<Transaction> list = query.list();
		for (Transaction transaction : list) {
			migrate(transaction);
		}
		log.info("Finished Migrator7");
	}

	/**
	 * Migrates the Transaction
	 * 
	 * @param transaction
	 */
	private void migrate(Transaction transaction) {
		log.info("***Migrating Transaction ID: " + transaction.getID() + " "
				+ transaction);
		Set<AccountTransaction> oldATs = new HashSet<AccountTransaction>(
				transaction.getAccountTransactionEntriesList());

		for (TransactionItem tItem : transaction.getTransactionItems()) {
			if (tItem.getType() == TransactionItem.TYPE_ITEM
					&& tItem.getItem() != null && tItem.getItem().isInventory()) {
				matchATandIP(oldATs, tItem.getPurchases(), tItem.getItem()
						.getAssestsAccount());
			}
		}

		// Compressing the Old AccountTransactions
		oldATs = compress(oldATs);

		TransactionEffectsImpl tEffects = new TransactionEffectsImpl(
				transaction);
		transaction.getEffects(tEffects);
		List<AccountTransaction> newATs = tEffects.getNewATs();

		// Finding Intersection
		Collection<?> intersection = CollectionUtils.intersection(oldATs,
				newATs);

		oldATs.removeAll(intersection);
		newATs.removeAll(intersection);
		if (!oldATs.isEmpty() || !newATs.isEmpty()) {
			log.warn("Removing ATs : " + oldATs);
			// transaction.getAccountTransactionEntriesList().removeAll(oldATs);
			log.warn("Creating ATs : " + newATs);
			// transaction.getAccountTransactionEntriesList().addAll(newATs);
		}
	}

	/**
	 * It will check the Suitable AccountTrannsaction for this Purchases and
	 * removes it from List
	 * 
	 * @param oldATs
	 * @param purchases
	 * @param assetAccount
	 * @return
	 */
	private double matchATandIP(Set<AccountTransaction> oldATs,
			Set<InventoryPurchase> purchases, Account assetAccount) {
		double assetChange = 0.00D;
		for (InventoryPurchase purchase : purchases) {
			double purchaseValue = purchase.getQuantity().calculatePrice(
					purchase.getCost());
			assetChange += purchaseValue;
			Account effectingAccount = purchase.getEffectingAccount();
			purchaseValue = (effectingAccount.isIncrease() ? 1 : -1)
					* purchaseValue;
			checkAndRemoveAT(oldATs, purchase.getEffectingAccount(),
					purchaseValue);
		}
		checkAndRemoveAT(oldATs, assetAccount, assetChange);
		return assetChange;
	}

	/**
	 * It will check the AccountTransaction with same account and amount. If
	 * exists removes that from list and Make the VAR 'updateAccount' false;
	 * 
	 * @param ats
	 * @param account
	 * @param amount
	 * @return
	 */
	private AccountTransaction checkAndRemoveAT(Set<AccountTransaction> ats,
			Account account, double amount) {
		Iterator<AccountTransaction> iterator = ats.iterator();
		while (iterator.hasNext()) {
			AccountTransaction next = iterator.next();
			if (next.getAccount().getID() == account.getID()
					&& DecimalUtil.isEquals(next.getAmount(), amount)) {
				log.info("Found the AT match for Purchase : " + next);
				// next.setUpdateAccount(false);
				// getSession().saveOrUpdate(next);
				// iterator.remove();
				return next;
			}
		}
		return null;
	}

	/**
	 * It will make the similar AccountTransactions that has same Transaction
	 * and Account into one
	 * 
	 * @param oldATs
	 * @return
	 */
	private Set<AccountTransaction> compress(Set<AccountTransaction> oldATs) {
		List<AccountTransaction> compressed = new ArrayList<AccountTransaction>();
		for (AccountTransaction at : oldATs) {
			int index = compressed.indexOf(at);
			if (index >= 0) {
				compressed.get(index).add(at);
			} else {
				compressed.add(at);
			}
		}
		return new HashSet<AccountTransaction>(compressed);
	}

}
