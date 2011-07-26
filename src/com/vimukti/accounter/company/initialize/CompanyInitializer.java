package com.vimukti.accounter.company.initialize;

import com.vimukti.accounter.core.Company;

public abstract class CompanyInitializer {

	/**
	 * this is used to get the company type
	 */
	public abstract void init();
	public abstract Company getCompany();
	
}
