package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Luxembourg extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Capellen", "Clervaux", "Diekirch",
				"Echternach", "Esch-sur-Alzette", "Grevenmacher", "Luxemburg",
				"Mersch", "Redange", "Remich", "Vianden", "Wiltz" };
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
