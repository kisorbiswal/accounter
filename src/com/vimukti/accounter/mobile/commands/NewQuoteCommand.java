package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

/**
 * 
 * @author SaiPrasad N
 * 
 */
public class NewQuoteCommand extends NewAbstractTransactionCommand {

	private static final String PHONE = "phone";
	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String EXPIRATION_DATE = "expirationDate";
	private static final String PAYMENT_TERMS = "paymentTerms";

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
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
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
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
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
				false, true, true) {

			@Override
			public List<ClientItem> getItems(Context context) {
				return context.getClientCompany().getServiceItems();
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseEnterName(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<ClientPaymentTerms> getLists(Context context) {
				return getClientCompany().getPaymentsTerms();
			}
		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getConstants().contactName()), getConstants().contacts(), true,
				true, null) {

			@Override
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(
						((ClientCustomer) NewQuoteCommand.this.get(CUSTOMER)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientCustomer) get(CUSTOMER).getValue())
						.getDisplayName();
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phone(), true,
				true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getConstants().deliveryDate()), getConstants().deliveryDate(),
				true, true));

		list.add(new DateRequirement(EXPIRATION_DATE, getMessages()
				.pleaseEnter(getConstants().expirationDate()), getConstants()
				.expirationDate(), true, false));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
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
				return e.getName().startsWith(name);
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

		ClientEstimate estimate = new ClientEstimate();

		ClientCustomer customer = get(CUSTOMER).getValue();
		estimate.setCustomer(customer);
		estimate.setType(ClientEstimate.TYPE_ESTIMATE);

		ClientFinanceDate date = get(DATE).getValue();
		estimate.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		estimate.setNumber(number);

		ClientContact contact = get(CONTACT).getValue();
		if (contact != null)
			estimate.setContact(contact);

		ClientAddress billTo = get(BILL_TO).getValue();
		estimate.setAddress(billTo);

		String phone = get(PHONE).getValue();
		estimate.setPhone(phone);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			estimate.setAmountsIncludeVAT(isVatInclusive);
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		if (preferences.isEnableMultiCurrency()) {
			ClientCurrency currency = get(CURRENCY).getValue();
			if (currency != null) {
				estimate.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			estimate.setCurrencyFactor(factor);
		}

		estimate.setTransactionItems(items);
		double taxTotal = updateTotals(context, estimate, true);
		estimate.setTaxTotal(taxTotal);
		ClientPaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		estimate.setPaymentTerm(paymentTerm.getID());

		ClientFinanceDate d = get(DATE).getValue();
		estimate.setDate(d.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		estimate.setDeliveryDate(deliveryDate.getDate());
		ClientFinanceDate expiryDdate = get(EXPIRATION_DATE).getValue();
		estimate.setExpirationDate(expiryDdate.getDate());

		String memo = get(MEMO).getValue();
		estimate.setMemo(memo);
		create(estimate, context);

		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().quote());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().quote());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		ArrayList<ClientPaymentTerms> paymentTerms = context.getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get("expirationDate").setDefaultValue(new ClientFinanceDate());

		get(MEMO).setDefaultValue(" ");
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().quote());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> allrecords = get(ITEMS).getValue();
		double[] result = getTransactionTotal(context, false, allrecords, true);
		makeResult.add("Net Amount: " + result[0]);
		if (context.getClientCompany().getPreferences().isTrackTax()) {
			makeResult.add("Total Tax: " + result[1]);
		}
		makeResult.add("Total: " + (result[0] + result[1]));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

}
