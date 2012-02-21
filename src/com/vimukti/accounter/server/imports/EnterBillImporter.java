package com.vimukti.accounter.server.imports;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Field;

public class EnterBillImporter extends TransactionImporter<ClientEnterBill> {

	@Override
	public ClientEnterBill getData() {
		ClientTransaction enterBill = new ClientEnterBill();
		getTransactionData(enterBill);
		((ClientEnterBill) enterBill).setVendor(getLong(messages.supplier()));
		((ClientEnterBill) enterBill).setContact(getContactData());
		((ClientEnterBill) enterBill)
				.setPhone(getString(messages.phoneNumber()));
		((ClientEnterBill) enterBill).setDueDate(getFinanceDate(
				messages.dueDate()).getDate());
		((ClientEnterBill) enterBill).setDeliveryDate(getFinanceDate(
				messages.deliveryDate()).getDate());
		((ClientEnterBill) enterBill).setMemo(messages.memo());

		return (ClientEnterBill) enterBill;
	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> listFields = super.getAllFields();
		listFields.add(new Field<String>(messages.supplier(), messages
				.payeeName(Global.get().Vendor()), true));
		listFields.add(new Field<String>(messages.contactName(), messages
				.contactName()));
		listFields.add(new Field<String>(messages.title(), messages.title()));
		listFields.add(new Field<String>(messages.businessPhone(), messages
				.businessPhone()));
		listFields.add(new Field<String>(messages.email(), messages.email()));
		listFields.add(new Field<String>(messages.phoneNumber(), messages
				.phone()));
		listFields.add(new Field<ClientFinanceDate>(messages.dueDate(),
				messages.dueDate()));
		listFields.add(new Field<ClientFinanceDate>(messages.deliveryDate(),
				messages.deliveryDate()));
		listFields.add(new Field<String>(messages.memo(), messages.memo()));

		return super.getAllFields();
	}
}
