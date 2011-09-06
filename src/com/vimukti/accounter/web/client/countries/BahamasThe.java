package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class BahamasThe extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abaco", "Acklins Island", "Andros",
				"Berry Islands", "Biminis", "Cat Island", "Crooked Island",
				"Eleuthera", "Exuma and Cays", "Grand Bahama",
				"Inagua Islands", "Long Island", "Mayaguana", "New Providence",
				"Ragged Island", "Rum Cay", "San Salvador" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BSD";
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
