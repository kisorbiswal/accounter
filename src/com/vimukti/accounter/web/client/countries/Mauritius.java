package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mauritius extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Black River", "Flacq", "Grand Port",
				"Moka", "Pamplemousses", "Plaines Wilhelm",
				"Riviere du Rempart", "Rodrigues", "Savanne" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MUR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

}
