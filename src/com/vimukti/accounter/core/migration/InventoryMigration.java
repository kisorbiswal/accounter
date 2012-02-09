package com.vimukti.accounter.core.migration;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
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
			Account assestsAccount = inventoryItem.getAssestsAccount();
			if (assestsAccount != null) {
				continue;
			}

			AccounterThreadLocal.set(inventoryItem.getCreatedBy());
			Company company = inventoryItem.getCompany();

			String nextNum = getNextAccountNumber(company.getId(),
					Account.SUBBASETYPE_CURRENT_ASSET);
			log.info("Creating Account with number " + nextNum + " for Item '"
					+ inventoryItem.getName() + "' of Company '"
					+ company.getTradingName() + "'.");
			Account account = new Account(Account.TYPE_INVENTORY_ASSET,
					nextNum, "Inventory Assets",
					Account.CASH_FLOW_CATEGORY_OPERATING);
			account.setCompany(company);
			session.save(account);
			inventoryItem.setAssestsAccount(account);
			session.saveOrUpdate(inventoryItem);

		}

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
