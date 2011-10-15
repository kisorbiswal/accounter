package com.vimukti.accounter.mobile.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
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
		list.add(new Requirement("Preferred Shipping Method", true, true));
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
		result = nameRequirement(context, CUSTOMER_NAME,
				"Please enter the  Customer Name");
		if (result != null) {
			return result;
		}
		
		if (context.getCompany().getPreferences().getUseCustomerId()) {
			result = numberRequirement(context, NUMBER,
					"Please Enter the Customer Number.");
			if (result != null) {
				return result;
			}
		}
		setDefaultValues();
		result = optionalRequirements(context);
		if (result != null) {
			return result;
		}

		markDone();
		return createCustomerObject(context);
	}

	/**
	 * Setting the default Values
	 */
	private void setDefaultValues() {

		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(CUSTOMER_SINCEDATE).setDefaultValue(new Date());
		get(BALANCE_ASOF_DATE).setDefaultValue(new Date());
		get(ADDRESS).setDefaultValue(new Address());
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createCustomerObject(Context context) {

		ICountryPreferences countryPreferences = context.getCompany()
				.getCountryPreferences();
		CompanyPreferences preferences = context.getCompany().getPreferences();

		Customer customer = new Customer();
		customer.setCompany(context.getCompany());
		String name = get(CUSTOMER_NAME).getValue();
		String number = null;
		if (preferences.getUseCustomerId()) {
			number = get(NUMBER).getValue().toString();
		}
		Set<Contact> contacts = (get(CUSTOMER_CONTACT).getValue());
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		Date balancedate = get(BALANCE_ASOF_DATE).getValue();
		Date customerSincedate = get(CUSTOMER_SINCEDATE).getValue();
		double balance = get(BALANCE).getValue();
		Address adress = get(ADDRESS).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEBADRESS).getValue();
		SalesPerson salesPerson = get(SALESPERSON).getValue();
		CreditRating creditRating = get(CREDIT_RATING).getValue();
		PriceLevel priceLevel = get(PRICE_LEVEL).getValue();
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(BANK_ACCOUNT_NUM).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		PaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();
		CustomerGroup customerGroup = get(CUSTOMER_GROUP).getValue();
		String vatRegistredNum = get(VATREGISTER_NUM).getValue();
		TAXCode taxCode = get(CUSTOMER_VATCODE).getValue();
		ShippingMethod shippingMethod = get("Preferred Shipping Method")
				.getValue();
		// String panNum = get(PAN_NUM).getValue();
		String cstNum = get(CST_NUM).getValue();
		String serviceTaxNum = get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();
		HashSet<Address> addresses = new HashSet<Address>();
		if (adress != null) {
			addresses.add(adress);
		}
		customer.setName(name);
		if (preferences.getUseCustomerId())
			customer.setNumber(number);
		customer.setContacts(contacts);
		customer.setBalance(balance);
		customer.setBalanceAsOf(new FinanceDate(balancedate));
		customer.setCreatedDate(new Timestamp(customerSincedate.getTime()));
		customer.setAddress(addresses);
		customer.setPhoneNo(phoneNum);
		customer.setFaxNo(faxNum);
		customer.setWebPageAddress(webaddress);
		customer.setBankAccountNo(bankAccountNum);
		customer.setBankBranch(bankBranch);
		customer.setBankName(bankName);
		customer.setEmail(emailId);
		customer.setSalesPerson(salesPerson);
		customer.setPriceLevel(priceLevel);
		customer.setCreditRating(creditRating);
		customer.setActive(isActive);
		customer.setPaymentMethod(paymentMethod);
		customer.setPaymentTerm(paymentTerms);
		customer.setCustomerGroup(customerGroup);
		customer.setShippingMethod(shippingMethod);
		if (preferences.isTrackTax()) {
			if (countryPreferences.isVatAvailable()) {
				customer.setTAXCode(taxCode);
				customer.setVATRegistrationNumber(vatRegistredNum);
			}
			if (countryPreferences.isSalesTaxAvailable()) {
				customer.setCSTno(cstNum);
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				customer.setServiceTaxRegistrationNo(serviceTaxNum);
			}
			if (countryPreferences.isTDSAvailable()) {
				customer.setTINNumber(tinNum);
			}
			// customer.setPANno(panNum);
		}

		Session session = context.getHibernateSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(customer);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add(" Customer was created successfully.");

		return result;

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result optionalRequirements(Context context) {
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CONTACTS:
				return contact(context, "Enter the Contact Details",
						CUSTOMER_CONTACT, null);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		ResultList list = new ResultList("values");

		Requirement contactReq = get(CUSTOMER_CONTACT);
		Set<Contact> contacts = contactReq.getValue();
		selection = context.getSelection(CUSTOMER_CONTACT);
		if (selection != null) {
			Result contact = contact(context, "customer contact",
					CUSTOMER_CONTACT, (Contact) selection);
			if (contact != null) {
				return contact;
			}
		}
		selection = context.getSelection("values");

		Requirement customerNameReq = get(CUSTOMER_NAME);
		String name = (String) customerNameReq.getValue();

		Requirement customerNumReq = get(NUMBER);
		String num = (String) customerNumReq.getValue();

		if (selection != null) {
			if (selection == "customerName") {
				context.setAttribute(INPUT_ATTR, CUSTOMER_NAME);
				return text(context, "Enter Customer Name", name);
			} else if (selection == "customerNumber") {
				return number(context, NUMBER, num);
			}
		}

		Record nameRecord = new Record("customerName");
		nameRecord.add("", "Customer Name");
		nameRecord.add("", name);
		list.add(nameRecord);

		Record numberRecord = new Record("customerNumber");
		numberRecord.add("", "Customer Number");
		numberRecord.add("", num);
		list.add(numberRecord);

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		String activeString = "";
		if (isActive) {
			activeString = "This customer is Active";
		} else {
			activeString = "This customer is InActive";
		}
		if (selection == activeString) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		if (isActive) {
			activeString = "This customer is Active";
		} else {
			activeString = "This customer is InActive";
		}
		Record isActiveRecord = new Record(activeString);
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);

		Result result = dateOptionalRequirement(context, list,
				CUSTOMER_SINCEDATE, "Enter CustomerSinceDate", selection);

		if (result != null) {
			return result;
		}
		result = amountOptionalRequirement(context, list, selection, BALANCE,
				"Enter Balance");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, BALANCE_ASOF_DATE,
				"Enter the " + BALANCE_ASOF_DATE, selection);
		if (result != null) {
			return result;
		}
		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, FAX,
				"Enter Fax Number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, EMAIL,
				"Enter email ");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, PHONE,
				"Enter Phone Number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, WEBADRESS,
				"Enter webPageAdress ");
		if (result != null) {
			return result;
		}

		result = salesPersonRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		// result = priceLevelRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		result = creditRatingRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, BANK_NAME,
				"Enter Bank Name ");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection,
				BANK_ACCOUNT_NUM, "Enter Bank AccountNumber");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection,
				BANK_BRANCH, "Enter bankBranch Name ");
		if (result != null) {
			return result;
		}
		ICountryPreferences countryPreferences = context.getCompany()
				.getCountryPreferences();
		CompanyPreferences preferences = context.getCompany().getPreferences();

		result = paymentMethodRequirement(context, list, (String) selection);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = preferredShippingMethodRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = customerGroupRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (preferences.isTrackTax()) {
			if (countryPreferences.isVatAvailable()) {
				result = stringOptionalRequirement(context, list, selection,
						VATREGISTER_NUM, "Enter vatRegisteration Number ");
				if (result != null) {
					return result;
				}
			}
			result = customerVatCodeRequirement(context, list, selection);
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
				result = stringOptionalRequirement(context, list, selection,
						CST_NUM, "Enter CST Number ");
				if (result != null) {
					return result;
				}
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				result = stringOptionalRequirement(context, list, selection,
						SERVICE_TAX_NUM,
						"Enter Service tax registration Number ");
				if (result != null) {
					return result;
				}
			}
			if (countryPreferences.isTDSAvailable()) {
				result = stringOptionalRequirement(context, list, selection,
						TIN_NUM, "Enter Taxpayer identification number");
				if (result != null) {
					return result;
				}
			}
		}

		result = context.makeResult();
		result.add("Customer is ready to create with following values.");
		result.add(list);
		result.add("Contacts:-");
		ResultList contactList = new ResultList(CUSTOMER_CONTACT);
		if (contacts != null) {
			for (Contact item : contacts) {
				Record itemRec = new Record(item);
				itemRec.add(PRIMARY, item.getVersion());
				itemRec.add(CONTACT_NAME, item.getName());
				itemRec.add(TITLE, item.getTitle());
				itemRec.add(BUSINESS_PHONE, item.getBusinessPhone());
				itemRec.add(EMAIL, item.getEmail());
				contactList.add(itemRec);
			}
		}
		result.add(contactList);
		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_CONTACTS);
		moreItems.add("", "Add more contacts");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Customer.");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result customerVatCodeRequirement(Context context, ResultList list,
			Object selection) {

		Object customerVatCodeObj = context.getSelection(TAXCODE);
		if (customerVatCodeObj instanceof ActionNames) {
			customerVatCodeObj = null;
			selection = TAXCODE;
		}
		Requirement customerVatCodeReq = get(CUSTOMER_VATCODE);
		TAXCode vatCode = (TAXCode) customerVatCodeReq.getValue();

		if (customerVatCodeObj != null) {
			vatCode = (TAXCode) customerVatCodeObj;
			customerVatCodeReq.setValue(vatCode);
		}
		if (selection != null) {
			if (selection.equals("vatCode")) {
				context.setAttribute(INPUT_ATTR, CUSTOMER_VATCODE);
				return taxCode(context, vatCode);
			}
		}
		Record customerVatCodeRecord = new Record("vatCode");
		customerVatCodeRecord.add("Name", "vatCode");
		customerVatCodeRecord.add(
				"Value",
				vatCode == null ? "" : vatCode.getName() + "-"
						+ vatCode.getSalesTaxRate());
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
			Object selection) {

		Object customerGroupObj = context.getSelection(CUSTOMER_GROUP);
		if (customerGroupObj instanceof ActionNames) {
			customerGroupObj = null;
			selection = CUSTOMER_GROUP;
		}
		Requirement customerGroupReq = get(CUSTOMER_GROUP);
		CustomerGroup customerGroup = (CustomerGroup) customerGroupReq
				.getValue();
		if (customerGroupObj != null) {
			customerGroup = (CustomerGroup) customerGroupObj;
			customerGroupReq.setValue(customerGroup);
		}
		if (selection != null)
			if (selection == CUSTOMER_GROUP) {
				context.setAttribute(INPUT_ATTR, CUSTOMER_GROUP);
				return customerGroups(context, customerGroup);
			}

		Record customerGroupRecord = new Record(CUSTOMER_GROUP);
		customerGroupRecord.add("Name", CUSTOMER_GROUP);
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
			CustomerGroup oldCustomerGroup) {
		List<CustomerGroup> customerGroups = new ArrayList<CustomerGroup>(
				context.getCompany().getCustomerGroups());
		Result result = context.makeResult();
		result.add("Select CustomerGroup");

		ResultList list = new ResultList(CUSTOMER_GROUP);
		if (oldCustomerGroup != null) {
			list.add(createCustomerGroupRecord(oldCustomerGroup));
		}
		ActionNames selection = context.getSelection(CUSTOMER_GROUP);

		List<Record> actions = new ArrayList<Record>();

		List<CustomerGroup> pagination = pagination(context, selection,
				actions, customerGroups, new ArrayList<CustomerGroup>(),
				CUSTOMERGROUP_TO_SHOW);

		for (CustomerGroup term : pagination) {
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

	private Record createCustomerGroupRecord(CustomerGroup oldCustomerGroup) {
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
			Object selection) {

		Object crediRatingObj = context.getSelection(CREDIT_RATING);
		if (crediRatingObj instanceof ActionNames) {
			crediRatingObj = null;
			selection = CREDIT_RATING;
		}
		Requirement creditRatingReq = get(CREDIT_RATING);
		CreditRating creditRating = (CreditRating) creditRatingReq.getValue();
		if (crediRatingObj != null) {
			creditRating = (CreditRating) crediRatingObj;
			creditRatingReq.setValue(creditRating);
		}
		if (selection != null)
			if (selection == CREDIT_RATING) {
				context.setAttribute(INPUT_ATTR, CREDIT_RATING);
				return creditRatings(context, creditRating);
			}

		Record priceLevelRecord = new Record(CREDIT_RATING);
		priceLevelRecord.add("Name", CREDIT_RATING);
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
	private Result creditRatings(Context context, CreditRating oldCreditRating) {

		List<CreditRating> creditRatings = getCreditRatingsList(context);
		Result result = context.makeResult();
		result.add("Select CreditRating");

		ResultList list = new ResultList(CREDIT_RATING);

		if (oldCreditRating != null) {
			list.add(createCreditRatingRecord(oldCreditRating));
		}
		ActionNames selection = context.getSelection(CREDIT_RATING);

		List<Record> actions = new ArrayList<Record>();

		List<CreditRating> pagination = pagination(context, selection, actions,
				creditRatings, new ArrayList<CreditRating>(),
				CREDITRATING_TO_SHOW);

		for (CreditRating term : pagination) {
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
	private Record createCreditRatingRecord(CreditRating oldCreditRating) {
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
		PriceLevel priceLevel = (PriceLevel) priceLevelReq.getValue();

		if (priceLevelObj != null) {
			priceLevel = (PriceLevel) priceLevelObj;
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
	private Result priceLevels(Context context, PriceLevel oldPriceLevel) {

		List<PriceLevel> priceLevels = new ArrayList<PriceLevel>(context
				.getCompany().getPriceLevels());
		Result result = context.makeResult();
		result.add("Select PriceLevel");

		ResultList list = new ResultList(PRICE_LEVEL);
		int num = 0;
		if (oldPriceLevel != null) {
			list.add(createCreditRatingRecord(oldPriceLevel));
			num++;
		}
		for (PriceLevel priceLevel : priceLevels) {
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
	private Record createCreditRatingRecord(PriceLevel oldPriceLevel) {
		Record record = new Record(oldPriceLevel);
		record.add("Name", oldPriceLevel.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result salesPersonRequirement(Context context, ResultList list,
			Object selection) {

		Object salesPersonObj = context.getSelection(SALESPERSON);
		if (salesPersonObj instanceof ActionNames) {
			salesPersonObj = null;
			selection = SALESPERSON;
		}
		Requirement salesPersonReq = get(SALESPERSON);
		SalesPerson salesPerson = (SalesPerson) salesPersonReq.getValue();
		if (salesPersonObj != null) {
			salesPerson = (SalesPerson) salesPersonObj;
			salesPersonReq.setValue(salesPerson);
		}
		if (selection != null)
			if (selection == SALESPERSON) {
				context.setAttribute(INPUT_ATTR, SALESPERSON);
				return salesPersons(context, salesPerson);
			}

		Record salesPersonRecord = new Record(SALESPERSON);
		salesPersonRecord.add("Name", SALESPERSON);
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
	protected Result salesPersons(Context context, SalesPerson oldsalesPerson) {
		List<SalesPerson> salesPersons = getsalePersonsList(context);
		Result result = context.makeResult();
		result.add("Select SalesPerson");

		ResultList list = new ResultList(SALESPERSON);
		if (oldsalesPerson != null) {
			list.add(createSalesPersonRecord(oldsalesPerson));
		}

		ActionNames selection = context.getSelection(SALESPERSON);

		List<Record> actions = new ArrayList<Record>();

		List<SalesPerson> pagination = pagination(context, selection, actions,
				salesPersons, new ArrayList<SalesPerson>(), SALESPERSON_TO_SHOW);

		for (SalesPerson term : pagination) {
			list.add(createSalesPersonRecord(term));
		}

		for (Record record : actions) {
			list.add(record);
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create SalesPerson");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldsalesPerson
	 * @return {@link Record}
	 */
	private Record createSalesPersonRecord(SalesPerson oldsalesPerson) {
		Record record = new Record(oldsalesPerson);
		record.add("Name", SALESPERSON);
		record.add("value", oldsalesPerson.getName());
		return record;
	}

	private List<SalesPerson> getsalePersonsList(Context context) {
		Set<SalesPerson> salesPersons = context.getCompany().getSalesPersons();
		return new ArrayList<SalesPerson>(salesPersons);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private List<CreditRating> getCreditRatingsList(Context context) {
		Set<CreditRating> creditRatings = context.getCompany()
				.getCreditRatings();
		return new ArrayList<CreditRating>(creditRatings);
	}

}
