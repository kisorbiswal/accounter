package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Senegal extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Dakar", "Diourbel", "Fatick",
				"Kaolack", "Kolda", "Louga", "Matam", "Saint-Louis",
				"Tambacounda", "Thiès", "Ziguinchor" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XOF";
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
