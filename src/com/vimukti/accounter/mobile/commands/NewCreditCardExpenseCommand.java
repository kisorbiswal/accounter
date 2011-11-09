package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewCreditCardExpenseCommand extends NewAbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseEnterName(
				Global.get().Vendor()), getMessages().payeeName(
				Global.get().Vendor()), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return new ArrayList<Vendor>(context.getCompany().getVendors());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isEnableMultiCurrency()) {
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
				String primaryCurrency = getClientCompany().getPreferences()
						.getPrimaryCurrency();
				ClientCurrency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (getClientCompany().getPreferences()
							.isEnableMultiCurrency()
							&& !((ClientCurrency) get(CURRENCY).getValue())
									.equals(getClientCompany().getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

			}
		});

		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				true, true, false) {

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

		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Account()), Global.get()
				.Account(), true, true, true) {

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
		});
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getConstants().contactName()), getConstants().contacts(), true,
				true, null) {

			@Override
			protected List<Contact> getLists(Context context) {
				return new ArrayList<Contact>(
						((Vendor) NewCreditCardExpenseCommand.this.get(VENDOR)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientVendor) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phone(), true,
				true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), true, true, null) {

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
				return getPaymentMethods();
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
									|| e.getType() == Account.TYPE_OTHER_ASSET) {
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
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}
		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getConstants().deliveryDate()), getConstants().deliveryDate(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, new ChangeListner<TAXCode>() {

					@Override
					public void onSelection(TAXCode value) {
						setTaxCodeToItems(value);
					}
				}) {

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().toLowerCase().startsWith(name);
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

	// @Override
	// public Result run(Context context) {
	// setDefaultValues();
	// String process = (String) context.getAttribute(PROCESS_ATTR);
	// Result result = context.makeResult();
	// if (process != null) {
	// if (process.equals(ADDRESS_PROCESS)) {
	// result = addressProcess(context);
	// if (result != null) {
	// return result;
	// }
	// } else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
	// result = transactionItemProcess(context);
	// if (result != null) {
	// return result;
	// }
	// } else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
	// result = transactionAccountProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	// Result makeResult = context.makeResult();
	// makeResult.add(getMessages().readyToCreate(
	// getConstants().creditCardExpense()));
	// ResultList list = new ResultList("values");
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	// setTransactionType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
	// result = createSupplierRequirement(context, list, SUPPLIER,
	// getMessages().vendorName(Global.get().Vendor()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = itemsAndAccountsRequirement(context, makeResult, actions,
	// new ListFilter<Account>() {
	//
	// @Override
	// public boolean filter(Account account) {
	// if (account.getType() != Account.TYPE_CASH
	// && account.getType() != Account.TYPE_BANK
	// && account.getType() != Account.TYPE_INVENTORY_ASSET
	// && account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
	// && account.getType() != Account.TYPE_ACCOUNT_PAYABLE
	// && account.getType() != Account.TYPE_INCOME
	// && account.getType() != Account.TYPE_OTHER_INCOME
	// && account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
	// && account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
	// && account.getType() != Account.TYPE_OTHER_ASSET
	// && account.getType() != Account.TYPE_EQUITY
	// && account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// }, false);
	// if (result != null) {
	// return result;
	// }
	//
	// result = accountRequirement(context, list, "payFrom", getConstants()
	// .bankAccounts(), new ListFilter<Account>() {
	//
	// @Override
	// public boolean filter(Account account) {
	// return account.getIsActive()
	// && Arrays.asList(Account.TYPE_BANK,
	// Account.TYPE_OTHER_CURRENT_ASSET)
	// .contains(account.getType());
	// }
	// });
	// if (result != null) {
	// return result;
	// }
	// makeResult.add(actions);
	// ClientCompanyPreferences preferences = getClientCompany()
	// .getPreferences();
	// if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
	// result = taxCodeRequirement(context, list);
	// if (result != null) {
	// return result;
	// }
	// }
	//
	// result = createOptionalResult(context, list, actions, makeResult);
	// if (result != null) {
	// return result;
	// }
	// completeProcess(context);
	//
	// return closeCommand();
	// }

	// private void setDefaultValues() {
	// get(DATE).setDefaultValue(new ClientFinanceDate());
	// get(NUMBER).setDefaultValue(getNextTransactionNumber());
	// get(PHONE).setDefaultValue(" ");
	// ClientContact contact = new ClientContact();
	// contact.setName(null);
	// get(CONTACT).setDefaultValue(contact);
	// get(MEMO).setDefaultValue(" ");
	// get("deliveryDate").setDefaultValue(new ClientFinanceDate());
	// get(PAYMENT_METHOD).setDefaultValue(getConstants().creditCard());
	// }
	//
	// private void completeProcess(Context context) {
	//
	// ClientCreditCardCharge creditCardCharge = new ClientCreditCardCharge();
	//
	// ClientVendor supplier = get(SUPPLIER).getValue();
	// creditCardCharge.setVendor(supplier.getID());
	//
	// ClientContact contact = get(CONTACT).getValue();
	// creditCardCharge.setContact(contact);
	//
	// ClientFinanceDate date = get(DATE).getValue();
	// creditCardCharge.setDate(date.getDate());
	//
	// creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
	//
	// String number = get(NUMBER).getValue();
	// creditCardCharge.setNumber(number);
	//
	// String paymentMethod = get(PAYMENT_METHOD).getValue();
	// creditCardCharge.setPaymentMethod(paymentMethod);
	//
	// String phone = get(PHONE).getValue();
	// creditCardCharge.setPhone(phone);
	//
	// Account account = get("payFrom").getValue();
	// creditCardCharge.setPayFrom(account.getID());
	//
	// ClientFinanceDate deliveryDate = get("deliveryDate").getValue();
	// creditCardCharge.setDeliveryDate(deliveryDate.getDate());
	//
	// List<ClientTransactionItem> items = get(ITEMS).getValue();
	// List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
	// items.addAll(accounts);
	// creditCardCharge.setTransactionItems(items);
	// ClientCompanyPreferences preferences = getClientCompany()
	// .getPreferences();
	// if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
	// ClientTAXCode taxCode = get(TAXCODE).getValue();
	// for (ClientTransactionItem item : items) {
	// item.setTaxCode(taxCode.getID());
	// }
	// }
	// String memo = get(MEMO).getValue();
	// creditCardCharge.setMemo(memo);
	// updateTotals(creditCardCharge, false);
	// create(creditCardCharge, context);
	// }

	// private Result createOptionalResult(Context context, ResultList list,
	// ResultList actions, Result makeResult) {
	//
	// if (context.getAttribute(INPUT_ATTR) == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	//
	// Object selection = context.getSelection(ACTIONS);
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// markDone();
	// return null;
	// default:
	// break;
	// }
	// }
	//
	// selection = context.getSelection("values");
	// Result result = dateOptionalRequirement(context, list, DATE,
	// getConstants().creditDate(),
	// getMessages().pleaseEnter(getConstants().creditDate()),
	// selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(
	// context,
	// list,
	// selection,
	// NUMBER,
	// getConstants().creditCardExpense() + " "
	// + getConstants().number(),
	// getMessages().pleaseEnter(
	// getConstants().creditCardExpense() + " "
	// + getConstants().number()));
	// if (result != null) {
	// return result;
	// }
	// Requirement vendorReq = get(SUPPLIER);
	// ClientVendor vendor = vendorReq.getValue();
	// result = contactRequirement(context, list, selection, vendor);
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(context, list, selection, PHONE,
	// getConstants().phoneNumber(),
	// getMessages().pleaseEnter(getConstants().phoneNumber()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = dateOptionalRequirement(context, list, "deliveryDate",
	// getConstants().deliveryDate(),
	// getMessages().pleaseEnter(getConstants().deliveryDate()),
	// selection);
	// if (result != null) {
	// return result;
	// }
	//
	// result = stringOptionalRequirement(context, list, selection, MEMO,
	// getConstants().memo(), getConstants().addMemo());
	// if (result != null) {
	// return result;
	// }
	//
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add("",
	// getMessages()
	// .finishToCreate(getConstants().creditCardExpense()));
	// actions.add(finish);
	// return makeResult;
	// }

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}
		ClientCreditCardCharge creditCardCharge = new ClientCreditCardCharge();

		ClientVendor supplier = get(VENDOR).getValue();
		creditCardCharge.setVendor(supplier.getID());

		ClientContact contact = get(CONTACT).getValue();
		creditCardCharge.setContact(contact);

		ClientFinanceDate date = get(DATE).getValue();
		creditCardCharge.setDate(date.getDate());

		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		String number = get(NUMBER).getValue();
		creditCardCharge.setNumber(number);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		creditCardCharge.setPaymentMethod(paymentMethod);

		String phone = get(PHONE).getValue();
		creditCardCharge.setPhone(phone);

		Account account = get("payFrom").getValue();
		creditCardCharge.setPayFrom(account.getID());

		ClientFinanceDate deliveryDate = get("deliveryDate").getValue();
		creditCardCharge.setDeliveryDate(deliveryDate.getDate());
		items.addAll(accounts);
		creditCardCharge.setTransactionItems(items);
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			creditCardCharge.setAmountsIncludeVAT(isVatInclusive);
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}

		if (context.getClientCompany().getPreferences().isEnableMultiCurrency()) {
			ClientCurrency currency = get(CURRENCY).getValue();
			if (currency != null) {
				creditCardCharge.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			creditCardCharge.setCurrencyFactor(factor);
		}

		String memo = get(MEMO).getValue();
		creditCardCharge.setMemo(memo);
		updateTotals(context, creditCardCharge, false);
		create(creditCardCharge, context);

		return null;
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

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(
						ClientTransaction.TYPE_CREDIT_CARD_EXPENSE, context
								.getClientCompany().getID());
		return nextTransactionNumber;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().creditCardExpense());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().creditCardExpense());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(getNextTransactionNumber(context));
		get(PHONE).setDefaultValue(" ");
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue(" ");
		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get(PAYMENT_METHOD).setDefaultValue(getConstants().creditCard());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(
				getConstants().creditCardExpense());
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
		double[] result = getTransactionTotal(context, false, allrecords, true);
		makeResult.add("Net Amount: " + result[0]);
		if (context.getClientCompany().getPreferences().isTrackTax()) {
			makeResult.add("Total Tax: " + result[1]);
		}
		makeResult.add("Total: " + (result[0] + result[1]));
	}
}
