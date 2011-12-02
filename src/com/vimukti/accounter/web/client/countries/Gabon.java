package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Gabon extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Estuaire", "Haut-Ogooué", "Moyen-Ogooué",
				"Ngounié", "Nyanga", "Ogooué-Ivindo", "Ogooué-Lolo",
				"Ogooué-Maritime", "Woleu-Ntem" };
	}

}
