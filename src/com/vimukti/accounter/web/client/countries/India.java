package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class India implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andaman and Nicobar Islands",
				"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bangla",
				"Bihar", "Chandigarh", "Chhattisgarh",
				"Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa",
				"Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
				"Jharkhand", "Karnataka", "Kerala", "Lakshadweep",
				"Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
				"Mizoram", "Nagaland", "Orissa", "Pondicherry", "Punjab",
				"Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttaranchal",
				"Uttar Pradesh" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "INR";
	}

	@Override
	public String getDefaultTimeZone(String state) {
		if (state != null) {
			return "UTC-12:00 Etc/GMT+12";
		}
		return null;
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().april();
	}

}
