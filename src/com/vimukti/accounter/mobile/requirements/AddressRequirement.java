package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.ClientAddress;

public class AddressRequirement extends MultiRequirement<ClientAddress> {
	private static final String ADDRESS1 = "address1";
	private static final String CITY = "city";
	private static final String STREET = "street";
	private static final String STATE = "stateOrProvinence";
	private static final String COUNTRY = "countryOrRegion";

	public AddressRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		setDefaultValue(new ClientAddress());
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(ADDRESS1, "Please enter address1",
				"Address1", true, true));
		list.add(new StringRequirement(CITY, "Please enter city", "City", true,
				true));
		list.add(new StringRequirement(STREET, "Please enter street", "Stree",
				true, true));
		list.add(new StringRequirement(STATE, "Please enter state", "State",
				true, true));
		list.add(new StringRequirement(COUNTRY, "Please enter country",
				"Country", true, true));
	}

	@Override
	protected Result onFinish(Context context) {
		ClientAddress address = getValue();
		address.setAddress1((String) getRequirement(ADDRESS1).getValue());
		address.setCity((String) getRequirement(CITY).getValue());
		address.setStreet((String) getRequirement(STREET).getValue());
		address.setStateOrProvinence((String) getRequirement(STATE).getValue());
		address.setCountryOrRegion((String) getRequirement(COUNTRY).getValue());
		return null;
	}

	@Override
	protected String getDisplayValue() {
		ClientAddress address = getValue();
		return address.getAddressForMobile();
	}

}
