package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Pakistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azad Kashmir", "Baluchistan",
				"Federal Capital Area", "Federally administered Tribal Areas",
				"Northern Areas", "North-West Frontier", "Punjab", "Sind" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PKR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.july();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+5:00 Asia/Karachi";
	}

}
