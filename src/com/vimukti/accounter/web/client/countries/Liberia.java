package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Liberia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bomi", "Bong", "Grand Bassa",
				"Grand Cape Mount", "Grand Gedeh", "Maryland and Grand Kru",
				"Montserrado", "Nimba" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LRD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
