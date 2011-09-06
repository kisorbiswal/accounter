package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Switzerland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aargau", "Appenzell-Ausser Rhoden",
				"Appenzell Inner-Rhoden", "Basel-Landschaft", "Basel-Stadt",
				"Bern", "Freiburg", "Genf", "Glarus", "Graubünden", "Jura",
				"Luzern", "Neuenburg", "Nidwalden", "Obwalden", "Sankt Gallen",
				"Schaffhausen", "Schwyz", "Solothurn", "Tessin", "Thurgau",
				"Uri", "Waadt", "Wallis", "Zug", "Zürich" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CHF";
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
