package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
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
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewEnterBillCommand extends NewAbstractTransactionCommand {

	private ClientEnterBill enterBill;

	@Override
	protected String getWelcomeMessage() {

		return getMessages().create(getMessages().enterBill());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().enterBill());
	}

	@Override
	protected void setDefaultValues(Context context) {

		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER)
				.setDefaultValue(
						NumberUtils.getNextTransactionNumber(
								ClientTransaction.TYPE_ENTER_BILL,
								context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
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
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().enterBill());
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
						get(PHONE).setValue(value.getPhoneNo());
						get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								get(CONTACT).setValue(contact);
								break;
							}
						}
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							NewEnterBillCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

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
				return e.getName().startsWith(name);
			}
		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter("Currency Factor"), getMessages().currencyFactor()) {
			@Override
			protected ClientCurrency getSelectedCurrency() {
				Vendor vendor = (Vendor) NewEnterBillCommand.this.get(VENDOR)
						.getValue();
				return getCurrency(vendor.getCurrency().getID());
			}

		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerms(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select account Items", getMessages().Account(), true,
				true) {
			@Override
			protected void addRequirement(List<Requirement> list) {
				super.addRequirement(list);
				list.add(new CustomerRequirement(ACCOUNT_CUSTOMER,
						getMessages().pleaseSelect(Global.get().Customer()),
						Global.get().Customer(), true, true, null) {
					@Override
					public Result run(Context context, Result makeResult,
							ResultList list, ResultList actions) {
						if (getPreferences()
								.isBillableExpsesEnbldForProductandServices()
								&& getPreferences()
										.isProductandSerivesTrackingByCustomerEnabled()) {
							return super
									.run(context, makeResult, list, actions);
						}
						return null;
					}

					@Override
					protected List<Customer> getLists(Context context) {
						return NewEnterBillCommand.this.getCustomers();
					}
				});

				list.add(new BooleanRequirement(IS_BILLABLE, true) {
					@Override
					public Result run(Context context, Result makeResult,
							ResultList list, ResultList actions) {
						if (getPreferences()
								.isBillableExpsesEnbldForProductandServices()
								&& getPreferences()
										.isProductandSerivesTrackingByCustomerEnabled()) {
							return super
									.run(context, makeResult, list, actions);
						}
						return null;
					}

					@Override
					protected String getTrueString() {
						return getMessages().billabe();
					}

					@Override
					protected String getFalseString() {
						return "Not Billable";
					}
				});
			}

			@Override
			protected List<Account> getAccounts(Context context) {
				return com.vimukti.accounter.web.client.core.Utility
						.filteredList(new ListFilter<Account>() {

							@Override
							public boolean filter(Account account) {
								if (account.getType() == Account.TYPE_EXPENSE
										|| account.getType() == Account.TYPE_COST_OF_GOODS_SOLD
										|| account.getType() == Account.TYPE_OTHER_EXPENSE) {
									return true;
								} else {
									return false;
								}
							}
						}, new ArrayList<Account>(context.getCompany()
								.getAccounts()));
			}

			@Override
			protected Payee getPayee() {
				return (Vendor) NewEnterBillCommand.this.get(VENDOR).getValue();
			}

			@Override
			protected double getCurrencyFactor() {
				return NewEnterBillCommand.this.getCurrencyFactor();
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
				return (Vendor) NewEnterBillCommand.this.get(VENDOR).getValue();
			}

			@Override
			protected double getCurrencyFactor() {
				return NewEnterBillCommand.this.getCurrencyFactor();
			}

		});

		list.add(new ContactRequirement(CONTACT, "Please Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected Payee getPayee() {
				return get(VENDOR).getValue();
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
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

		Vendor vendor = (Vendor) get(VENDOR).getValue();
		enterBill.setVendor(vendor.getID());
		ClientFinanceDate date = get(DATE).getValue();
		if (date != null) {
			enterBill.setDate(date.getDate());
		} else {
			enterBill.setDate(System.currentTimeMillis());
		}
		String number = get(NUMBER).getValue();
		enterBill.setNumber(number);
		enterBill.setType(ClientTransaction.TYPE_ENTER_BILL);

		items.addAll(accounts);

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			enterBill.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}

		/*
		 * if (preferences.isEnableMultiCurrency()) { Currency currency =
		 * get(CURRENCY).getValue(); if (currency != null) {
		 * enterBill.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * enterBill.setCurrencyFactor(factor); }
		 */
		enterBill.setTransactionItems(items);
		updateTotals(context, enterBill, false);

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		enterBill.setDueDate(dueDate.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		enterBill.setDeliveryDate(deliveryDate);

		Contact contact = get(CONTACT).getValue();
		enterBill.setContact(toClientContact(contact));

		PaymentTerms paymentTerm = get("paymentTerms").getValue();
		enterBill.setPaymentTerm(paymentTerm.getID());

		String phone = get(PHONE).getValue();
		enterBill.setPhone(phone);
		enterBill.setCurrency(vendor.getCurrency().getID());
		enterBill.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		if (enterBill.getID() == 0) {
			enterBill.setDiscountDate(enterBill.getDate().getDate());
		}
		create(enterBill, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isKeepTrackofBills()) {
			addFirstMessage(context, "You dnt have permission to do this.");
			return "cancel";
		}
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select an Enter bill to update.");
				return "bills List";
			}
			enterBill = getTransaction(string, AccounterCoreType.ENTERBILL,
					context);

			if (enterBill == null) {
				addFirstMessage(context, "Select an Enter bill to update.");
				return "Bills List " + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			enterBill = new ClientEnterBill();
		}
		return null;
	}

	private void setValues(Context context) {
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(enterBill.getVendor(),
						AccounterCoreType.VENDOR));
		get(CURRENCY_FACTOR).setValue(enterBill.getCurrencyFactor());
		get(DATE).setValue(enterBill.getDate());
		get(NUMBER).setValue(enterBill.getNumber());
		get(CONTACT).setValue(toServerContact(enterBill.getContact()));
		get(PHONE).setValue(enterBill.getPhone());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(enterBill.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(DUE_DATE).setValue(new ClientFinanceDate(enterBill.getDueDate()));
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(enterBill.getDeliveryDate()));
		ArrayList<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		ArrayList<ClientTransactionItem> accountsList = new ArrayList<ClientTransactionItem>();
		if (enterBill.getTransactionItems() != null
				&& !enterBill.getTransactionItems().isEmpty()) {
			for (ClientTransactionItem item : enterBill.getTransactionItems()) {
				if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
					accountsList.add(item);
				} else {
					itemsList.add(item);
				}
			}
		}
		get(ACCOUNTS).setValue(accountsList);
		get(ITEMS).setValue(itemsList);
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							enterBill.getTransactionItems(), context));
		}
		get(MEMO).setValue(enterBill.getMemo());
	}

	@Override
	protected Payee getPayee() {
		return (Vendor) NewEnterBillCommand.this.get(VENDOR).getValue();
	}

}
