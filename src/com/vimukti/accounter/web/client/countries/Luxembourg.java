package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Luxembourg extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Capellen", "Clervaux", "Diekirch",
				"Echternach", "Esch-sur-Alzette", "Grevenmacher", "Luxemburg",
				"Mersch", "Redange", "Remich", "Vianden", "Wiltz" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Luxembourg";
	}

}
