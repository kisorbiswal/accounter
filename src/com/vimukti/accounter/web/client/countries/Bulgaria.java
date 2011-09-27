package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Bulgaria extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Blagoevgrad", "Burgas", "Dobric",
				"Gabrovo", "Haskovo", "Jambol", "Kardzali", "Kjustendil",
				"Lovec", "Montana", "Pazardzik", "Pernik", "Pleven", "Plovdiv",
				"Razgrad", "Ruse", "Silistra", "Sliven", "Smoljan",
				"Sofija grad", "Sofijska oblast", "Stara Zagora", "Sumen",
				"Targoviste", "Varna", "Veliko Tarnovo", "Vidin", "Vraca" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BGL";
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
