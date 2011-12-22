package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Thailand extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Central", "Krung Thep",
				"Northeastern", "Northern", "Southern" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "THB";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.october();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+7:00 Asia/Bangkok";
	}

}
