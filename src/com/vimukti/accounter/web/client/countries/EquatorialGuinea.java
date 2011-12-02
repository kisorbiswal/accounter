package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class EquatorialGuinea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
		// GAQ also...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Annobón", "Bioko Norte", "Bioko Sur",
				"Centro Sur", "Kié-Ntem", "Litoral", "Wele-Nzas" };
	}

}
