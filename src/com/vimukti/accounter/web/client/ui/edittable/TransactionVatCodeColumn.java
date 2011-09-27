package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class TransactionVatCodeColumn extends
		ComboColumn<ClientTransactionItem, ClientTAXCode> {
	private AbstractDropDownTable<ClientTAXCode> taxCodeTable = new TaxCodeTable(
			getTaxCodeFilter(), isSales());
	private ClientCompany company;

	public TransactionVatCodeColumn() {
		company = Accounter.getCompany();
	}

	protected abstract ListFilter<ClientTAXCode> getTaxCodeFilter();

	protected abstract boolean isSales();

	@Override
	protected ClientTAXCode getValue(ClientTransactionItem row) {
		return company.getTAXCode(row.getTaxCode());
	}

	@Override
	public AbstractDropDownTable<ClientTAXCode> getDisplayTable(
			ClientTransactionItem row) {
		return taxCodeTable;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().taxCode();
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientTAXCode newValue) {
		row.setTaxCode(newValue.getID());
		getTable().update(row);
	}
}
