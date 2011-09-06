package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SaintKittsAndNevis extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Christ Church Nichola Town",
				"Saint Anne Sandy Point", "Saint George Basseterre",
				"Saint George Gingerland", "Saint James Windward",
				"Saint John Capesterre", "Saint John Figtree",
				"Saint Mary Cayon", "Saint Paul Capesterre",
				"Saint Paul Charlestown", "Saint Peter Basseterre",
				"Saint Thomas Lowland", "Saint Thomas Middle Island",
				"Trinity Palmetto Point" };
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

		return Accounter.constants().january();
	}

}
