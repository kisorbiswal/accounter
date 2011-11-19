package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SanMarino extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Acquaviva", "Borgo Maggiore", "Chiesanuova",
				"Domagnano", "Faetano", "Fiorentino", "Montegiardino",
				"San Marino", "Serravalle" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
