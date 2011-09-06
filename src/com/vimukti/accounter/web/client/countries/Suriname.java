package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Suriname extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Brokopondo", "Commewijne", "Coronie",
				"Marowijne", "Nickerie", "Para", "Paramaribo", "Saramacca",
				"Wanica" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SRG";
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
