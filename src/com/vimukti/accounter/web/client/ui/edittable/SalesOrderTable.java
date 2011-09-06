package com.vimukti.accounter.web.client.ui.edittable;

import java.util.Arrays;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.customers.CustomerTransactionTable;

public abstract class SalesOrderTable extends CustomerTransactionTable {
	@Override
	protected void initColumns() {
		this.addColumn(new ImageEditColumn<ClientTransactionItem>() {

			@Override
			public ImageResource getResource(ClientTransactionItem row) {
				return SalesOrderTable.this.getImage(row);
			}

			@Override
			public int getWidth() {
				return 20;
			}

		});

		this.addColumn(new TransactionItemNameColumn() {

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
							if (Arrays.asList(ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_CREDIT_CARD,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_FIXED_ASSET).contains(
									account.getType())) {
								return true;
							}
						} else {
							if (Arrays.asList(ClientAccount.TYPE_CREDIT_CARD,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_FIXED_ASSET).contains(
									account.getType())) {

								return true;
							}
						}
						return false;
					}
				};
			}

		});

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

		this.addColumn(new TransactionDiscountColumn());

		this.addColumn(new TransactionTotalColumn());

		if (getCompany().getPreferences().getDoYouPaySalesTax()) {
			if (this.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				this.addColumn(new TransactionVatCodeColumn());
			}

			this.addColumn(new TransactionVatColumn());
		}

		
		
		this.addColumn(new DeleteColumn<ClientTransactionItem>() {

			@Override
			protected void onDelete(ClientTransactionItem row) {
				delete(row);
			}
		});
	}
}
