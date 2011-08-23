package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.VList;

public class TestTable extends CellTable<ClientAccount> {
	VList<ClientAccount> accounts;

	public TestTable() {
		accounts = getAccounts();
		ListDataProvider<ClientAccount> list = new VListDataProvider<ClientAccount>(
				accounts);
		list.addDataDisplay(this);

		initColumns();
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	}

	private void initColumns() {
		Column<ClientAccount, String> column = new Column<ClientAccount, String>(
				new AccountComboCell(accounts, true)) {

			@Override
			public String getValue(ClientAccount object) {
				return object.getName();
			}
		};
		this.addColumn(column);
	}

	private VList<ClientAccount> getAccounts() {

		VList<ClientAccount> list = new VList<ClientAccount>();

		ClientAccount acc1 = new ClientAccount();
		acc1.setNumber("1000");
		acc1.setName("IncomeAccount");
		acc1.setType(14);
		list.add(acc1);

		ClientAccount acc2 = new ClientAccount();
		acc2.setNumber("2000");
		acc2.setName("CODSAccount");
		acc2.setType(15);
		list.add(acc2);

		ClientAccount acc3 = new ClientAccount();
		acc3.setNumber("3000");
		acc3.setName("ExpenseAccount");
		acc3.setType(16);
		list.add(acc3);

		return list;
	}

}