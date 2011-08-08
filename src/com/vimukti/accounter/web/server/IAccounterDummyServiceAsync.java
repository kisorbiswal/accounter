package com.vimukti.accounter.web.server;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface IAccounterDummyServiceAsync {

	void getClientAccount(AsyncCallback<ClientAccount> callback);

	void getClientCreditRating(AsyncCallback<ClientCreditRating> callback);

	void getClientCustomer(AsyncCallback<ClientCustomer> callback);

	void getClientCustomerGroup(AsyncCallback<ClientCustomerGroup> callback);

	void getClientFiscalYear(AsyncCallback<ClientFiscalYear> callback);

	void getClientItem(AsyncCallback<ClientItem> callback);

	void getClientItemGroup(AsyncCallback<ClientItemGroup> callback);

	void getClientPaymentTerms(AsyncCallback<ClientPaymentTerms> callback);

	void getClientPriceLevel(AsyncCallback<ClientPriceLevel> callback);

	void getClientSalesPerson(AsyncCallback<ClientSalesPerson> callback);

	void getClientShippingMethod(AsyncCallback<ClientShippingMethod> callback);

	void getClientShippingTerms(AsyncCallback<ClientShippingTerms> callback);

	// void getClientTaxCode(AsyncCallback<ClientTaxCode> callback);

	void getClientTaxGroup(AsyncCallback<ClientTAXGroup> callback);

	void getClientVendorGroup(AsyncCallback<ClientVendorGroup> callback);

	void getClientVendor(AsyncCallback<ClientVendor> callback);

	void getClientBank(AsyncCallback<ClientBank> callback);

	// void getClientTaxAgency(AsyncCallback<ClientTaxAgency> callback);

	// void getAccounterCore(AsyncCallback<AccounterCore> callback);

	void getClientTAXAgency(AsyncCallback<ClientTAXAgency> callback);

	void getClientVATReturnBox(AsyncCallback<ClientVATReturnBox> callback);

	void getClientTAXCode(AsyncCallback<ClientTAXCode> callback);

	void getClientVATGroup(AsyncCallback<ClientTAXGroup> callback);

	void getClientVATItem(AsyncCallback<ClientTAXItem> callback);

	void getClientVATItemGroup(AsyncCallback<ClientTAXItemGroup> callback);

	void getClientVATReturn(AsyncCallback<ClientVATReturn> callback);

	void getClientFixedAsset(AsyncCallback<ClientFixedAsset> callback);

	void getClientDepreciation(AsyncCallback<ClientDepreciation> callback);

	void getAccounterCommand(AsyncCallback<AccounterCommand> callback);

}
