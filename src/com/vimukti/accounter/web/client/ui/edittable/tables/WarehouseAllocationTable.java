package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientWareHouseAllocation;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.QuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class WarehouseAllocationTable extends
		EditTable<ClientWareHouseAllocation> {

	/**
	 * This method will add 4 empty records to the table.
	 */
	public WarehouseAllocationTable() {
		addEmptyRecords();
	}

	protected void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			ClientWareHouseAllocation item = new ClientWareHouseAllocation();
			add(item);
		}
	}

	@Override
	protected void initColumns() {

		this.addColumn(new TextEditColumn<ClientWareHouseAllocation>() {

			@Override
			protected String getValue(ClientWareHouseAllocation row) {
				ClientItem item = Accounter.getCompany().getItem(row.getItem());
				// return item.getName();
				return null;
			}

			@Override
			protected void setValue(ClientWareHouseAllocation row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		});

		this.addColumn(new QuantityColumn<ClientWareHouseAllocation>() {

			@Override
			protected ClientQuantity getQuantity(ClientWareHouseAllocation row) {
				return row.getQuantity();
			}

			@Override
			protected void setQuantity(ClientWareHouseAllocation row,
					ClientQuantity d) {
				row.setQuantity(d);
			}
		});

		this.addColumn(new WareHouseColumn());
	}
}
