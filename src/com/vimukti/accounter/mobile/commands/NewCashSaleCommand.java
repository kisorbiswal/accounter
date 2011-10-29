package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingMethodRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingTermRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewCashSaleCommand extends NewAbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";
	private static final String SHIPPING_TERMS = "shippingTerms";
	private static final String SHIPPING_METHODS = "shippingMethods";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Customer()),
				getMessages().customerNumber(Global.get().Customer()), false,
				true, null) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
			}
		});

		list.add(new TransactionItemItemsRequirement(ITEMS, getMessages()
				.pleaseEnterName(getConstants().item()), getConstants().item(),
				false, true, true) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}
		});

		list.add(new TransactionItemAccountsRequirement(ACCOUNTS, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Account()), Global.get()
				.Account(), false, true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				}, getClientCompany().getAccounts());
			}
		});
		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), false, true, null) {

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
				return new ArrayList<String>(getClientCompany()
						.getPaymentMethods().values());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
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
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(
						((ClientCustomer) NewCashSaleCommand.this.get(CUSTOMER)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientCustomer) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phone(), true,
				true));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterNameOrNumber(
						getMessages().depositAccount(Global.get().Account())),
				getMessages().depositAccount(Global.get().Account()), false,
				true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depositAccount(Global.get().Account()));
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET)
								.contains(account.getType());
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Account());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, new ChangeListner<ClientTAXCode>() {

					@Override
					public void onSelection(ClientTAXCode value) {
						setTaxCodeToItems(value);
					}
				}) {

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
				return e.getName().startsWith(name);
			}
		});

		list.add(new ShippingTermRequirement(SHIPPING_TERMS, getMessages()
				.pleaseEnterName(getConstants().shippingTerm()), getConstants()
				.shippingTerm(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().shippingTerm());
			}

			@Override
			protected List<ClientShippingTerms> getLists(Context context) {
				return getClientCompany().getShippingTerms();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().shippingTerms());
			}

			@Override
			protected boolean filter(ClientShippingTerms e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS, getMessages()
				.pleaseEnterName(getConstants().shippingMethod()),
				getConstants().shippingMethod(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getConstants().shippingMethod());
			}

			@Override
			protected List<ClientShippingMethod> getLists(Context context) {
				return getClientCompany().getShippingMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().shippingMethod());
			}

			@Override
			protected boolean filter(ClientShippingMethod e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getConstants().deliveryDate()), getConstants().deliveryDate(),
				true, true));
	}

	protected void setTaxCodeToItems(ClientTAXCode value) {
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

		ClientCashSales cashSale = new ClientCashSales();
		ClientFinanceDate date = get(DATE).getValue();
		cashSale.setDate(date.getDate());

		cashSale.setType(Transaction.TYPE_CASH_SALES);
		if (context.getCompany().getPreferences().getUseCustomerId()) {
			String number = get(NUMBER).getValue();
			cashSale.setNumber(number);
		}

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		accounts.addAll(items);
		cashSale.setTransactionItems(accounts);

		// TODO Location
		// TODO Class
		ClientShippingTerms shippingTerms = get(SHIPPING_TERMS).getValue();
		cashSale.setShippingTerm(shippingTerms != null ? shippingTerms.getID()
				: 0);

		ClientShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		cashSale.setShippingMethod(shippingMethod != null ? shippingMethod
				.getID() : 0);

		ClientCustomer customer = get(CUSTOMER).getValue();
		cashSale.setCustomer(customer.getID());

		ClientContact contact = get(CONTACT).getValue();
		cashSale.setContact(contact);

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

		ClientAccount account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		cashSale.setDepositIn(account.getID());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		cashSale.setDeliverydate(deliveryDate.getDate());

		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		// if (context.getCompany())
		// ClientTAXCode taxCode = get(TAXCODE).getValue();
		// cashSale.setTaxTotal(getTaxTotal(accounts, taxCode));
		updateTotals(context, cashSale, true);
		create(cashSale, context);

		return null;
	}

	public ClientAddress getAddress(int type, ClientCustomer customer) {
		for (ClientAddress address : customer.getAddress()) {

			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_CASH_SALES,
						context.getClientCompany().getID());
		return nextTransactionNumber;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().cashSale());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().cashSale());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(getNextTransactionNumber(context));
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(PAYMENT_METHOD).setDefaultValue(getConstants().cash());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().cashSale());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
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

	@Override
	protected String initObject(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
