package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Kenya extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Central", "Coast", "Eastern",
				"Nairobi", "North Eastern", "Nyanza", "Rift Valley", "Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KES";
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
