package com.vimukti.accounter.company.initialize;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;

public class CompanyInitializedFactory {

	/**
	 * this is use to get the initializer for the company
	 * 
	 * @param company
	 */

	public static CompanyInitializer getInitializer(Company company) {
		if (company.getCountry().equals(CountryPreferenceFactory.UNITED_STATES)) {
			return new USCompanyInitializer(company);
		} else if(company.getCountry().equals(CountryPreferenceFactory.UNITED_KINGDOM)) {
			return new UKCompanyInitializer(company);
		} else if(company.getCountry().equals(CountryPreferenceFactory.INDIA)) {
			return new IndianCompanyInitializer(company);
		} else {
			return new OtherCompanyInitializer(company);
		}
	}

}
