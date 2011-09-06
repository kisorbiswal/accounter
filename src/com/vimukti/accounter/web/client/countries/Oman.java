package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Oman extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "ad-Dah_iliyah", "al-Batinah",
				"aš-Šarqiyah", "az?-Z?ahirah", "Maskat", "Musandam", "Z?ufar" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "OMR";
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
