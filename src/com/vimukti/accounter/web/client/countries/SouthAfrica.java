package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SouthAfrica extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Eastern Cape", "Free State",
				"Gauteng", "KwaZulu Natal", "Limpopo", "Mpumalanga",
				"Northern Cape", "North West", "Western Cape" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ZAR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().april();
	}

}
