package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class AntiguaAndBarbuda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Barbuda", "Saint George",
				"Saint John", "Saint Mary", "Saint Paul", "Saint Peter",
				"Saint Philip" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XCD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {

		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().april();
	}

}
