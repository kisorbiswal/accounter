package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Tonga extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Eua", "Ha'apai", "Niuas", "Tongatapu", "Vava'u" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TOP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.messages().january();
	}

}
