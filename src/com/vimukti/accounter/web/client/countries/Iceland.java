package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Iceland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Austurland", "Hofuoborgarsvxoi",
				"Norourland eystra", "Norourland vestra", "Suourland",
				"Suournes", "Vestfiroir", "Vesturland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ISK";
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
		return "UTC+0:00 Atlantic/Reykjavik";
	}

}
