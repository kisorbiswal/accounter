package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Israel extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hadarom", "Haifa", "Hamerkaz",
				"Haz_afon", "Jerusalem", "Judea and Samaria", "Tel Aviv" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ILS";
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
