package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;

public class PurchaseOrderUSGrid extends VendorTransactionUSGrid {

	public PurchaseOrderUSGrid() {
		super(true);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "",
				FinanceApplication.getVendorsMessages().name(),
				FinanceApplication.getCustomersMessages().description(),
				FinanceApplication.getCustomersMessages().quantity(),
				FinanceApplication.getVendorsMessages().unitPrice(),
				FinanceApplication.getVendorsMessages().onHand(),
				FinanceApplication.getVendorsMessages().total(),
				FinanceApplication.getVendorsMessages().billsReceived(), " " };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 8)
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
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return 0;

		}
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
				return (item.getQuantity() != 0 || item.getLineTotal() == 0) ? item.getQuantity() : "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return DataUtils.getAmountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? DataUtils
						.getAmountAsString(item.getUnitPrice()) : "";
			}
		case 5:
			return item.getBackOrder() + "";
		case 6:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 7:
			return item.getInvoiced() + "";
		case 8:
			return FinanceApplication.getFinanceMenuImages().delete();
			// return "/images/delete.png";
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
			case 5:
				return false;
			case 6:
				return true;
			case 7:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			case 5:
				return false;
			case 6:
				return false;
			case 7:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SERVICE:
			switch (col) {
			case 5:
				return false;
			case 7:
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
		//To edit the total field 
		case 6:

			String lineTotalAmtString = value.toString() != null
					|| value.toString().length() != 0 ? value.toString() : "0";
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
					if (!AccounterValidator.validateGridLineTotal(lineTotal)
							&& !AccounterValidator.isAmountTooLarge(lineTotal)) {
						item.setLineTotal(lineTotal);
						item.setUnitPrice(0.0D);
						item.setQuantity(0);
					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						item.setLineTotal(0.0D);
						item.setUnitPrice(0.0D);
						item.setQuantity(0);
						Accounter.showError(e.getMessage());

					}

				}
			}

		}

		super.updateTotals();
		super.updateData(item);
	}

}
