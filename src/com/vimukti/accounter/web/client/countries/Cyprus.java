package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Cyprus extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Government controlled area",
				"Turkish controlled area" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CYP";
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
