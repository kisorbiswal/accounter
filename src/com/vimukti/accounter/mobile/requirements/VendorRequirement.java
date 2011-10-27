package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;

public abstract class VendorRequirement extends ListRequirement<ClientVendor> {

	public VendorRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientVendor> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientVendor value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		record.add(" ,Balance", value.getBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientVendor value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(Global.get().Vendor()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Vendor());
	}

}
