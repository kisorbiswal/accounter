package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Pakistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azad Kashmir", "Baluchistan",
				"Federal Capital Area", "Federally administered Tribal Areas",
				"Northern Areas", "North-West Frontier", "Punjab", "Sind" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PKR";
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
