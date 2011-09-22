package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class CustomerItemTransactionTable extends
		CustomerTransactionTable {

	/**
	 * Creates the instance
	 */
	public CustomerItemTransactionTable() {
		this(true);
	}

	public CustomerItemTransactionTable(boolean needDiscount) {
		super(needDiscount);
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

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

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
