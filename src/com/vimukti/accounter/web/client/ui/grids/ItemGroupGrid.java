package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.FocusWidget;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class ItemGroupGrid extends ListGrid<ClientItem> {

	ItemCombo itemCombo;
	ClientItemGroup itemGroup;

	public ItemGroupGrid(boolean isMultiSelectionEnable,
			ClientItemGroup itemGroup) {
		super(isMultiSelectionEnable);
		this.itemGroup = itemGroup;
		itemCombo = new ItemCombo(Accounter.getCustomersMessages().item(), 1,
				false);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientItem obj, int index) {
		ClientItem clientItem = (ClientItem) obj;
		switch (index) {
		case 0:
			return clientItem.getName();
		case 1:
			return clientItem.getSalesPrice();

		default:
			return "";
		}

	}

	@Override
	protected String[] getSelectValues(ClientItem obj, int index) {

		return null;
	}

	@Override
	protected boolean isEditable(ClientItem obj, int row, int index) {
		switch (index) {
		case 0:
			return true;
		case 1:
			return false;
		}
		return false;
	}

	@Override
	protected void onClick(ClientItem obj, int row, int index) {
		// NOTHING TO DO.
	}

	@Override
	public void onDoubleClick(ClientItem obj) {
		// NOTHING TO DO.
	}

	@Override
	protected void onValueChange(ClientItem obj, int index, Object value) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(ClientItem obj1, ClientItem obj2, int index) {
		return 0;
	}

	@Override
	public void editComplete(ClientItem item, Object value, int col) {
		if (col == 0) {
			item.setItemGroup(itemGroup.getID());
		}
		super.editComplete(item, value, col);
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// NOTHING TO DO.
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.getFinanceUIConstants().name(),
				Accounter.getFinanceUIConstants().price() };
	}

	@Override
	protected void addOrEditSelectBox(ClientItem obj, Object value) {

		if (currentCol == 0) {
			FocusWidget widget = (FocusWidget) itemCombo.getMainWidget();
			this.setWidget(currentRow, currentCol, widget);
		} else
			super.addOrEditSelectBox(obj, value);
	}

}
