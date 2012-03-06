package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class TransactionClassColumn extends
		ComboColumn<ClientTransactionItem, ClientAccounterClass> {
	private final AbstractDropDownTable<ClientAccounterClass> classTable = new ClassTable();
	private ClientCompany company;

	public TransactionClassColumn() {
		company = Accounter.getCompany();
	}

	@Override
	protected ClientAccounterClass getValue(ClientTransactionItem row) {
		return company.getAccounterClass(row.getAccounterClass());
	}

	@Override
	protected void setValue(ClientTransactionItem row,
			ClientAccounterClass newValue) {
		if (newValue != null)
			row.setAccounterClass(newValue.getID());
		getTable().update(row);
	}

	@Override
	public AbstractDropDownTable<ClientAccounterClass> getDisplayTable(
			ClientTransactionItem row) {
		return classTable;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected String getColumnName() {
		return "Class";
	}
}
