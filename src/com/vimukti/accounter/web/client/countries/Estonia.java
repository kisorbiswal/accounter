package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Estonia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Harju", "Hiiu", "Ida-Viru", "J‰rva",
				"Jogeva", "L‰‰ne", "L‰‰ne-Viru", "P‰rnu", "Polva", "Rapla",
				"Saare", "Tartu", "Valga", "Viljandi", "Voru" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EEK";
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
