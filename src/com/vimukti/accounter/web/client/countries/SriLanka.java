package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SriLanka extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amparai", "Anuradhapuraya",
				"Badulla", "Colombo", "Galla", "Gampaha", "Hambantota",
				"Kalatura", "Kegalla", "Kilinochchi", "Kurunegala",
				"Mad?akalpuwa", "Maha Nuwara", "Mannarama", "Matale", "Matara",
				"Monaragala", "Nuwara Eliya", "Puttalama", "Ratnapuraya",
				"Tirikunamalaya", "Vavuniyawa", "Yapanaya" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LKR";
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
