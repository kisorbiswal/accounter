package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Palau extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Aimeliik", "Airai", "Angaur", "Hatobohei",
				"Kayangel", "Koror", "Melekeok", "Ngaraard", "Ngardmau",
				"Ngaremlengui", "Ngatpang", "Ngchesar", "Ngerchelong",
				"Ngiwal", "Peleliu", "Sonsorol" };
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
		return Accounter.messages().january();
	}
}
