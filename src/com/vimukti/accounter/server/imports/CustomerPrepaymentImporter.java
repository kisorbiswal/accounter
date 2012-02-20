package com.vimukti.accounter.server.imports;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.imports.Field;

public class CustomerPrepaymentImporter extends
		TransactionImporter<ClientCustomerPrePayment> {

	@Override
	public ClientCustomerPrePayment getData() {
		ClientTransaction custPrepayment = new ClientCustomerPrePayment();
		getTransactionData(custPrepayment);
		((ClientCustomerPrePayment) custPrepayment)
				.setCustomer(getLong(messages.customer()));
		((ClientCustomerPrePayment) custPrepayment)
				.setDepositIn(getLong(messages.depositIn()));
		((ClientCustomerPrePayment) custPrepayment).setTotal(getDouble(messages
				.amount()));
		((ClientCustomerPrePayment) custPrepayment)
				.setPaymentMethod(getString(messages.paymentMethod()));
		((ClientCustomerPrePayment) custPrepayment)
				.setCheckNumber(getString(messages.checkNo()));
		((ClientCustomerPrePayment) custPrepayment).setMemo(getString(messages
				.memo()));
		return ((ClientCustomerPrePayment) custPrepayment);
	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> list = super.getAllFields();
		list.add(new Field<String>(messages.customer(),
				Global.get().Customer(), true));
		list.add(new Field<String>(messages.depositIn(), messages.depositIn(),
				true));
		list.add(new Field<Double>(messages.amount(), messages.amount(), true));
		list.add(new Field<String>(messages.paymentMethod(), messages
				.paymentMethod(), true));
		list.add(new Field<String>(messages.checkNo(), messages.checkNo()));
		list.add(new Field<String>(messages.memo(), messages.memo()));
		return list;
	}

}
