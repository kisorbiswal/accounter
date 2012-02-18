package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.core.ClientCustomFieldValue;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public abstract class PayeeImporter<T> extends AbstractImporter<IAccounterCore> {

	protected void getData(ClientPayee payee) {
		payee.setCurrency(getLong(messages.currency()));
		payee.setOpeningBalance(getDouble(messages.openingBalance()));
		payee.setFaxNo(getString(messages.fax()));
		payee.setPhoneNo(getString(messages.phone()));
		payee.setWebPageAddress(getString(messages.webPageAddress()));
		payee.setPaymentMethod(getString(messages.paymentMethod()));
		payee.setVATRegistrationNumber(getString(messages.taxRegNo()));
		payee.setTAXCode(getLong(messages.taxCode()));
		payee.setBankAccountNo(getString(messages.bankAccountNumber()));
		payee.setBankName(getString(messages.bankName()));
		payee.setBankBranch(getString(messages.bankBranch()));
	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> fields = new ArrayList<Field<?>>();
		fields.add(new Field<Long>(messages.currency(), messages.currency()));
		fields.add(new Field<Double>(messages.openingBalances(), messages
				.openingBalances()));
		fields.add(new Field<String>(messages.fax(), messages.fax()));
		fields.add(new Field<String>(messages.phone(), messages.phone()));
		fields.add(new Field<String>(messages.webPageAddress(), messages
				.webPageAddress()));
		fields.add(new Field<String>(messages.paymentMethod(), messages
				.paymentMethod()));
		fields.add(new Field<Long>(messages.taxRegNo(), messages.taxRegNo()));
		fields.add(new Field<Long>(messages.taxCode(), messages.taxCode()));
		fields.add(new Field<String>(messages.bankAccountNumber(), messages
				.bankAccountNumber()));
		fields.add(new Field<String>(messages.bankName(), messages.bankName()));
		fields.add(new Field<Long>(messages.bankBranch(), messages.bankBranch()));
		return fields;
	}

	protected Set<ClientCustomFieldValue> getSetCusFieldList(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

}
