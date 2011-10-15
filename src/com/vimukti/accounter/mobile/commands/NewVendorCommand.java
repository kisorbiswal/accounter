package com.vimukti.accounter.mobile.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class NewVendorCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";

	private static final int VENDORGROUP_TO_SHOW = 5;

	protected static final String ACCOUNT = "Account";
	protected static final String CREDIT_LIMIT = "Credit Limit";

	private static final String PREFERRED_SHIPPING_METHOD = "Preferred Shipping Method";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String ACCOUNT_NO = "Account No";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_BRANCH = "Bank Branch";
	private static final String VENDOR_GROUP = "Vendor Group";
	private static final String VAT_REGISTRATION_NUMBER = "Vat Registration Number";

	private static final String VENDOR_VAT_CODE = "Vendor Vat Code";
	private static final String VENDOR_NAME = "Vendor Name";
	private static final String VENDOR_NUMBER = "Vendor Number";
	private static final String VENDOR_SINCE = "Vendor Since";
	private static final String ACTIVE = "Active";
	private static final String TRACK_PAYMENTS_FOR_1099 = "Track payments for 1099";
	private static final String BALANCE = "Balance";
	private static final String BALANCE_AS_OF = "Balance As Of";
	private static final String ADDRESS = "address";
	private static final String PHONE = "Phone";
	private static final String FAX = "Fax";
	private static final String EMAIL = "E-mail";
	private static final String WEB_PAGE_ADDRESS = "Web Page Address";
	private static final String CONTACTS = "Contacts";
	private static final String CONTACT_NAME = "Contact Name";
	private static final String TITLE = "Title";
	private static final String BUSINESS_PHONE = "Business Phone";

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	private int accountingType;

	protected static final String PRIMARY = "Primary";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(VENDOR_NAME, false, true));

		list.add(new Requirement(VENDOR_NUMBER, false, true));

		list.add(new Requirement(ACTIVE, true, true));
		list.add(new Requirement(VENDOR_SINCE, true, true));
		list.add(new Requirement(BALANCE, true, true));
		list.add(new Requirement(BALANCE_AS_OF, true, true));
		list.add(new Requirement(ADDRESS, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEB_PAGE_ADDRESS, true, true));

		list.add(new Requirement(TRACK_PAYMENTS_FOR_1099, true, true));

		list.add(new ObjectListRequirement(CONTACTS, true, true) {
			@Override
			public void addRequirements(List<Requirement> list) {

				list.add(new Requirement(PRIMARY, true, true));
				list.add(new Requirement(CONTACT_NAME, false, true));
				list.add(new Requirement(TITLE, true, true));
				list.add(new Requirement(BUSINESS_PHONE, true, true));
				list.add(new Requirement(EMAIL, true, true));

			}
		});

		list.add(new Requirement(ACCOUNT, true, true));
		list.add(new Requirement(CREDIT_LIMIT, true, true));
		list.add(new Requirement(PREFERRED_SHIPPING_METHOD, true, true));
		list.add(new Requirement(PAYMENT_METHOD, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(ACCOUNT_NO, true, true));
		list.add(new Requirement(BANK_NAME, true, true));
		list.add(new Requirement(BANK_BRANCH, true, true));
		list.add(new Requirement(VENDOR_GROUP, true, true));
		list.add(new Requirement(VAT_REGISTRATION_NUMBER, true, true));
		list.add(new Requirement(VENDOR_VAT_CODE, true, true));

	}

	@Override
	public Result run(Context context) {
		accountingType = context.getCompany().getAccountingType();
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
			if (process.equals(CONTACT_PROCESS)) {
				result = contactProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		result = nameRequirement(context, null, VENDOR_NAME,
				"Please enter the  Vendor Name");
		if (result != null) {
			return result;
		}
		// if (context.getCompany().getPreferences().getUseVendorId()) {
		result = numberRequirement(context, null, VENDOR_NUMBER,
				"Please enter the  Vendor Number");
		if (result != null) {
			return result;
		}
		// }

		setDefaultValues();
		result = optionalRequirements(context);
		if (result != null) {
			return result;
		}
		createVendorObject(context);
		markDone();
		return result;
	}

	private void setDefaultValues() {
		get(ACTIVE).setDefaultValue(true);
		get(VENDOR_SINCE).setDefaultValue(new Date());
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(BALANCE_AS_OF).setDefaultValue(new Date());
		get(ADDRESS).setDefaultValue(new Address());

	}

	private Result createVendorObject(Context context) {

		ICountryPreferences countryPreferences = context.getCompany()
				.getCountryPreferences();
		CompanyPreferences preferences = context.getCompany().getPreferences();

		Vendor vendor = new Vendor();
		vendor.setCompany(context.getCompany());
		String name = get(VENDOR_NAME).getValue();
		String number = get(VENDOR_NUMBER).getValue().toString();

		Set<Contact> contacts = get(CONTACTS).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		Date balancedate = get(BALANCE_AS_OF).getValue();
		Date customerSincedate = get(VENDOR_SINCE).getValue();
		double balance = get(BALANCE).getValue();
		ArrayList<Address> adress = get(ADDRESS).getValue();
		Account account = get(ACCOUNT).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEB_PAGE_ADDRESS).getValue();
		double creditLimit = get(CREDIT_LIMIT).getValue();
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(ACCOUNT_NO).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		ShippingMethod shippingMethod = get(PREFERRED_SHIPPING_METHOD)
				.getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		PaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();
		VendorGroup vendorGroup = get(VENDOR_GROUP).getValue();
		String vatRegistredNum = get(VAT_REGISTRATION_NUMBER).getValue();
		TAXCode taxCode = get(VENDOR_VAT_CODE).getValue();

		vendor.setName(name);

		if (context.getCompany().getPreferences().getUseVendorId())
			vendor.setVendorNumber(number);
		vendor.setContacts(contacts);
		vendor.setBalance(balance);
		vendor.setBalanceAsOf(new FinanceDate(balancedate));
		vendor.setCreatedDate(new Timestamp(customerSincedate.getTime()));
		vendor.setAddress(new HashSet<Address>(adress));
		vendor.setPhoneNo(phoneNum);
		vendor.setFaxNo(faxNum);
		vendor.setWebPageAddress(webaddress);
		vendor.setBankAccountNo(bankAccountNum);
		vendor.setBankBranch(bankBranch);
		vendor.setBankName(bankName);
		vendor.setEmail(emailId);

		if (accountingType == ACCOUNTING_TYPE_US) {
			boolean isTrackPaymentsFor1099 = get(TRACK_PAYMENTS_FOR_1099)
					.getValue();
			vendor.setTrackPaymentsFor1099(isTrackPaymentsFor1099);
		}
		vendor.setExpenseAccount(account);
		vendor.setCreditLimit(creditLimit);
		vendor.setShippingMethod(shippingMethod);
		vendor.setPaymentMethod(paymentMethod);
		vendor.setPaymentTerms(paymentTerms);
		vendor.setVendorGroup(vendorGroup);
		vendor.setActive(isActive);
		vendor.setTAXCode(taxCode);
		vendor.setVATRegistrationNumber(vatRegistredNum);

		Session session = context.getHibernateSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(account);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add(" Vendor was created successfully.");

		return result;

	}

	private Result optionalRequirements(Context context) {
		// context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CONTACTS:
				return contact(context, "Enter the Contact Details", CONTACTS,
						null);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		ResultList list = new ResultList("values");
		selection = context.getSelection("values");

		String customerName = (String) get(VENDOR_NAME).getValue();
		Record nameRecord = new Record(customerName);
		nameRecord.add("Name", "customerName");
		nameRecord.add("Value", customerName);
		list.add(nameRecord);

		Requirement contactReq = get(CONTACTS);
		Set<Contact> contacts = contactReq.getValue();
		selection = context.getSelection(CONTACTS);
		if (selection != null) {
			Result contact = contact(context, "vendor contact", CONTACTS,
					(ClientContact) selection);
			if (contact != null) {
				return contact;
			}
		}
		selection = context.getSelection("values");

		Requirement isActiveReq = get(ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This Item is Active";
		} else {
			activeString = "This Item is InActive";
		}
		Record isActiveRecord = new Record(ACTIVE);
		isActiveRecord.add("Name", "Active ");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);

		if (accountingType == ACCOUNTING_TYPE_US) {
			Requirement trackPaymentsReq = get(TRACK_PAYMENTS_FOR_1099);
			Boolean trackPayments = (Boolean) trackPaymentsReq.getValue();
			if (selection == trackPayments) {
				context.setAttribute(INPUT_ATTR, ACTIVE);
				trackPayments = !trackPayments;
				trackPaymentsReq.setValue(trackPayments);
			}
			String paymentString = "";
			if (trackPayments) {
				paymentString = "This Item is Active";
			} else {
				paymentString = "This Item is InActive";
			}
			Record paymentRecord = new Record(ACTIVE);
			paymentRecord.add("Name", "");
			paymentRecord.add("Value", paymentString);
			list.add(paymentRecord);
		}
		Result result = dateOptionalRequirement(context, list, VENDOR_SINCE,
				VENDOR_SINCE, selection);

		if (result != null) {
			return result;
		}

		result = amountOptionalRequirement(context, list, selection, BALANCE,
				"Enter Balance");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, BALANCE_AS_OF,
				BALANCE_AS_OF, selection);
		if (result != null) {
			return result;
		}

		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, PHONE,
				"Enter Phone Number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, FAX,
				"Enter Fax Number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, EMAIL,
				"Enter Email Id");
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection,
				WEB_PAGE_ADDRESS, "Enter Web page address");
		if (result != null) {
			return result;
		}

		result = amountOptionalRequirement(context, list, selection,
				CREDIT_LIMIT, "Enter Credit Limit ");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, BANK_NAME,
				"Enter Bank name");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection,
				ACCOUNT_NO, "Enter Account Number ");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection,
				BANK_BRANCH, "Enter Bank branch name");
		if (result != null) {
			return result;
		}

		// result = accountsRequirement(context, list, (String) selection);
		// if (result != null) {
		// return result;
		// }
		//
		// result = accountsRequirement(context, "Account",
		// new ListFilter<Account>() {
		//
		// @Override
		// public boolean filter(Account e) {
		//
		// return e.getIsActive();
		// }
		// });
		// if (result != null) {
		// return result;
		// }
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, (String) selection);
		if (result != null) {
			return result;
		}

		result = preferredShippingMethodRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = vendorGroupRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection,
				VAT_REGISTRATION_NUMBER, "Enter vat Registeration Number");
		if (result != null) {
			return result;
		}
		result = VatCodeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Vendor is ready to create with following values.");
		result.add(list);
		result.add("Contacts:-");
		ResultList contactList = new ResultList(CONTACTS);
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
		finish.add("", "Finish to create Vendor.");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	private Result VatCodeRequirement(Context context, ResultList list,
			Object selection) {
		Object customerVatCodeObj = context.getSelection(TAXCODE);
		Requirement customerVatCodeReq = get(VENDOR_VAT_CODE);
		ClientTAXCode vatCode = (ClientTAXCode) customerVatCodeReq.getValue();

		if (customerVatCodeObj != null) {
			vatCode = (ClientTAXCode) customerVatCodeObj;
			customerVatCodeReq.setValue(vatCode);
		}
		if (selection != null) {
			if (selection.equals("vatCode")) {
				context.setAttribute(INPUT_ATTR, VENDOR_VAT_CODE);
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

	private Result vatRegisterationNumRequirement(Context context,
			ResultList list, Object selection) {

		Requirement req = get(VAT_REGISTRATION_NUMBER);
		String vatRegisterationNum = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(VAT_REGISTRATION_NUMBER)) {
			String order = context.getSelection(VAT_REGISTRATION_NUMBER);
			if (order == null) {
				order = context.getString();
			}
			vatRegisterationNum = order;
			req.setValue(vatRegisterationNum);
		}

		if (selection != null)
			if (selection == vatRegisterationNum) {
				context.setAttribute(INPUT_ATTR, VAT_REGISTRATION_NUMBER);
				return text(context, "Enter vat Registeration Number ",
						vatRegisterationNum);
			}

		Record vatRegisterationNumRecord = new Record(vatRegisterationNum);
		vatRegisterationNumRecord.add("Name", VAT_REGISTRATION_NUMBER);
		vatRegisterationNumRecord.add("Value", vatRegisterationNum);
		list.add(vatRegisterationNumRecord);
		Result result = new Result();
		result.add(list);
		return null;

	}

	private Result vendorGroupRequirement(Context context, ResultList list,
			Object selection) {

		Object vendorGroupObj = context.getSelection(VENDOR_GROUP);
		Requirement vendorGroupReq = get(VENDOR_GROUP);
		String vendorGRP = (String) vendorGroupReq.getValue();

		if (vendorGroupObj != null) {
			vendorGRP = (String) vendorGroupObj;
			vendorGroupReq.setValue(vendorGRP);
		}
		if (selection != null)
			if (selection == VENDOR_GROUP) {
				context.setAttribute(INPUT_ATTR, VENDOR_GROUP);
				return vendorGroups(context, vendorGRP);
			}
		Record customerGroupRecord = new Record(VENDOR_GROUP);
		customerGroupRecord.add("Name", VENDOR_GROUP);
		customerGroupRecord.add("Value", vendorGRP);
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	private Result accountsRequirement(Context context, ResultList list,
			Object selection) {

		Object accountObj = context.getSelection(ACCOUNT);
		Requirement accountReq = get(ACCOUNT);
		String account = (String) accountReq.getValue();

		if (accountObj != null) {
			account = (String) accountObj;
			accountReq.setValue(account);
		}
		if (selection != null)
			if (selection == ACCOUNT) {
				context.setAttribute(INPUT_ATTR, ACCOUNT);
				return account(context, account);
			}
		Record customerGroupRecord = new Record(ACCOUNT);
		customerGroupRecord.add("Name", ACCOUNT);
		customerGroupRecord.add("Value", account);
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	private Record createVendorGroupRecord(String oldVenodrGroup) {
		Record record = new Record(oldVenodrGroup);
		record.add("Name", VENDOR_GROUP);
		record.add("value", oldVenodrGroup);
		return record;
	}

	private Record createAccountRecord(String oldAccount) {
		Record record = new Record(oldAccount);
		record.add("Name", ACCOUNT);
		record.add("value", oldAccount);
		return record;
	}

	private Result vendorGroups(Context context, String oldVendorGroup) {
		Set<VendorGroup> vendorGroups = getVendorGroupsList(context);
		Result result = context.makeResult();
		result.add("Select Vendor Group");

		ResultList list = new ResultList(VENDOR_GROUP);
		int num = 0;
		if (oldVendorGroup != null) {
			list.add(createVendorGroupRecord(oldVendorGroup));
			num++;
		}
		for (VendorGroup vendor : vendorGroups) {
			if (vendor.getName() != oldVendorGroup) {
				list.add(createVendorGroupRecord(vendor.getName()));
				num++;
			}
			if (num == VENDORGROUP_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Vendor Group");
		result.add(commandList);

		return result;
	}

	private Result account(Context context, String oldAccount) {
		Set<Account> accounts = getAccountsList(context);
		Result result = context.makeResult();
		result.add("Select Account");

		ResultList list = new ResultList(ACCOUNT);
		int num = 0;
		if (oldAccount != null) {
			list.add(createAccountRecord(oldAccount));
			num++;
		}
		for (Account acc : accounts) {
			if (acc.getName() != oldAccount) {
				list.add(createAccountRecord(acc.getName()));
				num++;
			}
			if (num == VENDORGROUP_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Account");
		result.add(commandList);

		return result;
	}

	private Set<VendorGroup> getVendorGroupsList(Context context) {
		Set<VendorGroup> vendorGroups = context.getCompany().getVendorGroups();
		return vendorGroups;
	}

	private Set<Account> getAccountsList(Context context) {
		Set<Account> accounts = context.getCompany().getAccounts();
		Set<Account> list = new HashSet<Account>(accounts.size());
		for (Account acc : accounts) {
			if (acc.getIsActive())
				list.add(acc);
		}
		return list;
	}

	private Result vendorNumberRequirement(Context context) {
		Requirement vendorNumberReq = get(VENDOR_NUMBER);
		if (!vendorNumberReq.isDone()) {
			Integer vendorNum = context.getInteger();
			if (vendorNum != null) {
				vendorNumberReq.setValue(vendorNum);
			} else {
				context.setAttribute(INPUT_ATTR, VENDOR_NUMBER);
				return number(context, "Please Enter the Vendor Number.", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(VENDOR_NUMBER)) {
			vendorNumberReq.setValue(context.getInteger());
		}
		return null;
	}

	private Result vendorNameRequirement(Context context) {
		Requirement requirement = get(VENDOR_NAME);
		if (!requirement.isDone()) {
			String vendorName = context.getString();
			if (vendorName != null && (vendorName.trim().length() != 0)) {
				requirement.setValue(vendorName);
			} else {
				context.setAttribute(INPUT_ATTR, VENDOR_NAME);
				return text(context, "Please enter the  Vendor Name", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(VENDOR_NAME)) {
			requirement.setValue(input);
		}
		return null;
	}

}
