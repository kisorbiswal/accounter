package com.vimukti.accounter.web.client.imports;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;

public class CustomerImporter extends PayeeImporter<ClientCustomer> {

	@Override
	public ClientCustomer getData() {
		ClientPayee customer = new ClientCustomer();
		getData(customer);
		((ClientCustomer) customer).setName(getString("name"));
		((ClientCustomer) customer).setNumber(getString("number"));
		((ClientCustomer) customer).setSalesPerson(getLong("salesPerson"));
		((ClientCustomer) customer).setCreditRating(getLong(messages
				.creditRating()));
		((ClientCustomer) customer)
				.setCustomFieldValues(getSetCusFieldList(messages.CustomField()));
		((ClientCustomer) customer).setPayeeSince(getFinanceDate("payeeSince")
				.getDate());

		((ClientCustomer) customer)
				.setBalanceAsOf(getFinanceDate("balanceAsof").getDate());

		((ClientCustomer) customer).setAddress(getSetAddressList("adress"));

		((ClientCustomer) customer).setContacts(getSetContactList("contacts"));
		((ClientCustomer) customer).setMemo(getString("memo"));
		((ClientCustomer) customer).setPaymentTerm(getLong("paymentTerm"));
		((ClientCustomer) customer).setCustomerGroup(getLong("group"));
		return (ClientCustomer) customer;
	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> fields = super.getFields();
		fields.add(new Field<String>("name", messages.payeeName(Global.get()
				.Customer()), true));
		fields.add(new Field<String>("number", messages.number()));
		fields.add(new Field<Long>("salesPerson", messages.salesPerson()));
		fields.add(new Field<Long>("creditRating", messages.creditRating()));
		fields.add(new Field<String>(messages.CustomField(), messages
				.CustomField()));
		fields.add(new Field<ClientFinanceDate>("payeeSince", messages
				.payeeSince(Global.get().Customer())));
		fields.add(new Field<ClientFinanceDate>("balanceAsof", messages
				.balanceAsOf()));
		fields.add(new Field<ClientAddress>("adress", messages.address()));
		fields.add(new Field<ClientContact>("contacts", messages.contacts()));
		fields.add(new Field<String>("memo", messages.notes()));
		fields.add(new Field<Long>("paymentTerm", messages.paymentTerm()));
		fields.add(new Field<String>("group", messages.customergroup()));
		return fields;
	}
}
