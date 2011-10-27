package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;

public abstract class ShippingTermRequirement extends
		ListRequirement<ClientShippingTerms> {

	public ShippingTermRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext,
			ChangeListner<ClientShippingTerms> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientShippingTerms value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		record.add("Desc", value.getDescription());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientShippingTerms value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().shippingTerm()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().shippingTerm());
	}

}
