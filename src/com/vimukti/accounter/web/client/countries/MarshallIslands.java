package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class MarshallIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ailinlaplap", "Ailuk", "Arno", "Aur",
				"Bikini", "Ebon", "Enewetak", "Jabat", "Jaluit", "Kili",
				"Kwajalein", "Lae", "Lib", "Likiep", "Majuro", "Maloelap",
				"Mejit", "Mili", "Namorik", "Namu", "Rongelap", "Ujae",
				"Utrik", "Wotho", "Wotje" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().october();
	}

}
