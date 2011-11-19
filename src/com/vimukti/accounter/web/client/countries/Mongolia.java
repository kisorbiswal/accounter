package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mongolia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Arhangaj", "Bajanhongor", "Bajan-Ölgij", "Bulgan",
				"Darhan-Uul", "Dornod", "Dornogovi", "Dundgovi", "Govi-Altaj",
				"Govisumber", "Hèntij", "Hovd", "Hövsgöl", "Ömnögovi", "Orhon",
				"Övörhangaj", "Sèlèngè", "Sühbaatar", "Töv", "Ulaanbaatar",
				"Uvs", "Zavhan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MNT";
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
