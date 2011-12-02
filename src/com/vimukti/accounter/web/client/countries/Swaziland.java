package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Swaziland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Hhohho", "Lubombo", "Manzini", "Shiselweni" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SZL";
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
