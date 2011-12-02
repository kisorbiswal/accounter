package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientStockAdjustmentItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.ItemsDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class StockAdjustmentTable extends
		EditTable<ClientStockAdjustmentItem> {

	@Override
	protected void initColumns() {

		final ItemsDropDownTable itemTable = new ItemsDropDownTable(
				new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						if (e.getType() == ClientItem.TYPE_INVENTORY_PART) {
							return true;
						}
						return false;
					}
				});
		itemTable.setItemType(ClientItem.TYPE_INVENTORY_PART);

		this.addColumn(new ComboColumn<ClientStockAdjustmentItem, ClientItem>() {

			@Override
			protected ClientItem getValue(ClientStockAdjustmentItem row) {
				ClientItem item = Accounter.getCompany().getItem(row.getItem());
				return item;
			}

			@Override
			protected void setValue(ClientStockAdjustmentItem row,
					ClientItem newValue) {
				row.setItem(newValue.getID());
				ClientMeasurement measurement = Accounter.getCompany()
						.getMeasurement(newValue.getMeasurement());
				boolean isExist = false;
				for (ClientUnit unit : measurement.getUnits()) {
					if (unit.getID() == row.getAdjustmentQty().getUnit()) {
						isExist = true;
						break;
					}
				}
				if (!isExist) {
					row.getAdjustmentQty().setUnit(
							measurement.getDefaultUnit().getId());
				}
				update(row);
			}

			@Override
			public AbstractDropDownTable<ClientItem> getDisplayTable(
					ClientStockAdjustmentItem row) {
				return itemTable;
			}

			@Override
			public int getWidth() {
				return 180;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().item();
			}

		});

		this.addColumn(new TextEditColumn<ClientStockAdjustmentItem>() {

			@Override
			protected String getValue(ClientStockAdjustmentItem row) {
				return row.getComment();
			}

			@Override
			protected void setValue(ClientStockAdjustmentItem row, String value) {
				row.setComment(value);
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().comment();
			}
		});

		this.addColumn(new TextEditColumn<ClientStockAdjustmentItem>() {

			@Override
			protected String getValue(ClientStockAdjustmentItem row) {
				return getAvailableQuantity(row.getItem());
			}

			@Override
			protected void setValue(ClientStockAdjustmentItem row, String value) {
				// Nothing to do.
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().availableQty();
			}

			@Override
			public int getWidth() {
				return 200;
			}
		});

		this.addColumn(new StockAdjustmentQuantityColumn());

		this.addColumn(new DeleteColumn<ClientStockAdjustmentItem>());
	}

	protected abstract String getAvailableQuantity(long item);

	public void refresh() {
		for (ClientStockAdjustmentItem row : getAllRows()) {
			update(row);
		}
	}

	@Override
	public void setAllRows(List<ClientStockAdjustmentItem> rows) {
		for (ClientStockAdjustmentItem row : rows) {
			row.setId(0);
		}
		super.setAllRows(rows);
	}
}
