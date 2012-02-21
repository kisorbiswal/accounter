package com.vimukti.accounter.server.imports;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.Field;

/**
 * @author Prasanna Kumar G
 * 
 */
public class InvoiceImporter extends TransactionImporter<ClientInvoice> {

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> fields = super.getAllFields();
		fields.add(new Field<String>(messages.customer(), messages
				.payeeName(Global.get().Customer()), true));
		fields.add(new Field<String>(messages.number(), messages.invoiceNo()));
		fields.add(new Field<Long>(messages.paymentTerm(), messages
				.paymentTerm()));
		fields.add(new Field<Long>(messages.dueDate(), messages.dueDate()));
		fields.add(new Field<String>(messages.orderNo(), messages.orderNo()));
		fields.add(new Field<String>(messages.memo(), messages.memo()));
		return fields;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClientInvoice getData() {
		ClientInvoice invoice = new ClientInvoice();
		getTransactionData(invoice);
		invoice.setCustomer(getLong(messages.customer()));
		invoice.setNumber(getString(messages.number()));
		invoice.setPaymentTerm(getLong(messages.paymentTerm()));
		invoice.setDueDate(getLong(messages.dueDate()));
		invoice.setOrderNum(getString(messages.orderNo()));
		invoice.setMemo(getString(messages.memo()));
		return invoice;
	}

}
