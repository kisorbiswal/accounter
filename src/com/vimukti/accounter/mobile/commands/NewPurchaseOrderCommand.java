package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public class NewPurchaseOrderCommand extends NewAbstractTransactionCommand {

	public static final String STATUS = "status";
	public static final String VENDOR_ORDER_NO = "vendorOrderNo";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getConstants().purchaseOrder());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().purchaseOrder());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_PURCHASE_ORDER,
						context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		ArrayList<ClientPaymentTerms> paymentTerms = context.getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(DISPATCH_DATE).setDefaultValue(new ClientFinanceDate());
		get(RECIEVED_DATE).setDefaultValue(new ClientFinanceDate());
		get(STATUS).setDefaultValue(getConstants().open());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().purchaseOrder());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new VendorRequirement(VENDOR, getMessages()
				.pleaseSelectVendor(getConstants().Vendor()), getConstants()
				.vendor(), false, true, null)

		{

			@Override
			protected String getSetMessage() {

				return getMessages().hasSelected(Global.get().vendor());
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return context.getClientCompany().getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().vendor());
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				return e.getName().startsWith(name)
						|| e.getVendorNumber().startsWith(
								"" + getNumberFromString(name));
			}
		});
		list.add(new TransactionItemAccountsRequirement(ACCOUNTS,
				"please select accountItems", getConstants().Account(), true,
				true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_EXPENSE
								|| e.getType() == ClientAccount.TYPE_FIXED_ASSET
								|| e.getType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD
								|| e.getType() == ClientAccount.TYPE_OTHER_EXPENSE) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}
		});
		list.add(new TransactionItemItemsRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				true, true, true) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}
		});

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, true));
		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getConstants().status()), getConstants().status(), true, true,
				null) {

			@Override
			protected String getSetMessage() {

				return getMessages().hasSelected(getConstants().status());
			}

			@Override
			protected String getSelectString() {

				return getMessages().pleaseSelect(getConstants().status());
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().open());
				list.add(getConstants().completed());
				list.add(getConstants().cancelled());
				return list;
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getConstants().status());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<ClientPaymentTerms> getLists(Context context) {
				return getClientCompany().getPaymentsTerms();
			}
		});

		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(
						((ClientVendor) NewPurchaseOrderCommand.this
								.get(VENDOR).getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientCustomer) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getConstants().dueDate()), getConstants().dueDate(), true, true));
		list.add(new DateRequirement(DISPATCH_DATE, getMessages().pleaseEnter(
				getConstants().dispatchDate()), getConstants().dispatchDate(),
				true, true));
		list.add(new DateRequirement(RECIEVED_DATE, getMessages().pleaseEnter(
				getConstants().receivedDate()), getConstants().receivedDate(),
				true, true));
		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getConstants().orderNo()), getConstants().orderNo(), true, true));

		list.add(new NumberRequirement(VENDOR_ORDER_NO, getMessages()
				.pleaseEnter(
						Global.get().Vendor() + getConstants().orderNumber()),
				Global.get().Customer() + getConstants().orderNumber(), true,
				true));
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& !getClientCompany().getPreferences()
								.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context
						.getClientCompany().getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		ClientPurchaseOrder newPurchaseOrder = new ClientPurchaseOrder();
		newPurchaseOrder.setType(ClientTransaction.TYPE_PURCHASE_ORDER);

		ClientVendor vendor = get(VENDOR).getValue();
		ClientFinanceDate transaDate = get(DATE).getValue();
		newPurchaseOrder.setDate(transaDate.getDate());

		newPurchaseOrder.setVendor(vendor.getID());

		newPurchaseOrder.setPhone((String) get(PHONE).getValue());

		int statusNumber = 0;
		if (get(STATUS).getValue().equals(getConstants().open())) {
			statusNumber = 1;
		} else if (get(STATUS).equals(getConstants().completed())) {
			statusNumber = 2;
		} else if (get(STATUS).equals(getConstants().cancelled())) {
			statusNumber = 3;
		}
		newPurchaseOrder.setStatus(statusNumber);

		newPurchaseOrder.setNumber((String) get(NUMBER).getValue());

		ClientPaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		if (newPaymentTerms != null)
			newPurchaseOrder.setPaymentTerm(newPaymentTerms.getID());

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		newPurchaseOrder.setDate(dueDate.getDate());

		ClientFinanceDate receivedDate = get(RECIEVED_DATE).getValue();
		newPurchaseOrder.setDate(receivedDate.getDate());

		ClientFinanceDate dispatchDate = get(DISPATCH_DATE).getValue();
		newPurchaseOrder.setDate(dispatchDate.getDate());
		String no = get(ORDER_NO).getValue();
		newPurchaseOrder.setPurchaseOrderNumber(no);
		ClientAddress address = get(BILL_TO).getValue();
		newPurchaseOrder.setShippingAddress(address);
		if (context.getClientCompany().getPreferences().isEnableMultiCurrency()) {
			ClientCurrency currency = get(CURRENCY).getValue();
			if (currency != null) {
				newPurchaseOrder.setCurrency(currency.getID());
			}

		}
		newPurchaseOrder.setCurrencyFactor(1.0);
		items.addAll(accounts);
		newPurchaseOrder.setTransactionItems(items);
		updateTotals(context, newPurchaseOrder, false);

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			newPurchaseOrder.setAmountsIncludeVAT(isVatInclusive);
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		String memo = get(MEMO).getValue();
		newPurchaseOrder.setMemo(memo);
		create(newPurchaseOrder, context);
		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context,
					"Transaction total can not zero or less than zero.So you can't finish this command");
		}
	}
}