package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mozambique extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Cabo Delgado", "Gaza", "Inhambane",
				"Manica", "Maputo", "Maputo Provincia", "Nampula", "Niassa",
				"Sofala", "Tete", "Zambezia" };

		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MZN";
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Maputo";
	}

}
