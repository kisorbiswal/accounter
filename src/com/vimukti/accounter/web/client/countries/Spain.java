package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Spain extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andalusien", "Aragonien", "Asturien",
				"Balearen", "Baskenland", "Ceuta", "Extremadura", "Galizien",
				"Kanaren", "Kantabrien", "Kastilien-La Mancha",
				"Kastilien-León", "Katalonien", "La Rioja", "Madrid",
				"Melilla", "Murcia", "Navarra", "Valencia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
