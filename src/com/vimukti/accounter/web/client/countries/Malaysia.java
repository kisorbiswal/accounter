package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Malaysia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Johor", "Kedah", "Kelantan",
				"Kuala Lumpur", "Labuan", "Melaka", "Negeri Sembilan",
				"Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah",
				"Sarawak", "Selangor", "Terengganu" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "MYR";
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
