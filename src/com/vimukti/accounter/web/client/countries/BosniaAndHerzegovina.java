package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class BosniaAndHerzegovina extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "BAM";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Distrikt Brcko",
				"Federacija Bosna i Hercegovina", "Republika Srpska" };
	}

}
