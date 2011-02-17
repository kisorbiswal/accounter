package com.vimukti.accounter.datalayer;

import com.vimukti.accounter.ext.Command;

public interface IDataTransporter {

	public boolean processCommand(Command command);

	public void createTransaction(Command data);

	public void createCustomer(Command data);

	public void editCustomer(Command data);

	public void deleteCustomer(int customerId);

	public void createVendor(Command data);

	public void editVendor(Command data);

	public void deleteVendor(int vendorId);

	void addDataTransporterListener(IDataTransporterListener listener);

	void removeDataTransporterListener(IDataTransporterListener listener);

}
