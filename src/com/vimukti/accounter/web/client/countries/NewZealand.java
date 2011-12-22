package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class NewZealand extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Auckland", "Bay of Plenty",
				"Canterbury", "Gisborne", "Hawke's Bay", "Manawatu-Wanganui",
				"Marlborough", "Nelson", "Northland", "Otago", "Southland",
				"Taranaki", "Tasman", "Waikato", "Wellington", "West Coast" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NZD";
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
		return "UTC+12:00 Pacific/Auckland";
	}

}
