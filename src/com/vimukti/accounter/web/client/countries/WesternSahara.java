package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class WesternSahara extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MAD";
		// MRO also..
	}

	@Override
	public String[] getStates() {
		return new String[] { "al-'Ayun", "as-Samarah", "Bu Jaydur",
				"Wad-ad-Ðahab" };
	}

}
