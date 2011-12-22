package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Australia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Australian Capital Territory",
				"New South Wales", "Northern Territory", "Queensland",
				"South Australia", "Tasmania", "Victoria", "Western Australia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AUD";
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
		if (state.equals("Australian Capital Territory")) {
			// TODO
		} else if (state.equals("New South Wales")) {
			return "UTC+10:00 Australia/Sydney";
		} else if (state.equals("Northern Territory")) {
			return "UTC+9:30 Australia/Darwin";
		} else if (state.equals("Queensland")) {
			return "UTC+10:00 Australia/Queensland";
		} else if (state.equals("South Australia")) {
			return "UTC+9:30 Australia/Adelaide";
		} else if (state.equals("Tasmania")) {
			return "UTC+10:00 Australia/Tasmania";
		} else if (state.equals("Victoria")) {
			return "UTC+10:00 Australia/Victoria";
		} else if (state.equals("Western Australia")) {
			return "UTC+8:45 Australia/Eucla";
		}
		return null;
	}
}
