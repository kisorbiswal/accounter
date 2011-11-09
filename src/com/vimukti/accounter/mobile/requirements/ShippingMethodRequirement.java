package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class ShippingMethodRequirement extends
		ListRequirement<ShippingMethod> {

	public ShippingMethodRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, ChangeListner<ShippingMethod> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ShippingMethod value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		record.add("Desc", value.getDescription());
		return record;
	}

	@Override
	protected String getDisplayValue(ShippingMethod value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().shippingMethod()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().shippingMethod());
	}

}
