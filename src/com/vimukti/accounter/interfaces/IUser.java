package com.vimukti.accounter.interfaces;

import com.vimukti.accounter.datalayer.ICustomerListener;
import com.vimukti.accounter.datalayer.IVendorListener;

public interface IUser {

	/**
	 * method addCustomerListener
	 * 
	 * @param listener
	 */
	void addCustomerListener(ICustomerListener listener);
	/**
	 * method removeCustomerListener
	 * 
	 * @param listener
	 */
	
	void removeCustomerListener(ICustomerListener listener);
	/**
	 * method addVendorListener
	 * 
	 * @param listener
	 */
	
	void addVendorListener(IVendorListener listener);
	/**
	 * method removeVendorListener
	 * 
	 * @param listener
	 */
	
	void removeVendorListener(IVendorListener listener);
}
