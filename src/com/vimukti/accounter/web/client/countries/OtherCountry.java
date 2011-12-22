package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class OtherCountry extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public String[] getStates() {
		return new String[] {};
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return null;
	}

}
