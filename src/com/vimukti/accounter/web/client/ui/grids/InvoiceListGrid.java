package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class InvoiceListGrid extends BaseListGrid<InvoicesList> {

	public InvoiceListGrid() {
		super(false);
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE,
		// ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	protected Object getColumnValue(InvoicesList invoicesList, int col) {
		switch (col) {
		case 0:
			return false;
		case 1:
			return Utility.getTransactionName((invoicesList.getType()));
		case 2:
			return UIUtils.getDateByCompanyType(invoicesList.getDate());
		case 3:
			return invoicesList.getNumber();
		case 4:
			return invoicesList.getCustomerName();
		case 5:
			if (invoicesList.getDueDate() != null)
				return UIUtils.getDateByCompanyType(invoicesList.getDueDate());
			else
				return "";

		case 6:
			return invoicesList.getNetAmount();
		case 7:
			return amountAsString(invoicesList.getTotalPrice());
		case 8:
			return amountAsString(invoicesList.getBalance());
		case 9:

			if (!invoicesList.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";

			// case 10:
			// if (invoicesList.getStatus() == ClientTransaction.STATUS_DELETED)
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();

		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		customerConstants = Accounter.constants();
		return new String[] { " ", customerConstants.type(),
				customerConstants.date(), customerConstants.no(),
				Global.get().messages().customerName(Global.get().Customer()),
				customerConstants.dueDate(), customerConstants.netPrice(),
				customerConstants.totalPrice(), customerConstants.balance(),
				customerConstants.voided()
		// , ""
		};
	}

	@Override
	public void onDoubleClick(InvoicesList obj) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
	}

	protected void onClick(InvoicesList obj, int row, int col) {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		if (col == 9 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 9) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

		if (col == 0) {

			boolean isSelected = ((CheckBox) this.getWidget(row, col))
					.getValue();

			obj.setPrint(isSelected);

			// updateData(obj);

		}
	}

	private void showWarningDialog(final InvoicesList obj, final int col) {
		String msg = null;
		if (!obj.isVoided() && col == 9) {
			msg = Accounter.constants().doyouwanttoVoidtheTransaction();
		}
		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";

		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (col == 9)
							voidTransaction(obj);
						else if (col == 10)
							deleteTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final InvoicesList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final InvoicesList obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {

					if (viewType.equalsIgnoreCase("Open")
							|| viewType.equalsIgnoreCase("Over-Due"))
						deleteRecord(obj);
					obj.setStatus(ClientTransaction.STATUS_DELETED);
					isDeleted = true;
					obj.setVoided(true);
					updateData(obj);

				}

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.getTransactionId(), callback);
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 20;
		case 1:
			return 60;
		case 2:
			return 90;
		case 3:
			return 60;
		case 4:
			return -1;
		case 5:
			return 90;
		case 6:
			return 90;
		case 7:
			return 90;
		case 8:
			return 90;
		case 9:
			return 40;
		default:
			return -1;
		}
	}

	@Override
	protected void executeDelete(InvoicesList object) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(InvoicesList obj1, InvoicesList obj2, int index) {
		switch (index) {

		case 0:
			String type1 = Utility.getTransactionName((obj1.getType()));
			String type2 = Utility.getTransactionName((obj2.getType()));
			return type1.toLowerCase().compareTo(type2.toLowerCase());
		case 1:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;
		case 2:
			String num1 = obj1.getNumber();
			String num2 = obj2.getNumber();
			return num1.compareTo(num2);

		case 3:
			String name1 = obj1.getCustomerName().toLowerCase();
			String name2 = obj2.getCustomerName().toLowerCase();
			return name1.compareTo(name2);

		case 4:
			ClientFinanceDate dueDate1 = obj1.getDueDate();
			ClientFinanceDate dueDate2 = obj2.getDueDate();
			return dueDate1.compareTo(dueDate2);

		case 5:
			Double netPrice1 = obj1.getNetAmount();
			Double netPrice2 = obj2.getNetAmount();
			return netPrice1.compareTo(netPrice2);

		case 6:
			Double price1 = obj1.getTotalPrice();
			Double price2 = obj2.getTotalPrice();
			return price1.compareTo(price2);

		case 7:
			Double bal1 = obj1.getBalance();
			Double bal2 = obj2.getBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	private long getTransactionID(InvoicesList obj) {
		return obj.getTransactionId();
	}

	public boolean isVoided(InvoicesList obj) {
		return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(InvoicesList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	public void editComplete(InvoicesList item, Object value, int col) {

		if (col == 0) {
		}
		// TODO Auto-generated method stub
		super.editComplete(item, value, col);
	}
}
