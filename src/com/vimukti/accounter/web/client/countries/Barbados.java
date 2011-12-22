package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

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
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Barbados";
	}

}
