package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.Arrays;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccountable;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ImageEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

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
						if (getCompany().getPreferences().isRegisteredForVAT()) {
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

			@Override
			protected void setValue(ClientTransactionItem row,
					IAccountable newValue) {
				super.setValue(row, newValue);
				applyPriceLevel(row);
			}

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isISellThisItem();
					}
				};
			}

		});

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

		this.addColumn(new TransactionDiscountColumn());

		this.addColumn(new TransactionTotalColumn());

		if (getCompany().getPreferences().isRegisteredForVAT()) {
			this.addColumn(new TransactionVatCodeColumn());
			this.addColumn(new TransactionVatColumn());
		} else if (getCompany().getPreferences().isChargeSalesTax()) {
			this.addColumn(new TransactionVatColumn() {
				protected String getColumnName() {
					return Accounter.constants().tax();
				};
			});
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}
}
