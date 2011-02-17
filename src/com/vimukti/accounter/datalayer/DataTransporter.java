package com.vimukti.accounter.datalayer;

import com.vimukti.accounter.ext.Command;

public class DataTransporter implements IDataTransporter {

	@Override
	public void addDataTransporterListener(IDataTransporterListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createCustomer(Command data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createTransaction(Command data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createVendor(Command data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCustomer(int customerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteVendor(int vendorId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editCustomer(Command data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editVendor(Command data) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean processCommand(Command command) {

		return false;
	}

	@Override
	public void removeDataTransporterListener(IDataTransporterListener listener) {
		// TODO Auto-generated method stub

	}

}
