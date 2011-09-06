package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Bermuda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hamilton", "Saint George" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BMD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().april();
	}

}
