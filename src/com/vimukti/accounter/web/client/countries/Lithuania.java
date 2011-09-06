package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Lithuania extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Kaunas", "Klaipeda", "Panevezys",
				"Šiauliai", "Vilna" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LTL";
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
