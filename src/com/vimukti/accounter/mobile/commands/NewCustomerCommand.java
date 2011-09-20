package com.vimukti.accounter.mobile.commands;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

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
	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(CUSTOMER_NAME, false, true));
		if (getCompany().getPreferences().getUseCustomerId())
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
		list.add(new Requirement(PRICE_LEVEL, true, true));
		list.add(new Requirement(CREDIT_RATING, true, true));
		list.add(new Requirement(BANK_NAME, true, true));
		list.add(new Requirement(BANK_ACCOUNT_NUM, true, true));
		list.add(new Requirement(BANK_BRANCH, true, true));

		int accountingType = getCompany().getAccountingType();

		if (accountingType == ACCOUNTING_TYPE_UK
				|| accountingType == ACCOUNTING_TYPE_US) {
			list.add(new Requirement(PAYMENT_METHOD, true, true));
			list.add(new Requirement(PAYMENT_TERMS, true, true));
			list.add(new Requirement(CUSTOMER_GROUP, true, true));
		}
		if (accountingType == ACCOUNTING_TYPE_UK) {
			list.add(new Requirement(VATREGISTER_NUM, true, true));
			list.add(new Requirement(CUSTOMER_VATCODE, true, true));
		}
		if (accountingType == ACCOUNTING_TYPE_INDIA) {
			list.add(new Requirement(PAN_NUM, true, true));
			list.add(new Requirement(CST_NUM, true, true));
			list.add(new Requirement(SERVICE_TAX_NUM, true, true));
			list.add(new Requirement(TIN_NUM, true, true));
		}
	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(CONTACT_PROCESS)) {
				result = contactProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		result = customerNameRequirement(context);
		if (result == null) {
			// TODO
		}
		if (getCompany().getPreferences().getUseCustomerId()) {
			result = customerNumberRequirement(context);
			if (result == null) {
				// TODO
			}
		}
		result = optionalRequirements(context);
		if (result == null) {
			// TODO
		}
		return createCustomerObject(context);
	}

	/*
	 * * customer Number.
	 * 
	 * @param context
	 * 
	 * @return {@link Result}
	 */
	private Result customerNumberRequirement(Context context) {
		Requirement customerNumReq = get(NUMBER);
		if (!customerNumReq.isDone()) {
			String customerNum = context.getString();
			if (customerNum != null) {
				customerNumReq.setValue(customerNum);
			} else {
				return number(context, "Please Enter the Customer Number.",
						null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(NUMBER)) {
			input = context.getString();
			customerNumReq.setValue(input);
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createCustomerObject(Context context) {
		Customer customer = new Customer();
		String name = get(CUSTOMER_NAME).getValue();
		String number = get(NUMBER).getValue();
		Set<Contact> contacts = get(CUSTOMER_CONTACT).getValue();
		boolean isActive = (Boolean) get(IS_ACTIVE).getDefaultValue();
		FinanceDate balancedate = get(BALANCE_ASOF_DATE).getValue();
		Timestamp customerSincedate = get(CUSTOMER_SINCEDATE).getValue();
		double balance = get(BALANCE).getValue();
		Set<Address> adress = get(ADDRESS).getValue();
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
		CustomerGroup customerGroup = get(PAYMENT_TERMS).getValue();
		String vatRegistredNum = get(VATREGISTER_NUM).getValue();
		TAXCode taxCode = get(CUSTOMER_VATCODE).getValue();
		String panNum = get(PAN_NUM).getValue();
		String cstNum = get(CST_NUM).getValue();
		String serviceTaxNum = (String) get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();

		customer.setName(name);
		customer.setNumber(number);
		customer.setContacts(contacts);
		customer.setBalance(balance);
		customer.setBalanceAsOf(balancedate);
		customer.setCreatedDate(customerSincedate);
		customer.setAddress(adress);
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
		customer.setCustomerGroup(customerGroup);
		customer.setActive(isActive);
		customer.setPaymentMethod(paymentMethod);
		customer.setPaymentTerm(paymentTerms);
		customer.setTAXCode(taxCode);
		customer.setVATRegistrationNumber(vatRegistredNum);
		customer.setPANno(panNum);
		customer.setCSTno(cstNum);
		customer.setServiceTaxRegistrationNo(serviceTaxNum);
		customer.setTINNumber(tinNum);

		Session session = context.getSession();
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
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CONTACTS:
				return contact(context, "Enter the Contact Details", null);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		ResultList list = new ResultList("values");
		selection = context.getSelection("values");

		String customerName = (String) get(CUSTOMER_NAME).getValue();
		Record nameRecord = new Record(customerName);
		nameRecord.add("Name", "customerName");
		nameRecord.add("Value", customerName);
		list.add(nameRecord);

		Requirement contactReq = get(CUSTOMER_CONTACT);
		List<Contact> contacts = contactReq.getValue();
		selection = context.getSelection(CUSTOMER_CONTACT);
		if (selection != null) {
			Result contact = contact(context, "customer contact",
					(Contact) selection);
			if (contact != null) {
				return contact;
			}
		}

		// boolean isActive = (Boolean) get("isactive").getDefaultValue();
		// Record isActiveRecord = new Record(isActive);
		// isActiveRecord.add("Name", "Is Active");
		// isActiveRecord.add("Value", isActive);
		// list.add(isActiveRecord);

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getDefaultValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setDefaultValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This Item is Active";
		} else {
			activeString = "This Item is InActive";
		}
		Record isActiveRecord = new Record(IS_ACTIVE);
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);

		int company = getCompany().getAccountingType();

		Result result = customerSinceDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = balanceRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = balanceAsOfDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = faxNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = emailRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = phoneNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = webAdressRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = salesPersonRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = priceLevelRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = creditRatingRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankNameRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankAccountNumRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankBranchRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (company == ACCOUNTING_TYPE_UK || company == ACCOUNTING_TYPE_US) {
			result = paymentMethodRequirement(context, (String) selection);
			if (result != null) {
				return result;
			}
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = customerGroupRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (company == ACCOUNTING_TYPE_US) {
			result = vatRegisterationNumRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}
		result = customerVatCodeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (company == ACCOUNTING_TYPE_INDIA) {
			result = panNumRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			result = cstNumRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			result = serviceTaxRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			result = tinNumRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}
		result = context.makeResult();
		result.add("Customer is ready to create with following values.");
		result.add(list);
		result.add("Items:-");
		ResultList items = new ResultList(CUSTOMER_CONTACT);
		for (Contact item : contacts) {
			Record itemRec = new Record(item);
			itemRec.add(PRIMARY, item.getVersion());
			itemRec.add(CONTACT_NAME, item.getName());
			itemRec.add(TITLE, item.getTitle());
			itemRec.add(BUSINESS_PHONE, item.getBusinessPhone());
			itemRec.add(EMAIL, item.getEmail());
		}

		result.add(items);
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
	 * tin Num
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result tinNumRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(TIN_NUM);
		String tinNumber = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(TIN_NUM)) {
			String order = context.getSelection(TIN_NUM);
			if (order == null) {
				order = context.getString();
			}
			tinNumber = order;
			req.setDefaultValue(tinNumber);
		}

		if (selection == tinNumber) {
			context.setAttribute(INPUT_ATTR, TIN_NUM);
			return text(context, "Enter Taxpayer identification number",
					tinNumber);
		}

		Record tinNumRecord = new Record(tinNumber);
		tinNumRecord.add("Name", TIN_NUM);
		tinNumRecord.add("Value", tinNumber);
		list.add(tinNumRecord);

		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * Service Tax Num
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result serviceTaxRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(SERVICE_TAX_NUM);
		String serviceTaxNumber = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(SERVICE_TAX_NUM)) {
			String order = context.getSelection(SERVICE_TAX_NUM);
			if (order == null) {
				order = context.getString();
			}
			serviceTaxNumber = order;
			req.setDefaultValue(serviceTaxNumber);
		}

		if (selection == serviceTaxNumber) {
			context.setAttribute(INPUT_ATTR, SERVICE_TAX_NUM);
			return text(context, "Enter Service tax registration Number ",
					serviceTaxNumber);
		}

		Record serviceTaxRecord = new Record(serviceTaxNumber);
		serviceTaxRecord.add("Name", SERVICE_TAX_NUM);
		serviceTaxRecord.add("Value", serviceTaxNumber);
		list.add(serviceTaxRecord);

		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * CST Num
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result cstNumRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(CST_NUM);
		String cstNum = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(CST_NUM)) {
			String order = context.getSelection(CST_NUM);
			if (order == null) {
				order = context.getString();
			}
			cstNum = order;
			req.setDefaultValue(cstNum);
		}

		if (selection == cstNum) {
			context.setAttribute(INPUT_ATTR, CST_NUM);
			return text(context, "Enter CST Number ", cstNum);
		}

		Record cstNumRecord = new Record(cstNum);
		cstNumRecord.add("Name", BANK_ACCOUNT_NUM);
		cstNumRecord.add("Value", cstNum);
		list.add(cstNumRecord);

		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * Pan NUmber
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result panNumRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(PAN_NUM);
		String panNumber = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PAN_NUM)) {
			String order = context.getSelection(PAN_NUM);
			if (order == null) {
				order = context.getString();
			}
			panNumber = order;
			req.setDefaultValue(panNumber);
		}

		if (selection == panNumber) {
			context.setAttribute(INPUT_ATTR, PAN_NUM);
			return text(context, "Enter Personal Ledger number", panNumber);
		}

		Record panNumRecord = new Record(panNumber);
		panNumRecord.add("Name", PAN_NUM);
		panNumRecord.add("Value", panNumber);
		list.add(panNumRecord);

		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result vatRegisterationNumRequirement(Context context,
			ResultList list, Object selection) {

		Requirement req = get(VATREGISTER_NUM);
		String vatRegisterationNum = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(VATREGISTER_NUM)) {
			String order = context.getSelection(VATREGISTER_NUM);
			if (order == null) {
				order = context.getString();
			}
			vatRegisterationNum = order;
			req.setDefaultValue(vatRegisterationNum);
		}

		if (selection == vatRegisterationNum) {
			context.setAttribute(INPUT_ATTR, VATREGISTER_NUM);
			return text(context, "Enter vatRegisteration Number ",
					vatRegisterationNum);
		}

		Record vatRegisterationNumRecord = new Record(vatRegisterationNum);
		vatRegisterationNumRecord.add("Name", "vatRegisterationNum");
		vatRegisterationNumRecord.add("Value", vatRegisterationNum);
		list.add(vatRegisterationNumRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankBranchRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(BANK_BRANCH);
		String bankBranch = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(bankBranch)) {
			String order = context.getSelection(BANK_BRANCH);
			if (order == null) {
				order = context.getString();
			}
			bankBranch = order;
			req.setDefaultValue(bankBranch);
		}

		if (selection == bankBranch) {
			context.setAttribute(INPUT_ATTR, BANK_BRANCH);
			return text(context, "Enter bankBranch Name ", bankBranch);
		}

		Record bankBranchRecord = new Record(bankBranch);
		bankBranchRecord.add("Name", "bankBranch");
		bankBranchRecord.add("Value", bankBranch);
		list.add(bankBranchRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankAccountNumRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(BANK_ACCOUNT_NUM);
		String bankAccountNumber = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(BANK_ACCOUNT_NUM)) {
			String order = context.getSelection(BANK_ACCOUNT_NUM);
			if (order == null) {
				order = context.getString();
			}
			bankAccountNumber = order;
			req.setDefaultValue(bankAccountNumber);
		}

		if (selection == bankAccountNumber) {
			context.setAttribute(INPUT_ATTR, BANK_ACCOUNT_NUM);
			return text(context, "Enter bankAccount Number ", bankAccountNumber);
		}

		Record bankAccountNumRecord = new Record(bankAccountNumber);
		bankAccountNumRecord.add("Name", BANK_ACCOUNT_NUM);
		bankAccountNumRecord.add("Value", bankAccountNumber);
		list.add(bankAccountNumRecord);

		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankNameRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(BANK_NAME);
		String bankName = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(BANK_NAME)) {
			String order = context.getSelection(BANK_NAME);
			if (order == null) {
				order = context.getString();
			}
			bankName = order;
			req.setDefaultValue(bankName);
		}

		if (selection == bankName) {
			context.setAttribute(INPUT_ATTR, BANK_NAME);
			return text(context, "Enter webPageAdress ", bankName);
		}

		Record bankNameRecord = new Record(bankName);
		bankNameRecord.add("Name", BANK_NAME);
		bankNameRecord.add("Value", bankName);
		list.add(bankNameRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result webAdressRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(WEBADRESS);
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(WEBADRESS)) {
			String order = context.getSelection(WEBADRESS);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, WEBADRESS);
			return text(context, "Enter webPageAdress ", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "webPageAdress");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result emailRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(EMAIL);
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(EMAIL)) {
			String order = context.getSelection(EMAIL);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, EMAIL);
			return text(context, "Enter email ", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "email");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result faxNumRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(FAX);
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FAX)) {
			String order = context.getSelection(FAX);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, FAX);
			return text(context, "Enter Fax Number", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", "fax");
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result phoneNumRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(PHONE);
		String phone = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PHONE)) {
			String order = context.getSelection(PHONE);
			if (order == null) {
				order = context.getString();
			}
			phone = order;
			req.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, PHONE);
			return text(context, "Enter Phone Number", phone);
		}

		Record balanceRecord = new Record(phone);
		balanceRecord.add("Name", PHONE);
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result balanceRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(BALANCE);
		Double balance = (Double) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(BALANCE)) {
			Double order = context.getSelection(BALANCE);
			if (order == null) {
				order = context.getDouble();
			}
			balance = order;
			req.setDefaultValue(balance);
		}

		if (selection == balance) {
			context.setAttribute(INPUT_ATTR, BALANCE);
			return amount(context, "Enter Balance", balance);
		}

		Record balanceRecord = new Record(balance);
		balanceRecord.add("Name", BALANCE);
		balanceRecord.add("Value", balance);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
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

		Object customerVatCodeObj = context.getSelection(CUSTOMER_VATCODE);
		Requirement customerVatCodeReq = get(CUSTOMER_VATCODE);
		TAXCode vatCode = (TAXCode) customerVatCodeReq.getValue();

		if (selection == vatCode) {
			return taxCode(context, vatCode);
		}

		if (customerVatCodeObj != null) {
			vatCode = (TAXCode) customerVatCodeObj;
			customerVatCodeReq.setDefaultValue(vatCode);
		}

		Record customerVatCodeRecord = new Record(vatCode);
		customerVatCodeRecord.add("Name", CUSTOMER_VATCODE);
		customerVatCodeRecord.add("Value", vatCode.getName());
		list.add(customerVatCodeRecord);

		Result result = new Result();
		result.add(list);
		return result;
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
		Requirement customerGroupReq = get(CUSTOMER_GROUP);
		CustomerGroup customerGroup = (CustomerGroup) customerGroupReq
				.getValue();

		if (selection == customerGroup) {
			return customerGroups(context, customerGroup);
		}

		if (customerGroupObj != null) {
			customerGroup = (CustomerGroup) customerGroupObj;
			customerGroupReq.setDefaultValue(customerGroup);
		}

		Record customerGroupRecord = new Record(customerGroup);
		customerGroupRecord.add("Name", CUSTOMER_GROUP);
		customerGroupRecord.add("Value", customerGroup.getName());
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result customerGroups(Context context,
			CustomerGroup oldCustomerGroup) {
		List<CustomerGroup> customerGroups = getCustomerGroupsList();
		Result result = context.makeResult();
		result.add("Select CustomerGroup");

		ResultList list = new ResultList(CUSTOMER_GROUP);
		int num = 0;
		if (oldCustomerGroup != null) {
			list.add(createCustomerGroupRecord(oldCustomerGroup));
			num++;
		}
		for (CustomerGroup customerGroup : customerGroups) {
			if (customerGroup != oldCustomerGroup) {
				list.add(createCustomerGroupRecord(customerGroup));
				num++;
			}
			if (num == CUSTOMERGROUP_TO_SHOW) {
				break;
			}
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
		Requirement creditRatingReq = get(CREDIT_RATING);
		CreditRating creditRating = (CreditRating) creditRatingReq.getValue();

		if (selection == creditRating) {
			return creditRatings(context, creditRating);
		}

		if (crediRatingObj != null) {
			creditRating = (CreditRating) crediRatingObj;
			creditRatingReq.setDefaultValue(creditRating);
		}

		Record priceLevelRecord = new Record(creditRating);
		priceLevelRecord.add("Name", CREDIT_RATING);
		priceLevelRecord.add("Value", creditRating.getName());
		list.add(priceLevelRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result creditRatings(Context context, CreditRating oldCreditRating) {

		List<CreditRating> creditRatings = getCreditRatingsList();
		Result result = context.makeResult();
		result.add("Select CreditRating");

		ResultList list = new ResultList(CREDIT_RATING);
		int num = 0;
		if (oldCreditRating != null) {
			list.add(createCreditRatingRecord(oldCreditRating));
			num++;
		}
		for (CreditRating priceLevel : creditRatings) {
			if (priceLevel != oldCreditRating) {
				list.add(createCreditRatingRecord(priceLevel));
				num++;
			}
			if (num == CREDITRATING_TO_SHOW) {
				break;
			}
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

		if (selection == priceLevel) {
			return priceLevels(context, priceLevel);
		}

		if (priceLevelObj != null) {
			priceLevel = (PriceLevel) priceLevelObj;
			priceLevelReq.setDefaultValue(priceLevel);
		}

		Record priceLevelRecord = new Record(priceLevel);
		priceLevelRecord.add("Name", PRICE_LEVEL);
		priceLevelRecord.add("Value", priceLevel.getName());
		list.add(priceLevelRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result priceLevels(Context context, PriceLevel oldPriceLevel) {

		List<PriceLevel> priceLevels = getPriceLevelsList();
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
		Requirement salesPersonReq = get(SALESPERSON);
		SalesPerson salesPerson = (SalesPerson) salesPersonReq.getValue();

		if (selection == salesPerson) {
			return salesPersons(context, salesPerson);
		}
		if (salesPersonObj != null) {
			salesPerson = (SalesPerson) salesPersonObj;
			salesPersonReq.setDefaultValue(salesPerson);
		}

		Record salesPersonRecord = new Record(salesPerson);
		salesPersonRecord.add("Name", SALESPERSON);
		salesPersonRecord.add("Value", salesPerson.getName());
		list.add(salesPersonRecord);

		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * balanceAsOfDate
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result balanceAsOfDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(BALANCE_ASOF_DATE);
		Date balanceAsofdate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(BALANCE_ASOF_DATE)) {
			Date date = context.getSelection(BALANCE_ASOF_DATE);
			if (date == null) {
				date = context.getDate();
			}
			balanceAsofdate = date;
			dateReq.setDefaultValue(balanceAsofdate);
		}
		if (selection == balanceAsofdate) {
			context.setAttribute(INPUT_ATTR, BALANCE_ASOF_DATE);
			return date(context, "Enter BalanceAsOf  Date", balanceAsofdate);
		}

		Record transDateRecord = new Record(balanceAsofdate);
		transDateRecord.add("Name", BALANCE_ASOF_DATE);
		transDateRecord.add("Value", balanceAsofdate.toString());
		list.add(transDateRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * customerSinceDate
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result customerSinceDateRequirement(Context context,
			ResultList list, Object selection) {
		Requirement dateReq = get(CUSTOMER_SINCEDATE);
		Date customerSincedate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(CUSTOMER_GROUP)) {
			Date date = context.getSelection(CUSTOMER_SINCEDATE);
			if (date == null) {
				date = context.getDate();
			}
			customerSincedate = date;
			dateReq.setDefaultValue(customerSincedate);
		}
		if (selection == customerSincedate) {
			context.setAttribute("input", CUSTOMER_SINCEDATE);
			return date(context, "Enter Customer Since Date", customerSincedate);
		}

		Record transDateRecord = new Record(customerSincedate);
		transDateRecord.add("Name", CUSTOMER_SINCEDATE);
		transDateRecord.add("Value", customerSincedate.toString());
		list.add(transDateRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result customerNameRequirement(Context context) {
		Requirement requirement = get(CUSTOMER_NAME);
		if (!requirement.isDone()) {
			String customerName = context.getSelection(TEXT);
			if (customerName != null) {
				requirement.setValue(customerName);
			} else {
				return text(context, "Please enter the  Customer Name", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(CUSTOMER_NAME)) {
			requirement.setValue(input);
		}
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
		List<SalesPerson> salesPersons = getsalePersonsList();
		Result result = context.makeResult();
		result.add("Select SalesPerson");

		ResultList list = new ResultList(SALESPERSON);
		int num = 0;
		if (oldsalesPerson != null) {
			list.add(createSalesPersonRecord(oldsalesPerson));
			num++;
		}
		for (SalesPerson salesPerson : salesPersons) {
			if (salesPerson != oldsalesPerson) {
				list.add(createSalesPersonRecord(salesPerson));
				num++;
			}
			if (num == SALESPERSON_TO_SHOW) {
				break;
			}
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
		record.add("Name", oldsalesPerson.getName());
		return record;
	}

	private List<SalesPerson> getsalePersonsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<CustomerGroup> getCustomerGroupsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<CreditRating> getCreditRatingsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<PriceLevel> getPriceLevelsList() {
		// TODO Auto-generated method stub
		return null;
	}
}
