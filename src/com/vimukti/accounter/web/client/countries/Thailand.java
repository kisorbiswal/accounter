package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Thailand extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Central", "Krung Thep",
				"Northeastern", "Northern", "Southern" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "THB";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().october();
	}

}
