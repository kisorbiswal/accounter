package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.InventoryItemNamesColumn;
import com.vimukti.accounter.web.client.ui.edittable.QuantityColumn;

public abstract class CustomerInventoryItemTransactionTable extends
		CustomerTransactionTable {
	/**
	 * Creates the instance
	 */
	public CustomerInventoryItemTransactionTable(boolean enableTax,
			boolean showTaxCode, ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, currencyProvider);
	}

	public CustomerInventoryItemTransactionTable(boolean needDiscount,
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
			item.setType(ClientTransactionItem.TYPE_INVENTORY_ITEM);
			add(item);
		}
	}

	@Override
	protected void initColumns() {

		this.addColumn(new InventoryItemNamesColumn() {
			@Override
			public int getWidth() {
				return 150;
			}
		});

		// this.addColumn(new InventoryItemNameColumn() {
		//
		// @Override
		// public ListFilter<ClientItem> getItemsFilter() {
		// return new ListFilter<ClientItem>() {
		//
		// @Override
		// public boolean filter(ClientItem e) {
		// return (e.getType() == ClientItem.TYPE_INVENTORY_PART);
		// }
		// };
		// }
		//
		// @Override
		// protected void setValue(ClientTransactionItem row,
		// ClientItem newValue) {
		// super.setValue(row, newValue);
		// update(row);
		// // applyPriceLevel(row);
		// }
		// });

		// this.addColumn(new DescriptionEditColumn() {
		// @Override
		// public int getWidth() {
		// return 190;
		// }
		// });

		this.addColumn(new QuantityColumn<ClientTransactionItem>() {

			@Override
			protected ClientQuantity getQuantity(ClientTransactionItem row) {
				return row.getAvailableQuantity();
			}

			@Override
			protected void setQuantity(ClientTransactionItem row,
					ClientQuantity d) {
				row.setAvailableQuantity(d);
			}
		});

		this.addColumn(new WareHouseColumn() {

		});

		// this.addColumn(new UnitsColumn() {
		//
		// });
		//
		// this.addColumn(new TransactionUnitPriceColumn(currencyProvider));
		//
		// if (needDiscount) {
		// this.addColumn(new TransactionDiscountColumn(currencyProvider));
		// }
		//
		// this.addColumn(new TransactionTotalColumn(currencyProvider));
		//
		// // if (getCompany().getPreferences().isChargeSalesTax()) {
		//
		// if (enableTax) {
		// if (showTaxCode) {
		// this.addColumn(new TransactionVatCodeColumn() {
		//
		// @Override
		// protected ListFilter<ClientTAXCode> getTaxCodeFilter() {
		// return new ListFilter<ClientTAXCode>() {
		//
		// @Override
		// public boolean filter(ClientTAXCode e) {
		// if (e.getTAXItemGrpForSales() != 0) {
		// return true;
		// }
		// return false;
		// }
		// };
		// }
		//
		// @Override
		// protected boolean isSales() {
		// return true;
		// }
		// });
		//
		// this.addColumn(new TransactionVatColumn(currencyProvider));
		// } else {
		// this.addColumn(new TransactionTaxableColumn());
		// }
		// }
		//
		// // }
		//
		// this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

}
