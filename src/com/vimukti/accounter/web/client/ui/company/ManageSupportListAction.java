package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AccounterClassListDialog;
import com.vimukti.accounter.web.client.ui.ItemGroupListDialog;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;
import com.vimukti.accounter.web.client.ui.PaymentTermListDialog;
import com.vimukti.accounter.web.client.ui.PriceLevelListDialog;
import com.vimukti.accounter.web.client.ui.ShippingMethodListDialog;
import com.vimukti.accounter.web.client.ui.ShippingTermListDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.customers.CurrencyGroupListDialog;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;
import com.vimukti.accounter.web.client.ui.vendors.VendorGroupListDialog;

public class ManageSupportListAction extends Action {

	public static final int TYPE_PAYMENT_TERMS = 1;
	public static final int TYPE_CUSTOMER_GROUPS = 2;
	public static final int TYPE_VENDOR_GROUPS = 3;
	public static final int TYPE_ITEM_GROUPS = 4;
	public static final int TYPE_SHIPPING_METHODS = 5;
	public static final int TYPE_SHIPPING_TERMS = 6;
	public static final int TYPE_CLASSES = 7;
	public static final int TYPE_LOCATIONS = 8;
	public static final int TYPE_PRICE_LEVELS = 9;
	public static final int TYPE_CURRENCY_GROUPS = 10;

	private int type;

	public ManageSupportListAction(int type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
//		GWT.runAsync(new RunAsyncCallback() {
//
//			public void onSuccess() {
//				BaseDialog<? extends IAccounterCore> dialog = null;
//				switch (type) {
//				case TYPE_PAYMENT_TERMS:
//					dialog = new PaymentTermListDialog();
//					break;
//				case TYPE_CUSTOMER_GROUPS:
//					dialog = new CustomerGroupListDialog(messages
//							.manageCustomerGroup(Global.get().Customer()),
//							messages.toAddPayeeGroup(Global.get().Customer()));
//					break;
//				case TYPE_VENDOR_GROUPS:
//					dialog = new VendorGroupListDialog();
//					break;
//				case TYPE_ITEM_GROUPS:
//					dialog = new ItemGroupListDialog(
//							messages.manageItemGroup(), messages
//									.toAddItemGroup());
//					break;
//				case TYPE_SHIPPING_TERMS:
//					dialog = new ShippingTermListDialog(messages
//							.manageShippingTermList(), messages
//							.toAddShippingTerm());
//					break;
//				case TYPE_SHIPPING_METHODS:
//					dialog = new ShippingMethodListDialog(messages
//							.manageShippingMethodList(), messages
//							.toAddShippingMethod());
//					break;
//				case TYPE_CLASSES:
//					dialog = new AccounterClassListDialog(messages
//							.manageAccounterClass(), messages
//							.toAddAccounterClass());
//					break;
//				case TYPE_LOCATIONS:
//					dialog = new LocationGroupListDialog(messages
//							.manageLocationGroup(Global.get().Location()),
//							messages.toAddLocation());
//					break;
//				case TYPE_PRICE_LEVELS:
//					dialog = new PriceLevelListDialog(messages
//							.managePriceLevelListGroup(), " ");
//					break;
//				case TYPE_CURRENCY_GROUPS:
//					dialog = new CurrencyGroupListDialog(messages
//							.manageCurrency(), messages.toAddCurrencyGroup());
//					break;
//				}
//				if (dialog != null) {
//					dialog.center();
//				}
//
//			}
//
//			public void onFailure(Throwable e) {
//				Accounter.showError(Global.get().messages()
//						.unableToshowtheview());
//			}
//		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		FinanceMenuImages financeMenuImages = Accounter.getFinanceMenuImages();
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return financeMenuImages.paymentTermsList();
		case TYPE_CUSTOMER_GROUPS:
			return financeMenuImages.customers();
		case TYPE_VENDOR_GROUPS:
			return financeMenuImages.vendors();
		case TYPE_ITEM_GROUPS:
			return financeMenuImages.items();
		case TYPE_SHIPPING_TERMS:
			return financeMenuImages.shippingTermList();
		case TYPE_SHIPPING_METHODS:
			return financeMenuImages.shippingMethodsList();
		case TYPE_PRICE_LEVELS:
			return financeMenuImages.priceLevelList();
		case TYPE_CURRENCY_GROUPS:
			break;
		}
		return null;
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return "paymentTerms";
		case TYPE_CUSTOMER_GROUPS:
			return "customerGroupList";
		case TYPE_VENDOR_GROUPS:
			return "vendorGroupList";
		case TYPE_ITEM_GROUPS:
			return "itemGroupList";
		case TYPE_SHIPPING_TERMS:
			return "shippingTermsList";
		case TYPE_SHIPPING_METHODS:
			return "shippingMethodsList";
		case TYPE_CLASSES:
			return "accounter-Class-List";
		case TYPE_LOCATIONS:
			return "location-group-list";
		case TYPE_PRICE_LEVELS:
			return "priceLevels";
		case TYPE_CURRENCY_GROUPS:
			return "currencyGroupList";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return "payment_tem-list";
		case TYPE_CUSTOMER_GROUPS:
			return "customer-group";
		case TYPE_VENDOR_GROUPS:
			return "vendor_group-list";
		case TYPE_ITEM_GROUPS:
			return "item-group";
		case TYPE_SHIPPING_TERMS:
			return "shipping_term-list";
		case TYPE_SHIPPING_METHODS:
			return "shipping_method-list";
		case TYPE_CLASSES:
			return "accounter-Class-List";
		case TYPE_LOCATIONS:
			return "location-group-list";
		case TYPE_PRICE_LEVELS:
			return "price_level-list";
		case TYPE_CURRENCY_GROUPS:
			return "currency-group";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return messages.paymentTermList();
		case TYPE_CUSTOMER_GROUPS:
			return messages.payeeGroupList(Global.get().Customer());
		case TYPE_VENDOR_GROUPS:
			return Global.get().messages()
					.payeeGroupList(Global.get().Vendor());
		case TYPE_ITEM_GROUPS:
			return messages.itemGroupList();
		case TYPE_SHIPPING_TERMS:
			return messages.shippingTermList();
		case TYPE_SHIPPING_METHODS:
			return messages.shippingMethodList();
		case TYPE_CLASSES:
			return messages.accounterClassList();
		case TYPE_LOCATIONS:
			return messages.locationGroupList(Global.get().Location());
		case TYPE_PRICE_LEVELS:
			return messages.priceLevelList();
		case TYPE_CURRENCY_GROUPS:
			messages.currencyList();
		}
		return null;
	}

	public static ManageSupportListAction paymentTerms() {
		return new ManageSupportListAction(TYPE_PAYMENT_TERMS);
	}

	public static ManageSupportListAction customerGroups() {
		return new ManageSupportListAction(TYPE_CUSTOMER_GROUPS);
	}

	public static ManageSupportListAction vendorGroups() {
		return new ManageSupportListAction(TYPE_VENDOR_GROUPS);
	}

	public static ManageSupportListAction itemGroups() {
		return new ManageSupportListAction(TYPE_ITEM_GROUPS);
	}

	public static ManageSupportListAction shippingTerms() {
		return new ManageSupportListAction(TYPE_SHIPPING_TERMS);
	}

	public static ManageSupportListAction shippingMethods() {
		return new ManageSupportListAction(TYPE_SHIPPING_METHODS);
	}

	public static ManageSupportListAction classes() {
		return new ManageSupportListAction(TYPE_CLASSES);
	}

	public static ManageSupportListAction priceLevels() {
		return new ManageSupportListAction(TYPE_PRICE_LEVELS);
	}

	public static ManageSupportListAction locations() {
		return new ManageSupportListAction(TYPE_LOCATIONS);
	}

	public static ManageSupportListAction currencyGroups() {
		return new ManageSupportListAction(TYPE_CURRENCY_GROUPS);
	}

}
