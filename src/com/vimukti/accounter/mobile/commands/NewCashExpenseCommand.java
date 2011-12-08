package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class NewCashExpenseCommand extends NewAbstractTransactionCommand {
	ClientCashPurchase cashPurchase;

	@Override
	protected String getWelcomeMessage() {
		return cashPurchase.getID() == 0 ? getMessages().create(
				getMessages().cashExpense())
				: "Update Cash Expense Command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return cashPurchase.getID() == 0 ? getMessages().readyToCreate(
				getMessages().cashExpense())
				: "Cash expense is ready to update with following details";
	}

	@Override
	public String getSuccessMessage() {
		return cashPurchase.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().cashExpense()) : getMessages()
				.updateSuccessfully(getMessages().cashExpense());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().vendor(), false, true,
				new ChangeListner<Vendor>() {

					@Override
					public void onSelection(Vendor value) {
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							NewCashExpenseCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}

					}
				})

		{

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
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

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected ClientCurrency getSelectedCurrency() {
				Vendor vendor = (Vendor) NewCashExpenseCommand.this.get(VENDOR)
						.getValue();
				return getCurrency(vendor.getCurrency().getID());
			}

		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

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
						getMessages().directDebit(),
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
		list.add(new AccountRequirement(PAY_FROM, getMessages().pleaseSelect(
				getMessages().bankAccount()), getMessages().bankAccount(),
				false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET)
									.contains(e.getType());
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
				return e.getName().contains(name);
			}
		});
		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select accountItems", getMessages().Account(), true,
				true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
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
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected Payee getPayee() {
				return (Vendor) NewCashExpenseCommand.this.get(VENDOR)
						.getValue();
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
					if (item.isIBuyThisItem()) {
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
			protected Payee getPayee() {
				return (Vendor) NewCashExpenseCommand.this.get(VENDOR)
						.getValue();
			}

			@Override
			protected double getCurrencyFactor() {
				return NewCashExpenseCommand.this.getCurrencyFactor();
			}

		});
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

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * cashPurchase.setCurrency(currency.getID()); } double factor =
		 * get(CURRENCY_FACTOR).getValue();
		 * cashPurchase.setCurrencyFactor(factor); }
		 */

		cashPurchase.setTransactionItems(items);
		cashPurchase.setCurrency(vendor.getCurrency().getID());
		cashPurchase
				.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
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
				return "Expenses List ," + getMessages().cash();
			}
			cashPurchase = getTransaction(string,
					AccounterCoreType.CASHPURCHASE, context);

			if (cashPurchase == null) {
				addFirstMessage(context, "Select a Cash Expense to update.");
				return "Expenses List " + string + "," + getMessages().cash();
			}
			setValues(context);
		} else {
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			cashPurchase = new ClientCashPurchase();
		}
		setTransaction(cashPurchase);
		return null;
	}

	/***
	 * set init data
	 * 
	 * @param context
	 */
	private void setValues(Context context) {
		get(CURRENCY_FACTOR).setValue(cashPurchase.getCurrencyFactor());
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
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							cashPurchase.getTransactionItems(), context));
			get(IS_VAT_INCLUSIVE).setValue(cashPurchase.isAmountsIncludeVAT());
		}
		get(ACCOUNTS).setValue(accounts);
		get(ITEMS).setValue(items);
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getVendor(),
						AccounterCoreType.VENDOR));
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						cashPurchase.getPaymentMethodForCommands(),
						getMessages()));
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(cashPurchase.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		get(DATE).setValue(cashPurchase.getDate());
		get(NUMBER).setValue(cashPurchase.getNumber());
		get(MEMO).setValue(cashPurchase.getMemo());

		/* get(CURRENCY_FACTOR).setValue(cashPurchase.getCurrencyFactor()); */
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(NUMBER).setValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CASH_EXPENSE,
						context.getCompany()));
	}

	@Override
	protected Payee getPayee() {
		return (Vendor) NewCashExpenseCommand.this.get(VENDOR).getValue();
	}

}
