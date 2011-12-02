package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Benin extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[]{ "Alibori", "Atacora", "Atlantique", "Borgou", "Collines",
					"Couffo", "Donga", "Littoral", "Mono", "Ouémé", "Plateau",
					"Zou" };
	}

}
