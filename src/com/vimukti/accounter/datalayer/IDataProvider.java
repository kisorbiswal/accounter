package com.vimukti.accounter.datalayer;

import com.vimukti.accounter.interfaces.ICompany;
import com.vimukti.accounter.interfaces.ICustomer;
import com.vimukti.accounter.interfaces.IPreference;
import com.vimukti.accounter.interfaces.IUser;
import com.vimukti.accounter.interfaces.IVendor;

public interface IDataProvider {
	
	ICustomer getCustomer(String customerId);
	
	IVendor getVendor(String vendorId);
	
	ICompany getCompany(String companyId);
	
	IPreference getPreference(String companyId);
	
	IUser getUser(String UserId);
	
	
}
