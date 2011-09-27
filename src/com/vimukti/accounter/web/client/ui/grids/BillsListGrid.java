package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class BillsListGrid extends BaseListGrid<BillsList> {

	public BillsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(BillsList bills, int col) {
		switch (col) {
		case 0:
			return Utility.getTransactionName((bills.getType()));
		case 1:
			return UIUtils.getDateByCompanyType(bills.getDate());
		case 2:
			return bills.getNumber();
		case 3:
			return bills.getVendorName();
		case 4:
			return amountAsString(bills.getOriginalAmount());
		case 5:
			return amountAsString(bills.getBalance());
		case 6:
			if (!bills.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 7:
			// if (bills.isDeleted())
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
	protected int getCellWidth(int index) {
		if (index == 0)
			return 135;
		if (index == 1)
			return 100;
		if (index == 2)
			return 70;
		if (index == 4)
			return 100;
		if (index == 5)
			return 70;
		if (index == 6)
			return 90;
		else if (index == 8)
			return 30;
		return -1;
	};

	@Override
	protected String[] getColumns() {
		vendorConstants = Accounter.constants();
		return new String[] { vendorConstants.type(), vendorConstants.date(),
				vendorConstants.no(),
				Global.get().messages().vendorName(Global.get().Vendor()),
				vendorConstants.originalAmount(), vendorConstants.balance(),
				vendorConstants.Voided()
		// , ""
		};
	}

	@Override
	public void onDoubleClick(BillsList bills) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(bills.getType(), bills
					.getTransactionId());

	}

	@Override
	protected void onValueChange(BillsList obj, int col, Object value) {

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	protected void onClick(BillsList obj, int row, int col) {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		if (col == 6 && !obj.isVoided()) {

			if (obj.getType() != ClientTransaction.TYPE_EMPLOYEE_EXPENSE
					|| (obj.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE && obj
							.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)) {
				showWarningDialog(obj, this.getAccounterCoreType(obj), this
						.getTransactionID(obj), col);
			} else {
				Accounter.showError(Accounter.constants()
						.expensecantbevoiditisApproved());
			}
		}
		// else if (col == 7) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	protected void deleteTransaction(final BillsList obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					if (!viewType.equalsIgnoreCase("All"))
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
	protected void executeDelete(BillsList object) {

	}

	@Override
	protected int sort(BillsList obj1, BillsList obj2, int index) {

		switch (index) {
		case 0:
			String type1 = Utility.getTransactionName((obj1.getType()))
					.toLowerCase();
			String type2 = Utility.getTransactionName((obj2.getType()))
					.toLowerCase();
			return type1.compareTo(type2);

		case 1:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 2:
			int num1 = UIUtils.isInteger(obj1.getNumber()) ? Integer
					.parseInt(obj1.getNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getNumber()) ? Integer
					.parseInt(obj2.getNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getNumber().compareTo(obj2.getNumber());
		case 3:
			return obj1.getVendorName().toLowerCase().compareTo(
					obj2.getVendorName().toLowerCase());

		case 4:
			return obj1.getOriginalAmount().compareTo(obj2.getOriginalAmount());

		case 5:
			Double balance1 = obj1.getBalance();
			Double balance2 = obj2.getBalance();
			return UIUtils.compareTo(balance1, balance2);

		default:
			break;
		}
		return 0;
	}

	private long getTransactionID(BillsList obj) {
		return obj.getTransactionId();
	}

	public boolean isVoided(BillsList obj) {
		return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(BillsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

}
