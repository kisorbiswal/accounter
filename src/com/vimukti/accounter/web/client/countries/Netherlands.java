package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Netherlands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Drenthe", "Flevoland", "Friesland",
				"Gelderland", "Groningen", "Limburg", "Noord-Brabant",
				"Noord-Holland", "Overijssel", "Utrecht", "Zeeland",
				"Zuid-Holland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
