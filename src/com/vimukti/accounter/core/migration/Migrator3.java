package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.web.server.InventoryUtils;

public class Migrator3 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {
		Query query = getSession().getNamedQuery("get.All.InventoryItem")
				.setParameter("companyId", company.getId());
		List<Item> items = query.list();
		for (Item item : items) {
			int type = item.getType();
			if (type != Item.TYPE_INVENTORY_PART
					&& type != Item.TYPE_INVENTORY_ASSEMBLY) {
				continue;
			}

			query = getSession()
					.getNamedQuery("get.AccountTransaction.Of.Item")
					.setParameter("itemId", item.getID());
			List<AccountTransaction> list = query.list();
			for (AccountTransaction at : list) {
				Account account = at.getAccount();
				account.updateCurrentBalance(at.getTransaction(), (account
						.isIncrease() ? -at.getAmount() : at.getAmount()), 1);
				getSession().saveOrUpdate(account);
			}
		}

		// Deleting Purchases
		getSession().getNamedQuery("delete.InventoryPurchases.Of.Company")
				.setParameter("companyId", company.getId()).executeUpdate();

		InventoryUtils.remapSalesPurchases(items);
	}

	@Override
	public int getVersion() {
		return 3;
	}

}
