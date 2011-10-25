package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public abstract class PaymentTermRequirement extends
		ListRequirement<ClientPaymentTerms> {

	public PaymentTermRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientPaymentTerms> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientPaymentTerms value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return "Payment Term is selected";
	}

	@Override
	protected String getEmptyString() {
		return "There are no Payment Terms";
	}

	@Override
	protected String getDisplayValue(ClientPaymentTerms value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return getMessages().create(getConstants().paymentTerm());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().paymentTerm());
	}

	@Override
	protected List<ClientPaymentTerms> getLists(Context context,
			final String name) {
		return Utility.filteredList(new ListFilter<ClientPaymentTerms>() {

			@Override
			public boolean filter(ClientPaymentTerms e) {
				return e.getName().contains(name);
			}
		}, getLists(context));
	}
}
