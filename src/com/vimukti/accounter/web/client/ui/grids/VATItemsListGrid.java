/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

/**
 * @author Murali.A
 * 
 */
public class VATItemsListGrid extends BaseListGrid<ClientTAXItem> {

	/**
	 * @param isMultiSelectionEnable
	 */
	public VATItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 50;
		if (index == 5) {
			return 15;
		}
		if (index == 1)
			return 300;
		if (index == 4)
			return 100;
		if (index == 2)
			return 250;
		return super.getCellWidth(index);
		// return -1;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { FinanceApplication.getVATMessages().active(),
				FinanceApplication.getVATMessages().product(),
				FinanceApplication.getVATMessages().VATAgency(),
				FinanceApplication.getVATMessages().description(),
				FinanceApplication.getVATMessages().rate(), "" };
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientTAXItem item, int index) {
		switch (index) {
		case 0:
			return item.isActive();
		case 1:
			return item.getName() != null ? item.getName() : "";
		case 2:
			ClientTAXAgency agency = null;
			if (item.getTaxAgency() != null) {
				agency = FinanceApplication.getCompany().getTaxAgency(
						item.getTaxAgency());
			}
			return agency != null ? agency.getName() : "";
		case 3:
			return item.getDescription() != null ? item.getDescription() : "";
		case 4:
			if (item.isPercentage())
				return item.getTaxRate() + "%";
			else
				return DataUtils.getAmountAsString(item.getTaxRate());
		case 5:
			return FinanceApplication.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
	}

	@Override
	protected void onClick(ClientTAXItem obj, int row, int col) {
		List<ClientTAXItem> records = getRecords();
		if (col == 5)
			showWarnDialog(records.get(row));
	}

	protected void executeDelete(ClientTAXItem item) {
		ViewManager.getInstance().deleteObject(item, AccounterCoreType.TAXITEM,
				this);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientTAXItem obj) {
		VatActionFactory.getNewVatItemAction().run(obj, true);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.ListGrid#validateGrid()
	 */
	@Override
	public boolean validateGrid() {
		return false;
	}

	@Override
	protected int sort(ClientTAXItem obj1, ClientTAXItem obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());

		case 2:
			String agency1 = null;
			String agency2 = null;
			// if (FinanceApplication.getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_US) {
			// agency1 = getTaxAgency(obj1);
			// agency2 = getTaxAgency(obj2);
			// }
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				agency1 = getVATAgencyID(obj1);
				agency2 = getVATAgencyID(obj2);
			}
			return agency1.toLowerCase().compareTo(agency2.toLowerCase());
		case 3:
			String desc1 = obj1.getDescription() != null ? obj1
					.getDescription() : "";
			String desc2 = obj2.getDescription() != null ? obj2
					.getDescription() : "";
			;
			return desc1.toLowerCase().compareTo(desc2.toLowerCase());

		case 4:
			Double rate1 = obj1.getTaxRate();
			Double rate2 = obj2.getTaxRate();
			return rate1.compareTo(rate2);

		}
		return 0;
	}

	// private String getTaxAgency(ClientVATItem obj) {
	// ClientTaxAgency agency = null;
	//
	// if (obj.getVatAgency() != null) {
	// if (FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_US) {
	// agency = FinanceApplication.getCompany().getTaxAgency(
	// obj.getVatAgency());
	// }
	//
	// }
	// return agency != null ? agency.getName() : "";
	//
	// }

	private String getVATAgencyID(ClientTAXItem obj) {

		ClientTAXAgency agency = null;
		if (obj.getTaxAgency() != null) {
		
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				agency = FinanceApplication.getCompany().getTaxAgency(
						obj.getTaxAgency());

			}
		}
		return agency != null ? agency.getName() : "";

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.TAXITEM;
	}

}
