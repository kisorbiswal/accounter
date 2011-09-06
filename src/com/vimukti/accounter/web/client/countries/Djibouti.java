package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Djibouti extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String states[] = new String[] { "Ali Sabieh", "Dikhil", "Djibouti",
				"Obock", "Tadjoura" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DJF";
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
