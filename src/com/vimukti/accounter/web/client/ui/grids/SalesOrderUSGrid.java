package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;

public class SalesOrderUSGrid extends CustomerTransactionUSGrid {

	public SalesOrderUSGrid() {
		super(true);

	}

	@Override
	protected String[] getColumns() {

		return new String[] {
				"",
				FinanceApplication.getCustomersMessages().name(),
				FinanceApplication.getCustomersMessages().description(),
				FinanceApplication.getCustomersMessages().quantity(),
				FinanceApplication.getCustomersMessages().unitPrice(),
				FinanceApplication.getCustomersMessages().discountPerc(),
				// "Back Order",
				FinanceApplication.getCustomersMessages().total(),
				FinanceApplication.getVATMessages().tax(),
				FinanceApplication.getCustomersMessages().invoiced(), " " };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 9)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		if (index == 1)
			return 200;
		if (index == 5)
			return 80;
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 0 || col == 9) {
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		switch (col) {
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
		case 8:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			return ListGrid.COLUMN_TYPE_SELECT;
			// case 9:
			// return ListGrid.COLUMN_TYPE_SELECT;
		}
		return 0;
	}

	@Override
	protected String[] getSelectValues(ClientTransactionItem obj, int index) {
		switch (index) {
		case 9:
			return new String[] {
					FinanceApplication.getCustomersMessages().taxable(),
					FinanceApplication.getCustomersMessages().nonTaxable() };

		default:
			break;
		}
		return super.getSelectValues(obj, index);
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
								public boolean onYesClick()
										throws InvalidEntryException {
									SalesOrderUSGrid.super.editComplete(item,
											value, col);
									return false;
								}

								@Override
								public boolean onNoClick()
										throws InvalidEntryException {
									// TODO Auto-generated method stub
									return false;
								}

								@Override
								public boolean onCancelClick()
										throws InvalidEntryException {
									// TODO Auto-generated method stub
									return false;
								}
							});
				} else {
					super.editComplete(item, value, col);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		// if (Arrays.asList(3).contains(index))
		// return "";
		// }
		// if (item.getType() == ClientTransactionItem.TYPE_SERVICE) {
		// if (!Arrays.asList(0, 1, 2, 3,4,5,8, 6, 7, 9).contains(index))
		// return "";
		// }
		switch (index) {
		case 0:
			return getImageByType(item.getType());
		case 1:
			return getNameValue(item);
		case 2:
			return item.getDescription();
		case 3:
			return item.getQuantity();
		case 4:
			return DataUtils.getAmountAsString(item.getUnitPrice());
		case 5:
			return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
			// case 6:
			// return DataUtils.getAmountAsString(item.getBackOrder());
		case 6:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 7:
			return item.isTaxable() ? FinanceApplication.getVATMessages()
					.taxable() : FinanceApplication.getVATMessages()
					.nonTaxable();
		case 8:
			return DataUtils.getAmountAsString(item.getInvoiced());
		case 9:
			// return
			return FinanceApplication.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

}
