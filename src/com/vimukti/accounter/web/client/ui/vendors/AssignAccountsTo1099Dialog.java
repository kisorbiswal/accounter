package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.columns.AccountComboCell;

public class AssignAccountsTo1099Dialog extends BaseDialog {

	public AssignAccountsTo1099Dialog(String title, String desc) {
		super(title, desc);

		createControls();
	}

	private void createControls() {
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidth("570px");
		Label label = new Label(
				"Certain payments made to vendors must be assigned to IRS-defined boxes. To do so, assign to each box the accounts in which you track these payments.");
		label.addStyleName("centre");

		CellTable<Object> cellTable = new CellTable<Object>();

		CheckboxCell checkBox = new CheckboxCell();
		Column<Object, Boolean> column = new Column<Object, Boolean>(checkBox) {

			@Override
			public Boolean getValue(Object object) {
				// TODO Auto-generated method stub
				return false;
			}
		};

		TextColumn<Object> textColumn = new TextColumn<Object>() {

			@Override
			public String getValue(Object object) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		AccountComboCell salesAccountsCombo = new AccountComboCell(
				getAccounts(), false);
		Column<Object, String> accountsCell = new Column<Object, String>(
				salesAccountsCombo) {

			@Override
			public String getValue(Object object) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		ArrayList<Object> arrayList = new ArrayList<Object>();
		arrayList.add(new Object());

		cellTable.addColumn(column, Accounter.constants().use());
		cellTable.addColumn(textColumn, Accounter.constants()
				.get1099Information());
		cellTable.addColumn(accountsCell,
				Accounter.messages().account(Global.get().Account()));
		cellTable.setRowCount(1);
		cellTable.setRowData(arrayList);

		verticalPanel.add(label);
		verticalPanel.add(cellTable);

		setBodyLayout(verticalPanel);
		center();
	}

	private ArrayList<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> gridAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_BANK
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_EXPENSE
					&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
					&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
					&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
					&& account.getType() != ClientAccount.TYPE_EQUITY)
				gridAccounts.add(account);
		}
		return gridAccounts;
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

}
