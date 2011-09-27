package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Norway extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Akershus", "Aust-Agder", "Buskerud",
				"Finnmark", "Hedmark", "Hordaland", "More og Romsdal",
				"Nordland", "Nord-Trondelag", "Oppland", "Oslo", "Ostfold",
				"Rogaland", "Sogn og Fjordane", "Sor-Trondelag", "Telemark",
				"Troms", "Vest-Agder", "Vestfold" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "NOK";
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
