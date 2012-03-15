package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.DepositsTransfersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class DepositsTransfersListGrid extends
		BaseListGrid<DepositsTransfersList> {

	public DepositsTransfersListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("DepositsTransfersListGrid");
	}

	public DepositsTransfersListGrid(boolean isMultiSelectionEnable,
			int transactionType) {
		super(isMultiSelectionEnable, transactionType);
		this.getElement().setId("DepositsTransfersListGrid");
	}

	@Override
	protected int[] setColTypes() {
		if (type == 0) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE };
		}
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 9)
			return 40;
		else if (index == 0)
			return 65;
		else if (index == 1)
			return 80;
		else if (index == 2)
			return 50;
		else if (index == 4 || index == 3) {
			if (type == 0) {
				return 130;
			} else {
				return 80;
			}
		} else if (index == 5)
			if (type == 0) {
				return 40;
			} else {
				return 80;
			}
		else if (index == 6)
			return 40;

		return -1;
	}

	@Override
	protected void executeDelete(DepositsTransfersList object) {
		deleteTransaction(object);
	}

	@Override
	protected Object getColumnValue(DepositsTransfersList obj, int index) {
		switch (index) {
		case 0:
			return obj.getTransactionDate() != null ? UIUtils
					.getDateByCompanyType(obj.getTransactionDate()) : "";
		case 1:
			return Utility.getTransactionName(obj.getType());
		case 2:
			return obj.getTransactionNumber();
		case 3:
			return obj.getInAccount() != null ? obj.getInAccount() : "";
		case 4:
			if (type == 0) {
				return DataUtils.amountAsStringWithCurrency(obj.getAmount(),
						getCompany().getCurrency(obj.getCurrency()));
			} else {
				return obj.getFromAccount() != null ? obj.getFromAccount() : "";
			}
		case 5:
			if (type == 0) {
				return Accounter.getFinanceImages().delete();

			} else {
				return DataUtils.amountAsStringWithCurrency(obj.getAmount(),
						getCompany().getCurrency(obj.getCurrency()));
			}

		case 6:
			return Accounter.getFinanceImages().delete();

		}
		return null;
	}

	@Override
	protected void onClick(DepositsTransfersList obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getStatus(), obj.getType())) {
			return;
		}
		if (type == 0) {
			if (col == 5) {
				showWarningDialog(obj, col);
			}
		} else {
			if (col == 6) {
				showWarningDialog(obj, col);
			}
		}
		// else if (col == 9)
		// showWarningDialog(obj, col);

	}

	private void showWarningDialog(final DepositsTransfersList obj,
			final int col) {
		String msg = null;
		if (type == 0) {
			if (col == 5) {
				msg = messages.doyouwanttoDeletetheTransaction();
			}
		} else {
			if (col == 6) {
				msg = messages.doyouwanttoDeletetheTransaction();
			}
		}

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
						if (type == 0) {
							if (col == 5) {
								deleteTransaction(obj);
							}
						} else {
							if (col == 6) {
								deleteTransaction(obj);
							}
						}
						return true;
					}

				});
	}

	protected void deleteTransaction(final DepositsTransfersList obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException accounterException) {
				int errorCode = accounterException.getErrorCode();
				String errorString = AccounterExceptions
						.getErrorString(errorCode);
				Accounter.showError(errorString);
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					deleteRecord(obj);
					obj.setStatus(ClientTransaction.STATUS_DELETED);
					obj.setVoided(true);
					updateData(obj);

				}

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.delete(type, obj.getTransactionId(), callback);
	}

	@Override
	public void onDoubleClick(DepositsTransfersList obj) {
		if (isCanOpenTransactionView(obj.getStatus(), obj.getType())) {
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
		}
	}

	@Override
	protected String[] getColumns() {
		if (type == 0) {
			return new String[] { messages.transactionDate(), messages.type(),
					messages.number(), messages.depositTo(), messages.amount(),
					messages.delete() };
		} else {
			return new String[] { messages.transactionDate(), messages.type(),
					messages.number(), messages.transferTo(),
					messages.transferFrom(), messages.amount(),
					messages.delete() };
		}

	}

	@Override
	protected String[] setHeaderStyle() {
		if (type == 0) {
			return new String[] { "transactionDate-value", "type-value",
					"number-value", "depositTo-value", "amount-value",
					"delete-value" };
		} else {
			return new String[] { "transactionDate-value", "type-value",
					"number-value", "transferTo-value", "transferFrom-value",
					"amount-value", "delete-value" };
		}
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type == 0) {
			return new String[] { "transactionDate", "type", "number",
					"depositTo", "amount", "delete" };
		} else {
			return new String[] { "transactionDate", "type", "number",
					"transferTo", "transferFrom", "amount", "delete" };
		}
	}
}
