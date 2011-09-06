package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Madagascar extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Antananarivo", "Antsiranana",
				"Fianarantsoa", "Mahajanga", "Toamasina", "Toliary" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MGF";
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
