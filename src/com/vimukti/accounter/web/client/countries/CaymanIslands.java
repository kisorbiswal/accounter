package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CaymanIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Grand Cayman" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KYD";
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
