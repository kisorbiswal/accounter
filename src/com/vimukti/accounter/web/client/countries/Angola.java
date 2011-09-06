package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Angola extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bengo", "Benguela", "Bié", "Cabinda",
				"Cuando-Cubango", "Cuanza-Norte", "Cuanza-Sul", "Cunene",
				"Huambo", "Huíla", "Luanda", "Lunda Norte", "Lunda Sul",
				"Malanje", "Moxico", "Namibe", "Uíge", "Zaire" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "AOK";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {

		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
