package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Afghanistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return new String[] { "Badah_san", "Badgis", "Baglan", "Balh_",
				"Bamiyan", "Farah", "Faryab", "Gawr", "Gazni", "H_awst",
				"Herat", "Hilmand", "Jawzjan", "Kabul", "Kandahar", "Kapisa",
				"Kunarha", "Kunduz", "Lagman", "Lawgar", "Maydan-Wardak",
				"Nangarhar", "Nimruz", "Nuristan", "Paktika", "Paktiya",
				"Parwan", "Samangan", "Sar-e Pul", "Tah_ar", "Uruzgan", "Zabul" };
	}

	@Override
	public String getPreferredCurrency() {
		return "AFN";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {

		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
