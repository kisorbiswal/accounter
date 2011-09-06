package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Dominica extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Saint Andrew", "Saint David",
				"Saint George", "Saint John", "Saint Joseph", "Saint Luke",
				"Saint Mark", "Saint Patrick", "Saint Paul", "Saint Peter" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XCD";
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
