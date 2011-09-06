package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Azerbaijan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abseron", "Aran", "Baki",
				"Dagliq Sirvan", "G?nc?-Qazax", "K?lb?c?r-Laçin", "L?nk?ran",
				"Naxçivan", "Quba-Xaçmaz", "S?ki-Zaqatala", "Yuxari Qarabag" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AZM";
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
