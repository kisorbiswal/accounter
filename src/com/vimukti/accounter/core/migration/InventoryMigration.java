package com.vimukti.accounter.core.migration;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.utils.HibernateUtil;

public class InventoryMigration {

	Logger log = Logger.getLogger(InventoryMigration.class);

	public void start() {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("get.InventoryItem.donnot.have.assetAccount");
		List<Item> list = query.list();
		for (Item inventoryItem : list) {
			Account assetsAccount = inventoryItem.getAssestsAccount();
			if (assetsAccount == null) {
				createAssetsAccount(session, inventoryItem);
				continue;
			}

			if (assetsAccount.getName().equals(
					AccounterServerConstants.ASSETS_INVENTORY)
					&& assetsAccount.getType() == Account.TYPE_OTHER_CURRENT_ASSET) {
				assetsAccount.setType(Account.TYPE_INVENTORY_ASSET);
				session.save(assetsAccount);
				continue;
			}

			List<Account> assetsAccounts = session
					.getNamedQuery("get.AssetsAccountOfCompany")
					.setEntity("company", inventoryItem.getCompany()).list();

			if (assetsAccounts == null || assetsAccounts.isEmpty()) {
				createAssetsAccount(session, inventoryItem);
			} else {
				inventoryItem.setAssestsAccount(assetsAccounts.get(0));
			}

		}

	}

	private void createAssetsAccount(Session session, Item inventoryItem) {
		AccounterThreadLocal.set(inventoryItem.getCreatedBy());
		Company company = inventoryItem.getCompany();

		String nextNum = getNextAccountNumber(company.getId(),
				Account.SUBBASETYPE_CURRENT_ASSET);
		log.info("Creating Account with number " + nextNum + " for Item '"
				+ inventoryItem.getName() + "' of Company '"
				+ company.getTradingName() + "'.");
		Account account = new Account(Account.TYPE_INVENTORY_ASSET, nextNum,
				AccounterServerConstants.ASSETS_INVENTORY,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		account.setCompany(company);
		session.save(account);
		inventoryItem.setAssestsAccount(account);
		session.saveOrUpdate(inventoryItem);
	}

	private String getNextAccountNumber(long companyId, int subbaseType) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			Query query = session.getNamedQuery("getNextAccountNumber")
					.setParameter("companyId", companyId)
					.setParameter("subbaseType", subbaseType);
			String maxNumber = (String) query.uniqueResult();
			return NumberUtils.getStringwithIncreamentedDigit(maxNumber);
		} finally {
			session.setFlushMode(flushMode);
		}
	}
}
