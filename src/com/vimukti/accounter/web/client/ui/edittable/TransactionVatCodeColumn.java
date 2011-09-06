package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TransactionVatCodeColumn extends
		ComboColumn<ClientTransactionItem, ClientTAXCode> {
	private AbstractDropDownTable<ClientTAXCode> taxCodeTable = new TaxCodeTable();
	private ClientCompany company;

	public TransactionVatCodeColumn() {
		company = Accounter.getCompany();
	}

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
		return Accounter.constants().vatCode();
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientTAXCode newValue) {
		row.setTaxCode(newValue.getID());
	}
}
