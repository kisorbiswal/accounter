package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Contact;
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
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.SalesPersonRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingMethodRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
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
	private static final String SHIPPING_METHODS = "shippingMethod";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String CONTACT = "contact";
	private static final String SHIPTO = "shipTo";
	private static final String BILLTO = "billTo";
	private ClientCustomer customer;

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
				getMessages().number()), getMessages().number(), false, true) {
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
				getMessages().openingBalance()),
				getMessages().openingBalance(), true, true));

		list.add(new DateRequirement(BALANCE_ASOF_DATE, getMessages()
				.pleaseEnter(getMessages().balanceAsOfDate()), getMessages()
				.balanceAsOfDate(), true, true));

		list.add(new AddressRequirement(BILLTO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));
		list.add(new AddressRequirement(SHIPTO, getMessages().pleaseEnter(
				getMessages().shipTo()), getMessages().shipTo(), true, true));

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new StringRequirement(FAX, getMessages().pleaseEnter(
				getMessages().fax()), getMessages().fax(), true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new URLRequirement(WEBADRESS, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new SalesPersonRequirement(SALESPERSON,
				"please enter the sales person name", getMessages()
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
				return e.getFirstName().startsWith(name);
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS,
				"please enter the shipping method name", getMessages()
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
						getMessages().shippingMethod());
			}

		});

		// TODO list.add(new Requirement(PRICE_LEVEL, true, true));

		list.add(new CreditRatingRequirement(CREDIT_RATING,
				"Please enter the credit rating name", getMessages()
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
				getMessages().bankName()), getMessages().bankName(), true, true));

		list.add(new NumberRequirement(BANK_ACCOUNT_NUM, getMessages()
				.pleaseEnter(getMessages().bankAccount()), getMessages()
				.bankAccountNumber(), true, true));

		list.add(new NameRequirement(BANK_BRANCH, getMessages().pleaseEnter(
				getMessages().bankBranch()), getMessages().bankBranch(), true,
				true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Payment method has been selected";
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
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
						getMessages().paymentMethod());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS,
				"Please enter the payment term name", getMessages()
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
				.pleaseEnter(getMessages().vatRegistrationNumber()),
				getMessages().vatRegistrationNumber(), true, true) {
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
				"Please enter the tax code name", getMessages().taxCode(),
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
				.pleaseEnter(getMessages().serviceTax()), getMessages()
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
				getMessages().tinNumber()), getMessages().tinNumber(), true,
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
				.pleaseSelect(getMessages().contact()), CONTACT, true, true) {

			@Override
			protected List<ClientContact> getList() {
				if (customer.getID() != 0) {
					return new ArrayList<ClientContact>(customer.getContacts());
				}
				return null;
			}

		});

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ICountryPreferences countryPreferences = context.getCompany()
				.getCountryPreferences();
		ClientCompanyPreferences preferences = context.getPreferences();

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
		customer.setContacts(new HashSet<ClientContact>(contact));
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

	@Override
	protected String getWelcomeMessage() {
		if (customer.getID() == 0) {
			return "Creating New Customer..";
		} else {
			return "Updating '" + customer.getDisplayName() + "' Customer..";
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (customer.getID() == 0) {
			return getMessages().readyToCreate(Global.get().Customer());
		} else {
			return getMessages().readyToUpdate(Global.get().Customer());
		}
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
		if (customer.getID() == 0) {
			return getMessages().createSuccessfully(Global.get().Customer());
		} else {
			return getMessages().updateSuccessfully(Global.get().Customer());
		}
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Customer to update.");
				return "Customers";
			}
			ClientPayee customerByName = CommandUtils.getPayeeByName(
					context.getCompany(), string);
			if (customerByName == null) {
				long numberFromString = getNumberFromString(string);
				if (numberFromString != 0) {
					string = String.valueOf(numberFromString);
				}
				customerByName = CommandUtils.getCustomerByNumber(
						context.getCompany(), string);
				if (customerByName == null) {
					addFirstMessage(context, "Select a Customer to update.");
					return "Customers " + string.trim();
				}
			}
			customer = (ClientCustomer) customerByName;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(CUSTOMER_NAME).setValue(string);
			}
			customer = new ClientCustomer();
		}
		return null;
	}

	private void setValues() {
		get(CUSTOMER_NAME).setValue(customer.getName());
		get(NUMBER).setValue(customer.getNumber());
		get(CUSTOMER_GROUP).setValue(
				CommandUtils.getServerObjectById(customer.getCustomerGroup(),
						AccounterCoreType.VENDOR_GROUP));
		get(CUSTOMER_SINCEDATE).setValue(
				new ClientFinanceDate(customer.getPayeeSince()));
		get(BALANCE).setValue(customer.getBalance());
		// get(BALANCE).setEditable(false);
		get(BALANCE_ASOF_DATE).setValue(
				new ClientFinanceDate(customer.getBalanceAsOf()));
		Set<ClientAddress> address = customer.getAddress();
		for (ClientAddress clientAddress : address) {
			if (clientAddress.getType() == ClientAddress.TYPE_BILL_TO) {
				get(BILLTO).setValue(clientAddress);
			} else if (clientAddress.getType() == ClientAddress.TYPE_SHIP_TO) {
				get(SHIPTO).setValue(clientAddress);
			}
		}
		get(CREDIT_RATING).setValue(
				CommandUtils.getServerObjectById(customer.getCreditRating(),
						AccounterCoreType.CREDIT_RATING));
		get(SALESPERSON).setValue(
				CommandUtils.getServerObjectById(customer.getSalesPerson(),
						AccounterCoreType.SALES_PERSON));
		get(PAYMENT_METHOD).setDefaultValue(getMessages().cash());
		get(CUSTOMER_VATCODE).setValue(
				CommandUtils.getServerObjectById(customer.getTAXCode(),
						AccounterCoreType.TAX_CODE));

		ArrayList<Contact> arrayList = new ArrayList<Contact>();
		for (ClientContact contact : customer.getContacts()) {
			arrayList.add(toServerContact(contact));
		}
		get(CONTACT).setValue(arrayList);
		get(PHONE).setValue(customer.getPhoneNo());
		get(FAX).setValue(customer.getFaxNo());
		get(EMAIL).setValue(customer.getEmail());
		get(WEBADRESS).setValue(customer.getWebPageAddress());
		get(BANK_NAME).setValue(customer.getBankName());
		get(BANK_ACCOUNT_NUM).setValue(customer.getBankAccountNo());
		get(BANK_BRANCH).setValue(customer.getBankBranch());
		get(SHIPPING_METHODS).setValue(
				CommandUtils.getServerObjectById(customer.getShippingMethod(),
						AccounterCoreType.SHIPPING_METHOD));
		get(PAYMENT_METHOD).setValue(customer.getPaymentMethod());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(customer.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(VATREGISTER_NUM).setValue(customer.getVATRegistrationNumber());
		get(CST_NUM).setValue(customer.getCstNumber());
		get(SERVICE_TAX_NUM).setValue(
				customer.getServiceTaxRegistrationNumber());
		get(TIN_NUM).setValue(customer.getTinNumber());
	}
}
