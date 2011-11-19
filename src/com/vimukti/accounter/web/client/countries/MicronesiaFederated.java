package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class MicronesiaFederated extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Chuuk", "Kusaie", "Pohnpei", "Yap" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}

}
