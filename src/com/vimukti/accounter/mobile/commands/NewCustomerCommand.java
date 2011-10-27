package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CreditRatingRequirement;
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
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class NewCustomerCommand extends NewAbstractCommand {

	private static final String INPUT_ATTR = "input";
	private static final int SALESPERSON_TO_SHOW = 5;
	private static final int CREDITRATING_TO_SHOW = 5;
	private static final int CUSTOMERGROUP_TO_SHOW = 5;
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
	private static final String IS_ACTIVE = "isActive";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String CUSTOMER_CONTACT = "contact";
	private static final String PRIMARY = "primary";
	private static final String CONTACT_NAME = "contactName";
	private static final String TITLE = "title";
	private static final String BUSINESS_PHONE = "businessPhone";
	private static final String CUSTOMER_VATCODE = "customerVatCode";
	private static final String CUSTOMER_SINCEDATE = "customerSinceDate";
	private static final String BALANCE_ASOF_DATE = "balanceAsOfDate";
	private static final String SALESPERSON = "salesPerson";
	private static final String PRICE_LEVEL = "priceLevel";
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

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(CUSTOMER_CONTACT,
				"Please Enter Customer name", getMessages().customerName(
						Global.get().Customer()), false, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), false, true));

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getConstants().active();
			}

			@Override
			protected String getFalseString() {
				return getConstants().inActive();
			}
		});

		list.add(new DateRequirement(CUSTOMER_SINCEDATE, getMessages()
				.pleaseEnter(
						getMessages().customerSince(Global.get().Customer())),
				getMessages().customerSince(Global.get().Customer()), true,
				true));

		list.add(new AmountRequirement(BALANCE, getMessages().pleaseEnter(
				getConstants().openingBalance()), getConstants()
				.openingBalance(), true, true));

		list.add(new DateRequirement(BALANCE_ASOF_DATE, getMessages()
				.pleaseEnter(getConstants().balanceAsOfDate()), getConstants()
				.balanceAsOfDate(), true, true));

		list.add(new AddressRequirement(ADDRESS, getMessages().pleaseEnter(
				getConstants().address()), getConstants().address(), true, true));

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
			protected List<ClientSalesPerson> getLists(Context context) {
				return getClientCompany().getSalesPersons();
			}

			@Override
			protected boolean filter(ClientSalesPerson e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS,
				"please enter the shipping method name", getConstants()
						.shippingMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Shipping method has been selected";
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

		// TODO list.add(new Requirement(PRICE_LEVEL, true, true));

		list.add(new CreditRatingRequirement(CREDIT_RATING,
				"Please enter the credit rating name", getConstants()
						.creditRating(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Credit Rating has been selected";
			}

			@Override
			protected List<ClientCreditRating> getLists(Context context) {
				return getClientCompany().getCreditRatings();
			}

			@Override
			protected boolean filter(ClientCreditRating e, String name) {
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
				return new ArrayList<String>(getClientCompany()
						.getPaymentMethods().values());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS,
				"Please enter the payment term name", getConstants()
						.paymentTerm(), true, true, null) {

			@Override
			protected List<ClientPaymentTerms> getLists(Context context) {
				return getClientCompany().getPaymentsTerms();
			}
		});

		list.add(new CustomerGroupRequirement(CUSTOMER_GROUP,
				"Please enter the customer group", getMessages().customerGroup(
						Global.get().Customer()), true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Customer Group has been selected";
			}

			@Override
			protected List<ClientCustomerGroup> getLists(Context context) {
				return getClientCompany().getCustomerGroups();
			}

			@Override
			protected boolean filter(ClientCustomerGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(VATREGISTER_NUM, getMessages()
				.pleaseEnter(getConstants().vatRegistrationNumber()),
				getConstants().vatRegistrationNumber(), true, true));

		list.add(new TaxCodeRequirement(CUSTOMER_VATCODE,
				"Please enter the tax code name", getConstants().taxCode(),
				true, true, null) {

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(PAN_NUM, "Please enter the pan number",
				"Pan Number", true, true));

		list.add(new NumberRequirement(CST_NUM, getMessages().pleaseEnter(
				getMessages().customerNumber(Global.get().Customer())),
				getMessages().customerNumber(Global.get().Customer()), true,
				true));

		list.add(new NumberRequirement(SERVICE_TAX_NUM, getMessages()
				.pleaseEnter(getConstants().serviceTax()), getConstants()
				.serviceTax(), true, true));

		list.add(new NumberRequirement(TIN_NUM, getMessages().pleaseEnter(
				getConstants().tinNumber()), getConstants().tinNumber(), true,
				true));
	}

	// @Override
	// public Result run(Context context) {
	// Object attribute = context.getAttribute(INPUT_ATTR);
	// if (attribute == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	// String process = (String) context.getAttribute(PROCESS_ATTR);
	// Result result = null;
	// if (process != null) {
	// if (process.equals(CONTACT_PROCESS)) {
	// result = contactProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// if (process.equals(ADDRESS_PROCESS)) {
	// result = addressProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	//
	// Result makeResult = context.makeResult();
	// makeResult.add(getMessages().readyToCreate(getConstants().customer()));
	// ResultList list = new ResultList("values");
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	// makeResult.add(actions);
	//
	// result = nameRequirement(context, list, CUSTOMER_NAME, Global.get()
	// .Customer(),
	// getMessages().pleaseEnterName(Global.get().Customer()));
	// if (result != null) {
	// return result;
	// }
	//
	// if (context.getCompany().getPreferences().getUseCustomerId()) {
	// result = numberRequirement(context, list, NUMBER, getMessages()
	// .customerNumber(Global.get().Customer()), getMessages()
	// .pleaseEnter(getConstants().customerNumber()));
	// if (result != null) {
	// return result;
	// }
	// }
	// setDefaultValues();
	//
	// result = optionalRequirements(context, list, actions, makeResult);
	// if (result != null) {
	// return result;
	// }
	//
	// createCustomerObject(context);
	//
	// markDone();
	//
	// result = new Result();
	// result.add(getMessages().createSuccessfully(Global.get().Customer()));
	//
	// return result;
	//
	// }

	/**
	 * Setting the default Values
	 */
	private void setDefaultValues() {

		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(CUSTOMER_SINCEDATE).setDefaultValue(new ClientFinanceDate());
		get(BALANCE_ASOF_DATE).setDefaultValue(new ClientFinanceDate());
		get(ADDRESS).setDefaultValue(new ClientAddress());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ICountryPreferences countryPreferences = getClientCompany()
				.getCountryPreferences();
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();

		ClientCustomer customer = new ClientCustomer();
		String name = get(CUSTOMER_NAME).getValue();
		String number = null;
		if (preferences.getUseCustomerId()) {
			number = get(NUMBER).getValue().toString();
		}
		ClientContact contact = (get(CUSTOMER_CONTACT).getValue());
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		ClientFinanceDate balancedate = get(BALANCE_ASOF_DATE).getValue();
		double balance = get(BALANCE).getValue();
		ClientAddress adress = get(ADDRESS).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEBADRESS).getValue();
		ClientSalesPerson salesPerson = get(SALESPERSON).getValue();
		ClientCreditRating creditRating = get(CREDIT_RATING).getValue();
		ClientPriceLevel priceLevel = get(PRICE_LEVEL).getValue();
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(BANK_ACCOUNT_NUM).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientPaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();
		ClientCustomerGroup customerGroup = get(CUSTOMER_GROUP).getValue();
		String vatRegistredNum = get(VATREGISTER_NUM).getValue();
		ClientTAXCode taxCode = get(CUSTOMER_VATCODE).getValue();
		ClientShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		// String panNum = get(PAN_NUM).getValue();
		String cstNum = get(CST_NUM).getValue();
		String serviceTaxNum = get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();
		HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
		if (adress != null) {
			addresses.add(adress);
		}
		customer.setName(name);
		if (preferences.getUseCustomerId())
			customer.setNumber(number);

		HashSet<ClientContact> contacts = new HashSet<ClientContact>();
		if (contact != null)
			contacts.add(contact);
		customer.setContacts(contacts);
		customer.setBalance(balance);
		if (balancedate != null) {
			customer.setBalanceAsOf(balancedate.getDate());
		}
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
		if (priceLevel != null) {
			customer.setPriceLevel(priceLevel.getID());
		}
		if (creditRating != null) {
			customer.setCreditRating(creditRating.getID());
		}
		customer.setActive(isActive);
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
	 * @param context
	 * @param actions
	 * @param list
	 * @param makeResult
	 * @return
	 */
	// private Result optionalRequirements(Context context, ResultList list,
	// ResultList actions, Result makeResult) {
	// Object selection = context.getSelection(ACTIONS);
	//
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// return null;
	// default:
	// break;
	// }
	// }
	//
	// selection = context.getSelection("values");
	// booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
	// getMessages().active(getConstants().customer()), getMessages()
	// .inActive(getConstants().customer()));
	//
	// Result result = dateOptionalRequirement(
	// context,
	// list,
	// CUSTOMER_SINCEDATE,
	// getMessages().customerSince(Global.get().Customer()),
	// getMessages().pleaseEnter(
	// getMessages().customerSince(Global.get().Customer())),
	// selection);
	//
	// if (result != null) {
	// return result;
	// }
	// result = amountOptionalRequirement(context, list, selection, BALANCE,
	// getMessages().pleaseEnter(getConstants().balance()),
	// getConstants().balance());
	// if (result != null) {
	// return result;
	// }
	// result = dateOptionalRequirement(context, list, BALANCE_ASOF_DATE,
	// getConstants().balanceAsOfDate(),
	// getMessages().pleaseEnter(getConstants().balanceAsOfDate()),
	// selection);
	// if (result != null) {
	// return result;
	// }
	// result = addressOptionalRequirement(context, list, selection, ADDRESS,
	// getMessages().pleaseEnter(getConstants().address()),
	// getConstants().address());
	// if (result != null) {
	// return result;
	// }
	//
	// result = contactOptionalRequirement(context, list, selection,
	// CUSTOMER_CONTACT,
	// getMessages().pleaseEnter(getConstants().contact()),
	// getConstants().contact());
	// if (result != null) {
	// return result;
	// }
	// result = numberOptionalRequirement(context, list, selection, FAX,
	// getConstants().faxNumber(),
	// getMessages().pleaseEnter(getConstants().fax()));
	// if (result != null) {
	// return result;
	// }
	// result = stringOptionalRequirement(context, list, selection, EMAIL,
	// getConstants().email(),
	// getMessages().pleaseEnter(getConstants().email()));
	// if (result != null) {
	// return result;
	// }
	// result = numberOptionalRequirement(context, list, selection, PHONE,
	// getConstants().phoneNumber(),
	// getMessages().pleaseEnter(getConstants().phoneNumber()));
	// if (result != null) {
	// return result;
	// }
	// result = stringOptionalRequirement(context, list, selection, WEBADRESS,
	// getConstants().webPageAddress(),
	// getMessages().pleaseEnter(getConstants().webPageAddress()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = salesPersonRequirement(context, list, selection,
	// getConstants().salesPerson());
	// if (result != null) {
	// return result;
	// }
	// // result = priceLevelRequirement(context, list, selection);
	// // if (result != null) {
	// // return result;
	// // }
	//
	// result = creditRatingRequirement(context, list, selection,
	// getConstants().creditRating());
	// if (result != null) {
	// return result;
	// }
	// result = stringOptionalRequirement(context, list, selection, BANK_NAME,
	// getConstants().bankName(),
	// getMessages().pleaseEnter(getConstants().bankName()));
	// if (result != null) {
	// return result;
	// }
	// result = numberOptionalRequirement(context, list, selection,
	// BANK_ACCOUNT_NUM, getConstants().bankAccountNumber(),
	// getMessages().pleaseEnter(getConstants().bankAccountNumber()));
	// if (result != null) {
	// return result;
	// }
	// result = stringOptionalRequirement(context, list, selection,
	// BANK_BRANCH, getConstants().bankBranch(), getMessages()
	// .pleaseEnter(getConstants().bankBranch()));
	// if (result != null) {
	// return result;
	// }
	// ICountryPreferences countryPreferences = getClientCompany()
	// .getCountryPreferences();
	// ClientCompanyPreferences preferences = getClientCompany()
	// .getPreferences();
	//
	// result = paymentMethodOptionalRequirement(context, list,
	// (String) selection);
	// if (result != null) {
	// return result;
	// }
	// result = paymentTermRequirement(context, list, selection);
	// if (result != null) {
	// return result;
	// }
	// if (preferences.isDoProductShipMents()) {
	// result = shippingMethodRequirement(context, list, selection);
	// if (result != null) {
	// return result;
	// }
	// }
	// result = customerGroupRequirement(context, list, selection,
	// getMessages().customerGroup(Global.get().Customer()));
	// if (result != null) {
	// return result;
	// }
	// if (preferences.isTrackTax()) {
	// if (countryPreferences.isVatAvailable()) {
	// result = numberOptionalRequirement(
	// context,
	// list,
	// selection,
	// VATREGISTER_NUM,
	// getConstants().vatRegistrationNumber(),
	// getMessages().pleaseEnter(
	// getConstants().vatRegistrationNumber()));
	// if (result != null) {
	// return result;
	// }
	// }
	// result = customerVatCodeRequirement(context, list, selection,
	// getConstants().vatCode());
	// if (result != null) {
	// return result;
	// }
	// }
	//
	// if (preferences.isTrackTax()) {
	// // result = stringOptionalRequirement(context, list, selection,
	// // PAN_NUM, "Enter Personal Ledger number");
	// // if (result != null) {
	// // return result;
	// // }
	// if (countryPreferences.isSalesTaxAvailable()) {
	// result = numberOptionalRequirement(context, list, selection,
	// CST_NUM,
	// getMessages().pleaseEnter(getConstants().cstNumber()),
	// getConstants().cstNumber());
	// if (result != null) {
	// return result;
	// }
	// }
	// if (countryPreferences.isServiceTaxAvailable()) {
	// result = numberOptionalRequirement(
	// context,
	// list,
	// selection,
	// SERVICE_TAX_NUM,
	// getMessages().pleaseEnter(
	// getConstants().serviceTaxRegistrationNumber()),
	// getConstants().serviceTaxRegistrationNumber());
	// if (result != null) {
	// return result;
	// }
	// }
	// if (countryPreferences.isTDSAvailable()) {
	// result = numberOptionalRequirement(context, list, selection,
	// TIN_NUM,
	// getMessages().pleaseEnter(getConstants().tinNumber()),
	// getConstants().tinNumber());
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	//
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add("", "Finish to create Customer.");
	// actions.add(finish);
	// return makeResult;
	// }

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	// private Result customerVatCodeRequirement(Context context, ResultList
	// list,
	// Object selection, String name) {
	//
	// Object customerVatCodeObj = context.getSelection(TAXCODE);
	// if (customerVatCodeObj instanceof ActionNames) {
	// customerVatCodeObj = null;
	// selection = "vatCode";
	// }
	// Requirement customerVatCodeReq = get(CUSTOMER_VATCODE);
	// ClientTAXCode vatCode = (ClientTAXCode) customerVatCodeReq.getValue();
	//
	// if (customerVatCodeObj != null) {
	// vatCode = (ClientTAXCode) customerVatCodeObj;
	// customerVatCodeReq.setValue(vatCode);
	// }
	// if (selection != null) {
	// if (selection.equals("vatCode")) {
	// context.setAttribute(INPUT_ATTR, CUSTOMER_VATCODE);
	// return taxCode(context, vatCode);
	// }
	// }
	// Record customerVatCodeRecord = new Record("vatCode");
	// customerVatCodeRecord.add("", name);
	// customerVatCodeRecord.add("", vatCode == null ? "" : vatCode.getName()
	// + "-" + vatCode.getSalesTaxRate());
	// list.add(customerVatCodeRecord);
	//
	// Result result = new Result();
	// result.add(list);
	// return null;
	// }

	/**
	 * CustomerGroup
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link CustomerGroupResult}
	 */
	// private Result customerGroupRequirement(Context context, ResultList list,
	// Object selection, String name) {
	//
	// Object customerGroupObj = context.getSelection(CUSTOMER_GROUP);
	// if (customerGroupObj instanceof ActionNames) {
	// customerGroupObj = null;
	// selection = CUSTOMER_GROUP;
	// }
	// Requirement customerGroupReq = get(CUSTOMER_GROUP);
	// ClientCustomerGroup customerGroup = (ClientCustomerGroup)
	// customerGroupReq
	// .getValue();
	// if (customerGroupObj != null) {
	// customerGroup = (ClientCustomerGroup) customerGroupObj;
	// customerGroupReq.setValue(customerGroup);
	// }
	// if (selection != null)
	// if (selection == CUSTOMER_GROUP) {
	// context.setLast(RequirementType.CUSTOMERGROUP, customerGroup);
	// context.setAttribute(INPUT_ATTR, CUSTOMER_GROUP);
	// return customerGroups(context, customerGroup);
	// }
	//
	// Record customerGroupRecord = new Record(CUSTOMER_GROUP);
	// customerGroupRecord.add("Name", name);
	// customerGroupRecord.add("Value", customerGroup == null ? ""
	// : customerGroup.getName());
	// list.add(customerGroupRecord);
	//
	// Result result = new Result();
	// result.add(list);
	// return null;
	// }

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	// private Result customerGroups(Context context,
	// ClientCustomerGroup oldCustomerGroup) {
	//
	// List<ClientCustomerGroup> customerGroups = getClientCompany()
	// .getCustomerGroups();
	// Result result = context.makeResult();
	// result.add("Select CustomerGroup");
	// Object last = context.getLast(RequirementType.CUSTOMERGROUP);
	// ResultList list = new ResultList(CUSTOMER_GROUP);
	// List<ClientCustomerGroup> skipCustomerGroups = new
	// ArrayList<ClientCustomerGroup>();
	// if (last != null) {
	// list.add(createCustomerGroupRecord(oldCustomerGroup));
	// skipCustomerGroups.add((ClientCustomerGroup) last);
	// }
	// ActionNames selection = context.getSelection(CUSTOMER_GROUP);
	//
	// List<Record> actions = new ArrayList<Record>();
	//
	// List<ClientCustomerGroup> pagination = pagination(context, selection,
	// actions, customerGroups, skipCustomerGroups,
	// CUSTOMERGROUP_TO_SHOW);
	//
	// for (ClientCustomerGroup term : pagination) {
	// list.add(createCustomerGroupRecord(term));
	// }
	//
	// for (Record record : actions) {
	// list.add(record);
	// }
	// result.add(list);
	//
	// CommandList commandList = new CommandList();
	// commandList.add("Create CustomerGroup");
	// result.add(commandList);
	//
	// return result;
	// }

	private Record createCustomerGroupRecord(
			ClientCustomerGroup oldCustomerGroup) {
		Record record = new Record(oldCustomerGroup);
		record.add("Name", oldCustomerGroup.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	// private Result creditRatingRequirement(Context context, ResultList list,
	// Object selection, String name) {
	//
	// Object crediRatingObj = context.getSelection(CREDIT_RATING);
	// if (crediRatingObj instanceof ActionNames) {
	// crediRatingObj = null;
	// selection = CREDIT_RATING;
	// }
	// Requirement creditRatingReq = get(CREDIT_RATING);
	// ClientCreditRating creditRating = (ClientCreditRating) creditRatingReq
	// .getValue();
	// if (crediRatingObj != null) {
	// creditRating = (ClientCreditRating) crediRatingObj;
	// creditRatingReq.setValue(creditRating);
	// }
	// if (selection != null)
	// if (selection == CREDIT_RATING) {
	// context.setAttribute(INPUT_ATTR, CREDIT_RATING);
	// context.setLast(RequirementType.CREDITRATING, creditRating);
	// return creditRatings(context, creditRating);
	// }
	//
	// Record priceLevelRecord = new Record(CREDIT_RATING);
	// priceLevelRecord.add("Name", name);
	// priceLevelRecord.add("Value",
	// creditRating == null ? "" : creditRating.getName());
	// list.add(priceLevelRecord);
	//
	// Result result = new Result();
	// result.add(list);
	// return null;
	// }

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	// private Result creditRatings(Context context,
	// ClientCreditRating oldCreditRating) {
	//
	// List<ClientCreditRating> creditRatings = getCreditRatingsList();
	// Result result = context.makeResult();
	// result.add("Select CreditRating");
	//
	// ResultList list = new ResultList(CREDIT_RATING);
	// List<ClientCreditRating> skipCreditRatings = new
	// ArrayList<ClientCreditRating>();
	// Object last = context.getLast(RequirementType.CREDITRATING);
	// if (last != null) {
	// list.add(createCreditRatingRecord(oldCreditRating));
	// skipCreditRatings.add((ClientCreditRating) last);
	// }
	// ActionNames selection = context.getSelection(CREDIT_RATING);
	//
	// List<Record> actions = new ArrayList<Record>();
	//
	// List<ClientCreditRating> pagination = pagination(context, selection,
	// actions, creditRatings, skipCreditRatings, CREDITRATING_TO_SHOW);
	//
	// for (ClientCreditRating term : pagination) {
	// list.add(createCreditRatingRecord(term));
	// }
	//
	// for (Record record : actions) {
	// list.add(record);
	// }
	// result.add(list);
	//
	// CommandList commandList = new CommandList();
	// commandList.add("Create creditRating");
	// result.add(commandList);
	// return result;
	// }

	/**
	 * 
	 * @param oldCreditRating
	 * @return
	 */
	private Record createCreditRatingRecord(ClientCreditRating oldCreditRating) {
		Record record = new Record(oldCreditRating);
		record.add("Name", oldCreditRating.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @param name
	 * @return {@link Result}
	 */
	// private Result salesPersonRequirement(Context context, ResultList list,
	// Object selection, String name) {
	//
	// Object salesPersonObj = context.getSelection(SALESPERSON);
	// if (salesPersonObj instanceof ActionNames) {
	// salesPersonObj = null;
	// selection = SALESPERSON;
	// }
	// Requirement salesPersonReq = get(SALESPERSON);
	// ClientSalesPerson salesPerson = (ClientSalesPerson) salesPersonReq
	// .getValue();
	// if (salesPersonObj != null) {
	// salesPerson = (ClientSalesPerson) salesPersonObj;
	// salesPersonReq.setValue(salesPerson);
	// }
	// if (selection != null)
	// if (selection == SALESPERSON) {
	// context.setLast(RequirementType.SALESPERSON, salesPerson);
	// context.setAttribute(INPUT_ATTR, SALESPERSON);
	// return salesPersons(context, salesPerson);
	// }
	//
	// Record salesPersonRecord = new Record(SALESPERSON);
	// salesPersonRecord.add("Name", name);
	// salesPersonRecord.add("Value",
	// salesPerson == null ? "" : salesPerson.getFirstName());
	// list.add(salesPersonRecord);
	//
	// Result result = new Result();
	// result.add(list);
	// return null;
	// }

	/**
	 * 
	 * @param context
	 * 
	 * @param string
	 * @return {@link SalesPerson Result}
	 */
	// protected Result salesPersons(Context context,
	// ClientSalesPerson oldsalesPerson) {
	// List<ClientSalesPerson> salesPersons = getClientCompany()
	// .getSalesPersons();
	// Result result = context.makeResult();
	// result.add("Select SalesPerson");
	// Object last = context.getLast(RequirementType.SALESPERSON);
	// List<ClientSalesPerson> skipingrecords = new
	// ArrayList<ClientSalesPerson>();
	// ResultList list = new ResultList(SALESPERSON);
	// if (last != null) {
	// list.add(createSalesPersonRecord(oldsalesPerson));
	// skipingrecords.add((ClientSalesPerson) last);
	// }
	//
	// ActionNames selection = context.getSelection(SALESPERSON);
	//
	// List<Record> actions = new ArrayList<Record>();
	//
	// List<ClientSalesPerson> pagination = pagination(context, selection,
	// actions, salesPersons, skipingrecords, SALESPERSON_TO_SHOW);
	//
	// for (ClientSalesPerson term : pagination) {
	// list.add(createSalesPersonRecord(term));
	// }
	//
	// for (Record record : actions) {
	// list.add(record);
	// }
	// result.add(list);
	//
	// CommandList commandList = new CommandList();
	// commandList.add("Create Sales Person");
	// result.add(commandList);
	// return result;
	// }

	/**
	 * 
	 * @param oldsalesPerson
	 * @return {@link Record}
	 */
	private Record createSalesPersonRecord(ClientSalesPerson oldsalesPerson) {
		Record record = new Record(oldsalesPerson);
		record.add("Name", SALESPERSON);
		record.add("value", oldsalesPerson.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private List<ClientCreditRating> getCreditRatingsList() {
		ArrayList<ClientCreditRating> creditRatings = getClientCompany()
				.getCreditRatings();
		return new ArrayList<ClientCreditRating>(creditRatings);
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
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(CUSTOMER_SINCEDATE).setDefaultValue(new ClientFinanceDate());
		get(BALANCE_ASOF_DATE).setDefaultValue(new ClientFinanceDate());
		get(ADDRESS).setDefaultValue(new ClientAddress());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(Global.get().Customer());
	}

}
