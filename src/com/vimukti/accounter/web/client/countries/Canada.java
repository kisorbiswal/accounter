package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Canada extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alberta", "British Columbia",
				"Manitoba", "New Brunswick", "Newfoundland and Labrador",
				"Northwest Territories", "Nova Scotia", "Nunavut", "Ontario",
				"Prince Edward Island", "Québec", "Saskatchewan", "Yukon" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CAD";
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
