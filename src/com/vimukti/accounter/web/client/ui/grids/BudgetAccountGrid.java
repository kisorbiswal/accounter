package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.company.AddBudgetAmountDialogue;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class BudgetAccountGrid extends BaseListGrid<ClientBudgetItem> {

	AccounterConstants companyConstants;
	ClientCurrency currency = getCompany().getPreferences()
			.getPrimaryCurrency();
	List<ClientBudgetItem> budgetItem = new ArrayList<ClientBudgetItem>();

	public BudgetAccountGrid() {
		super(false);
	}

	@Override
	protected int getColumnType(int index) {

		switch (index) {
		case 14:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		}
	}

	@Override
	protected Object getColumnValue(ClientBudgetItem budget, int index) {

		switch (index) {
		case 0:
			return budget.getAccountsName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(budget.getJanuaryAmount(), currency);
		case 2:
			return DataUtils.amountAsStringWithCurrency(budget.getFebruaryAmount(), currency);
		case 3:
			return DataUtils.amountAsStringWithCurrency(budget.getMarchAmount(), currency);
		case 4:
			return DataUtils.amountAsStringWithCurrency(budget.getAprilAmount(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(budget.getMayAmount(), currency);
		case 6:
			return DataUtils.amountAsStringWithCurrency(budget.getJuneAmount(), currency);
		case 7:
			return DataUtils.amountAsStringWithCurrency(budget.getJulyAmount(), currency);
		case 8:
			return DataUtils.amountAsStringWithCurrency(budget.getAugustAmount(), currency);
		case 9:
			return DataUtils.amountAsStringWithCurrency(budget.getSpetemberAmount(), currency);
		case 10:
			return DataUtils.amountAsStringWithCurrency(budget.getOctoberAmount(), currency);
		case 11:
			return DataUtils.amountAsStringWithCurrency(budget.getNovemberAmount(), currency);
		case 12:
			return DataUtils.amountAsStringWithCurrency(budget.getDecemberAmount(), currency);
		case 13:
			return DataUtils.amountAsStringWithCurrency(budget.getTotalAmount(), currency);
		case 14:
			return Accounter.getFinanceMenuImages().accounterRegisterIcon();
		default:
			break;
		}

		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {

		case 0:
			return 120;
		case 1:
			return 60;
		case 2:
			return 60;
		case 3:
			return 60;
		case 4:
			return 60;
		case 5:
			return 60;
		case 6:
			return 60;
		case 7:
			return 60;
		case 8:
			return 60;
		case 9:
			return 60;
		case 10:
			return 60;
		case 11:
			return 60;
		case 12:
			return 60;
		case 13:
			return 60;
		case 14:
			return 25;

		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[15];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Global.get().Account();
				break;
			case 1:
				colArray[index] = Accounter.constants().jan();
				break;
			case 2:
				colArray[index] = Accounter.constants().feb();
				break;
			case 3:
				colArray[index] = Accounter.constants().mar();
				break;
			case 4:
				colArray[index] = Accounter.constants().apr();
				break;
			case 5:
				colArray[index] = Accounter.constants().may();
				break;
			case 6:
				colArray[index] = Accounter.constants().jun();
				break;
			case 7:
				colArray[index] = Accounter.constants().jul();
				break;
			case 8:
				colArray[index] = Accounter.constants().aug();
				break;
			case 9:
				colArray[index] = Accounter.constants().sept();
				break;
			case 10:
				colArray[index] = Accounter.constants().oct();
				break;
			case 11:
				colArray[index] = Accounter.constants().nov();
				break;
			case 12:
				colArray[index] = Accounter.constants().dec();
				break;
			case 13:
				colArray[index] = Accounter.constants().total();
				break;
			case 14:
				colArray[index] = Accounter.constants().add();
				break;
			default:
				break;
			}
		}
		return colArray;
	}

	public void addData(ClientBudgetItem obj, ClientAccount account) {
		budgetItem.add(obj);
		obj.setAccountsName(account.getName());
		obj.setAccount(account);
		super.addData(obj);

	}

	@Override
	protected int[] setColTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void executeDelete(ClientBudgetItem object) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDoubleClick(final ClientBudgetItem obj) {
		HashMap<String, String> map = new HashMap<String, String>();
		String budgetTitle = "Add Budget for " + obj.getAccountsName();
		AddBudgetAmountDialogue assignAccountsTo1099Dialog = new AddBudgetAmountDialogue(
				budgetTitle, "", map, obj);
		assignAccountsTo1099Dialog
				.setCallback(new ActionCallback<HashMap<String, String>>() {

					@Override
					public void actionResult(HashMap<String, String> result) {
						refreshView(result, obj);

					}
				});
		assignAccountsTo1099Dialog.show();

	}

	private void refreshView(HashMap<String, String> result,
			ClientBudgetItem obj) {

		obj.setJanuaryAmount(Double.parseDouble(result.get("jan")));
		obj.setFebruaryAmount(Double.parseDouble(result.get("feb")));
		obj.setMarchAmount(Double.parseDouble(result.get("mar")));
		obj.setAprilAmount(Double.parseDouble(result.get("apr")));
		obj.setMayAmount(Double.parseDouble(result.get("may")));
		obj.setJuneAmount(Double.parseDouble(result.get("jun")));
		obj.setJulyAmount(Double.parseDouble(result.get("jul")));
		obj.setAugustAmount(Double.parseDouble(result.get("aug")));
		obj.setOctoberAmount(Double.parseDouble(result.get("oct")));
		obj.setNovemberAmount(Double.parseDouble(result.get("nov")));
		obj.setSeptemberAmount(Double.parseDouble(result.get("sept")));
		obj.setDecemberAmount(Double.parseDouble(result.get("dec")));

		Double total;
		total = Double.parseDouble(result.get("jan"))
				+ Double.parseDouble(result.get("feb"))
				+ Double.parseDouble(result.get("mar"))
				+ Double.parseDouble(result.get("apr"))
				+ Double.parseDouble(result.get("may"))
				+ Double.parseDouble(result.get("jun"))
				+ Double.parseDouble(result.get("jul"))
				+ Double.parseDouble(result.get("aug"))
				+ Double.parseDouble(result.get("oct"))
				+ Double.parseDouble(result.get("nov"))
				+ Double.parseDouble(result.get("sept"))
				+ Double.parseDouble(result.get("dec"));

		obj.setTotalAmount(total);

		refreshAllRecords();

	}
}
