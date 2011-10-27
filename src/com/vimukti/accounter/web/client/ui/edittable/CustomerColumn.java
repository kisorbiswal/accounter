package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;

public class CustomerColumn extends
		ComboColumn<ClientTransactionItem, ClientCustomer> {

	CustomerDropDownTable customers = new CustomerDropDownTable(
			getCustomersFilter());

	private ListFilter<ClientCustomer> getCustomersFilter() {
		return new ListFilter<ClientCustomer>() {

			@Override
			public boolean filter(ClientCustomer e) {
				return e.isActive();
			}
		};
	}

	@Override
	protected ClientCustomer getValue(ClientTransactionItem row) {
		return Accounter.getCompany().getCustomer(row.getCustomer());
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientCustomer newValue) {
		row.setCustomer(newValue.getID());
	}

	@Override
	public AbstractDropDownTable<ClientCustomer> getDisplayTable(
			ClientTransactionItem row) {
		return customers;
	}

	@Override
	public int getWidth() {
		return 130;
	}

	@Override
	protected String getColumnName() {
		return Accounter.messages().Customer(Global.get().Customer());
	}

}
