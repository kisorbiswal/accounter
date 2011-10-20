package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Chad extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Batha", "Biltine", "Bourkou-Ennedi-Tibesti",
				"Chari-Baguirmi", "Guéra", "Kanem", "Lac", "Logone Occidental",
				"Logone Oriental", "Mayo-Kébbi", "Moyen-Chari", "Ouaddaï",
				"Salamat", "Tandjilé" };
	}

}
