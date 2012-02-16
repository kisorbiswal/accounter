package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientInvoice;

/**
 * @author Prasanna Kumar G
 * 
 */
public class InvoiceImporter extends AbstractImporter<ClientInvoice> {

	@Override
	protected List<Field<?>> getAllFields() {
		List<Field<?>> fields = new ArrayList<Field<?>>();

		fields.add(new Field<String>("customer", true));
		fields.add(new Field<String>("number"));

		return fields;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClientInvoice getData() {
		ClientInvoice invoice = new ClientInvoice();
		Field<String> fieldByName = (Field<String>) getFieldByName("customer");
		String value = fieldByName.getValue();
		invoice.setCustomer(getCustomerByName(value));
		return invoice;
	}

}
