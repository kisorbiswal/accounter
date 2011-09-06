package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Guyana extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Barima-Waini", "Cuyuni-Mazaruni",
				"Demerara-Mahaica", "East Berbice-Corentyne",
				"Essequibo Islands-West Demerara", "Mahaica-Berbice",
				"Pomeroon-Supenaam", "Upper Demerara-Berbice",
				"Upper Takutu-Upper Essequibo" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GYD";
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
