package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class CustomerItemTransactionTable extends
		CustomerTransactionTable {

	/**
	 * Creates the instance
	 */
	public CustomerItemTransactionTable(boolean enableTax, boolean showTaxCode,
			ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, currencyProvider);
	}

	public CustomerItemTransactionTable(boolean needDiscount,
			boolean enableTax, boolean showTaxCode,
			ICurrencyProvider currencyProvider) {
		super(needDiscount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		addEmptyRecords();
	}

	/**
	 * This method will add 4 empty records to the table.
	 */
	protected void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			ClientTransactionItem item = new ClientTransactionItem();
			item.setType(ClientTransactionItem.TYPE_ITEM);
			add(item);
		}
	}

	@Override
	protected void initColumns() {

		this.addColumn(new ItemNameColumn() {

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isISellThisItem();
					}
				};
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientItem newValue) {
				super.setValue(row, newValue);
				update(row);
				// applyPriceLevel(row);
			}
		});

		this.addColumn(new DescriptionEditColumn() {
			@Override
			public int getWidth() {
				return 190;
			}
		});

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn(currencyProvider));

		if (needDiscount) {
			this.addColumn(new TransactionDiscountColumn(currencyProvider));
		}

		this.addColumn(new TransactionTotalColumn(currencyProvider));

		// if (getCompany().getPreferences().isChargeSalesTax()) {

		if (enableTax) {
			if (showTaxCode) {
				this.addColumn(new TransactionVatCodeColumn() {

					@Override
					protected ListFilter<ClientTAXCode> getTaxCodeFilter() {
						return new ListFilter<ClientTAXCode>() {

							@Override
							public boolean filter(ClientTAXCode e) {
								if (e.getTAXItemGrpForSales() != 0) {
									return true;
								}
								return false;
							}
						};
					}

					@Override
					protected boolean isSales() {
						return true;
					}
				});

				this.addColumn(new TransactionVatColumn(currencyProvider));
			} else {
				this.addColumn(new TransactionTaxableColumn());
			}
		}

		// }

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}
}
