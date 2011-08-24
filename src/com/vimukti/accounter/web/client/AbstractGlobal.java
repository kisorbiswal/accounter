/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;

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
			return constants().Customer();
		case ClientCustomer.CLIENT:
			return constants().Client();
		case ClientCustomer.TENANT:
			return constants().Tenant();
		case ClientCustomer.DONAR:
			return constants().Donar();
		case ClientCustomer.GUEST:
			return constants().Guest();
		case ClientCustomer.PATITEINT:
			return constants().Patitent();
		default:
			return constants().Customer();
		}
	}

	@Override
	public String customer() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return constants().customer();
		case ClientCustomer.CLIENT:
			return constants().client();
		case ClientCustomer.TENANT:
			return constants().Tenant();
		case ClientCustomer.DONAR:
			return constants().donar();
		case ClientCustomer.GUEST:
			return constants().guest();
		case ClientCustomer.PATITEINT:
			return constants().patitent();
		default:
			return constants().customer();
		}
	}

	@Override
	public String Account() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return constants().Account();
		case ClientAccount.LEGAND:
			return constants().Ledger();
		default:
			return constants().Account();
		}
	}

	@Override
	public String account() {
		int referCustomers = preferences().getReferAccounts();
		switch (referCustomers) {
		case ClientAccount.ACCOUNT:
			return Accounter.constants().account();
		case ClientAccount.LEGAND:
			return constants().ledger();
		default:
			return Accounter.constants().Account();
		}
	}

	@Override
	public String Vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return constants().Supplier();
		case ClientVendor.VENDOR:
			return constants().Vendor();
		default:
			return constants().Vendor();
		}
	}

	@Override
	public String vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return constants().supplier();
		case ClientVendor.VENDOR:
			return constants().vendor();
		default:
			return constants().vendor();
		}
	}

	@Override
	public String Location() {

		return null;
	}

}
