package com.vimukti.accounter.mobile.requirements;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDisplayValue(ClientVendor value) {
		return value != null ? value.getDisplayName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return getMessages().create(Global.get().Vendor());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Vendor());
	}

}
