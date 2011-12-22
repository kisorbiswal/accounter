package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Grenada extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Carriacou-Petite Martinique",
				"Saint Andrew", "Saint Davids", "Saint George", "Saint John",
				"Saint Mark", "Saint Patrick" };
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
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Grenada";
	}

}
