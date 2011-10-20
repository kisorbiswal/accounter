package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Guam extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Agana Heights", "Agat", "Barrigada",
				"Chalan-Pago-Ordot", "Dededo", "Hagatña", "Inarajan",
				"Mangilao", "Merizo", "Mongmong-Toto-Maite", "Santa Rita",
				"Sinajana", "Talofofo", "Tamuning", "Yigo", "Yona" };
	}

}
