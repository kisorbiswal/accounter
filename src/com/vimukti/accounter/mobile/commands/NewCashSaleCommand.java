package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingMethodRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingTermRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewCashSaleCommand extends NewAbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";
	private static final String SHIPPING_TERMS = "shippingTerms";
	private static final String SHIPPING_METHODS = "shippingMethods";
	ClientCashSales cashSale;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Customer()),
				getMessages().payeeNumber(Global.get().Customer()), false,
				true, null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});

		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (getPreferences().isEnableMultiCurrency()) { return
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
		 * != null) { if (getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue()) .equals(getPreferences()
		 * .getPrimaryCurrency())) { return super.run(context, makeResult, list,
		 * actions); } } return null;
		 * 
		 * } });
		 */
		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getMessages().items(),
				true, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(getMessages().Account()),
				getMessages().Account(), true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account account) {
							if (account.getType() != ClientAccount.TYPE_CASH
									&& account.getType() != ClientAccount.TYPE_BANK
									&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
									&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
									&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
									&& account.getType() != ClientAccount.TYPE_EXPENSE
									&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
									&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
									&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
									&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
									&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
									&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
									&& account.getType() != ClientAccount.TYPE_EQUITY) {
								return true;
							} else {
								return false;
							}
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}
		});
		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return new ArrayList<String>(CommandUtils.getPaymentMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contacts(), true,
				true, null) {

			@Override
			protected List<Contact> getLists(Context context) {
				return new ArrayList<Contact>(
						((Customer) NewCashSaleCommand.this.get(CUSTOMER)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((Customer) get(CUSTOMER).getValue()).getName();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phone(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterNameOrNumber(getMessages().depositAccount()),
				getMessages().depositAccount(), false, true, null) {

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("Create BankAccount", "Bank"));
				list.add(new UserCommand("Create BankAccount",
						"Create Other CurrentAsset Account",
						"Other Current Asset"));
				list.add(new UserCommand("Create BankAccount",
						"Create CreditAccount", "CreditAccount"));
				list.add(new UserCommand("Create BankAccount",
						"Create FixedAsset Account", "FixedAsset"));
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().depositAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(Account.TYPE_BANK,
									Account.TYPE_OTHER_CURRENT_ASSET).contains(
									e.getType());
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				new ChangeListner<TAXCode>() {

					@Override
					public void onSelection(TAXCode value) {
						setTaxCodeToItems(value);
					}
				}) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& !getPreferences().isTaxPerDetailLine()) {
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

		list.add(new ShippingTermRequirement(SHIPPING_TERMS, getMessages()
				.pleaseEnterName(getMessages().shippingTerm()), getMessages()
				.shippingTerm(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().shippingTerm());
			}

			@Override
			protected List<ShippingTerms> getLists(Context context) {
				return new ArrayList<ShippingTerms>(context.getCompany()
						.getShippingTerms());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingTerms());
			}

			@Override
			protected boolean filter(ShippingTerms e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS, getMessages()
				.pleaseEnterName(getMessages().shippingMethod()), getMessages()
				.shippingMethod(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().shippingMethod());
			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				return new ArrayList<ShippingMethod>(context.getCompany()
						.getShippingMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingMethod());
			}

		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));
	}

	protected void setTaxCodeToItems(TAXCode value) {
		List<ClientTransactionItem> items = this.get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		List<ClientTransactionItem> allrecords = new ArrayList<ClientTransactionItem>();
		allrecords.addAll(items);
		allrecords.addAll(accounts);
		for (ClientTransactionItem clientTransactionItem : allrecords) {
			clientTransactionItem.setTaxCode(value.getID());
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		ClientFinanceDate date = get(DATE).getValue();
		cashSale.setDate(date.getDate());

		cashSale.setType(Transaction.TYPE_CASH_SALES);
		if (context.getCompany().getPreferences().getUseCustomerId()) {
			String number = get(NUMBER).getValue();
			cashSale.setNumber(number);
		}

		accounts.addAll(items);
		cashSale.setTransactionItems(accounts);

		// TODO Location
		// TODO Class
		ShippingTerms shippingTerms = get(SHIPPING_TERMS).getValue();
		cashSale.setShippingTerm(shippingTerms != null ? shippingTerms.getID()
				: 0);

		ShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		cashSale.setShippingMethod(shippingMethod != null ? shippingMethod
				.getID() : 0);

		Customer customer = get(CUSTOMER).getValue();
		cashSale.setCustomer(customer.getID());

		Contact contact = get(CONTACT).getValue();
		cashSale.setContact(toClientContact(contact));

		cashSale.setShippingAdress(getAddress(ClientAddress.TYPE_SHIP_TO,
				customer));
		cashSale.setShippingAdress(getAddress(ClientAddress.TYPE_BILL_TO,
				customer));

		String phone = get(PHONE).getValue();
		cashSale.setPhone(phone);

		String memo = get(MEMO).getValue();
		cashSale.setMemo(memo);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashSale.setPaymentMethod(paymentMethod);

		Account account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		cashSale.setDepositIn(account.getID());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		cashSale.setDeliverydate(deliveryDate.getDate());

		ClientCompanyPreferences preferences = context.getPreferences();
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			cashSale.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		/*
		 * if (preferences.isEnableMultiCurrency()) { Currency currency =
		 * get(CURRENCY).getValue(); if (currency != null) {
		 * cashSale.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * cashSale.setCurrencyFactor(factor); }
		 */

		// if (context.getCompany())
		// ClientTAXCode taxCode = get(TAXCODE).getValue();
		// cashSale.setTaxTotal(getTaxTotal(accounts, taxCode));
		double taxTotal = updateTotals(context, cashSale, true);
		cashSale.setTaxTotal(taxTotal);
		create(cashSale, context);

		return null;
	}

	public ClientAddress getAddress(int type, Customer customer) {
		for (Address address : customer.getAddress()) {

			if (address.getType() == type) {
				return toClientAddress(address);
			}

		}
		return null;
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_CASH_SALES,
						context.getCompany().getID());
		return nextTransactionNumber;
	}

	@Override
	protected String getWelcomeMessage() {
		return cashSale.getID() == 0 ? getMessages().creating(
				getMessages().cashSale()) : "Cash sale updating...";
	}

	@Override
	protected String getDetailsMessage() {
		return cashSale.getID() == 0 ? getMessages().readyToCreate(
				getMessages().cashSale())
				: "Cash sale is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(getNextTransactionNumber(context));
		Contact contact = new Contact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(PAYMENT_METHOD).setDefaultValue(getMessages().cash());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return cashSale.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().cashSale()) : getMessages().updateSuccessfully(
				getMessages().cashSale());
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
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Cash Sale to update.");
				return "Invoices List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientCashSales invoiceByNum = (ClientCashSales) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.CASHSALES);
			if (invoiceByNum == null) {
				addFirstMessage(context, "Select a Cash Sale to update.");
				return "Invoices List " + string;
			}
			cashSale = invoiceByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			cashSale = new ClientCashSales();
		}
		setTransaction(cashSale);
		return null;
	}

	private void setValues() {
		List<ClientTransactionItem> transactionItems = cashSale
				.getTransactionItems();
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ITEM) {
				items.add(clientTransactionItem);
			}
		}
		get(ITEMS).setValue(items);
		get(ACCOUNTS).setValue(accounts);
		get(DATE).setValue(cashSale.getDate());
		get(NUMBER).setValue(cashSale.getNumber());
		get(SHIPPING_TERMS).setValue(
				CommandUtils.getServerObjectById(cashSale.getShippingTerm(),
						AccounterCoreType.SHIPPING_TERM));
		get(SHIPPING_METHODS).setValue(
				CommandUtils.getServerObjectById(cashSale.getShippingMethod(),
						AccounterCoreType.SHIPPING_METHOD));
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(cashSale.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(CONTACT).setValue(toServerContact(cashSale.getContact()));
		get(PHONE).setValue(cashSale.getPhone());
		get(MEMO).setValue(cashSale.getMemo());
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						cashSale.getPaymentMethodForCommands(), getMessages()));
		get(DEPOSIT_OR_TRANSFER_TO).setValue(
				CommandUtils.getServerObjectById(cashSale.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(cashSale.getDeliverydate()));
		/* get(CURRENCY_FACTOR).setValue(cashSale.getCurrencyFactor()); */
	}

}
