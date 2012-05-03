package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.core.ClientBuildAssembly;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class BuildAssembliesGrid extends BaseListGrid<ClientBuildAssembly> {

	public BuildAssembliesGrid() {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "assemblyItem", "description", "buildQuantity",
				"quantityOnHand", "reorderPoint", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "assemblyItem-value", "description-value",
				"buildQuantity-value", "quantityOnHand-value",
				"reorderPoint-value", "delete-value" };
	}

	@Override
	protected void executeDelete(ClientBuildAssembly object) {

	}

	@Override
	protected Object getColumnValue(ClientBuildAssembly obj, int index) {
		ClientItem item = getCompany().getItem(obj.getInventoryAssembly());
		switch (index) {
		case 0:
			return item.getName();
		case 1:
			return obj.getMemo();
		case 2:
			return obj.getQuantityToBuild();
		case 3:
			return item.getOnhandQty();
		case 4:
			return item.getReorderPoint();
		case 5:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientBuildAssembly obj) {
		ReportsRPC.openTransactionView(ClientTransaction.TYPE_BUILD_ASSEMBLY,
				obj.getID());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.assemblyItem(), messages.description(),
				messages.buildQuantity(), messages.quantityOnHand(),
				messages.reorderPoint(), messages.delete() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 150;
		case 2:
			return 100;
		case 3:
			return 120;
		case 4:
			return 120;
		case 5:
			return 40;

		default:
			return -1;
		}
	}
}
