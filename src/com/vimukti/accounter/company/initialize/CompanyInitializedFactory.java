package com.vimukti.accounter.company.initialize;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.company.initialize.CompanyInitializer;
public class CompanyInitializedFactory {

	/**
	 * this is use to get the initializer for the company
	 * 
	 * @param company
	 */

	public static CompanyInitializer getInitializer(Company company) {
		switch (company.getAccountingType()) {
		case Company.ACCOUNTING_TYPE_US:

			return new USCompanyInitializer(); 
			
		case Company.ACCOUNTING_TYPE_UK:
			return new UKCompanyInitializer();
			
		case Company.ACCOUNTING_TYPE_INDIA:
			return new IndianCompanyInitializer();
		}
		return null;
	}

}
