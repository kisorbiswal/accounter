package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class IsleOfMan extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "IMP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Castletown", "Douglas", "Laxey", "Onchan",
				"Peel", "Port Erin", "Port Saint Mary", "Ramsey" };
	}

}
