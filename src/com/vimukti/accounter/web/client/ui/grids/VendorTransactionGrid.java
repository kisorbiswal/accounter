package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;

public class VendorTransactionGrid extends
		AbstractTransactionGrid<ClientTransactionItem> {

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		return getRecords();
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		CustomCombo<E> combo = null;
		return combo;
	}

	@Override
	public void setTaxCode(long taxCode) {

	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 1:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 2:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 3:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;

		}
	}

	@Override
	protected String[] getColumns() {
		if (getCompany().getPreferences().getDoYouPaySalesTax()) {
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				return new String[] { "", Accounter.constants().name(),
						Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().total(),
						Accounter.constants().newVATCode(),
						Accounter.constants().VAT(), " " };
			} else {
				return new String[] { "", Accounter.constants().name(),
						Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().total(),
						Accounter.constants().isTaxPayble(), " " };
			}

		} else {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().total(), " " };
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 6 || index == 0)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		// else if (index == 2)
		// return 150;
		else if (index == 4 || index == 5)
			return 100;
		else if (index == 3)
			return 80;
		return -1;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int index) {

		return false;
	}

}
