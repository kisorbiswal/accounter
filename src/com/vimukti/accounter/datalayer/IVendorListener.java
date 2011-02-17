package com.vimukti.accounter.datalayer;

import com.vimukti.accounter.interfaces.IVendor;


public interface IVendorListener {

	/**
	 * Method vendorAdded
	 * 
	 * @param vendor
	 */
	void vendorAdded(IVendor vendor);
	/**
	 * Method vendorDeleted
	 * 
	 * @param vendor
	 */
	
	void vendorDeleted(IVendor vendor);
	/**
	 * Method vendorModified
	 * 
	 * @param vendor
	 * @param property
	 */
	
	void vendorModified(IVendor vendor,String property);
}
