package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Iraq extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "IQD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "al-Anbar", "al-Basrah", "al-Mut_anna",
				"al-Qadisiyah", "an-Najaf", "as-Sulaymaniyah", "at-Ta'mim",
				"Babil", "Bagdad", "Dahuk", "Ði Qar", "Diyala", "Irbil",
				"Karbala", "Maysan", "Ninawa", "Salah?-ad-Din", "Wasit" };
	}

}
