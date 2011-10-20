package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CentralAfricanRepublic extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bamingui-Bangoran", "Bangui", "Basse-Kotto",
				"Haute-Kotto", "Haut-Mbomou", "Kémo", "Lobaye",
				"Mambéré-Kadéï", "Mbomou", "Nana-Gribizi", "Nana-Mambéré",
				"Ombella Mpoko", "Ouaka", "Ouham", "Ouham-Pendé",
				"Sangha-Mbaéré", "Vakaga" };
	}

}
