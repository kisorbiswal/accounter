package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Namibia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Caprivi", "Erongo", "Hardap", "Karas", "Kavango",
				"Khomas", "Kunene", "Ohangwena", "Omaheke", "Omusati",
				"Oshana", "Oshikoto", "Otjozondjupa" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NAD";
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
