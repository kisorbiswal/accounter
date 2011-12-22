package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Suriname extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Brokopondo", "Commewijne", "Coronie",
				"Marowijne", "Nickerie", "Para", "Paramaribo", "Saramacca",
				"Wanica" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SRG";
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
		return "UTC-3:00 America/Paramaribo";
	}

}
