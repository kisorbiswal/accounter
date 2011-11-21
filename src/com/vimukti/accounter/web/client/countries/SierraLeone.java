package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SierraLeone extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Eastern", "Northern", "Southern", "Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SLL";
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
