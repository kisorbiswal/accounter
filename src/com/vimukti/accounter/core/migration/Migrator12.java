package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.web.server.InventoryUtils;

public class Migrator12 extends AbstractMigrator {

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator12.");
		Query query = getSession().getNamedQuery("get.all.Items").setEntity(
				"company", company);
		List<Item> items = query.list();

		InventoryUtils.remapSalesPurchases(items);

		log.info("Finished Migrator12.");
	}

	@Override
	public int getVersion() {
		return 12;
	}
}
