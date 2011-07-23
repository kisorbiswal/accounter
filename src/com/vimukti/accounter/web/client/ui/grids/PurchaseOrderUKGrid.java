package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;

public class PurchaseOrderUKGrid extends VendorTransactionUKGrid {

	public PurchaseOrderUKGrid() {
		super(true);
		isPurchseOrderTransaction = true;
	}

	public PurchaseOrderUKGrid(boolean isAddNewRequired) {
		super(isAddNewRequired);
		this.isAddNewRequired = isAddNewRequired;

	}

	@Override
	protected String[] getColumns() {
		return new String[] {
				"",
				Accounter.getVendorsMessages().name(),
				Accounter.getCustomersMessages().description(),
				Accounter.getCustomersMessages().quantity(),
				Accounter.getVendorsMessages().unitPrice(),
				// "On Hand",
				Accounter.getVendorsMessages().total(),
				Accounter.getVendorsMessages().billsReceived(),
				Accounter.getVATMessages().newVATCode(),
				Accounter.getVATMessages().VAT(), " " };
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getCellWidth(int index) {
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
		else if (index == 7 || index == 8)
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
			// case 5:
			// return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 8:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_IMAGE;

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
				return (item.getQuantity() != 0 || item.getLineTotal() == 0) ? item
						.getQuantity()
						: "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return DataUtils.getAmountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? DataUtils
						.getAmountAsString(item.getUnitPrice())
						: "";
			}
			// case 5:
			// return item.getBackOrder() + "";
		case 5:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 6:
			return item.getInvoiced() + "";
		case 7:
			return getTAXCodeName(item.getTaxCode());
		case 8:
			return DataUtils.getAmountAsString(item.getVATfraction());
		case 9:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		switch (colIndex) {
		case 1:
			return super.getCustomCombo(obj, colIndex);
		case 7:
			return super.getCustomCombo(obj, colIndex);
		default:
			break;
		}

		return null;

	}

	@Override
	public void editComplete(ClientTransactionItem item, Object value, int col) {
		switch (col) {
		case 5:

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
			break;
		// case 6:
		// String invoice = value.toString() != null
		// || value.toString().length() != 0 ? value.toString() : "0";
		// int i = 0;
		// try {
		// i = Integer.parseInt(invoice);
		// } catch (Exception e) {
		// Accounter.showError(AccounterErrorType.INVALIDENTRY);
		// }
		// item.setQuantity(i);
		//
		// return;
		}

		super.editComplete(item, value, col);
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {

		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_COMMENT:
			switch (col) {
			case 2:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			// case 4:
			// return false;
			case 6:
				return false;
			case 5:
				return false;
			case 8:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SALESTAX:
			switch (col) {
			case 3:
				return false;
			case 4:
				return true;
			case 5:
				return false;
			case 6:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
			case 6:
				return false;
			case 8:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SERVICE:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
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
}
