package com.vimukti.accounter.server.imports;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Field;

public class VendorImporter extends PayeeImporter<ClientVendor> {

	@Override
	public ClientVendor getData() {
		ClientPayee vendor = new ClientVendor();
		getData(vendor);
		((ClientVendor) vendor).setCreditLimit(getLong(messages.creditLimit()));
		((ClientVendor) vendor).setShippingMethod(getLong(messages
				.preferredShippingMethod()));
		((ClientVendor) vendor)
				.setCustomFieldValues(getSetCusFieldList(messages.CustomField()));

		((ClientVendor) vendor).setName(getString(messages.payeeName(Global
				.get().vendor())));
		((ClientVendor) vendor).setVendorNumber(getString(messages.number()));

		((ClientVendor) vendor)
				.setCustomFieldValues(getSetCusFieldList(messages.CustomField()));
		((ClientVendor) vendor).setPayeeSince(getFinanceDate("payeeSince")
				.getDate());
		((ClientVendor) vendor).setBalanceAsOf(getFinanceDate(
				messages.balanceAsOf()).getDate());
		((ClientVendor) vendor)
				.setAddress(getSetAddressList(messages.address()));
		((ClientVendor) vendor).setContacts(getSetContactList(messages
				.contacts()));
		((ClientVendor) vendor).setMemo(getString(messages.notes()));
		((ClientVendor) vendor)
				.setPaymentTerms(getLong(messages.paymentTerm()));
		((ClientVendor) vendor).setVendorGroup(getLong(messages.group()));
		return (ClientVendor) vendor;

	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> fields = super.getAllFields();
		fields.add(new Field<String>("payeeName", messages.payeeName(Global
				.get().vendor()), true));
		fields.add(new Field<Long>(messages.creditLimit(), messages
				.creditLimit()));
		fields.add(new Field<String>(messages.CustomField(), messages
				.CustomField()));
		fields.add(new Field<Long>(messages.preferredShippingMethod(), messages
				.preferredShippingMethod()));
		fields.add(new Field<ClientFinanceDate>("payeeSince", messages
				.payeeSince(Global.get().vendor())));
		fields.add(new Field<ClientFinanceDate>(messages.balanceAsOf(),
				messages.balanceAsOf()));
		fields.add(new Field<ClientAddress>(messages.address(), messages
				.address()));
		fields.add(new Field<ClientContact>(messages.address(), messages
				.contacts()));
		fields.add(new Field<String>(messages.address(), messages.notes()));
		fields.add(new Field<Long>(messages.paymentTerm(), messages
				.paymentTerm()));
		fields.add(new Field<String>(messages.group(), messages.group()));
		return fields;
	}
}
