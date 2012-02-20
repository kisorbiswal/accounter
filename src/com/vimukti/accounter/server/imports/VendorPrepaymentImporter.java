package com.vimukti.accounter.server.imports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.imports.Field;

public class VendorPrepaymentImporter extends
		TransactionImporter<ClientPayBill> {

	@Override
	public ClientPayBill getData() {
		ClientTransaction vendorPrepayment = new ClientPayBill();
		getTransactionData(vendorPrepayment);
		((ClientPayBill) vendorPrepayment)
				.setVendor(getLong(messages.vendor()));
		((ClientPayBill) vendorPrepayment).setPayFrom(getLong(messages
				.payFrom()));
		((ClientPayBill) vendorPrepayment)
				.setTotal(getDouble(messages.amount()));
		((ClientPayBill) vendorPrepayment).setPaymentMethod(getString(messages
				.paymentMethod()));
		((ClientPayBill) vendorPrepayment).setCheckNumber(getString(messages
				.checkNo()));
		((ClientPayBill) vendorPrepayment).setMemo(getString(messages.memo()));
		return ((ClientPayBill) vendorPrepayment);
	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> list = super.getAllFields();
		list.add(new Field<String>(messages.vendor(), messages.payTo(), true));
		list.add(new Field<String>(messages.payFrom(), messages.payFrom(), true));
		list.add(new Field<Double>(messages.amount(), messages.amount(), true));
		list.add(new Field<String>(messages.paymentMethod(), messages
				.paymentMethod(), true));
		list.add(new Field<String>(messages.checkNo(), messages.checkNo()));
		list.add(new Field<String>(messages.memo(), messages.memo()));
		return list;
	}

}
