package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class UnitedStates extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alabama", "Alaska", "Arizona",
				"Arkansas", "California", "Colorado", "Connecticut",
				"Delaware", "District of Columbia", "Florida", "Georgia",
				"Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
				"Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts",
				"Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
				"Nebraska", "Nevada", "New Hampshire", "New Jersey",
				"New Mexico", "New York", "North Carolina", "North Dakota",
				"Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
				"South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
				"Vermont", "Virginia", "Washington", "West Virginia",
				"Wisconsin", "Wyoming"

		};
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		OrganizationType[] types = {
				new OrganizationType("", Accounter.constants()
						.soleProprietorship(), Accounter.constants()
						.soleProprietorshipDesc()),
				new OrganizationType("", Accounter.constants()
						.partnershipOrLLP(), Accounter.constants()
						.partnershipOrLLPDesc()),
				new OrganizationType("", Accounter.constants().LLC(), Accounter
						.constants().LLCDesc()),
				new OrganizationType("", Accounter.constants().corporation(),
						Accounter.constants().corporationDesc()),
				new OrganizationType("", Accounter.constants().sCorporation(),
						Accounter.constants().sCorporationDesc()),
				new OrganizationType("", Accounter.constants().nonProfit(),
						Accounter.constants().nonProfitDesc()) };
		return types;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().october();
	}

	@Override
	public boolean isServiceTaxIdAndCompanyTaxIdSame() {
		return true;
	}

	@Override
	public boolean isVatAvailable() {
		return false;
	}


	@Override
	public boolean isSalesTaxAvailable() {
		return true;
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		return true;
	}


	@Override
	public boolean isServiceTaxAvailable() {
		return false;
	}

	@Override
	public boolean isTDSAvailable() {
		return false;
	}

	
}
