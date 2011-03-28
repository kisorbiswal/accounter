package com.vimukti.accounter.web.server;

import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientDepreciation;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

@SuppressWarnings("serial")
public class AccounterDummyServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterDummyService {

	@Override
	public ClientAccount getClientAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientCreditRating getClientCreditRating() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientCustomer getClientCustomer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientCustomerGroup getClientCustomerGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFiscalYear getClientFiscalYear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientItem getClientItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientItemGroup getClientItemGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientPaymentTerms getClientPaymentTerms() {
		return null;
	}

	@Override
	public ClientPriceLevel getClientPriceLevel() {
		return null;
	}

	@Override
	public ClientSalesPerson getClientSalesPerson() {
		return null;
	}

	@Override
	public ClientShippingMethod getClientShippingMethod() {
		return null;
	}

	@Override
	public ClientShippingTerms getClientShippingTerms() {
		return null;
	}

	// @Override
	// public ClientTaxCode getClientTaxCode() {
	// return null;
	// }

	@Override
	public ClientTAXGroup getClientTaxGroup() {
		return null;
	}

	@Override
	public ClientVendorGroup getClientVendorGroup() {
		return null;
	}

	@Override
	public ClientBank getClientBank() {
		return null;
	}

	@Override
	public ClientVendor getClientVendor() {
		return null;
	}

//	@Override
//	public ClientTaxAgency getClientTaxAgency() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	// @Override
	// public AccounterCore getAccounterCore() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public ClientTAXAgency getClientTAXAgency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientTAXCode getClientTAXCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientTAXGroup getClientVATGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientTAXItem getClientVATItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientTAXItemGroup getClientVATItemGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientVATReturn getClientVATReturn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientVATReturnBox getClientVATReturnBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFixedAsset getClientFixedAsset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDepreciation getClientDepreciation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InvalidOperationException getInvalidOperationException() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCommand getAccounterCommand() {
		// TODO Auto-generated method stub
		return null;
	}

}
