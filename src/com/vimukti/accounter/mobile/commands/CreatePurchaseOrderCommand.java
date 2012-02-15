package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class CreatePurchaseOrderCommand extends AbstractTransactionCommand {

	public static final String STATUS = "status";
	public static final String VENDOR_ORDER_NO = "vendorOrderNo";
	ClientPurchaseOrder purchaseOrder;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Purchase order to update.");
				return "Invoices List";
			}
			purchaseOrder = getTransaction(string,
					AccounterCoreType.PURCHASEORDER, context);

			if (purchaseOrder == null) {
				addFirstMessage(context, "Select a purchase order to update.");
				return "Invoices List " + string;
			}
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			purchaseOrder = new ClientPurchaseOrder();
		}
		setTransaction(purchaseOrder);
		return null;
	}

	private void setValues() {
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> transactionItems = purchaseOrder
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		get(ITEMS).setValue(items);
		get(ACCOUNTS).setValue(accounts);
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(purchaseOrder.getVendor(),
						AccounterCoreType.VENDOR));
		get(DATE).setValue(purchaseOrder.getDate());
		get(PHONE).setValue(purchaseOrder.getPhone());
		get(STATUS).setValue(purchaseOrder.getStatus());
		get(NUMBER).setValue(purchaseOrder.getNumber());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(
						purchaseOrder.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(DUE_DATE).setValue(
				new ClientFinanceDate(purchaseOrder.getDueDate()));
		get(RECIEVED_DATE).setValue(purchaseOrder.getDate());
		get(DISPATCH_DATE).setValue(
				new ClientFinanceDate(purchaseOrder.getDespatchDate()));
		get(ORDER_NO).setValue(purchaseOrder.getPurchaseOrderNumber());
		get(BILL_TO).setValue(purchaseOrder.getShippingAddress());
		/* get(CURRENCY_FACTOR).setValue(purchaseOrder.getCurrencyFactor()); */
		get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(purchaseOrder));
		get(MEMO).setValue(purchaseOrder.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return purchaseOrder.getID() == 0 ? getMessages().create(
				getMessages().purchaseOrder())
				: "Update purchase order command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return purchaseOrder.getID() == 0 ? getMessages().readyToCreate(
				getMessages().purchaseOrder())
				: "Purchase order is ready to create with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_PURCHASE_ORDER,
						context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(DISPATCH_DATE).setDefaultValue(new ClientFinanceDate());
		get(RECIEVED_DATE).setDefaultValue(new ClientFinanceDate());
		get(STATUS).setDefaultValue(getMessages().open());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */

	}

	@Override
	public String getSuccessMessage() {
		return purchaseOrder.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().purchaseOrder()) : getMessages()
				.updateSuccessfully(getMessages().purchaseOrder());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().Vendor(), false, true,
				null)

		{

			@Override
			protected String getSetMessage() {

				return getMessages().hasSelected(Global.get().vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name)
						|| e.getVendorNumber().startsWith(
								"" + getNumberFromString(name));
			}
		});

		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (context.getPreferences().isEnableMultiCurrency()) { return
		 * super.run(context, makeResult, list, actions); } else { return null;
		 * } }
		 * 
		 * @Override protected List<Currency> getLists(Context context) { return
		 * new ArrayList<Currency>(context.getCompany() .getCurrencies()); } });
		 * 
		 * list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		 * .pleaseSelect(getConstants().currency()), getConstants() .currency(),
		 * false, true) {
		 * 
		 * @Override protected String getDisplayValue(Double value) {
		 * ClientCurrency primaryCurrency = getPreferences()
		 * .getPrimaryCurrency(); Currency selc = get(CURRENCY).getValue();
		 * return "1 " + selc.getFormalName() + " = " + value + " " +
		 * primaryCurrency.getFormalName(); }
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if (get(CURRENCY).getValue()
		 * != null) { if (context.getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue())
		 * .equals(context.getPreferences() .getPrimaryCurrency())) { return
		 * super.run(context, makeResult, list, actions); } } return null;
		 * 
		 * } });
		 */

		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select accountItems", getMessages().Account(), true,
				true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getIsActive()
									&& Arrays.asList(Account.TYPE_EXPENSE,
											Account.TYPE_COST_OF_GOODS_SOLD,
											Account.TYPE_OTHER_EXPENSE)
											.contains(e.getType());
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected Currency getCurrency() {
				// TODO Auto-generated method stub
				return null;
			}

		});
		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getMessages().items(),
				true, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.getType() != Item.TYPE_SERVICE) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return false;
			}

			@Override
			protected double getCurrencyFactor() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			protected Currency getCurrency() {
				// TODO Auto-generated method stub
				return null;
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, true));
		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getMessages().status()), getMessages().status(), true, true,
				null) {

			@Override
			protected String getSetMessage() {

				return getMessages().hasSelected(getMessages().status());
			}

			@Override
			protected String getSelectString() {

				return getMessages().pleaseSelect(getMessages().status());
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().open());
				list.add(getMessages().completed());
				list.add(getMessages().cancelled());
				return list;
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getMessages().status());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected Payee getPayee() {
				return get(VENDOR).getValue();
			}

		});

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));
		list.add(new DateRequirement(DISPATCH_DATE, getMessages().pleaseEnter(
				getMessages().dispatchDate()), getMessages().dispatchDate(),
				true, true));
		list.add(new DateRequirement(RECIEVED_DATE, getMessages().pleaseEnter(
				getMessages().receivedDate()), getMessages().receivedDate(),
				true, true));
		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().orderNo()), getMessages().orderNo(), true, true));

		list.add(new NumberRequirement(VENDOR_ORDER_NO, getMessages()
				.pleaseEnter(
						Global.get().Vendor() + getMessages().orderNumber()),
				Global.get().Customer() + getMessages().orderNumber(), true,
				true));
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()) {
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
		purchaseOrder.setType(ClientTransaction.TYPE_PURCHASE_ORDER);

		Vendor vendor = get(VENDOR).getValue();
		ClientFinanceDate transaDate = get(DATE).getValue();
		purchaseOrder.setDate(transaDate.getDate());

		purchaseOrder.setVendor(vendor.getID());

		purchaseOrder.setPhone((String) get(PHONE).getValue());

		int statusNumber = 0;
		if (get(STATUS).getValue().equals(getMessages().open())) {
			statusNumber = 1;
		} else if (get(STATUS).equals(getMessages().completed())) {
			statusNumber = 2;
		} else if (get(STATUS).equals(getMessages().cancelled())) {
			statusNumber = 3;
		}
		purchaseOrder.setStatus(statusNumber);

		purchaseOrder.setNumber((String) get(NUMBER).getValue());

		PaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		if (newPaymentTerms != null)
			purchaseOrder.setPaymentTerm(newPaymentTerms.getID());

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		purchaseOrder.setDate(dueDate.getDate());

		ClientFinanceDate receivedDate = get(RECIEVED_DATE).getValue();
		purchaseOrder.setDate(receivedDate.getDate());

		ClientFinanceDate dispatchDate = get(DISPATCH_DATE).getValue();
		purchaseOrder.setDate(dispatchDate.getDate());
		String no = get(ORDER_NO).getValue();
		purchaseOrder.setPurchaseOrderNumber(no);
		ClientAddress address = get(BILL_TO).getValue();
		purchaseOrder.setShippingAddress(address);
		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * purchaseOrder.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * purchaseOrder.setCurrencyFactor(factor); }
		 */

		items.addAll(accounts);
		purchaseOrder.setTransactionItems(items);
		updateTotals(context, purchaseOrder, false);

		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		String memo = get(MEMO).getValue();
		purchaseOrder.setMemo(memo);
		create(purchaseOrder, context);
		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(
					context,
					"Transaction total can not zero or less than zero.So you can't finish this command");
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Currency getCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

}