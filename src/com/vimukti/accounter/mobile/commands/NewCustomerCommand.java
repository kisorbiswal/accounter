package com.vimukti.accounter.mobile.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
		// if (getCompany().getPreferences().getUseCustomerId())
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

		// int accountingType = getCompany().getAccountingType();
		// if (accountingType != ACCOUNTING_TYPE_INDIA) {
		list.add(new Requirement(PAYMENT_METHOD, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(CUSTOMER_GROUP, true, true));
		// }
		// if (accountingType == ACCOUNTING_TYPE_UK) {
		// if (getCompany().getPreferences().isRegisteredForVAT()) {
		list.add(new Requirement(VATREGISTER_NUM, true, true));
		list.add(new Requirement(CUSTOMER_VATCODE, true, true));
		// }
		// }
		// if (accountingType == ACCOUNTING_TYPE_INDIA) {
		list.add(new Requirement(PAN_NUM, true, true));
		list.add(new Requirement(CST_NUM, true, true));
		list.add(new Requirement(SERVICE_TAX_NUM, true, true));
		list.add(new Requirement(TIN_NUM, true, true));
		// }
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
		result = nameRequirement(context, CUSTOMER_NAME,
				"Please enter the  Customer Name");
		if (result != null) {
			return result;
		}
		// if (context.getCompany().getPreferences().getUseCustomerId()) {
		result = numberRequirement(context, NUMBER,
				"Please Enter the Customer Number.");
		if (result != null) {
			return result;
		}
		// }

		setDefaultValues();
		result = optionalRequirements(context);
		if (result != null) {
			return result;
		}
		createCustomerObject(context);
		markDone();
		return result;
	}

	private void setDefaultValues() {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(CUSTOMER_SINCEDATE).setDefaultValue(new Date());
		get(BALANCE_ASOF_DATE).setDefaultValue(new Date());
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private void createCustomerObject(Context context) {

		Customer customer = new Customer();
		String name = get(CUSTOMER_NAME).getValue();
		String number = get(NUMBER).getValue();
		Set<Contact> contacts = get(CUSTOMER_CONTACT).getValue();
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
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
		String serviceTaxNum = get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();

		customer.setName(name);
		if (context.getCompany().getPreferences().getUseCustomerId())
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
		customer.setActive(isActive);
		int accountingType = context.getCompany().getAccountingType();
		if (accountingType != ACCOUNTING_TYPE_INDIA) {
			customer.setPaymentMethod(paymentMethod);
			customer.setPaymentTerm(paymentTerms);
			customer.setCustomerGroup(customerGroup);
		}
		if (accountingType == ACCOUNTING_TYPE_UK) {
			if (context.getCompany().getPreferences().isRegisteredForVAT()) {
				customer.setTAXCode(taxCode);
				customer.setVATRegistrationNumber(vatRegistredNum);
			}
		}
		if (accountingType == ACCOUNTING_TYPE_INDIA) {
			customer.setPANno(panNum);
			customer.setCSTno(cstNum);
			customer.setServiceTaxRegistrationNo(serviceTaxNum);
			customer.setTINNumber(tinNum);
		}

		create(customer, context);

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result optionalRequirements(Context context) {
		// context.setAttribute(INPUT_ATTR, "optional");
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
					CUSTOMER_CONTACT, (Contact) selection);
			if (contact != null) {
				return contact;
			}
		}
		selection = context.getSelection("values");

		// Requirement isActiveReq = get(IS_ACTIVE);
		// Boolean isActive = (Boolean) isActiveReq.getValue();
		// if (selection == isActive) {
		// context.setAttribute(INPUT_ATTR, IS_ACTIVE);
		// isActive = !isActive;
		// isActiveReq.setValue(isActive);
		// }
		// String activeString = "";
		// if (isActive) {
		// activeString = "This Item is Active";
		// } else {
		// activeString = "This Item is InActive";
		// }
		// Record isActiveRecord = new Record(IS_ACTIVE);
		// isActiveRecord.add("Name", "");
		// isActiveRecord.add("Value", activeString);
		// list.add(isActiveRecord);

		int company = context.getCompany().getAccountingType();

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
		// result = billToRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }
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
		// result = priceLevelRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

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
			result = paymentMethodRequirement(context, list, (String) selection);
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
			req.setValue(tinNumber);
		}
		if (selection != null)
			if (selection == TIN_NUM) {
				context.setAttribute(INPUT_ATTR, TIN_NUM);
				return text(context, "Enter Taxpayer identification number",
						tinNumber);
			}

		Record tinNumRecord = new Record(TIN_NUM);
		tinNumRecord.add("Name", TIN_NUM);
		tinNumRecord.add("Value", tinNumber);
		list.add(tinNumRecord);

		Result result = new Result();
		result.add(list);
		return null;

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
			req.setValue(serviceTaxNumber);
		}
		if (selection != null)
			if (selection == SERVICE_TAX_NUM) {
				context.setAttribute(INPUT_ATTR, SERVICE_TAX_NUM);
				return text(context, "Enter Service tax registration Number ",
						serviceTaxNumber);
			}

		Record serviceTaxRecord = new Record(SERVICE_TAX_NUM);
		serviceTaxRecord.add("Name", SERVICE_TAX_NUM);
		serviceTaxRecord.add("Value", serviceTaxNumber);
		list.add(serviceTaxRecord);

		Result result = new Result();
		result.add(list);
		return null;

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
			req.setValue(cstNum);
		}
		if (selection != null)
			if (selection == CST_NUM) {
				context.setAttribute(INPUT_ATTR, CST_NUM);
				return text(context, "Enter CST Number ", cstNum);
			}

		Record cstNumRecord = new Record(CST_NUM);
		cstNumRecord.add("Name", CST_NUM);
		cstNumRecord.add("Value", cstNum);
		list.add(cstNumRecord);

		Result result = new Result();
		result.add(list);
		return null;

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
			req.setValue(panNumber);
		}
		if (selection != null)
			if (selection == PAN_NUM) {
				context.setAttribute(INPUT_ATTR, PAN_NUM);
				return text(context, "Enter Personal Ledger number", panNumber);
			}

		Record panNumRecord = new Record(PAN_NUM);
		panNumRecord.add("Name", PAN_NUM);
		panNumRecord.add("Value", panNumber);
		list.add(panNumRecord);

		Result result = new Result();
		result.add(list);
		return null;

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
			req.setValue(vatRegisterationNum);
		}
		if (selection != null)
			if (selection == "vatRegisterationNum") {
				context.setAttribute(INPUT_ATTR, VATREGISTER_NUM);
				return text(context, "Enter vatRegisteration Number ",
						vatRegisterationNum);
			}

		Record vatRegisterationNumRecord = new Record("vatRegisterationNum");
		vatRegisterationNumRecord.add("Name", "vatRegisterationNum");
		vatRegisterationNumRecord.add("Value", vatRegisterationNum);
		list.add(vatRegisterationNumRecord);
		Result result = new Result();
		result.add(list);
		return null;

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
		if (attribute.equals(BANK_BRANCH)) {
			String order = context.getSelection(BANK_BRANCH);
			if (order == null) {
				order = context.getString();
			}
			bankBranch = order;
			req.setValue(bankBranch);
		}
		if (selection != null)
			if (selection == "bankBranch") {
				context.setAttribute(INPUT_ATTR, BANK_BRANCH);
				return text(context, "Enter bankBranch Name ", bankBranch);
			}

		Record bankBranchRecord = new Record("bankBranch");
		bankBranchRecord.add("Name", "bankBranch");
		bankBranchRecord.add("Value", bankBranch);
		list.add(bankBranchRecord);
		Result result = new Result();
		result.add(list);
		return null;

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
			req.setValue(bankAccountNumber);
		}
		if (selection != null)
			if (selection == BANK_ACCOUNT_NUM) {
				context.setAttribute(INPUT_ATTR, BANK_ACCOUNT_NUM);
				return text(context, "Enter bankAccount Number ",
						bankAccountNumber);
			}

		Record bankAccountNumRecord = new Record(BANK_ACCOUNT_NUM);
		bankAccountNumRecord.add("Name", BANK_ACCOUNT_NUM);
		bankAccountNumRecord.add("Value", bankAccountNumber);
		list.add(bankAccountNumRecord);

		Result result = new Result();
		result.add(list);
		return null;

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
			req.setValue(bankName);
		}
		if (selection != null) {
			if (selection == BANK_NAME) {
				context.setAttribute(INPUT_ATTR, BANK_NAME);
				return text(context, "Enter Bank Name ", bankName);
			}
		}
		Record bankNameRecord = new Record(BANK_NAME);
		bankNameRecord.add("Name", BANK_NAME);
		bankNameRecord.add("Value", bankName);
		list.add(bankNameRecord);
		Result result = new Result();
		result.add(list);
		return null;

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
		String webAdress = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(WEBADRESS)) {
			String order = context.getSelection(WEBADRESS);
			if (order == null) {
				order = context.getString();
			}
			webAdress = order;
			req.setValue(webAdress);
		}
		if (selection != null) {
			if (selection == "webPageAdress") {
				context.setAttribute(INPUT_ATTR, WEBADRESS);
				return text(context, "Enter webPageAdress ", webAdress);
			}
		}
		Record balanceRecord = new Record("webPageAdress");
		balanceRecord.add("Name", "webPageAdress");
		balanceRecord.add("Value", webAdress);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return null;

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
		String emailText = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(EMAIL)) {
			String order = context.getSelection(EMAIL);
			if (order == null) {
				order = context.getString();
			}
			emailText = order;
			req.setValue(emailText);
		}
		if (selection != null) {
			if (selection == "email") {
				context.setAttribute(INPUT_ATTR, EMAIL);
				return text(context, "Enter email ", emailText);
			}
		}
		Record balanceRecord = new Record("email");
		balanceRecord.add("Name", "email");
		balanceRecord.add("Value", emailText);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return null;
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
		String faxNum = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FAX)) {
			String order = context.getSelection(FAX);
			if (order == null) {
				order = context.getString();
			}
			faxNum = order;
			req.setValue(faxNum);
		}
		if (selection != null) {
			if (selection == "fax") {
				context.setAttribute(INPUT_ATTR, FAX);
				return text(context, "Enter Fax Number", faxNum);
			}
		}
		Record balanceRecord = new Record("fax");
		balanceRecord.add("Name", "fax");
		balanceRecord.add("Value", faxNum);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return null;
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
			req.setValue(phone);
		}
		if (selection != null) {
			if (selection == PHONE) {
				context.setAttribute(INPUT_ATTR, PHONE);
				return text(context, "Enter Phone Number", phone);
			}
		}
		Record balanceRecord = new Record(PHONE);
		balanceRecord.add("Name", PHONE);
		balanceRecord.add("Value", phone);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return null;
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

		CreditRating crediRatingObj = context.getSelection(CREDIT_RATING);
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
	 * @return {@link Result}
	 */
	private Result salesPersonRequirement(Context context, ResultList list,
			Object selection) {

		Object salesPersonObj = context.getSelection(SALESPERSON);
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
				salesPerson == null ? "" : salesPerson.getName());
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
