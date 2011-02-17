package com.vimukti.accounter.datalayer;

import com.vimukti.accounter.interfaces.ICustomer;

public interface ICustomerListener {

	/**
	 * Method customerAdded
	 * 
	 * @param customer
	 */
	void customerAdded(ICustomer customer);
	/**
	 * Method customerDeleted
	 * 
	 * @param customer
	 */
	
	void customerDeleted(ICustomer customer);
	/**
	 * Method customerModified
	 * 
	 * @param customer
	 * @param property
	 */
	
	void customerModified(ICustomer customer,String property);
}
