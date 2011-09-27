package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Bahrain extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Capital Governorate",
				"Central Governorate", "Muharraq Governorate",
				"Northern Governorate", "Southern Governorate" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BHD";
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
