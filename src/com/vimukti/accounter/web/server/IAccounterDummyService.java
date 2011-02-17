package com.vimukti.accounter.web.server;

import com.google.gwt.user.client.rpc.RemoteService;
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
import com.vimukti.accounter.web.client.core.ClientVATAgency;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

public interface IAccounterDummyService extends RemoteService {

	public ClientAccount getClientAccount();

	public ClientCustomer getClientCustomer();

	public ClientItem getClientItem();

	public ClientTAXGroup getClientTaxGroup();

	// public ClientTaxCode getClientTaxCode();

	public ClientCustomerGroup getClientCustomerGroup();

	public ClientVendorGroup getClientVendorGroup();

	public ClientPaymentTerms getClientPaymentTerms();

	public ClientShippingMethod getClientShippingMethod();

	public ClientShippingTerms getClientShippingTerms();

	public ClientPriceLevel getClientPriceLevel();

	public ClientItemGroup getClientItemGroup();

	public ClientSalesPerson getClientSalesPerson();

	public ClientCreditRating getClientCreditRating();

	public ClientFiscalYear getClientFiscalYear();

	public ClientVendor getClientVendor();

	public ClientBank getClientBank();

	// public ClientTaxAgency getClientTaxAgency();

	public AccounterCommand getAccounterCommand();

	public ClientTAXAgency getClientTAXAgency();

	public ClientTAXCode getClientTAXCode();

	public ClientVATReturn getClientVATReturn();

	public ClientVATReturnBox getClientVATReturnBox();

	public ClientTAXGroup getClientVATGroup();

	public ClientTAXItem getClientVATItem();

	public ClientTAXItemGroup getClientVATItemGroup();

	public ClientFixedAsset getClientFixedAsset();

	public ClientDepreciation getClientDepreciation();

	public InvalidOperationException getInvalidOperationException();
}
