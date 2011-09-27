package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Azerbaijan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abseron", "Aran", "Baki",
				"Dagliq Sirvan", "Ganja-Gazakh", "Kalbajar-Lachin", "Lankaran",
				"Naxcivan", "Quba-Xacmaz", "Shaki-Zaqatala", "Yuxari Qarabag" };
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
