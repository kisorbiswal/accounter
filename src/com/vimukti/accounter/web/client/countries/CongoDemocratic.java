package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CongoDemocratic extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CDZ";
		// formerly ZRZ
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bandundu", "Bas-Congo", "Équateur",
				"Haut-Congo", "Kasai-Occidental", "Kasai-Oriental", "Katanga",
				"Kinshasa", "Maniema", "Nord-Kivu", "Sud-Kivu" };
	}

}
