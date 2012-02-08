package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.utils.HibernateUtil;

public class InventoryMigration {

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

			int nextNum = getNextAccountNumber(company.getId(),
					Account.SUBBASETYPE_CURRENT_ASSET);
			Account account = new Account(Account.TYPE_INVENTORY_ASSET,
					nextNum, "Inventory Assets",
					Account.CASH_FLOW_CATEGORY_OPERATING);
			account.setCompany(company);
			session.save(account);
			inventoryItem.setAssestsAccount(account);
			session.saveOrUpdate(inventoryItem);

		}

	}

	private int getNextAccountNumber(long companyId, int subbaseType) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			Query query = session.getNamedQuery("getNextAccountNumber")
					.setParameter("companyId", companyId)
					.setParameter("subbaseType", subbaseType);
			return (Integer) query.uniqueResult();
		} finally {
			session.setFlushMode(flushMode);
		}
	}

}
