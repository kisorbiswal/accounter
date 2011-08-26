package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;

public class SalesOrderGrid extends CustomerTransactionGrid {

	public SalesOrderGrid() {
		super(true);

	}

	@Override
	protected String[] getColumns() {
		if (getCompany().getPreferences().getDoYouPaySalesTax()) {
			if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK) {
				return new String[] { "", Accounter.constants().name(),
						Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().discountPerc(),
						Accounter.constants().total(),
						Accounter.constants().newVATCode(),
						Accounter.constants().vat(),
						Accounter.constants().invoiced(), " " };
			} else {
				return new String[] { "", Accounter.constants().name(),
						Accounter.constants().description(),
						Accounter.constants().quantity(),
						Accounter.constants().unitPrice(),
						Accounter.constants().discountPerc(),
						Accounter.constants().total(),
						Accounter.constants().tax(),
						Accounter.constants().invoiced(), " " };
			}

		} else {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().discountPerc(),
					Accounter.constants().total(),
					Accounter.constants().invoiced(), " " };
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

	private int getUSGridCellWidth(int index) {
		if (index == 0 || index == 9)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		if (index == 1)
			return 200;
		if (index == 5)
			return 80;
		if (index == 8) {
			if (!getCompany().getPreferences().getDoYouPaySalesTax()) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
		}
		return -1;
	}

	private int getUKGridCellWidth(int index) {
		if (index == 0 || index == 10)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 7 || index == 2)
			return 80;
		else if (index == 3 || index == 4 || index == 9)
			return 90;
		else if (index == 5)
			return 80;
		else if (index == 6)
			return 100;
		else if (index == 1) {
			if (UIUtils.isMSIEBrowser()) {
				return 90;
			} else {
				return 100;
			}
		} else if (index == 8) {
			if (!getCompany().getPreferences().getDoYouPaySalesTax()) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			return 70;
		}
		return -1;

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
			if (getCompany().getPreferences().getDoYouPaySalesTax())
				return ListGrid.COLUMN_TYPE_SELECT;
			else
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 8:
			if (getCompany().getPreferences().getDoYouPaySalesTax())
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
			else {
				return ListGrid.COLUMN_TYPE_IMAGE;
			}
		case 9:
			if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK)
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
			else
				return ListGrid.COLUMN_TYPE_IMAGE;
		case 10:
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		return 0;
	}

	@Override
	public void editComplete(final ClientTransactionItem item,
			final Object value, final int col) {
		if (col == 3) {

			try {
				double changedqty = DataUtils.getAmountStringAsDouble(value
						.toString());
				if (changedqty < (item.getInvoiced() / item.getUnitPrice())) {
					Accounter.showWarning("error", AccounterType.WARNING,
							new ErrorDialogHandler() {

								@Override
								public boolean onYesClick() {
									SalesOrderGrid.super.editComplete(item,
											value, col);
									return false;
								}

								@Override
								public boolean onNoClick() {
									return false;
								}

								@Override
								public boolean onCancelClick() {
									return false;
								}
							});
				} else {
					super.editComplete(item, value, col);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (col == 7) {
			if (value.equals("Taxable")) {
				item.setTaxable(true);
			} else
				item.setTaxable(false);
		}
		super.editComplete(item, value, col);
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {

		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			// case 3:
			// return false;
			// case 5:
			// return false;
			// case 6:
			// return false;
			// case 7:
			// return false;
			case 8:
				return false;

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
			// case 6:
			// return false;
			case 6:
				return false;
			case 8:
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
				// case 8:
				// return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_SERVICE:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
			case 8:
				return false;
			default:
				return true;
			}
		}

		return true;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {

			if (!Arrays.asList(0, 2, 9).contains(index))
				return "";

		}
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 2, 8, 7, 9).contains(index))
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
				return amountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? amountAsString(item
						.getUnitPrice()) : "";
			}
		case 5:
			return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
			// case 6:
			// return amountAsString(item.getBackOrder());
		case 6:
			return amountAsString(item.getLineTotal());
		case 7:
			if (getCompany().getPreferences().getDoYouPaySalesTax()) {
				if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK) {
					return getTAXCodeName(item.getTaxCode());
				} else {
					return item.isTaxable() ? Accounter.constants().taxable()
							: Accounter.constants().nonTaxable();
				}
			} else {
				return amountAsString(item.getInvoiced());
			}
		case 8:
			if (getCompany().getPreferences().getDoYouPaySalesTax()) {
				if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK) {
					return amountAsString(item.getVATfraction());
				} else {
					return amountAsString(item.getInvoiced());
				}
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}
		case 9:
			if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK) {
				return amountAsString(item.getInvoiced());
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}
		case 10:
			return Accounter.getFinanceMenuImages().delete();
		default:
			return "";
		}
	}

}
