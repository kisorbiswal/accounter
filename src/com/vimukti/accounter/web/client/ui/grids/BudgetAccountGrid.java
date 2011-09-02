package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccountBudget;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;

public class BudgetAccountGrid extends ListGrid<ClientAccountBudget> {

	AccounterConstants companyConstants;

	public BudgetAccountGrid() {
		super(false);

	}

	@Override
	protected int getColumnType(int index) {

		return ListGrid.COLUMN_TYPE_TEXTBOX;
	}

	@Override
	protected Object getColumnValue(ClientAccountBudget budget, int index) {
		switch (index) {
		case 0:
			return budget.getAccountsName();
		case 1:
			return amountAsString(budget.getJanuaryAmount());
		case 2:
			return amountAsString(budget.getFebruaryAmount());
		case 3:
			return amountAsString(budget.getMarchAmount());
		case 4:
			return amountAsString(budget.getAprilAmount());
		case 5:
			return amountAsString(budget.getMayAmount());
		case 6:
			return amountAsString(budget.getJuneAmount());
		case 7:
			return amountAsString(budget.getJulyAmount());
		case 8:
			return amountAsString(budget.getAugustAmount());
		case 9:
			return amountAsString(budget.getSpetemberAmount());
		case 10:
			return amountAsString(budget.getOctoberAmount());
		case 11:
			return amountAsString(budget.getNovemberAmount());
		case 12:
			return amountAsString(budget.getDecemberAmount());
		case 13:
			return amountAsString(budget.getTotalAmount());
		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getSelectValues(ClientAccountBudget obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(ClientAccountBudget obj, int index,
			Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(ClientAccountBudget obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(ClientAccountBudget obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientAccountBudget obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientAccountBudget obj1, ClientAccountBudget obj2,
			int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 50;
		default:
			return 20;
		}
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

	public void addData(ClientAccountBudget obj, ClientAccount account) {
		obj.setAccountsName(account.getName());
		super.addData(obj);

	}
}
