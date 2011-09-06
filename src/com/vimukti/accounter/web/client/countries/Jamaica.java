package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Jamaica extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Clarendon", "Hanover", "Kingston",
				"Manchester", "Portland", "Saint Andrew", "Saint Ann",
				"Saint Catherine", "Saint Elizabeth", "Saint James",
				"Saint Mary", "Saint Thomas", "Trelawney", "Westmoreland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "JMD";
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
