package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Uganda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Central", "Eastern", "Northern",
				"Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "UGX";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().july();
	}

}
