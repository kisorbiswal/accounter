package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SaoTomeAndPrincipe extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Água Grande", "Cantagalo", "Caué", "Lemba",
				"Lobata", "Mé-Zochi", "Pagué" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "STD";
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
