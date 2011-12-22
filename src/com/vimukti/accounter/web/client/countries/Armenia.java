package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Armenia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aragatsotn", "Ararat", "Armavir",
				"Gegharkunik", "Kotayk", "Lori", "Shirak", "Syunik", "Tavush",
				"Vayots Dzor", "Yerevan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AMD";
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
		return "UTC+4:00 Asia/Yerevan";
	}

}
