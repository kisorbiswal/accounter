/**
 * 
 */
package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

/**
 * @author Fernandez
 * 
 */
public enum SelectItemType {

	CUSTOMER(ActionFactory.getNewCustomerAction()),

	VENDOR(ActionFactory.getNewVendorAction()),

	ACCOUNT(ActionFactory.getNewAccountAction()),

	BANK_ACCOUNT(ActionFactory.getNewBankAccountAction()),

	ADDRESS, BRANDING_THEME(ActionFactory.getNewBrandThemeAction()),

	BANK_NAME(ActionFactory.getNewbankAction()),

	CONTACTS,

	CREDIT_RATING(ActionFactory.getCreditRatingListAction()),

	CUSTOMER_GROUP(ActionFactory.getCustomerGroupListAction()),

	SALES_PERSON(ActionFactory.getNewSalesperSonAction()),

	ITEM(ActionFactory.getNewItemAction()),

	ITEM_GROUP(ActionFactory.getItemGroupListAction()),

	ITEM_TAX(ActionFactory.getNewItemTaxAction()),

	PAYEE(ActionFactory.getNewPayeeAction()),

	PAYMENT_METHODS,

	PAYMENT_TERMS(ActionFactory.getPaymentTermListAction()),

	PRICE_LEVEL(ActionFactory.getPriceLevelListAction()),

	SHIPPING_METHOD(ActionFactory.getShippingMethodListAction()),

	SHIPPING_TERMS(ActionFactory.getShippingTermListAction()),

	TAX_AGENCY(ActionFactory.getNewTAXAgencyAction()),

	// TAX_CODE(ActionFactory.getManageSalesTaxCodesAction()),
	TAXITEM(ActionFactory.getManageSalesTaxItemsAction()),

	TAX_GROUP(ActionFactory.getManageSalesTaxGroupsAction()),

	VENDOR_GROUP(ActionFactory.getVendorGroupListAction()),

	VAT_ITEM(ActionFactory.getNewVatItemAction()),

	WAREHOUSE(ActionFactory.getWareHouseViewAction()),

	TAX_CODE(ActionFactory.getAddEditSalesTaxCodeAction()), SHIP_TO, PHONE, EMAIL, FAX, CURRENCY;

	private Action action;

	private AccounterAsyncCallback<IsSerializable> callback;

	private BaseDialog dialog;

	SelectItemType() {

	}

	SelectItemType(Action action) {
		this.action = action;
	}

	SelectItemType(AccounterAsyncCallback<IsSerializable> callback) {
		this.callback = callback;
	}

	public Action getAction() {
		return action;
	}

	SelectItemType(BaseDialog dialog) {

		this.dialog = dialog;
	}

	public BaseDialog getDialog() {
		return dialog;
	}

}
