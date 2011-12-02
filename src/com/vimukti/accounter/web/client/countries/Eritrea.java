package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Eritrea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ERN";
		// ETB also...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Anseba", "Debub", "Debub-Keih-Bahri",
				"Gash-Barka", "Maekel", "Semien-Keih-Bahri" };
	}

}
