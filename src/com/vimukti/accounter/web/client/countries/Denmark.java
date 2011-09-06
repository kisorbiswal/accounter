package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Denmark extends AbstractCountryPreferences{

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hovedstaden", "Midtjylland",
				"Nordjylland", "Sjælland", "Syddanmark" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DKK";
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
