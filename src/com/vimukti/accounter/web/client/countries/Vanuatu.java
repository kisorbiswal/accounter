package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Vanuatu extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "VUV";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Malampa", "Penama", "Sanma", "Shefa", "Tafea",
				"Torba" };
	}

}
