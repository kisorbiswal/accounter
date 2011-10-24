package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;

public abstract class ShippingMethodRequirement extends
		ListRequirement<ClientShippingMethod> {

	public ShippingMethodRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext,
			ChangeListner<ClientShippingMethod> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientShippingMethod value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		record.add("Desc", value.getDescription());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientShippingMethod value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return getMessages().create(getConstants().shippingMethod());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().shippingMethod());
	}

}
