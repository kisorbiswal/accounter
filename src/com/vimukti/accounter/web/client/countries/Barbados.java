package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Barbados extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String states[] = new String[] { "Christ Church", "Saint Andrew",
				"Saint George", "Saint James", "Saint John", "Saint Joseph",
				"Saint Lucy", "Saint Michael", "Saint Peter", "Saint Philip",
				"Saint Thomas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BBD";
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
