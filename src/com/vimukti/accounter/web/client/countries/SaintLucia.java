package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SaintLucia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Anse-la-Raye", "Canaries",
				"Castries", "Choiseul", "Dennery", "Gros Inlet", "Laborie",
				"Micoud", "Soufrière", "Vieux Fort" };
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

		return Accounter.constants().april();
	}

}
