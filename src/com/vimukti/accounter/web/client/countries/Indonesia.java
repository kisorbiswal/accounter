package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Indonesia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aceh", "Bali", "Bangka-Belitung",
				"Banten", "Bengkulu", "Gorontalo", "Jakarta", "Jambi",
				"Jawa Barat", "Jawa Tengah", "Jawa Timur", "Kalimantan Barat",
				"Kalimantan Selatan", "Kalimantan Tengah", "Kalimantan Timur",
				"Lampung", "Maluku", "Maluku Utara", "Nusa Tenggara Barat",
				"Nusa Tenggara Timur", "Papua", "Riau", "Riau Kepulauan",
				"Sulawesi Selatan", "Sulawesi Tengah", "Sulawesi Tenggara",
				"Sulawesi Utara", "Sumatera Barat", "Sumatera Selatan",
				"Sumatera Utara", "Yogyakarta" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "IDR";
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
