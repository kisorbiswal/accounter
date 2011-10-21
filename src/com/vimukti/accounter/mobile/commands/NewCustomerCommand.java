package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
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

public class NewCustomerCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";
	private static final int SALESPERSON_TO_SHOW = 5;
	private static final int PRICELEVEL_TO_SHOW = 5;
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
	private static final String CUSTOMER_CONTACT = "customerContact";
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

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(CUSTOMER_NAME, false, true));
		list.add(new Requirement(NUMBER, false, true));
		list.add(new ObjectListRequirement(CUSTOMER_CONTACT, true, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(PRIMARY, true, true));
				list.add(new Requirement(CONTACT_NAME, false, true));
				list.add(new Requirement(TITLE, true, true));
				list.add(new Requirement(BUSINESS_PHONE, true, true));
				list.add(new Requirement(EMAIL, true, true));
			}
		});
		list.add(new Requirement(IS_ACTIVE, true, true));
		list.add(new Requirement(CUSTOMER_SINCEDATE, true, true));
		list.add(new Requirement(BALANCE, true, true));
		list.add(new Requirement(BALANCE_ASOF_DATE, true, true));
		list.add(new Requirement(ADDRESS, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEBADRESS, true, true));
		list.add(new Requirement(SALESPERSON, true, true));
		list.add(new Requirement(SHIPPING_METHODS, true, true));
		list.add(new Requirement(PRICE_LEVEL, true, true));
		list.add(new Requirement(CREDIT_RATING, true, true));
		list.add(new Requirement(BANK_NAME, true, true));
		list.add(new Requirement(BANK_ACCOUNT_NUM, true, true));
		list.add(new Requirement(BANK_BRANCH, true, true));
		list.add(new Requirement(PAYMENT_METHOD, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(CUSTOMER_GROUP, true, true));
		list.add(new Requirement(VATREGISTER_NUM, true, true));
		list.add(new Requirement(CUSTOMER_VATCODE, true, true));
		list.add(new Requirement(PAN_NUM, true, true));
		list.add(new Requirement(CST_NUM, true, true));
		list.add(new Requirement(SERVICE_TAX_NUM, true, true));
		list.add(new Requirement(TIN_NUM, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(CONTACT_PROCESS)) {
				result = contactProcess(context);
				if (result != null) {
					return result;
				}
			}
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().customer()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, CUSTOMER_NAME, Global.get()
				.Customer(), getMessages().pleaseEnter(Global.get().Customer()));
		if (result != null) {
			return result;
		}

		if (context.getCompany().getPreferences().getUseCustomerId()) {
			result = numberRequirement(context, list, NUMBER, getMessages()
					.customerNumber(Global.get().Customer()), getMessages()
					.pleaseEnter(getConstants().customerNumber()));
			if (result != null) {
				return result;
			}
		}
		setDefaultValues();
		result = optionalRequirements(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		createCustomerObject(context);

		markDone();

		result = new Result();
		result.add(getMessages().createSuccessfully(Global.get().Customer()));

		return result;

	}

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

	/**
	 * 
	 * @param context
	 * @return
	 */
	private void createCustomerObject(Context context) {

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
		Set<ClientContact> contacts = (get(CUSTOMER_CONTACT).getValue());
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		ClientFinanceDate balancedate = get(BALANCE_ASOF_DATE).getValue();
		ClientFinanceDate customerSincedate = get(CUSTOMER_SINCEDATE)
				.getValue();
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

	}

	/**
	 * 
	 * @param context
	 * @param actions
	 * @param list
	 * @param makeResult
	 * @return
	 */
	private Result optionalRequirements(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
				getMessages().active(getConstants().customer()), getMessages()
						.inActive(getConstants().customer()));

		Result result = dateOptionalRequirement(
				context,
				list,
				CUSTOMER_SINCEDATE,
				getMessages().customerSince(Global.get().Customer()),
				getMessages().pleaseEnter(
						getMessages().customerSince(Global.get().Customer())),
				selection);

		if (result != null) {
			return result;
		}
		result = amountOptionalRequirement(context, list, selection, BALANCE,
				getMessages().pleaseEnter(getConstants().balance()),
				getConstants().balance());
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, BALANCE_ASOF_DATE,
				getConstants().balanceAsOfDate(),
				getMessages().pleaseEnter(getConstants().balanceAsOfDate()),
				selection);
		if (result != null) {
			return result;
		}
		result = addressOptionalRequirement(context, list, selection, ADDRESS,
				getMessages().pleaseEnter(getConstants().address()),
				getConstants().address());
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, FAX,
				getMessages().pleaseEnter(getConstants().fax()), getConstants()
						.fax());
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, EMAIL,
				getMessages().pleaseEnter(getConstants().email()),
				getConstants().email());
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, PHONE,
				getMessages().pleaseEnter(getConstants().phone()),
				getConstants().phone());
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, WEBADRESS,
				getMessages().pleaseEnter(getConstants().webPageAddress()),
				getConstants().webPageAddress());
		if (result != null) {
			return result;
		}

		result = salesPersonRequirement(context, list, selection,
				getConstants().salesPerson());
		if (result != null) {
			return result;
		}
		// result = priceLevelRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		result = creditRatingRequirement(context, list, selection,
				getConstants().creditRating());
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, BANK_NAME,
				getMessages().pleaseEnter(getConstants().bankName()),
				getConstants().bankName());
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection,
				BANK_ACCOUNT_NUM,
				getMessages().pleaseEnter(getConstants().bankAccountNumber()),
				getConstants().bankAccountNumber());
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection,
				BANK_BRANCH,
				getMessages().pleaseEnter(getConstants().bankBranch()),
				getConstants().bankBranch());
		if (result != null) {
			return result;
		}
		ICountryPreferences countryPreferences = getClientCompany()
				.getCountryPreferences();
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();

		result = paymentMethodOptionalRequirement(context, list,
				(String) selection);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (preferences.isDoProductShipMents()) {
			result = shippingMethodRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}
		result = customerGroupRequirement(context, list, selection,
				getMessages().customerGroup(Global.get().Customer()));
		if (result != null) {
			return result;
		}
		if (preferences.isTrackTax()) {
			if (countryPreferences.isVatAvailable()) {
				result = numberOptionalRequirement(
						context,
						list,
						selection,
						VATREGISTER_NUM,
						getMessages().pleaseEnter(
								getConstants().vatRegistrationNumber()),
						getConstants().vatRegistrationNumber());
				if (result != null) {
					return result;
				}
			}
			result = customerVatCodeRequirement(context, list, selection,
					getConstants().vatCode());
			if (result != null) {
				return result;
			}
		}

		if (preferences.isTrackTax()) {
			// result = stringOptionalRequirement(context, list, selection,
			// PAN_NUM, "Enter Personal Ledger number");
			// if (result != null) {
			// return result;
			// }
			if (countryPreferences.isSalesTaxAvailable()) {
				result = numberOptionalRequirement(context, list, selection,
						CST_NUM,
						getMessages().pleaseEnter(getConstants().cstNumber()),
						getConstants().cstNumber());
				if (result != null) {
					return result;
				}
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				result = numberOptionalRequirement(
						context,
						list,
						selection,
						SERVICE_TAX_NUM,
						getMessages().pleaseEnter(
								getConstants().serviceTaxRegistrationNumber()),
						getConstants().serviceTaxRegistrationNumber());
				if (result != null) {
					return result;
				}
			}
			if (countryPreferences.isTDSAvailable()) {
				result = numberOptionalRequirement(context, list, selection,
						TIN_NUM,
						getMessages().pleaseEnter(getConstants().tinNumber()),
						getConstants().tinNumber());
				if (result != null) {
					return result;
				}
			}
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Customer.");
		actions.add(finish);
		return makeResult;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result customerVatCodeRequirement(Context context, ResultList list,
			Object selection, String name) {

		Object customerVatCodeObj = context.getSelection(TAXCODE);
		if (customerVatCodeObj instanceof ActionNames) {
			customerVatCodeObj = null;
			selection = "vatCode";
		}
		Requirement customerVatCodeReq = get(CUSTOMER_VATCODE);
		ClientTAXCode vatCode = (ClientTAXCode) customerVatCodeReq.getValue();

		if (customerVatCodeObj != null) {
			vatCode = (ClientTAXCode) customerVatCodeObj;
			customerVatCodeReq.setValue(vatCode);
		}
		if (selection != null) {
			if (selection.equals("vatCode")) {
				context.setAttribute(INPUT_ATTR, CUSTOMER_VATCODE);
				return taxCode(context, vatCode);
			}
		}
		Record customerVatCodeRecord = new Record("vatCode");
		customerVatCodeRecord.add("", name);
		customerVatCodeRecord.add("", vatCode == null ? "" : vatCode.getName()
				+ "-" + vatCode.getSalesTaxRate());
		list.add(customerVatCodeRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	/**
	 * CustomerGroup
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link CustomerGroupResult}
	 */
	private Result customerGroupRequirement(Context context, ResultList list,
			Object selection, String name) {

		Object customerGroupObj = context.getSelection(CUSTOMER_GROUP);
		if (customerGroupObj instanceof ActionNames) {
			customerGroupObj = null;
			selection = CUSTOMER_GROUP;
		}
		Requirement customerGroupReq = get(CUSTOMER_GROUP);
		ClientCustomerGroup customerGroup = (ClientCustomerGroup) customerGroupReq
				.getValue();
		if (customerGroupObj != null) {
			customerGroup = (ClientCustomerGroup) customerGroupObj;
			customerGroupReq.setValue(customerGroup);
		}
		if (selection != null)
			if (selection == CUSTOMER_GROUP) {
				context.setAttribute(INPUT_ATTR, CUSTOMER_GROUP);
				return customerGroups(context, customerGroup);
			}

		Record customerGroupRecord = new Record(CUSTOMER_GROUP);
		customerGroupRecord.add("Name", name);
		customerGroupRecord.add("Value", customerGroup == null ? ""
				: customerGroup.getName());
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result customerGroups(Context context,
			ClientCustomerGroup oldCustomerGroup) {
		List<ClientCustomerGroup> customerGroups = getClientCompany()
				.getCustomerGroups();
		Result result = context.makeResult();
		result.add("Select CustomerGroup");

		ResultList list = new ResultList(CUSTOMER_GROUP);
		if (oldCustomerGroup != null) {
			list.add(createCustomerGroupRecord(oldCustomerGroup));
		}
		ActionNames selection = context.getSelection(CUSTOMER_GROUP);

		List<Record> actions = new ArrayList<Record>();

		List<ClientCustomerGroup> pagination = pagination(context, selection,
				actions, customerGroups, new ArrayList<ClientCustomerGroup>(),
				CUSTOMERGROUP_TO_SHOW);

		for (ClientCustomerGroup term : pagination) {
			list.add(createCustomerGroupRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create CustomerGroup");
		result.add(commandList);

		return result;
	}

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
	private Result creditRatingRequirement(Context context, ResultList list,
			Object selection, String name) {

		Object crediRatingObj = context.getSelection(CREDIT_RATING);
		if (crediRatingObj instanceof ActionNames) {
			crediRatingObj = null;
			selection = CREDIT_RATING;
		}
		Requirement creditRatingReq = get(CREDIT_RATING);
		ClientCreditRating creditRating = (ClientCreditRating) creditRatingReq
				.getValue();
		if (crediRatingObj != null) {
			creditRating = (ClientCreditRating) crediRatingObj;
			creditRatingReq.setValue(creditRating);
		}
		if (selection != null)
			if (selection == CREDIT_RATING) {
				context.setAttribute(INPUT_ATTR, CREDIT_RATING);
				return creditRatings(context, creditRating);
			}

		Record priceLevelRecord = new Record(CREDIT_RATING);
		priceLevelRecord.add("Name", name);
		priceLevelRecord.add("Value",
				creditRating == null ? "" : creditRating.getName());
		list.add(priceLevelRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result creditRatings(Context context,
			ClientCreditRating oldCreditRating) {

		List<ClientCreditRating> creditRatings = getCreditRatingsList();
		Result result = context.makeResult();
		result.add("Select CreditRating");

		ResultList list = new ResultList(CREDIT_RATING);

		if (oldCreditRating != null) {
			list.add(createCreditRatingRecord(oldCreditRating));
		}
		ActionNames selection = context.getSelection(CREDIT_RATING);

		List<Record> actions = new ArrayList<Record>();

		List<ClientCreditRating> pagination = pagination(context, selection,
				actions, creditRatings, new ArrayList<ClientCreditRating>(),
				CREDITRATING_TO_SHOW);

		for (ClientCreditRating term : pagination) {
			list.add(createCreditRatingRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create creditRating");
		result.add(commandList);
		return result;
	}

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
	 * @return
	 */
	private Result priceLevelRequirement(Context context, ResultList list,
			Object selection) {

		Object priceLevelObj = context.getSelection(PRICE_LEVEL);
		Requirement priceLevelReq = get(PRICE_LEVEL);
		ClientPriceLevel priceLevel = (ClientPriceLevel) priceLevelReq
				.getValue();

		if (priceLevelObj != null) {
			priceLevel = (ClientPriceLevel) priceLevelObj;
			priceLevelReq.setValue(priceLevel);
		}
		if (selection != null)
			if (selection == PRICE_LEVEL) {
				context.setAttribute(INPUT_ATTR, PRICE_LEVEL);
				return priceLevels(context, priceLevel);
			}

		Record priceLevelRecord = new Record(PRICE_LEVEL);
		priceLevelRecord.add("Name", PRICE_LEVEL);
		priceLevelRecord.add("Value",
				priceLevel == null ? "" : priceLevel.getName());
		list.add(priceLevelRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result priceLevels(Context context, ClientPriceLevel oldPriceLevel) {

		List<ClientPriceLevel> priceLevels = getClientCompany()
				.getPriceLevels();
		Result result = context.makeResult();
		result.add("Select PriceLevel");

		ResultList list = new ResultList(PRICE_LEVEL);
		int num = 0;
		if (oldPriceLevel != null) {
			list.add(createCreditRatingRecord(oldPriceLevel));
			num++;
		}
		for (ClientPriceLevel priceLevel : priceLevels) {
			if (priceLevel != oldPriceLevel) {
				list.add(createCreditRatingRecord(priceLevel));
				num++;
			}
			if (num == PRICELEVEL_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create priceLevel");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldPriceLevel
	 * @return
	 */
	private Record createCreditRatingRecord(ClientPriceLevel oldPriceLevel) {
		Record record = new Record(oldPriceLevel);
		record.add("Name", oldPriceLevel.getName());
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
	private Result salesPersonRequirement(Context context, ResultList list,
			Object selection, String name) {

		Object salesPersonObj = context.getSelection(SALESPERSON);
		if (salesPersonObj instanceof ActionNames) {
			salesPersonObj = null;
			selection = SALESPERSON;
		}
		Requirement salesPersonReq = get(SALESPERSON);
		ClientSalesPerson salesPerson = (ClientSalesPerson) salesPersonReq
				.getValue();
		if (salesPersonObj != null) {
			salesPerson = (ClientSalesPerson) salesPersonObj;
			salesPersonReq.setValue(salesPerson);
		}
		if (selection != null)
			if (selection == SALESPERSON) {
				context.setAttribute(INPUT_ATTR, SALESPERSON);
				return salesPersons(context, salesPerson);
			}

		Record salesPersonRecord = new Record(SALESPERSON);
		salesPersonRecord.add("Name", name);
		salesPersonRecord.add("Value",
				salesPerson == null ? "" : salesPerson.getFirstName());
		list.add(salesPersonRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	/**
	 * 
	 * @param context
	 * 
	 * @param string
	 * @return {@link SalesPerson Result}
	 */
	protected Result salesPersons(Context context,
			ClientSalesPerson oldsalesPerson) {
		List<ClientSalesPerson> salesPersons = getClientCompany()
				.getSalesPersons();
		Result result = context.makeResult();
		result.add("Select SalesPerson");

		ResultList list = new ResultList(SALESPERSON);
		if (oldsalesPerson != null) {
			list.add(createSalesPersonRecord(oldsalesPerson));
		}

		ActionNames selection = context.getSelection(SALESPERSON);

		List<Record> actions = new ArrayList<Record>();

		List<ClientSalesPerson> pagination = pagination(context, selection,
				actions, salesPersons, new ArrayList<ClientSalesPerson>(),
				SALESPERSON_TO_SHOW);

		for (ClientSalesPerson term : pagination) {
			list.add(createSalesPersonRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Sales Person");
		result.add(commandList);
		return result;
	}

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

}
