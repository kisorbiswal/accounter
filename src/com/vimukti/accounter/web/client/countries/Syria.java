package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Syria extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "al-H?asakah", "al-Ladiqiyah", "al-Qunaytirah",
				"ar-Raqqah", "as-Suwayda", "Damaskus", "Dar'a", "Dayr-az-Zawr",
				"H?alab", "H?amah", "H?ims", "Idlib", "Tartus" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SYP";
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
		return "UTC+2:00 Asia/Damascus";
	}

}
