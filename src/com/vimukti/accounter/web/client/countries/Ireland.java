package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ireland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Carlow", "Cavan", "Clare", "Cork",
				"Donegal", "Dublin", "Galway", "Kerry", "Kildare", "Kilkenny",
				"Laois", "Leitrim", "Limerick", "Longford", "Louth", "Mayo",
				"Meath", "Monaghan", "Offaly", "Roscommon", "Sligo",
				"Tipperary North Riding", "Tipperary South Riding",
				"Waterford", "Westmeath", "Wexford", "Wicklow" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
