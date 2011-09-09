package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;

public class PurchaseOrderGrid extends VendorTransactionGrid {

	public PurchaseOrderGrid() {
		super(true);
	}

	@Override
	protected String[] getColumns() {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
				&& getCompany().getPreferences().isDoYouChargesalesTax()) {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().total(),
					Accounter.constants().billsReceived(),
					Accounter.constants().newVATCode(),
					Accounter.constants().vat(), " " };

		} else {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().total(),
					Accounter.constants().billsReceived(), " " };
		}
	}

	@Override
	protected int getColumnType(int col) {

		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& getCompany().getPreferences().isDoYouChargesalesTax())
				return ListGrid.COLUMN_TYPE_SELECT;
			else
				return ListGrid.COLUMN_TYPE_IMAGE;
		case 8:
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
			else
				return ListGrid.COLUMN_TYPE_IMAGE;
		case 9:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;

		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			return getUKGridCellWidth(index);
		} else {
			return getUSGridCellWidth(index);
		}
	}

	private int getUKGridCellWidth(int index) {

		if (index == 0 || index == 9)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 2 || index == 6)
			return 90;
		else if (index == 4 || index == 5)
			return 100;
		else if (index == 3)
			return 80;
		else if (index == 1) {
			if (UIUtils.isMSIEBrowser()) {
				return 90;
			} else {
				return 100;
			}
		} else if (index == 8)
			return 80;
		else if (index == 7) {
			if (!getCompany().getPreferences().isDoYouChargesalesTax()) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			return 80;
		}
		return -1;

	}

	private int getUSGridCellWidth(int index) {
		if (index == 0 || index == 7)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		if (index == 1) {
			return 200;
		}
		return -1;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int col) {
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		//
		// if (Arrays.asList(3).contains(col))
		// return "";
		// }

		switch (col) {
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
						.getQuantity()
						: "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return amountAsString(getAmountInForeignCurrency(item
						.getUnitPrice()));
			else {
				return (getAmountInForeignCurrency(item.getUnitPrice()) != 0 || item
						.getLineTotal() == 0) ? amountAsString(getAmountInForeignCurrency(item
						.getUnitPrice()))
						: "";
			}
		case 5:
			return amountAsString(item.getLineTotal());
		case 6:
			return item.getInvoiced() + "";
		case 7:
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& getCompany().getPreferences().isDoYouChargesalesTax()) {
				return getTAXCodeName(item.getTaxCode());
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}

		case 8:
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				return amountAsString(item.getVATfraction());
		case 9:
			return Accounter.getFinanceMenuImages().delete();
		default:
			return "";
		}
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;

		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			case 6:
				return false;
			case 8:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			case 6:
				return false;
			case 8:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SERVICE:
			switch (col) {
			case 6:
				return false;
			case 8:
				return false;

			default:
				return true;
			}

		}

		return true;
	}

	@Override
	public void editComplete(ClientTransactionItem item, Object value, int col) {

		super.editComplete(item, value, col);
		switch (col) {
		// To edit the total field
		case 6:

			String lineTotalAmtString = value.toString() != null
					|| value.toString().length() != 0 ? ((Object) value)
					.toString() : "0";
			// if (lineTotalAmtString.contains(""
			// + UIUtils.getCurrencySymbol() + "")) {
			// lineTotalAmtString = lineTotalAmtString.replaceAll(""
			// + UIUtils.getCurrencySymbol() + "", "");
			// }
			if (item.getType() == TYPE_SALESTAX
					|| item.getType() == TYPE_ACCOUNT
					|| item.getType() == TYPE_SERVICE) {

				// lineTotalAmtString = lineTotalAmtString.replaceAll(",",
				// "");
				Double lineTotal = Double.parseDouble(DataUtils
						.getReformatedAmount(lineTotalAmtString)
						+ "");
				try {
					if (!AccounterValidator.isValidGridLineTotal(lineTotal)
							&& !AccounterValidator.isAmountTooLarge(lineTotal)) {
						double newValue = getAmountInBaseCurrency((Double) lineTotal);
						item.setLineTotal(newValue);
						item.setUnitPrice(0.0D);
						item.setQuantity(null);
					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						item.setLineTotal(0.0D);
						item.setUnitPrice(0.0D);
						item.setQuantity(null);
						Accounter.showError(e.getMessage());
					}
				}
			}
		}
		super.updateTotals();
		super.updateData(item);
	}
}
