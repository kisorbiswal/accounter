package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
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
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCashExpenseCommand extends NewAbstractTransactionCommand {
	ClientCashPurchase cashPurchase;

	@Override
	protected String getWelcomeMessage() {
		return cashPurchase.getID() == 0 ? getMessages().create(
				getConstants().cashExpense())
				: "Update Cash Expense Command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return cashPurchase.getID() == 0 ? getMessages().readyToCreate(
				getConstants().cashExpense())
				: "Cash expense is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CASH_EXPENSE,
						context.getCompany()));
		get(CURRENCY_FACTOR).setDefaultValue(1.0);

	}

	@Override
	public String getSuccessMessage() {
		return cashPurchase.getID() == 0 ? getMessages().createSuccessfully(
				getConstants().cashExpense()) : getMessages()
				.updateSuccessfully(getConstants().cashExpense());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getConstants().Vendor()), getConstants().vendor(), false, true,
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

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});

		list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				ClientCurrency primaryCurrency = getPreferences()
						.getPrimaryCurrency();
				Currency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency.getFormalName();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getConstants().paymentMethod()), getConstants()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
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
						getConstants().cash(), getConstants().creditCard(),
						getConstants().directDebit(),
						getConstants().masterCard(),
						getConstants().onlineBanking(),
						getConstants().standingOrder(),
						getConstants().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getType() == Account.TYPE_BANK
									|| e.getType() == Account.TYPE_OTHER_CURRENT_ASSET) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().bankAccounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});
		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select accountItems", getConstants().Account(), true,
				true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}
		});
		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
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
		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

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
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

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
		List<ClientTransactionItem> allrecords = new ArrayList<ClientTransactionItem>();
		allrecords.addAll(items);
		allrecords.addAll(accounts);
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : allrecords) {
				if (taxCode != null) {
					item.setTaxCode(taxCode.getID());
				}
			}
		}

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		double[] result = getTransactionTotal(context, isVatInclusive,
				allrecords, true);
		makeResult.add("Net Amount: " + result[0]);
		if (context.getPreferences().isTrackTax()) {
			makeResult.add("Total Tax: " + result[1]);
		}
		makeResult.add("Total: " + (result[0] + result[1]));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		cashPurchase.setType(ClientTransaction.TYPE_CASH_EXPENSE);
		Vendor vendor = get(VENDOR).getValue();
		cashPurchase.setVendor(vendor.getID());
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashPurchase.setPaymentMethod(paymentMethod);
		Account account = get(PAY_FROM).getValue();
		cashPurchase.setPayFrom(account.getID());
		ClientFinanceDate date = get(DATE).getValue();
		cashPurchase.setDate(date.getDate());
		String number = get(NUMBER).getValue();
		cashPurchase.setNumber(number);
		String memoText = get(MEMO).getValue();
		cashPurchase.setMemo(memoText);

		items.addAll(accounts);

		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				cashPurchase.setCurrency(currency.getID());
			}
		}

		cashPurchase.setTransactionItems(items);
		double factor = get(CURRENCY_FACTOR).getValue();
		cashPurchase.setCurrencyFactor(factor);
		updateTotals(context, cashPurchase, false);
		create(cashPurchase, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Cash Expense to update.");
				return "Expenses List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientCashPurchase transactionByNum = (ClientCashPurchase) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.CASHPURCHASE);
			if (transactionByNum == null) {
				addFirstMessage(context, "Select a Cash Expense to update.");
				return "Expenses List " + string;
			}
			cashPurchase = transactionByNum;
			setValues();
		} else {
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
		List<ClientTransactionItem> transactionItems = cashPurchase
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		get(ACCOUNTS).setValue(accounts);
		get(ITEMS).setValue(items);
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getVendor(),
						AccounterCoreType.VENDOR));
		get(PAYMENT_METHOD).setValue(cashPurchase.getPaymentMethod());
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(DATE).setValue(cashPurchase.getDate());
		get(NUMBER).setValue(cashPurchase.getNumber());
		get(MEMO).setValue(cashPurchase.getMemo());
		get(CURRENCY_FACTOR).setValue(cashPurchase.getCurrencyFactor());
	}
}
