package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class SalesOrderTable extends CustomerItemTransactionTable {

	public SalesOrderTable(boolean enableTax, boolean showTaxCode,
			ICurrencyProvider currencyProvider) {
		super(enableTax, showTaxCode, currencyProvider);
	}

	@Override
	protected void initColumns() {

		this.addColumn(new ItemNameColumn() {

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientItem newValue) {
				super.setValue(row, newValue);
				update(row);
				// applyPriceLevel(row);
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

		this.addColumn(new TransactionUnitPriceColumn(currencyProvider));

		this.addColumn(new TransactionDiscountColumn(currencyProvider));

		this.addColumn(new TransactionTotalColumn(currencyProvider));

		if (getCompany().getPreferences().isTrackTax()
				&& getCompany().getPreferences().isTaxPerDetailLine()) {
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
		}

		this.addColumn(new AmountColumn<ClientTransactionItem>(currencyProvider) {

			@Override
			protected double getAmount(ClientTransactionItem row) {
				return row.getInvoiced();
			}

			@Override
			protected void setAmount(ClientTransactionItem row, double value) {
				row.setInvoiced(value);
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().invoiced();
			}

			@Override
			public int getWidth() {
				return 190;
			}
		});

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}
}
