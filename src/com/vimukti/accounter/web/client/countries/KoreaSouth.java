package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class KoreaSouth extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Cheju", "Chollabuk", "Chollanam",
				"Chungchongbuk", "Chungchongnam", "Inchon", "Kangwon",
				"Kwangju", "Kyonggi", "Kyongsangbuk", "Kyongsangnam", "Pusan",
				"Soul", "Taegu", "Taejon", "Ulsan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KRW";
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
