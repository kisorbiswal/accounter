package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class PaymentsListGrid extends BaseListGrid<PaymentsList> {

	public PaymentsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	protected Object getColumnValue(PaymentsList obj, int col) {
		switch (col) {
		case 0:
			return obj.getPaymentDate() != null ? UIUtils
					.getDateByCompanyType(obj.getPaymentDate()) : "";
		case 1:
			return obj.getPaymentNumber();
		case 2:
			return Utility.getStatus(obj.getType(), obj.getStatus());
		case 3:
			return obj.getIssuedDate() != null ? UIUtils
					.getDateByCompanyType(obj.getIssuedDate()) : "";
		case 4:
			return obj.getName() != null ? obj.getName() : "";
		case 5:
			return Utility.getTransactionName(getType(obj));
		case 6:
			return obj.getPaymentMethodName() != null ? obj
					.getPaymentMethodName() : "";
		case 7:
			return amountAsString(obj.getAmountPaid());
		case 8:
			if (!obj.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 9:
			// if (obj.getStatus() == ClientTransaction.STATUS_DELETED)
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();
		}
		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 85;
		else if (index == 1)
			return 80;
		else if (index == 2)
			return 65;
		else if (index == 3)
			return 65;
		else if (index == 7)
			return 105;
		else if (index == 6)
			return 100;
		else if (index == 5)
			return 125;
		else if (index == 8)
			return 40;
		return -1;
	}

	@Override
	protected String[] getColumns() {
		bankingContants = Accounter.constants();
		return new String[] { bankingContants.paymentDate(),
				bankingContants.paymentNo(), bankingContants.status(),
				bankingContants.issueDate(), bankingContants.name(),
				bankingContants.type(), bankingContants.paymentMethod(),
				bankingContants.amountPaid(), bankingContants.Voided()
		// , ""
		};
	}

	@Override
	protected void onClick(PaymentsList obj, int row, int col) {
		if (col == 8 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 9)
		// showWarningDialog(obj, col);

	};

	private void showWarningDialog(final PaymentsList obj, final int col) {
		String msg = null;
		if (col == 8 && !obj.isVoided()) {
			msg = Accounter.constants().doyouwanttoVoidtheTransaction();
		}
		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";

		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						// NOTHING TO DO.
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (col == 8)
							voidTransaction(obj);
						// else if (col == 9)
						// deleteTransaction(obj);
						return true;
					}

				});
	}

	protected void voidTransaction(final PaymentsList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final PaymentsList obj) {
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
					obj.setVoided(true);
					updateData(obj);

				}

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.getTransactionId(), callback);
	}

	@Override
	public void onDoubleClick(PaymentsList payments, int currentRow,
			int currentCol) {
		// ReportsRPC.openTransactionView(payments.getType(), payments
		// .getTransactionId());
		ReportsRPC.openTransactionView(getType(payments),
				payments.getTransactionId());
	}

	@Override
	public void onDoubleClick(PaymentsList obj) {
		// NOTHING TO DO.
	}

	@Override
	protected void executeDelete(PaymentsList object) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(PaymentsList obj1, PaymentsList obj2, int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getPaymentDate();
			ClientFinanceDate date2 = obj2.getPaymentDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);

			// break;
		case 1:
			int num1 = UIUtils.isInteger(obj1.getPaymentNumber()) ? Integer
					.parseInt(obj1.getPaymentNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getPaymentNumber()) ? Integer
					.parseInt(obj2.getPaymentNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getPaymentNumber().compareTo(
						obj2.getPaymentNumber());

		case 2:
			Integer status1 = obj1.getStatus();
			Integer status2 = obj2.getStatus();
			return status1.compareTo(status2);

		case 3:
			ClientFinanceDate issuedate1 = obj1.getIssuedDate();
			ClientFinanceDate issuedate2 = obj2.getPaymentDate();
			if (issuedate1 != null && issuedate2 != null)
				return issuedate1.compareTo(issuedate2);

		case 4:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

		case 5:

			String type1 = Utility.getTransactionName(obj1.getType())
					.toLowerCase();
			String type2 = Utility.getTransactionName(obj2.getType())
					.toLowerCase();
			type1.compareTo(type2);

		case 6:
			String method1 = obj1.getPaymentMethodName() != null ? obj1
					.getPaymentMethodName() : "";
			String method2 = obj2.getPaymentMethodName() != null ? obj2
					.getPaymentMethodName() : "";
			return method1.toLowerCase().compareTo(method2.toLowerCase());

		case 7:
			Double amt1 = obj1.getAmountPaid();
			Double amt2 = obj2.getAmountPaid();
			return amt1.compareTo(amt2);

		default:
			break;
		}

		return 0;
	}


	int getType(PaymentsList paymentsList) {
		if (paymentsList.getType() == 11) {
			return paymentsList.getPayBillType() == ClientPayBill.TYPE_PAYBILL ? ClientTransaction.TYPE_PAY_BILL
					: ClientTransaction.TYPE_VENDOR_PAYMENT;
		}

		return paymentsList.getType();
	}

	public AccounterCoreType getType() {
		return null;
	}

	private long getTransactionID(PaymentsList obj) {
		return obj.getTransactionId();
	}

	public boolean isVoided(PaymentsList obj) {
		return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(PaymentsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

}
