package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Germany extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Baden-Wurttemberg", "Bayern",
				"Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen",
				"Mecklenburg-Vorpommern", "Niedersachsen",
				"Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland",
				"Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thuringen" };
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
