package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ethiopia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ETB";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Addis Abeba", "Afar", "Amhara", "Benishangul",
				"Diredawa", "Gambella", "Harar", "Oromia", "Somali",
				"Southern", "Tigray" };
	}

}
