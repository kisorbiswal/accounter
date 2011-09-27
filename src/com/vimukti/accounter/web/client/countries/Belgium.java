package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Belgium extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Antwerp", "Limburg",
				"Flemish Brabant", "East Flanders", "West Flanders", "Hainaut",
				"Walloon Brabant", "Namur", "Liege", "Luxembourg" };
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
