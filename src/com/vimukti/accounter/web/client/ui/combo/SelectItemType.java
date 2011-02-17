/**
 * 
 */
package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

/**
 * @author Fernandez
 * 
 */
public enum SelectItemType {

	CUSTOMER(CustomersActionFactory.getNewCustomerAction()),

	VENDOR(VendorsActionFactory.getNewVendorAction()),

	ACCOUNT(CompanyActionFactory.getNewAccountAction()),

	BANK_ACCOUNT(BankingActionFactory.getNewBankAccountAction()),

	ADDRESS,

	BANK_NAME(CompanyActionFactory.getNewbankAction()),

	CONTACTS,

	CREDIT_RATING(CompanyActionFactory.getCreditRatingListAction()),

	CUSTOMER_GROUP(CompanyActionFactory.getCustomerGroupListAction()),

	SALES_PERSON(CompanyActionFactory.getNewSalesperSonAction()),

	ITEM(CompanyActionFactory.getNewItemAction()),

	ITEM_GROUP(CompanyActionFactory.getItemGroupListAction()),

	ITEM_TAX(CompanyActionFactory.getNewItemTaxAction()),

	PAYEE(CompanyActionFactory.getNewPayeeAction()),

	PAYMENT_METHODS,

	PAYMENT_TERMS(CompanyActionFactory.getPaymentTermListAction()),

	PRICE_LEVEL(CompanyActionFactory.getPriceLevelListAction()),

	SHIPPING_METHOD(CompanyActionFactory.getShippingMethodListAction()),

	SHIPPING_TERMS(CompanyActionFactory.getShippingTermListAction()),

	TAX_AGENCY(CompanyActionFactory.getNewTAXAgencyAction()),

	// TAX_CODE(CompanyActionFactory.getManageSalesTaxCodesAction()),
	TAXITEM(CompanyActionFactory.getManageSalesTaxItemsAction()),

	TAX_GROUP(CompanyActionFactory.getManageSalesTaxGroupsAction()),

	VENDOR_GROUP(CompanyActionFactory.getVendorGroupListAction()),

	VAT_ITEM(VatActionFactory.getNewVatItemAction()),

	TAX_CODE(CustomersActionFactory.getAddEditSalesTaxCodeAction());

	private Action action;
	@SuppressWarnings("unused")
	private AsyncCallback<IsSerializable> callback;
	@SuppressWarnings("unchecked")
	private BaseDialog dialog;

	SelectItemType() {

	}

	SelectItemType(Action action) {
		this.action = action;
	}

	SelectItemType(AsyncCallback<IsSerializable> callback) {
		this.callback = callback;
	}

	public Action getAction() {
		return action;
	}

	@SuppressWarnings("unchecked")
	SelectItemType(BaseDialog dialog) {

		this.dialog = dialog;
	}

	@SuppressWarnings("unchecked")
	public BaseDialog getDialog() {
		return dialog;
	}

}
