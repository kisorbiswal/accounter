package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class ReceivedPaymentListGrid extends BaseListGrid<ReceivePaymentsList> {

	public ReceivedPaymentListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(ReceivePaymentsList receivePayment, int col) {
		switch (col) {
		case 0:
			return Utility.getTransactionName(receivePayment.getType());
		case 1:
			return UIUtils
					.getDateByCompanyType(receivePayment.getPaymentDate());
		case 2:
			return receivePayment.getNumber();
		case 3:
			return receivePayment.getCustomerName();
		case 4:
			return receivePayment.getPaymentMethodName();
		case 5:
			return amountAsString(receivePayment.getAmountPaid());
		case 6:
			if (!receivePayment.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 7:
			// if (receivePayment.isDeleted())
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
		return new String[] { customerConstants.type(),
				customerConstants.paymentDate(), customerConstants.no(),
				Accounter.messages().customerName(Global.get().Customer()),
				customerConstants.paymentMethod(),
				customerConstants.amountPaid(), customerConstants.voided()
		// , ""
		};
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE,
		// ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	public void onDoubleClick(ReceivePaymentsList receivePaymentList) {
		ReportsRPC.openTransactionView(receivePaymentList.getType(),
				receivePaymentList.getTransactionId());
	}

	protected void onClick(ReceivePaymentsList obj, int row, int col) {
		if (col == 6 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 7) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private void showWarningDialog(final ReceivePaymentsList obj, final int col) {
		String msg = null;
		if (col == 6 && !obj.isVoided()) {
			msg = Accounter.constants().doyouwanttoVoidtheTransaction();
		}
		// else if (col == 7) {
		// msg = "Do you want to Delete the Transaction";
		//
		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {

						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {

						if (col == 6)
							voidTransaction(obj);
						// else if (col == 7)
						// deleteTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final ReceivePaymentsList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()), obj
				.getTransactionId());
	}

	protected void deleteTransaction(final ReceivePaymentsList obj) {
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
	protected int getCellWidth(int index) {
		if (index == 4)
			return 150;
		else if (index == 0)
			return 150;
		else if (index == 1)
			return 100;

		else if (index == 2)
			return 50;

		else if (index == 5)
			return 100;

		else if (index == 6)
			return 80;

		return -1;
	}

	@Override
	protected void executeDelete(ReceivePaymentsList object) {
		// its not using any where return null;

	}

	@Override
	protected int sort(ReceivePaymentsList obj1, ReceivePaymentsList obj2,
			int index) {
		switch (index) {
		case 0:
			String type1 = Utility.getTransactionName(obj1.getType());
			String type2 = Utility.getTransactionName(obj2.getType());
			return type1.toLowerCase().compareTo(type2.toLowerCase());
		case 1:
			ClientFinanceDate paymentDate1 = obj1.getPaymentDate();
			ClientFinanceDate paymentDate2 = obj2.getPaymentDate();
			return paymentDate1.compareTo(paymentDate2);

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
			String name1 = obj1.getCustomerName();
			String name2 = obj2.getCustomerName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());

		case 4:
			String paymentMethod1 = obj1.getPaymentMethodName().toLowerCase();
			String paymentMethod2 = obj2.getPaymentMethodName().toLowerCase();
			return paymentMethod1.compareTo(paymentMethod2);

		case 5:
			Double amt1 = obj1.getAmountPaid();
			Double amt2 = obj2.getAmountPaid();
			return amt1.compareTo(amt2);

		}
		return 0;
	}


	public AccounterCoreType getType() {
		return null;
	}

	private long getTransactionID(ReceivePaymentsList obj) {
		return obj.getTransactionId();
	}

	public boolean isVoided(ReceivePaymentsList obj) {
		return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(ReceivePaymentsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}
}
