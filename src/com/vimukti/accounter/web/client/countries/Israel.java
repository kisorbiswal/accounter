package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Israel extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hadarom", "Haifa", "Hamerkaz",
				"Haz_afon", "Jerusalem", "Judea and Samaria", "Tel Aviv" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ILS";
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
		return "UTC+2:00 Asia/Jerusalem";
	}

}
