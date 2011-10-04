package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

/**
 * This class is used for every Customer Transactions
 * 
 * @author vimukti04
 * 
 */
public abstract class CustomerTransactionTable extends AbstractTransactionTable {

	public CustomerTransactionTable(boolean needDiscount,
			ICurrencyProvider currencyProvider) {
		super(needDiscount, true, currencyProvider);
	}

}
