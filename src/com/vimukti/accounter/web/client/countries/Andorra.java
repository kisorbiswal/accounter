package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Andorra extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Andorra la Vella", "Canillo", "Encamp",
				"Escaldes-Engordany", "La Massana", "Ordino",
				"Sant Julià de Lòria" };
	}

}
