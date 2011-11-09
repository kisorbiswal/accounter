package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CreditRatingRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerGroupRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.SalesPersonRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingMethodRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class NewCustomerCommand extends NewAbstractCommand {

	protected static final String NUMBER = "customerNumber";
	protected static final String BALANCE = "balance";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBADRESS = "webPageAdress";
	private static final String BANK_NAME = "bankName";
	private static final String BANK_ACCOUNT_NUM = "bankAccountNum";
	private static final String BANK_BRANCH = "bankBranch";
	private static final String VATREGISTER_NUM = "vatRegisterationNum";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String CUSTOMER_VATCODE = "customerVatCode";
	private static final String CUSTOMER_SINCEDATE = "customerSinceDate";
	private static final String BALANCE_ASOF_DATE = "balanceAsOfDate";
	private static final String SALESPERSON = "salesPerson";
	private static final String CREDIT_RATING = "creditRating";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String CUSTOMER_GROUP = "cusomerGroup";
	private static final String PAN_NUM = " Personal Ledger number";
	private static final String CST_NUM = "CST number";
	private static final String SERVICE_TAX_NUM = "Service tax registration no";
	private static final String TIN_NUM = "Taxpayer identification number";
	private static final String ADDRESS = "address";
	private static final String SHIPPING_METHODS = "shippingMethod";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String CONTACT = "contact";
	private static final String SHIPTO = "shipTo";
	private static final String BILLTO = "billTo";
	private boolean isUpdate;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(CUSTOMER_NAME,
				"Please Enter Customer name", getMessages().payeeName(
						Global.get().Customer()), false, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().getUseCustomerId()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new DateRequirement(CUSTOMER_SINCEDATE,
				getMessages().pleaseEnter(
						getMessages().payeeSince(Global.get().Customer())),
				getMessages().payeeSince(Global.get().Customer()), true, true));

		list.add(new AmountRequirement(BALANCE, getMessages().pleaseEnter(
				getConstants().openingBalance()), getConstants()
				.openingBalance(), true, true));

		list.add(new DateRequirement(BALANCE_ASOF_DATE, getMessages()
				.pleaseEnter(getConstants().balanceAsOfDate()), getConstants()
				.balanceAsOfDate(), true, true));

		list.add(new AddressRequirement(BILLTO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));
		list.add(new AddressRequirement(SHIPTO, getMessages().pleaseEnter(
				getConstants().shipTo()), getConstants().shipTo(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));

		list.add(new NumberRequirement(FAX, getMessages().pleaseEnter(
				getConstants().fax()), getConstants().fax(), true, true));

		list.add(new NameRequirement(EMAIL, getMessages().pleaseEnter(
				getConstants().email()), getConstants().email(), true, true));

		list.add(new NameRequirement(WEBADRESS, getMessages().pleaseEnter(
				getConstants().webSite()), getConstants().webSite(), true, true));

		list.add(new SalesPersonRequirement(SALESPERSON,
				"please enter the sales person name", getConstants()
						.salesPerson(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "sales person has been selected";
			}

			@Override
			protected List<SalesPerson> getLists(Context context) {
				return new ArrayList<SalesPerson>(context.getCompany()
						.getSalesPersons());
			}

			@Override
			protected boolean filter(SalesPerson e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS,
				"please enter the shipping method name", getConstants()
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
				return "Shipping method has been selected";
			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				return new ArrayList<ShippingMethod>(context.getCompany()
						.getShippingMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().shippingMethod());
			}

			@Override
			protected boolean filter(ShippingMethod e, String name) {
				return e.getName().startsWith(name);
			}
		});

		// TODO list.add(new Requirement(PRICE_LEVEL, true, true));

		list.add(new CreditRatingRequirement(CREDIT_RATING,
				"Please enter the credit rating name", getConstants()
						.creditRating(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Credit Rating has been selected";
			}

			@Override
			protected List<CreditRating> getLists(Context context) {
				return new ArrayList<CreditRating>(context.getCompany()
						.getCreditRatings());
			}

			@Override
			protected boolean filter(CreditRating e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NameRequirement(BANK_NAME, getMessages().pleaseEnter(
				getConstants().bankName()), getConstants().bankName(), true,
				true));

		list.add(new NumberRequirement(BANK_ACCOUNT_NUM, getMessages()
				.pleaseEnter(getConstants().bankAccounts()), getConstants()
				.bankAccountNumber(), true, true));

		list.add(new NameRequirement(BANK_BRANCH, getMessages().pleaseEnter(
				getConstants().bankBranch()), getConstants().bankBranch(),
				true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getConstants().paymentMethod()), getConstants()
				.paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Payment method has been selected";
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				ArrayList<String> arrayList = new ArrayList<String>(
						getPaymentMethods());
				Collections.sort(arrayList);
				return arrayList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS,
				"Please enter the payment term name", getConstants()
						.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new CustomerGroupRequirement(CUSTOMER_GROUP,
				"Please enter the customer group", getMessages().payeeGroup(
						Global.get().Customer()), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Customer Group has been selected";
			}

			@Override
			protected List<CustomerGroup> getLists(Context context) {
				return new ArrayList<CustomerGroup>(context.getCompany()
						.getCustomerGroups());
			}

			@Override
			protected boolean filter(CustomerGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(VATREGISTER_NUM, getMessages()
				.pleaseEnter(getConstants().vatRegistrationNumber()),
				getConstants().vatRegistrationNumber(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isVatAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new TaxCodeRequirement(CUSTOMER_VATCODE,
				"Please enter the tax code name", getConstants().taxCode(),
				true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()) {
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

		list.add(new NumberRequirement(PAN_NUM, "Please Enter the pan number",
				"Pan Number", true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(CST_NUM, getMessages().pleaseEnter(
				getMessages().payeeNumber(Global.get().Customer())),
				getMessages().payeeNumber(Global.get().Customer()), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isSalesTaxAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(SERVICE_TAX_NUM, getMessages()
				.pleaseEnter(getConstants().serviceTax()), getConstants()
				.serviceTax(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isServiceTaxAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(TIN_NUM, getMessages().pleaseEnter(
				getConstants().tinNumber()), getConstants().tinNumber(), true,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isTDSAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new CustomerContactRequirement(CONTACT, getMessages()
				.pleaseSelect(getConstants().contact()), CONTACT, true, true) {

			@Override
			protected List<ClientContact> getList() {
				List<ClientContact> contacts = getCustomerContacts();
				return new ArrayList<ClientContact>(contacts);
			}

			@Override
			protected String getEmptyString() {
				return isUpdate ? getMessages().youDontHaveAny(
						getConstants().contacts()) : "";
			}
		});

	}

	protected List<ClientContact> getCustomerContacts() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		LinkedHashMap<Integer, ClientAddress> allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		ICountryPreferences countryPreferences = context.getCompany()
				.getCountryPreferences();
		ClientCompanyPreferences preferences = context.getPreferences();

		ClientCustomer customer = new ClientCustomer();
		String name = get(CUSTOMER_NAME).getValue();
		String number = null;
		if (preferences.getUseCustomerId()) {
			number = get(NUMBER).getValue().toString();
		}
		List<ClientContact> contact = get(CONTACT).getValue();
		ClientFinanceDate balancedate = get(BALANCE_ASOF_DATE).getValue();
		double balance = get(BALANCE).getValue();
		ClientAddress shipptoAddress = get(SHIPTO).getValue();
		ClientAddress billToAdress = get(BILLTO).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEBADRESS).getValue();
		SalesPerson salesPerson = get(SALESPERSON).getValue();
		CreditRating creditRating = get(CREDIT_RATING).getValue();
		// ClientPriceLevel priceLevel = get(PRICE_LEVEL).getValue();
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(BANK_ACCOUNT_NUM).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		PaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();
		CustomerGroup customerGroup = get(CUSTOMER_GROUP).getValue();
		String vatRegistredNum = get(VATREGISTER_NUM).getValue();
		TAXCode taxCode = get(CUSTOMER_VATCODE).getValue();
		ShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		// String panNum = get(PAN_NUM).getValue();
		String cstNum = get(CST_NUM).getValue();
		String serviceTaxNum = get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();
		HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
		if (shipptoAddress != null && shipptoAddress.getAddress1() != "") {
			shipptoAddress.setType(ClientAddress.TYPE_SHIP_TO);
			addresses.add(shipptoAddress);
		}
		if (billToAdress != null && billToAdress.getAddress1() != "") {
			billToAdress.setType(ClientAddress.TYPE_BILL_TO);
			addresses.add(billToAdress);
		}
		customer.setName(name);
		if (preferences.getUseCustomerId())
			customer.setNumber(number);

		HashSet<ClientContact> contacts = new HashSet<ClientContact>();
		if (contact != null)
			for (ClientContact Contact : contact) {
				contacts.add(Contact);
			}
		customer.setContacts(contacts);
		customer.setBalance(balance);
		if (balancedate != null) {
			customer.setBalanceAsOf(balancedate.getDate());
		}
		if (!addresses.isEmpty())
			customer.setAddress(addresses);
		customer.setPhoneNo(phoneNum);
		customer.setFaxNo(faxNum);
		customer.setWebPageAddress(webaddress);
		customer.setBankAccountNo(bankAccountNum);
		customer.setBankBranch(bankBranch);
		customer.setBankName(bankName);
		customer.setEmail(emailId);
		if (salesPerson != null) {
			customer.setSalesPerson(salesPerson.getID());
		}
		// if (priceLevel != null) {
		// customer.setPriceLevel(priceLevel.getID());
		// }
		if (creditRating != null) {
			customer.setCreditRating(creditRating.getID());
		}
		customer.setActive(true);
		customer.setPaymentMethod(paymentMethod);
		if (paymentTerms != null) {
			customer.setPaymentTerm(paymentTerms.getID());
		}
		if (customerGroup != null) {
			customer.setCustomerGroup(customerGroup.getID());
		}
		if (preferences.isDoProductShipMents() && shippingMethod != null)
			customer.setShippingMethod(shippingMethod.getID());
		if (preferences.isTrackTax() && taxCode != null) {
			if (countryPreferences.isVatAvailable()) {
				customer.setTAXCode(taxCode.getID());
				customer.setVATRegistrationNumber(vatRegistredNum);
			}
			if (countryPreferences.isSalesTaxAvailable()) {
				customer.setCstNumber(cstNum);
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				customer.setServiceTaxRegistrationNumber(serviceTaxNum);
			}
			if (countryPreferences.isTDSAvailable()) {
				customer.setTinNumber(tinNum);
			}
			// customer.setPANno(panNum);
		}

		create(customer, context);
		return null;
	}

	/**
	 * 
	 * @param allAddresses
	 * @return
	 */
	private Set<ClientAddress> getAdress(
			LinkedHashMap<Integer, ClientAddress> allAddresses) {
		Collection add = allAddresses.values();
		Set<ClientAddress> toBeSet = new HashSet<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
		}
		return toBeSet;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating New Customer..";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(Global.get().Customer());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(CUSTOMER_SINCEDATE).setDefaultValue(new ClientFinanceDate());
		get(BALANCE_ASOF_DATE).setDefaultValue(new ClientFinanceDate());
		get(BILLTO).setDefaultValue(new ClientAddress());
		get(SHIPTO).setDefaultValue(new ClientAddress());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(Global.get().Customer());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		this.isUpdate = isUpdate;
		String string = context.getString();
		if (!string.isEmpty()) {
			get(CUSTOMER_NAME).setValue(string);
		}
		return null;
	}

}
