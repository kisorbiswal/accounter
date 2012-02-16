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
	public Type getType() {
		return Type.INVOICE;
	}

	@Override
	protected List<Field<?>> getFields() {
		List<Field<?>> fields = new ArrayList<Field<?>>();

		fields.add(new Field<String>("customer", true));
		fields.add(new Field<String>("number"));

		return fields;
	}

}
