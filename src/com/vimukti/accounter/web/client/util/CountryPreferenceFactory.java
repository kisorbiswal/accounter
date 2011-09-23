package com.vimukti.accounter.web.client.util;

import com.vimukti.accounter.web.client.countries.India;

public class CountryPreferenceFactory {
	public static ICountryPreferences get(String country) {
		if(country.equals("India")){
			return new India();
		}
		return null;
	}
}
