/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientVendor;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class AbstractGlobal implements IGlobal {

	@Override
	public String Customer() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return constants().Customer().trim();
		case ClientCustomer.CLIENT:
			return constants().Client().trim();
		case ClientCustomer.TENANT:
			return constants().Tenant().trim();
		case ClientCustomer.DONAR:
			return constants().Donar().trim();
		case ClientCustomer.GUEST:
			return constants().Guest().trim();
		case ClientCustomer.MEMBER:
			return constants().Member().trim();
		case ClientCustomer.PATITEINT:
			return constants().Patient().trim();
		default:
			return constants().Customer().trim();
		}
	}

	@Override
	public String customer() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return constants().customer().trim();
		case ClientCustomer.CLIENT:
			return constants().client().trim();
		case ClientCustomer.TENANT:
			return constants().tenant().trim();
		case ClientCustomer.DONAR:
			return constants().donar().trim();
		case ClientCustomer.GUEST:
			return constants().guest().trim();
		case ClientCustomer.MEMBER:
			return constants().member().trim();
		case ClientCustomer.PATITEINT:
			return constants().patient().trim();
		default:
			return constants().customer().trim();
		}
	}

	@Override
	public String Account() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return constants().Account().trim();
		case ClientAccount.LEGAND:
			return constants().Ledger().trim();
		case ClientAccount.CATEGORY:
			return constants().Category().trim();
		default:
			return constants().Account().trim();
		}
	}

	@Override
	public String account() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return constants().account().trim();
		case ClientAccount.LEGAND:
			return constants().ledger().trim();
		case ClientAccount.CATEGORY:
			return constants().category().trim();
		default:
			return constants().account().trim();
		}
	}

	@Override
	public String Vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return constants().Supplier().trim();
		case ClientVendor.VENDOR:
			return constants().Vendor().trim();
		default:
			return constants().Vendor().trim();
		}
	}

	@Override
	public String vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return constants().supplier().trim();
		case ClientVendor.VENDOR:
			return constants().vendor().trim();
		default:
			return constants().vendor().trim();
		}
	}

	@Override
	public String Location() {

		int locationTrackingId = (int) preferences().getLocationTrackingId();

		switch (locationTrackingId) {
		case ClientLocation.LOCATION:
			return constants().location().trim();
		case ClientLocation.BUSINESS:
			return constants().buisiness().trim();
		case ClientLocation.DEPARTMENT:
			return constants().department().trim();
		case ClientLocation.DIVISION:
			return constants().division().trim();
		case ClientLocation.PROPERTY:
			return constants().property().trim();
		case ClientLocation.STORE:
			return constants().store().trim();
		case ClientLocation.TERRITORY:
			return constants().territory().trim();
		default:
			return constants().location().trim();
		}
	}

}
