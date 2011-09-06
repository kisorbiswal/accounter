package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Uzbekistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andijon", "Buhoro", "Cizah",
				"Fargona", "Horazm", "Kaskadarya", "Korakalpogiston",
				"Namangan", "Navoi", "Samarkand", "Sirdare", "Surhondar",
				"Taschkent" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "UZS";
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
