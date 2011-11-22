package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCashPurchaseCommand extends NewAbstractTransactionCommand {
	ClientCashPurchase cashPurchase;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Cash Purchase to update.");
				return "Bills And Expenses List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientCashPurchase transactionByNum = (ClientCashPurchase) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.CASHPURCHASE);
			if (transactionByNum == null) {
				addFirstMessage(context, "Select a Cash Purchase to update.");
				return "Bills And Expenses List " + string;
			}
			cashPurchase = transactionByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			cashPurchase = new ClientCashPurchase();
		}
		return null;
	}

	private void setValues() {
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : cashPurchase
				.getTransactionItems()) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		get(ITEMS).setValue(items);
		get(ACCOUNTS).setValue(accounts);
		get(DATE).setValue(cashPurchase.getDate());
		get(NUMBER).setValue(cashPurchase.getNumber());
		/* get(CURRENCY_FACTOR).setValue(cashPurchase.getCurrencyFactor()); */
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getVendor(),
						AccounterCoreType.VENDOR));
		get(CONTACT).setValue(toServerContact(cashPurchase.getContact()));
		get(PHONE).setValue(cashPurchase.getPhone());
		get(MEMO).setValue(cashPurchase.getMemo());
		get(PAYMENT_METHOD).setValue(cashPurchase.getPaymentMethod());
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(CHEQUE_NO).setValue(cashPurchase.getCheckNumber());
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(cashPurchase.getDeliveryDate()));
	}

	@Override
	protected String getWelcomeMessage() {
		return cashPurchase.getID() == 0 ? getMessages().create(
				getMessages().cashPurchase())
				: "Update Cash Purchase command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return cashPurchase.getID() == 0 ? getMessages().readyToCreate(
				getMessages().cashPurchase())
				: "Cash Purchase is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CASH_PURCHASE,
						context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());

		Vendor v = (Vendor) get(VENDOR).getValue();
		if (v != null) {
			Set<Contact> contacts2 = v.getContacts();
			if (contacts2 != null)
				for (Contact c : contacts2) {
					get(CONTACT).setDefaultValue(c);
				}
		}
		get(MEMO).setDefaultValue("");

		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return cashPurchase.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().cashPurchase()) : getMessages()
				.updateSuccessfully(getMessages().cashPurchase());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().vendor(), false, true,
				null)

		{

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return new ArrayList<Vendor>(context.getCompany().getVendors());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
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

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));
		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));
		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getMessages().paymentMethod()), getMessages()
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

				/*
				 * Map<String, String> paymentMethods =
				 * context.getClientCompany() .getPaymentMethods(); List<String>
				 * paymentMethod = new ArrayList<String>(
				 * paymentMethods.values());
				 */
				String payVatMethodArray[] = new String[] {
						getMessages().cash(), getMessages().creditCard(),
						getMessages().check(), getMessages().directDebit(),
						getMessages().masterCard(),
						getMessages().onlineBanking(),
						getMessages().standingOrder(),
						getMessages().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(), getMessages().bankAccount(),
				false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("Create BankAccount", "Bank"));
				list.add(new UserCommand("Create BankAccount",
						"Create Other CurrentAsset Account",
						"Other Current Asset"));
			}

			@Override
			protected List<Account> getLists(Context context) {

				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(Account.TYPE_EXPENSE,
									Account.TYPE_COST_OF_GOODS_SOLD,
									Account.TYPE_OTHER_EXPENSE).contains(
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
				return getMessages()
						.youDontHaveAny(getMessages().bankAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}
		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select accountItems", getMessages().Account(), true,
				true) {
			@Override
			public List<Account> getAccounts(Context context) {
				Set<Account> accounts = context.getCompany().getAccounts();
				ArrayList<Account> arrayList = new ArrayList<Account>();
				for (Account account : accounts) {
					if (filter(account)) {
						arrayList.add(account);
					}
				}
				return arrayList;

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
						items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return false;
			}
		});

		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<Contact> getLists(Context context) {
				return new ArrayList<Contact>(
						((Vendor) NewCashPurchaseCommand.this.get(VENDOR)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((Vendor) get(VENDOR).getValue()).getName();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

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
				return e.getName().contains(name);
			}
		});

		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getMessages().checkNo()), getMessages().checkNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String paymentMethod = get(PAYMENT_METHOD).getValue();
				if (paymentMethod.equals(getMessages().check())) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
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
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		ClientFinanceDate date = get(DATE).getValue();
		cashPurchase.setDate(date.getDate());

		cashPurchase.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		String number = get(NUMBER).getValue();
		cashPurchase.setNumber(number);

		accounts.addAll(items);
		cashPurchase.setTransactionItems(accounts);

		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		/*
		 * if (preferences.isEnableMultiCurrency()) { Currency currency =
		 * get(CURRENCY).getValue(); if (currency != null) {
		 * cashPurchase.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * cashPurchase.setCurrencyFactor(factor); }
		 */
		Vendor vendor = get(VENDOR).getValue();
		cashPurchase.setVendor(vendor.getID());

		Contact contact = get(CONTACT).getValue();
		cashPurchase.setContact(toClientContact(contact));

		String phone = get(PHONE).getValue();
		cashPurchase.setPhone(phone);

		String memo = get(MEMO).getValue();
		cashPurchase.setMemo(memo);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashPurchase.setPaymentMethod(paymentMethod);

		Account account = get(PAY_FROM).getValue();
		cashPurchase.setPayFrom(account.getID());

		String chequeNo = get(CHEQUE_NO).getValue();
		if (paymentMethod.equals(getMessages().check())) {
			cashPurchase.setCheckNumber(chequeNo);
		}

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		cashPurchase.setDeliveryDate(deliveryDate.getDate());

		updateTotals(context, cashPurchase, false);
		create(cashPurchase, context);
		return null;
	}

	public boolean filter(Account account) {

		if (account.getType() != Account.TYPE_CASH
				&& account.getType() != Account.TYPE_BANK
				&& account.getType() != Account.TYPE_INVENTORY_ASSET
				&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
				&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
				&& account.getType() != Account.TYPE_INCOME
				&& account.getType() != Account.TYPE_OTHER_INCOME
				&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
				&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
				&& account.getType() != Account.TYPE_OTHER_ASSET
				&& account.getType() != Account.TYPE_EQUITY
				&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
			return true;
		} else {
			return false;
		}
	}
}