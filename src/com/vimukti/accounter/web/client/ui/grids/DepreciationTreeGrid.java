/**
 * This grid displays the assets(group by account) with depreciating amount 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientDepreciationDummyEntry;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Murali.A
 * 
 */
public class DepreciationTreeGrid extends
		TreeGrid<ClientDepreciationDummyEntry> {

	public DepreciationTreeGrid(String emptyMessage) {
		super(emptyMessage);
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected Object getColumnValue(ClientDepreciationDummyEntry obj, int index) {
		switch (index) {
		case 0:
			return obj.getFixedAssetName();
		case 1:
			return amountAsString(obj.getAmountToBeDepreciated());
		case 2:
			return obj.getAssetAccount() != 0 ? Accounter.getCompany()
					.getAccount(obj.getAssetAccount()).getName() : "";
		default:
			return "";
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] {
				Accounter.messages().account(Global.get().account()),
				Accounter.constants().amounttobeDepreciated(),
				Accounter.messages().accumulatedDepreciationAccount(
						Global.get().Account()) };
	}

	@Override
	protected void onClick(ClientDepreciationDummyEntry obj, int row, int index) {

	}

	/* This method sets the columnvalues for parent row */
	public void addParentOrEdit(int col, int row, String string) {
		if (col == 0) {
			addParent(string, Accounter.getFinanceMenuImages().newAccount());
		} else {
			this.setText(row, col, string);
		}
	}

	@Override
	public void addParentWithChilds(String name,
			List<ClientDepreciationDummyEntry> childNodes) {
		if (childNodes != null) {
			ClientDepreciationDummyEntry parent = new ClientDepreciationDummyEntry();
			for (ClientDepreciationDummyEntry entry : childNodes) {
				parent.setAmountToBeDepreciated(parent
						.getAmountToBeDepreciated()
						+ entry.getAmountToBeDepreciated());
			}
			addParentOrEdit(0, currentRow, name);
			addParentOrEdit(1, currentRow,
					amountAsString(parent.getAmountToBeDepreciated()));
			addParentOrEdit(2, currentRow, "");
			super.addNodes(childNodes);
		} else
			super.addParentWithChilds(name, childNodes);
	}

	@Override
	protected int sort(ClientDepreciationDummyEntry obj1,
			ClientDepreciationDummyEntry obj2, int index) {
		return 0;
	}

}
