package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Niger extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Agadez", "Diffa", "Dosso", "Maradi",
				"Niamey", "Tahoua", "Tillabery", "Zinder" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "Niger";
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
