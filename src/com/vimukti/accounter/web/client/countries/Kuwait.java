package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Kuwait extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Ah?madi", "al-'Asamah",
				"al-Farwaniyah", "al-Jahra'", "Hawalli", "Mubarak al-Kabir" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KWD";
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
		return "UTC+3:00 Asia/Kuwait";
	}

}
