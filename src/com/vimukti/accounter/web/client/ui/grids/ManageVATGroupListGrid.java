package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

/**
 * 
 * @author Raj Vimal
 * 
 */
public class ManageVATGroupListGrid extends BaseListGrid<ClientTAXGroup> {

	public ManageVATGroupListGrid() {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected Object getColumnValue(ClientTAXGroup vatGroup, int index) {
		switch (index) {
		case 0:
			return vatGroup.isActive();
		case 1:
			return vatGroup.getName() != null ? vatGroup.getName() : "";
		case 2:
			return vatGroup.getDescription() != null ? vatGroup
					.getDescription() : "";
		case 3:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}

		return "";
	}

	@Override
	public void onDoubleClick(ClientTAXGroup obj) {
		ActionFactory.getVatGroupAction().run(obj, true);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().active(),
				Accounter.constants().vatGroup(),
				Accounter.constants().description(), " " };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 50;
		case 3:
			return 15;
		default:
			return -1;
		}
	}

	@Override
	protected void onClick(ClientTAXGroup obj, int row, int col) {
		List<ClientTAXGroup> records = getRecords();
		if (col == 3)
			showWarnDialog(records.get(row));
	}

	@Override
	protected void executeDelete(ClientTAXGroup vatGroup) {
		deleteObject(vatGroup);

	}

	@Override
	protected int sort(ClientTAXGroup obj1, ClientTAXGroup obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getName().compareTo(obj2.getName());
		case 2:
			String desc1 = obj1.getDescription() != null ? obj1
					.getDescription() : "";
			String desc2 = obj2.getDescription() != null ? obj2
					.getDescription() : "";
			return desc1.compareTo(desc2);
		}
		return 0;
	}


	public AccounterCoreType getType() {
		return AccounterCoreType.TAX_GROUP;
	}

}
