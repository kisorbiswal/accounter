package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Australia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Australian Capital Territory",
				"New South Wales", "Northern Territory", "Queensland",
				"South Australia", "Tasmania", "Victoria", "Western Australia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "AUD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().july();
	}

}
