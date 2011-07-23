package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;

import com.google.gwt.dom.client.Style.Unit;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class SalesOrderUKGrid extends CustomerTransactionUKGrid {

	public SalesOrderUKGrid() {
		super(true);
	}

	@Override
	protected String[] getColumns() {

		return new String[] {
				"",
				Accounter.getCustomersMessages().name(),
				Accounter.getCustomersMessages().description(),
				Accounter.getCustomersMessages().quantity(),
				Accounter.getCustomersMessages().unitPrice(),
				Accounter.getCustomersMessages().discountPerc(),
				// "Back Order",
				Accounter.getCustomersMessages().total(),
				Accounter.getVATMessages().newVATCode(),
				Accounter.getVATMessages().VAT(),
				Accounter.getCustomersMessages().invoiced(), " " };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 10)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 7 || index == 8 || index == 2)
			return 70;
		else if (index == 3 || index == 4 || index == 9)
			return 90;
		else if (index == 5)
			return 80;
		else if (index == 6)
			return 100;
		return -1;

	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (!Accounter.getCompany().getpreferences()
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
			// case 7:
			// return false;
			// case 8:
			// return false;
			case 8:
				return false;
			case 9:
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
				// case 8:
				// return false;
			case 8:
				return false;
			case 9:
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
			case 9:
				return false;
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
			case 9:
				return false;
			default:
				return true;

			}
		}

		return true;
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 0 || col == 10) {
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
		case 6:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 7:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 8:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 9:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
			// case 10:
			// return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {
		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {

			if (!Arrays.asList(0, 2, 10).contains(index))
				return "";

		}
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		//
		// if (Arrays.asList(3).contains(index))
		// return "";
		//
		// }
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 2, 6, 7, 10).contains(index))
				return "";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_SERVICE) {
		// if (!Arrays.asList(0, 1, 2, 6, 7, 8, 9, 10).contains(index))
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
			return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
			// case 6:
			// return DataUtils.getAmountAsString(item.getBackOrder());
		case 6:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 7:
			return getTAXCodeName(item.getTaxCode());
		case 8:
			return DataUtils.getAmountAsString(item.getVATfraction());
		case 9:
			return DataUtils.getAmountAsString(item.getInvoiced());
		case 10:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		CustomCombo<E> combo = null;
		switch (colIndex) {
		case 1:
			return super.getCustomCombo(obj, colIndex);
		case 7:
			combo = (CustomCombo<E>) taxCodeCombo;
			if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				combo.downarrowpanel.getElement().getStyle().setMarginLeft(-7,
						Unit.PX);
			} else {
				
			}
			break;
		default:
			break;
		}

		return combo;

	}

	@Override
	public void editComplete(final ClientTransactionItem item,
			final Object value, final int col) {
		super.editComplete(item, value, col);

		// if (col == 3) {
		//
		// try {
		// double changedqty = DataUtils.getAmountStringAsDouble(value
		// .toString());
		// if (changedqty < (item.getInvoiced() / item.getUnitPrice())) {
		// Accounter.showWarning("error", AccounterType.WARNING,
		// new ErrorDialogHandler() {
		//
		// @Override
		// public boolean onYesClick()
		// throws InvalidEntryException {
		// // SalesOrderUKGrid.super.editComplete(item,
		// // value, col);
		// return true;
		// }
		//
		// @Override
		// public boolean onNoClick()
		// throws InvalidEntryException {
		// // TODO Auto-generated method stub
		// return true;
		// }
		//
		// @Override
		// public boolean onCancelClick()
		// throws InvalidEntryException {
		// // TODO Auto-generated method stub
		// return true;
		// }
		// });
		// }
		// // else {
		// // super.editComplete(item, value, col);
		// // }
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// else
		// super.editComplete(item, value, col);

	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		super.validateGrid();
		// for (ClientTransactionItem item : this.getRecords()) {
		// if (item.getType() != ClientTransactionItem.TYPE_COMMENT) {
		// try {
		// // double changedqty =
		// // DataUtils.getAmountStringAsDouble(value
		// // .toString());
		// if (item.getQuantity() < (item.getInvoiced() / item
		// .getUnitPrice())) {
		// Accounter.showWarning("error", AccounterType.WARNING,
		// new ErrorDialogHandler() {
		//
		// @Override
		// public boolean onYesClick()
		// throws InvalidEntryException {
		// // SalesOrderUKGrid.super.editComplete(item,
		// // value, col);
		// return true;
		// }
		//
		// @Override
		// public boolean onNoClick()
		// throws InvalidEntryException {
		// // TODO Auto-generated method stub
		// return true;
		// }
		//
		// @Override
		// public boolean onCancelClick()
		// throws InvalidEntryException {
		// // TODO Auto-generated method stub
		// return true;
		// }
		// });
		// }
		// // else {
		// // super.editComplete(item, value, col);
		// // }
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

		return true;
	}
}
