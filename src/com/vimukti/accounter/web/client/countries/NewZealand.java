package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class NewZealand extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Auckland", "Bay of Plenty",
				"Canterbury", "Gisborne", "Hawke's Bay", "Manawatu-Wanganui",
				"Marlborough", "Nelson", "Northland", "Otago", "Southland",
				"Taranaki", "Tasman", "Waikato", "Wellington", "West Coast" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NewZealand";
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
