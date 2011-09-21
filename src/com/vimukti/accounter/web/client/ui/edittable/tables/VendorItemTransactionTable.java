package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class VendorItemTransactionTable extends VendorTransactionTable {

	public VendorItemTransactionTable() {
		super();
		addEmptyRecords();
	}

	public VendorItemTransactionTable(boolean needDiscount) {
		super(needDiscount);
		addEmptyRecords();
	}

	/**
	 * This method will add 4 empty records to the table.
	 */
	private void addEmptyRecords() {
		for (int i = 0; i < 3; i++) {
			ClientTransactionItem item = new ClientTransactionItem();
			item.setType(ClientTransactionItem.TYPE_ITEM);
			add(item);
		}
	}

	@Override
	protected void initColumns() {

		ItemNameColumn transactionItemNameColumn = new ItemNameColumn() {

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientItem newValue) {
				super.setValue(row, newValue);
				// Unit Price is different. So that overriden the code here
				if (newValue != null) {
					row.setUnitPrice(newValue.getPurchasePrice());
					row.setTaxable(newValue.isTaxable());
					double lt = row.getQuantity().getValue()
							* row.getUnitPrice();
					double disc = row.getDiscount();
					row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
							* disc / 100))
							: lt);
				}
				update(row);
			}

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isIBuyThisItem();
					}
				};
			}
		};
		transactionItemNameColumn.setItemForCustomer(false);

		this.addColumn(transactionItemNameColumn);

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

		if (needDiscount) {
			this.addColumn(new TransactionDiscountColumn());
		}

		this.addColumn(new TransactionTotalColumn());

		if (getCompany().getPreferences().isRegisteredForVAT()) {

			this.addColumn(new TransactionVatCodeColumn());

			this.addColumn(new TransactionVatColumn());
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}
}
