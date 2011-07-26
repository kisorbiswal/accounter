/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;

/**
 * @author Murali.A
 * 
 */
public class CustomerTransactionUKGrid extends CustomerTransactionGrid {

	/**
	 * 
	 */

	boolean isAddNewRequired = true;

	public CustomerTransactionUKGrid() {
	}

	public CustomerTransactionUKGrid(boolean isAddNewRequired) {
		super(isAddNewRequired);
		this.isAddNewRequired = isAddNewRequired;
	}

	@Override
	protected String[] getColumns() {
		return new String[] {
				"",
				Accounter.getCustomersMessages().wareHouse(),
				Accounter.getCustomersMessages().items(),
				Accounter.getCustomersMessages().availableItems(),
				// FinanceApplication.getCustomersMessages().name(),
				Accounter.getCustomersMessages().description(),
				Accounter.getCustomersMessages().quantity(),
				Accounter.getCustomersMessages().unitPrice(),
				Accounter.getCustomersMessages().discountPerc(),
				Accounter.getCustomersMessages().total(),
				Accounter.getCustomersMessages().VATCode(),
				Accounter.getCustomersMessages().vat(), " " };
	}

	@Override
	public String[] getColumnNamesForPrinting() {
		return new String[] { Accounter.getCustomersMessages().description(),
				Accounter.getCustomersMessages().quantity(),
				Accounter.getCustomersMessages().unitPrice(),
				Accounter.getCustomersMessages().totalPrice(),
				Accounter.getVATMessages().vATRate(),
				Accounter.getVATMessages().vATAmount() };
	}

	@Override
	public String getColumnValueForPrinting(ClientTransactionItem item,
			int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
			if (!Arrays.asList(0, 2, 9).contains(index))
				return "";
		}
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 6, 9).contains(index))
				return "";
		}
		switch (index) {
		case 0:
			return item.getDescription();
			// return super.getNameValue(item);
		case 1:
			return item.getQuantity() + "";
		case 2:
			return DataUtils.getAmountAsString(item.getUnitPrice());
		case 3:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 4:
			return DataUtils.getAmountAsString(UIUtils.getVATItem(
					item.getTaxCode(), true).getTaxRate());
		default:
			return DataUtils.getAmountAsString(getVatTotal());
		}
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 0 || col == 11) {
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		switch (col) {
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 4:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_QUANTITY_POPUP;
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 8:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 9:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 10:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
			if (!Arrays.asList(0, 2, 9).contains(index))
				return "";
		}
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 6, 9).contains(index))
				return "";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		// if (Arrays.asList(3).contains(index))
		// return "";
		// }
		// if (item.getType() == ClientTransactionItem.TYPE_SERVICE) {
		// if (Arrays.asList(3, 4, 5).contains(index))
		// return "";
		// }
		switch (index) {
		case 0:
			return super.getImageByType(item.getType());
		case 1:
			return "";
		case 2:
			return "";
		case 3:
			return "";
			// case 1:
			// return super.getNameValue(item);
		case 4:
			return item.getDescription();
		case 5:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return item.getQuantity();
			else {
				return (item.getQuantity() != null || item.getLineTotal() == 0) ? item
						.getQuantity() : "";
			}
		case 6:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return DataUtils.getAmountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? DataUtils
						.getAmountAsString(item.getUnitPrice()) : "";
			}
		case 7:
			return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
		case 8:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 9:
			return getTAXCodeName(item.getTaxCode());
		case 10:
			return DataUtils.getAmountAsString(item.getVATfraction());
		case 11:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	private String getDecimalsUsingMaxDecimals(int quantity, int maxDecimalPoint) {
		String qty = String.valueOf(quantity);
		// if (maxDecimalPoint != 0) {
		// qty = !qty.contains(".") ? qty + ".0" : qty;
		// } else {
		// return qty;
		// }
		// int max = qty.substring(qty.indexOf(".") + 1).length();
		// if (maxDecimalPoint > max) {
		// for (int i = max; maxDecimalPoint != i; i++) {
		// qty = qty + "0";
		// }
		// }
		return qty;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 9)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 3 || index == 4)
			return 90;
		// else if (index == 2)
		// return 120;
		else if (index == 5)
			return 80;
		else if (index == 7)
			return 70;
		else if (index == 8)
			return 60;
		else if (index == 6)
			return 100;
		return -1;
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;
		if (!Accounter.getCompany().getpreferences().getDoYouPaySalesTax()) {
			if (col == 7 || col == 8)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			case 1:
				return false;
			case 2:
				return false;
			case 3:
				return false;

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
			case 4:
				return false;
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

	public long getTaxCode(ClientTransactionItem item) {
		return item.getTaxCode();
	}
}