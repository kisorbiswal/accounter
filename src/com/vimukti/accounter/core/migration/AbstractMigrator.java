package com.vimukti.accounter.core.migration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.ItemUpdate;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionEffectsImpl;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class AbstractMigrator implements IMigrator {

	protected Logger log = Logger.getLogger(getClass());

	protected String getNextAccountNumber(long companyId, int accountType) {
		return NumberUtils.getNextAccountNumber(companyId, accountType);
	}

	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	protected void migrate(Transaction transaction) throws AccounterException {
		log.info("***Migrating Transaction ID: " + transaction.getID()
				+ " Number: " + transaction.getNumber() + " " + transaction);

		Set<ItemUpdate> oldItemUpdates = new HashSet<ItemUpdate>(
				transaction.getItemUpdates());
		Set<AccountTransaction> oldATs = new HashSet<AccountTransaction>(
				transaction.getAccountTransactionEntriesList());

		Set<AccountTransaction> missingAts = new HashSet<AccountTransaction>();
		// for (TransactionItem tItem : transaction.getTransactionItems()) {
		// if (tItem.getType() == TransactionItem.TYPE_ITEM
		// && tItem.getItem() != null && tItem.getItem().isInventory()) {
		// matchATandIP(oldATs, tItem.getPurchases(), tItem.getItem()
		// .getAssestsAccount(), missingAts);
		// }
		// }
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
					oldAT.setUpdateAccount(true);
					getSession().saveOrUpdate(oldAT);
					break;
				}
			}
		}
	}

}
