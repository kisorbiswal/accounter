package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;

public class BudgetListGrid extends BaseListGrid<ClientBudgetItem> {

	public BudgetListGrid() {
		super(false, true);
	}

	// public void initBudgetItems(ClientBudgetItem budgetItems) {
	//
	// ClientBudgetItem budgetItem = new ClientBudgetItem();
	//
	// // this.setRecords(budgetItem);
	// }

	public void initBudgetItems(List<ClientBudgetItem> allBudgetItems) {
		this.setRecords(allBudgetItems);

	}

	@Override
	protected Object getColumnValue(ClientBudgetItem budget, int col) {
		switch (col) {
		case 0:
			return budget.getAccountsName();
			// case 1:
			// return amountAsString(budget.getJanuaryAmount());
			// case 2:
			// return amountAsString(budget.getFebruaryAmount());
			// case 3:
			// return amountAsString(budget.getMarchAmount());
			// case 4:
			// return amountAsString(budget.getAprilAmount());
			// case 5:
			// return amountAsString(budget.getMayAmount());
			// case 6:
			// return amountAsString(budget.getJuneAmount());
			// case 7:
			// return amountAsString(budget.getJulyAmount());
			// case 8:
			// return amountAsString(budget.getAugustAmount());
			// case 9:
			// return amountAsString(budget.getSpetemberAmount());
			// case 10:
			// return amountAsString(budget.getOctoberAmount());
			// case 11:
			// return amountAsString(budget.getNovemberAmount());
			// case 12:
			// return amountAsString(budget.getDecemberAmount());
		case 1:
			return amountAsString(budget.getTotalAmount());
		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[2];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Global.get().Account();
				break;
			// case 1:
			// colArray[index] = Accounter.constants().jan();
			// break;
			// case 2:
			// colArray[index] = Accounter.constants().feb();
			// break;
			// case 3:
			// colArray[index] = Accounter.constants().mar();
			// break;
			// case 4:
			// colArray[index] = Accounter.constants().apr();
			// break;
			// case 5:
			// colArray[index] = Accounter.constants().may();
			// break;
			// case 6:
			// colArray[index] = Accounter.constants().jun();
			// break;
			// case 7:
			// colArray[index] = Accounter.constants().jul();
			// break;
			// case 8:
			// colArray[index] = Accounter.constants().aug();
			// break;
			// case 9:
			// colArray[index] = Accounter.constants().sept();
			// break;
			// case 10:
			// colArray[index] = Accounter.constants().oct();
			// break;
			// case 11:
			// colArray[index] = Accounter.constants().nov();
			// break;
			// case 12:
			// colArray[index] = Accounter.constants().dec();
			// break;
			case 1:
				colArray[index] = Accounter.constants().total();
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	@Override
	protected void onClick(ClientBudgetItem obj, int row, int col) {
	}

	@Override
	protected void onValueChange(ClientBudgetItem obj, int col, Object value) {

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
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	protected void updateTotal(ClientBudgetItem customer, boolean add) {

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
			return 300;
			// } else if (index == 1) {
			// return 65;
			// } else if (index == 2) {
			// return 65;
			// } else if (index == 3) {
			// return 65;
			// } else if (index == 4) {
			// return 65;
			// } else if (index == 5) {
			// return 65;
			// } else if (index == 6) {
			// return 65;
			// } else if (index == 7) {
			// return 65;
			// } else if (index == 8) {
			// return 65;
			// } else if (index == 9) {
			// return 65;
			// } else if (index == 10) {
			// return 65;
			// } else if (index == 11) {
			// return 65;
		} else {
			return 65;
		}
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.BUDGET;
	}

	@Override
	public void addData(ClientBudgetItem obj) {
		super.addData(obj);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
		super.deleteFailed(caught);
	}

	@Override
	protected void executeDelete(ClientBudgetItem object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientBudgetItem obj) {
		// TODO Auto-generated method stub

	}

}
