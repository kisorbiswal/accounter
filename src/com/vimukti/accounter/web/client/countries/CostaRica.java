package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CostaRica extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alajuela", "Cartago", "Guanacaste",
				"Heredia", "Limón", "Puntarenas", "San José" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CRC";
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
