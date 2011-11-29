package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class NewCreditNoteCommand extends NewAbstractTransactionCommand {
	ClientCustomerCreditMemo creditMemo;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER,
				"Please Enter Customer name or number to set credit Note",
				"Customer", false, true, new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						NewCreditNoteCommand.this.get(CONTACT).setValue(null);
					}
				}) {

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
		 * actions); } } return null; } });
		 */
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().creditNo()), getMessages().creditNo(), true, true));
		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {
			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

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
									&& account.getType() != Account.TYPE_BANK
									&& account.getType() != Account.TYPE_INVENTORY_ASSET
									&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
									&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
									&& account.getType() != Account.TYPE_EXPENSE
									&& account.getType() != Account.TYPE_OTHER_EXPENSE
									&& account.getType() != Account.TYPE_COST_OF_GOODS_SOLD
									&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
									&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
									&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY
									&& account.getType() != Account.TYPE_OTHER_ASSET
									&& account.getType() != Account.TYPE_EQUITY) {
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
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Credit Note to update.");
				return "Invoices List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientCustomerCreditMemo invoiceByNum = (ClientCustomerCreditMemo) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.CUSTOMERCREDITMEMO);
			if (invoiceByNum == null) {
				addFirstMessage(context, "Select a Credit Note to update.");
				return "Invoices List " + string;
			}
			creditMemo = invoiceByNum;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			creditMemo = new ClientCustomerCreditMemo();
		}
		setTransaction(creditMemo);
		return null;
	}

	private void setValues() {
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(creditMemo.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(DATE).setValue(creditMemo.getDate());
		get(NUMBER).setValue(creditMemo.getNumber());
		get(CONTACT).setValue(toServerContact(creditMemo.getContact()));
		get(BILL_TO).setValue(creditMemo.getBillingAddress());
		/* get(CURRENCY_FACTOR).setValue(creditMemo.getCurrencyFactor()); */
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> transactionItems = creditMemo
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
		get(IS_VAT_INCLUSIVE).setValue(creditMemo.isAmountsIncludeVAT());
	}

	@Override
	protected String getWelcomeMessage() {
		return creditMemo.getID() == 0 ? "Create  New Customer CreditNote......."
				: "Updating Customer credit note";
	}

	@Override
	protected String getDetailsMessage() {
		return creditMemo.getID() == 0 ? getMessages().readyToCreate(
				getMessages().customerCreditNote(Global.get().customer()))
				: "Customer credit memo is ready to create with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
		get(BILL_TO).setDefaultValue(new ClientAddress());

	}

	@Override
	public String getSuccessMessage() {
		return creditMemo.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().customerCreditNote(Global.get().customer()))
				: getMessages().updateSuccessfully(
						getMessages().customerCreditNote(
								Global.get().customer()));

	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		creditMemo.setType(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		Customer customer = get(CUSTOMER).getValue();
		creditMemo.setCustomer(customer.getID());

		ClientFinanceDate date = get(DATE).getValue();
		creditMemo.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		creditMemo.setNumber(number);

		Contact contact = get(CONTACT).getValue();
		creditMemo.setContact(toClientContact(contact));

		ClientAddress billTo = get(BILL_TO).getValue();
		creditMemo.setBillingAddress(billTo);

		/*
		 * if (preferences.isEnableMultiCurrency()) { Currency currency =
		 * get(CURRENCY).getValue(); if (currency != null) {
		 * creditMemo.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * creditMemo.setCurrencyFactor(factor); }
		 */

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		items.addAll(accounts);
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			creditMemo.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		creditMemo.setTransactionItems(items);
		double taxTotal = updateTotals(context, creditMemo, true);
		creditMemo.setTaxTotal(taxTotal);
		create(creditMemo, context);
		return null;
	}
}
