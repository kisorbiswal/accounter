/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

/**
 * @author gwt
 * 
 */
public class ManageTAXCodeListGrid extends BaseListGrid<ClientTAXCode> {

	/**
	 * @param isMultiSelectionEnable
	 */
	public ManageTAXCodeListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientTAXCode taxCode, int index) {
		switch (index) {

		case 0:
			return taxCode.isActive();
		case 1:
			return taxCode.getName() != null ? taxCode.getName() : "";
		case 2:
			return taxCode.getDescription() != null ? taxCode.getDescription()
					: "";
		case 3:
			if (taxCode.isTaxable())
				// return FinanceApplication.getFinanceImages().tickMark()
				// .getURL();
				return "/images/Tick-mark.png";
			else
				// return FinanceApplication.getFinanceImages().balnkImage()
				// .getURL();
				return "/images/blank_16x16.png";
		case 4:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";

		}
		return "";
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientTAXCode obj) {
		VatActionFactory.getNewTAXCodeAction().run(obj, true);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.ListGrid#validateGrid()
	 */
	@Override
	public boolean validateGrid() {
		return false;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.getVATMessages().active(),
				Accounter.getVATMessages().code(),
				Accounter.getVATMessages().description(),
				Accounter.getVATMessages().taxable(), "" };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 50;
		if (index == 1)
			return 150;
		if (index == 3)
			return 100;
		if (index == 4) {
			return 15;
		}
		return super.getCellWidth(index);
	}

	@Override
	protected void onClick(ClientTAXCode obj, int row, int col) {
		List<ClientTAXCode> records = getRecords();
		if (col == 4)
			showWarnDialog(records.get(row));
	}

	@Override
	protected void executeDelete(ClientTAXCode taxCode) {
		ViewManager.getInstance().deleteObject(taxCode,
				AccounterCoreType.TAX_CODE, this);
	}

	@Override
	protected int sort(ClientTAXCode obj1, ClientTAXCode obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getName().compareTo(obj2.getName());
		case 2:
			String desc1 = obj1.getDescription() != null ? obj1
					.getDescription() : "";
			String desc2 = obj2.getDescription() != null ? obj2
					.getDescription() : "";
			return desc1.toLowerCase().compareTo(desc2.toLowerCase());
		case 3:
			Boolean taxable1 = obj1.isTaxable();
			Boolean taxable2 = obj2.isTaxable();
			return taxable1.compareTo(taxable2);

		}
		return 0;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.TAX_CODE;
	}

	@Override
	public void addData(ClientTAXCode obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}
}
