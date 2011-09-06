package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Malta extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Gozo and Comino", "Northern",
				"Northern Harbour", "South Eastern", "Southern Harbour",
				"Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MTL";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}

}
