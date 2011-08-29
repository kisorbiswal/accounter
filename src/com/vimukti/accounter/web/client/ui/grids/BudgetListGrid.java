package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BudgetList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;

public class BudgetListGrid extends BaseListGrid<BudgetList> {

	public BudgetListGrid() {
		super(false, true);
	}

	@Override
	protected Object getColumnValue(BudgetList budget, int col) {
		switch (col) {
		case 0:
			return budget.getAccountsName();
		case 1:
			return amountAsString(budget.getJanuaryMonthAmount());
		case 2:
			return amountAsString(budget.getFebruaryMonthAmount());
		case 3:
			return amountAsString(budget.getMarchMonthAmount());
		case 4:
			return amountAsString(budget.getAprilMonthAmount());
		case 5:
			return amountAsString(budget.getMayMonthAmount());
		case 6:
			return amountAsString(budget.getJuneMonthAmount());
		case 7:
			return amountAsString(budget.getJulyMonthAmount());
		case 8:
			return amountAsString(budget.getAugustMonthAmount());
		case 9:
			return amountAsString(budget.getSeptemberMonthAmount());
		case 10:
			return amountAsString(budget.getOctoberMonthAmount());
		case 11:
			return amountAsString(budget.getNovemberMonthAmount());
		case 12:
			return amountAsString(budget.getDecemberMonthAmount());
		case 13:
			updateTotal(budget, true);
		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[14];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Global.get().Account();
				break;
			case 1:
				colArray[index] = Accounter.constants().january();
				break;
			case 2:
				colArray[index] = Accounter.constants().february();
				break;
			case 3:
				colArray[index] = Accounter.constants().march();
				break;
			case 4:
				colArray[index] = Accounter.constants().april();
				break;
			case 5:
				colArray[index] = Accounter.constants().may();
				break;
			case 6:
				colArray[index] = Accounter.constants().june();
				break;
			case 7:
				colArray[index] = Accounter.constants().july();
				break;
			case 8:
				colArray[index] = Accounter.constants().august();
				break;
			case 9:
				colArray[index] = Accounter.constants().september();
				break;
			case 10:
				colArray[index] = Accounter.constants().october();
				break;
			case 11:
				colArray[index] = Accounter.constants().november();
				break;
			case 12:
				colArray[index] = Accounter.constants().december();
				break;
			case 13:
				colArray[index] = Accounter.constants().total();
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	@Override
	protected void onClick(BudgetList obj, int row, int col) {
	}

	@Override
	protected void onValueChange(BudgetList obj, int col, Object value) {

	}

	protected void executeDelete(final PayeeList recordToBeDeleted) {
		AccounterAsyncCallback<ClientCustomer> callback = new AccounterAsyncCallback<ClientCustomer>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientCustomer result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.CUSTOMER,
				recordToBeDeleted.id, callback);

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, };
	}

	protected void updateTotal(BudgetList customer, boolean add) {

		// if (add) {
		// if (customer.isActive())
		// total += customer.getBalance();
		// else
		// total += customer.getBalance();
		// } else
		// total -= customer.getBalance();
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal() {
		this.total = 0.0D;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 135;
		}
		return super.getCellWidth(index);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.CUSTOMER;
	}

	@Override
	public void addData(BudgetList obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterErrors
					.customerInUse());
			return;
		}
		super.deleteFailed(caught);
	}

	@Override
	protected void executeDelete(BudgetList object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(BudgetList obj) {
		// TODO Auto-generated method stub

	}

}
