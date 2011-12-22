package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Jordan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "'Ajlun", "al-'Aqabah", "al-Balqa'",
				"al-Karak", "al-Mafraq", "'Amman", "at-Tafilah", "az-Zarqa'",
				"Irbid", "Jaras", "Ma'an", "Madaba" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "JOD";
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
		return "UTC+2:00 Asia/Amman";
	}

}
