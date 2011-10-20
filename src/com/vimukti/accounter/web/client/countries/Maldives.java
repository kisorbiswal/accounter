package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Maldives extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MVR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Alif Alif", "Alif Dhaal", "Baa", "Dhaal",
				"Faaf", "Gaaf Alif", "Gaaf Dhaal", "Ghaviyani", "Haa Alif",
				"Haa Dhaal", "Kaaf", "Laam", "Lhaviyani", "Malé", "Miim",
				"Nuun", "Raa", "Shaviyani", "Siin", "Thaa", "Vaav" };
	}

}
