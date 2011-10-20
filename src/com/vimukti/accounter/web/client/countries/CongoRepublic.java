package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CongoRepublic extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CDZ";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bouenza", "Brazzaville", "Cuvette",
				"Cuvette-Ouest", "Kouilou", "Lékoumou", "Likouala", "Niari",
				"Plateaux", "Pool", "Sangha" };
	}

}
