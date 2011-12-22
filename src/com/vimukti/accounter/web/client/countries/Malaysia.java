package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Malaysia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Johor", "Kedah", "Kelantan",
				"Kuala Lumpur", "Labuan", "Melaka", "Negeri Sembilan",
				"Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah",
				"Sarawak", "Selangor", "Terengganu" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "MYR";
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
		// Asia/Kuala_Lumpur peninsular Malaysia
		// Asia/Kuching Sabah & Sarawak
		if (state.equals("Sabah") || state.equals("Sabah")) {
			return "UTC+8:00 Asia/Kuching";
		} else {
			return "UTC+8:00 Asia/Kuala_Lumpur";
		}
	}

}
