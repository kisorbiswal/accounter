package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class SouthGeorgiaAndSandwich extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "GBP";
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
		return "UTC-2:00 Atlantic/South_Georgia";
	}
}
