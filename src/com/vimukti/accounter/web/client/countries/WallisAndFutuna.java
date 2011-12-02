package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class WallisAndFutuna extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XPF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Alo", "Hahake", "Hihifo", "Mua", "Sigave" };
	}

}
