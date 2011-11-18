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
			return messages().Customer().trim();
		case ClientCustomer.CLIENT:
			return messages().Client().trim();
		case ClientCustomer.TENANT:
			return messages().Tenant().trim();
		case ClientCustomer.DONAR:
			return messages().Donar().trim();
		case ClientCustomer.GUEST:
			return messages().Guest().trim();
		case ClientCustomer.MEMBER:
			return messages().Member().trim();
		case ClientCustomer.PATITEINT:
			return messages().Patient().trim();
		default:
			return messages().Customer().trim();
		}
	}

	@Override
	public String customer() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return messages().customer().trim();
		case ClientCustomer.CLIENT:
			return messages().client().trim();
		case ClientCustomer.TENANT:
			return messages().tenant().trim();
		case ClientCustomer.DONAR:
			return messages().donar().trim();
		case ClientCustomer.GUEST:
			return messages().guest().trim();
		case ClientCustomer.MEMBER:
			return messages().member().trim();
		case ClientCustomer.PATITEINT:
			return messages().patient().trim();
		default:
			return messages().customer().trim();
		}
	}

	@Override
	public String Account() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return messages().Account().trim();
		case ClientAccount.LEGAND:
			return messages().Ledger().trim();
		case ClientAccount.CATEGORY:
			return messages().Category().trim();
		default:
			return messages().Account().trim();
		}
	}

	@Override
	public String Accounts() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return messages().Accounts().trim();
		case ClientAccount.LEGAND:
			return messages().Ledgers().trim();
		case ClientAccount.CATEGORY:
			return messages().Categories().trim();
		default:
			return messages().Accounts().trim();
		}
	}

	@Override
	public String account() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return messages().account().trim();
		case ClientAccount.LEGAND:
			return messages().ledger().trim();
		case ClientAccount.CATEGORY:
			return messages().category().trim();
		default:
			return messages().account().trim();
		}
	}

	@Override
	public String Vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return messages().Supplier().trim();
		case ClientVendor.VENDOR:
			return messages().Vendor().trim();
		default:
			return messages().Vendor().trim();
		}
	}

	@Override
	public String vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return messages().supplier().trim();
		case ClientVendor.VENDOR:
			return messages().vendor().trim();
		default:
			return messages().vendor().trim();
		}
	}

	@Override
	public String Location() {

		int locationTrackingId = (int) preferences().getLocationTrackingId();

		switch (locationTrackingId) {
		case ClientLocation.LOCATION:
			return messages().location().trim();
		case ClientLocation.BUSINESS:
			return messages().buisiness().trim();
		case ClientLocation.DEPARTMENT:
			return messages().department().trim();
		case ClientLocation.DIVISION:
			return messages().division().trim();
		case ClientLocation.PROPERTY:
			return messages().property().trim();
		case ClientLocation.STORE:
			return messages().store().trim();
		case ClientLocation.TERRITORY:
			return messages().territory().trim();
		default:
			return messages().location().trim();
		}
	}

}
