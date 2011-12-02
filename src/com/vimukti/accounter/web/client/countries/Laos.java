package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Laos extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "LAK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Attopu", "Bokeo", "Bolikhamsay", "Champasak",
				"Houaphanh", "Khammouane", "Luang Nam Tha", "Luang Prabang",
				"Oudomxay", "Phongsaly", "Saravan", "Savannakhet", "Sekong",
				"Viangchan Prefecture", "Viangchan Province", "Xaignabury",
				"Xiang Khuang" };
	}

}
