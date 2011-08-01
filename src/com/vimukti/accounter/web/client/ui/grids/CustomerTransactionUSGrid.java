package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.WriteChequeView;

public class CustomerTransactionUSGrid extends CustomerTransactionGrid {

	// private boolean isAddNewRequired = true;

	public CustomerTransactionUSGrid() {
		super(true);
	}

	public CustomerTransactionUSGrid(boolean isAddNewRequired) {
		super(isAddNewRequired);
		// this.isAddNewRequired = isAddNewRequired;
	}

	@Override
	protected String[] getColumns() {
		if (transactionView instanceof WriteChequeView)
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().discountPerc(),
					Accounter.constants().total(), " " };
		else
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().discountPerc(),
					Accounter.constants().total(), Accounter.constants().tax(),
					" " };
	}

	@Override
	public String[] getColumnNamesForPrinting() {
		return new String[] { Accounter.constants().quantity(),
				Accounter.constants().item(),
				Accounter.constants().description(),
				Accounter.constants().rate(), Accounter.constants().amount(),
				Accounter.constants().isTaxable() };
	}

	@Override
	public String getColumnValueForPrinting(ClientTransactionItem item,
			int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
			if (!Arrays.asList(0, 2, 8).contains(index))
				return "";
		}
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 2, 6, 8).contains(index))
				return "";
		}
		switch (index) {
		case 0:
			return item.getQuantity() + "";
		case 1:
			return getNameValue(item);
		case 2:
			return item.getDescription();
		case 3:
			return DataUtils.getAmountAsString(item.getUnitPrice());
		case 4:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 5:
			return item.isTaxable() ? Accounter.constants().taxable()
					: Accounter.constants().nonTaxable();
		default:
			return "";
		}
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 0 || col == 8) {
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		switch (col) {
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_QUANTITY_POPUP;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			if (transactionView instanceof WriteChequeView)
				return ListGrid.COLUMN_TYPE_IMAGE;
			else
				return ListGrid.COLUMN_TYPE_SELECT;
		}
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		if (transactionView instanceof WriteChequeView) {
			if (index == 7 || index == 0)
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			// else if (index == 2)
			// return 150;
			else if (index == 4 || index == 6)
				return 100;
			else if (index == 3 || index == 5)
				return 80;
		} else {
			if (index == 8 || index == 0)
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;

			else if (index == 2)
				return 150;
			else if (index == 4 || index == 6)
				return 100;
			else if (index == 3 || index == 5 || index == 7)
				return 80;
		}
		return -1;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {

		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
			if (!Arrays.asList(0, 2, 8).contains(index))
				return "";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		// if (Arrays.asList(3).contains(index))
		// return "";
		// }

		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 2, 6, 8).contains(index))
				return "";
		}
		switch (index) {
		case 0:
			return getImageByType(item.getType());
		case 1:
			return getNameValue(item);
		case 2:
			return item.getDescription();
		case 3:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return item.getQuantity();
			else {
				return (item.getQuantity() != null || item.getLineTotal() == 0) ? item
						.getQuantity() : "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return DataUtils.getAmountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? DataUtils
						.getAmountAsString(item.getUnitPrice()) : "";
			}
		case 5:
			return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
		case 6:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 7:
			if (transactionView instanceof WriteChequeView)
				return Accounter.getFinanceMenuImages().delete();
			else
				return item.isTaxable() ? Accounter.constants().taxable()
						: Accounter.constants().nonTaxable();

		case 8:
			return Accounter.getFinanceMenuImages().delete();
		default:
			return "";
		}
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;
		if (obj.getType() == TYPE_SERVICE
				&& !Accounter.getCompany().getPreferences()
						.getDoYouPaySalesTax()) {
			if (col == 7 || col == 8)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
			// case 5:
			// return false;

			default:
				return true;
			}
		case ClientTransactionItem.TYPE_COMMENT:
			switch (col) {
			case 2:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			case 6:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SALESTAX:
			switch (col) {
			case 1:
				return true;
			case 2:
				return true;
			case 6:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_SERVICE:

			switch (col) {
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 4:
				return true;
			case 5:
				return true;
			case 6:
				return true;
			case 7:
				return true;
			default:
				return false;
			}
		}
		return true;
	}

	@Override
	public long getTaxCode(ClientTransactionItem item) {
		return this.taxCode;
	}

	@Override
	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
	}
}