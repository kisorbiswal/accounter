package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AccountNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class CustomerAccountTransactionTable extends
		CustomerTransactionTable {

	public CustomerAccountTransactionTable() {
		super();
		addEmptyRecords();
	}

	public CustomerAccountTransactionTable(boolean needDiscount) {
		super(needDiscount);
		addEmptyRecords();
	}

	/**
	 * This method will add 4 empty records to the table.
	 */
	private void addEmptyRecords() {
		for (int i = 0; i < 3; i++) {
			ClientTransactionItem item = new ClientTransactionItem();
			item.setType(ClientTransactionItem.TYPE_ACCOUNT);
			add(item);
		}
	}

	@Override
	protected void initColumns() {
		this.addColumn(new AccountNameColumn() {

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_EXPENSE
								&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
								&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY) {
							return true;
						}
						return false;
					}
				};
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientAccount newValue) {
				super.setValue(row, newValue);
				// applyPriceLevel(row);
			}

			@Override
			public List<Integer> getCanAddedAccountTypes() {
				return Arrays.asList(ClientAccount.TYPE_INCOME,
						ClientAccount.TYPE_FIXED_ASSET);
			}
		});

		this.addColumn(new DescriptionEditColumn());

		// this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn() {
			@Override
			protected String getColumnName() {
				return Accounter.constants().amount();
			}
		});

		if (needDiscount) {
			this.addColumn(new TransactionDiscountColumn());
		}

		this.addColumn(new TransactionTotalColumn());

		// if (getCompany().getPreferences().isChargeSalesTax()) {

		if (getCompany().getPreferences().isRegisteredForVAT()) {

			this.addColumn(new TransactionVatCodeColumn());

			this.addColumn(new TransactionVatColumn());
		}

		// }

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

}
