package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class AmericanSamoa extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Eastern", "Manu'a", "Swains Island", "Western" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-11:00 Pacific/Pago_Pago";
	}

}
