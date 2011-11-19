package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Somalia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Awdaal", "Baakool", "Baarii", "Baay", "Banaadir",
				"Gaalguuduud", "Gedoo", "Hiiraan", "Jubbada Dhexe",
				"Jubbada Hoose", "Mudug", "Nuugaal", "Sanaag",
				"Shabeellaha Dhexe", "Shabeellaha Hoose", "Sool", "Togdeer",
				"Woqooyi Galbeed" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SOS";
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
