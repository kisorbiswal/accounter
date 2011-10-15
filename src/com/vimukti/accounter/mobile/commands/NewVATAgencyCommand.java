package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;

public class NewVATAgencyCommand extends AbstractVATCommand {

	private static final String PAYMENT_TERM = "paymentTerm";
	private static final String VAT_RETURN = "vatReturn";
	private static final String SALES_ACCOUNT = "salesLiabilityAccount";
	private static final String PURCHASE_ACCOUNT = "purchaseLiabilityAccount";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBSITE = "webPageAddress";
	private static final String VAT_AGENCY_CONTACT = "vatAgencyContact";
	protected static final String CONTACT_NAME = "contactName";
	protected static final String TITLE = "title";
	protected static final String BUSINESS_PHONE = "businessPhone";
	protected static final String CONTACT_EMAIL = "contactEmail";
	private static final String IS_ACTIVE = "isActive";
	private static final String SALES_ACCOUNTS = "salesAccounts";
	private static final String VAT_RETURNS = "vatReturns";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String VAT_AGENCY_ADDRESS = "vatAgencyAddress";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(PAYMENT_TERM, false, true));
		list.add(new Requirement(SALES_ACCOUNT, false, true));
		// if (getCompanyType(c) != 0) {
		list.add(new Requirement(VAT_RETURN, false, true));
		list.add(new Requirement(PURCHASE_ACCOUNT, false, true));
		// }
		list.add(new Requirement(VAT_AGENCY_ADDRESS, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEBSITE, true, true));
		list.add(new ObjectListRequirement(VAT_AGENCY_CONTACT, true, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(CONTACT_NAME, true, true));
				list.add(new Requirement(TITLE, true, true));
				list.add(new Requirement(BUSINESS_PHONE, true, true));
				list.add(new Requirement(CONTACT_EMAIL, true, true));
			}
		});
		list.add(new Requirement(IS_ACTIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		String process = (String) context.getAttribute(PROCESS_ATTR);
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

		setOptionalValues();

		result = nameRequirement(context);
		if (result != null) {
			return result;
		}

		result = paymentTermsRequirement(context);
		if (result != null) {
			return result;
		}

		result = salesAccountRequirement(context);
		if (result != null) {
			return result;
		}

		if (getCompanyType(context) != ACCOUNTING_TYPE_US) {
			result = purchaseAccountRequirement(context);
			if (result != null) {
				return result;
			}
			result = vatReturnRequirement(context);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}

		return createVatAgency(context);
	}

	private void setOptionalValues() {
		Requirement isActiveReq = get(IS_ACTIVE);
		if (isActiveReq.getDefaultValue() == null) {
			isActiveReq.setDefaultValue(true);
		}
		Requirement contactsReq = get(VAT_AGENCY_CONTACT);
		if (contactsReq.getDefaultValue() == null) {
			contactsReq.setDefaultValue(new HashSet<Contact>());
		}
		Requirement addressReq = get(VAT_AGENCY_ADDRESS);
		if (addressReq.getDefaultValue() == null) {
			addressReq.setDefaultValue(new Address());
		}
		Requirement phoneReq = get(PHONE);
		if (phoneReq.getDefaultValue() == null) {
			phoneReq.setDefaultValue(new String());
		}
		Requirement faxReq = get(FAX);
		if (faxReq.getDefaultValue() == null) {
			faxReq.setDefaultValue(new String());
		}
		Requirement emailReq = get(EMAIL);
		if (emailReq.getDefaultValue() == null) {
			emailReq.setDefaultValue(new String());
		}
		Requirement websiteReq = get(WEBSITE);
		if (websiteReq.getDefaultValue() == null) {
			websiteReq.setDefaultValue(new String());
		}
	}

	private Result createVatAgency(Context context) {
		TAXAgency taxAgency = new TAXAgency();
		taxAgency.setCompany(context.getCompany());
		String name = get(NAME).getValue();
		PaymentTerms paymentTerm = get(PAYMENT_TERM).getValue();
		Account salesAccount = get(SALES_ACCOUNT).getValue();
		salesAccount = (Account) context.getHibernateSession().merge(
				salesAccount);
		Address address = (Address) get(VAT_AGENCY_ADDRESS).getValue();
		String phone = (String) get(PHONE).getValue();
		String fax = (String) get(FAX).getValue();
		String email = (String) get(EMAIL).getValue();
		String website = (String) get(WEBSITE).getValue();
		Set<Contact> contacts = get(VAT_AGENCY_CONTACT).getValue();

		HashSet<Address> addresses = new HashSet<Address>();
		if (address != null) {
			addresses.add(address);
		}

		taxAgency.setName(name);
		taxAgency.setPaymentTerm(paymentTerm);
		taxAgency.setSalesLiabilityAccount(salesAccount);
		taxAgency.setAddress(addresses);
		taxAgency.setPhoneNo(phone);
		taxAgency.setFaxNo(fax);
		taxAgency.setEmail(email);
		taxAgency.setWebPageAddress(website);
		taxAgency.setContacts(contacts);
		if (getCompanyType(context) != ACCOUNTING_TYPE_US) {
			Account purchaseAccount = get(PURCHASE_ACCOUNT).getValue();
			purchaseAccount = (Account) context.getHibernateSession().merge(
					purchaseAccount);
			String vatReturn = get(VAT_RETURN).getValue();
			taxAgency.setPurchaseLiabilityAccount(purchaseAccount);
			if (vatReturn == "") {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
			} else if (vatReturn == "UK VAT") {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
			} else {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
			}
		}

		create(taxAgency, context);

		markDone();
		Result result = new Result();
		result.add(getConstants().taxAgencyCreated());

		return result;
	}

	private Result createOptionalResult(Context context) {
		// context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CONTACTS:
				return contact(
						context,
						getMessages().pleaseEnter(
								getConstants().contactDetails()),
						VAT_AGENCY_CONTACT, null);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		if (NAME == selection) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context,
					getMessages().pleaseEnter(getConstants().taxAgency()), name);
		}

		Requirement paymentTermReq = get(PAYMENT_TERM);
		PaymentTerms paymentTerm = (PaymentTerms) paymentTermReq.getValue();
		if (PAYMENT_TERM == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_TERM);
			return getPaymentTermsResult(context);
		}

		Requirement salesAccountReq = get(SALES_ACCOUNT);
		Account salesAccount = (Account) salesAccountReq.getValue();
		if (SALES_ACCOUNT == selection) {
			context.setAttribute(INPUT_ATTR, SALES_ACCOUNT);
			return getSalesAccountResult(context);
		}
		Requirement addressReq = get(VAT_AGENCY_ADDRESS);
		if (selection == "Address") {
			ClientAddress oldAddress = addressReq.getValue();
			Result result = address(context, "Address", VAT_AGENCY_ADDRESS,
					oldAddress);
			if (result != null) {
				return result;
			}
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(NAME);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Record paymentTermRecord = new Record(PAYMENT_TERM);
		paymentTermRecord.add(INPUT_ATTR, "Payment Term");
		paymentTermRecord.add("Value", paymentTerm.getName());
		list.add(paymentTermRecord);

		Record salesAccountRecord = new Record(SALES_ACCOUNT);
		salesAccountRecord.add(INPUT_ATTR, "Sales Liability Account");
		salesAccountRecord.add("Value", salesAccount.getName());
		list.add(salesAccountRecord);

		if (getCompanyType(context) != ACCOUNTING_TYPE_US) {
			Requirement purchseAccountReq = get(PURCHASE_ACCOUNT);
			Account purchaseAccount = (Account) purchseAccountReq.getValue();
			if (PURCHASE_ACCOUNT == selection) {
				context.setAttribute(INPUT_ATTR, PURCHASE_ACCOUNT);
				return getPurchaseAccountResult(context);
			}

			Requirement vatReturnReq = get(VAT_RETURN);
			String vatReturn = (String) vatReturnReq.getValue();
			if (VAT_RETURN == selection) {
				context.setAttribute(INPUT_ATTR, SALES_ACCOUNT);
				return getVatReturnResult(context);
			}

			Record purchaseAccountRecord = new Record(purchaseAccount);
			purchaseAccountRecord.add(INPUT_ATTR, "Purchase Liability Account");
			purchaseAccountRecord.add("Value", purchaseAccount.getName());
			list.add(purchaseAccountRecord);

			Record vatReturnRecord = new Record(vatReturn);
			vatReturnRecord.add(INPUT_ATTR, "Vat Return");
			vatReturnRecord.add("Value", vatReturn);
			list.add(vatReturnRecord);
		}

		Address address = addressReq.getValue();
		Record addressRecord = new Record("Address");
		addressRecord.add(INPUT_ATTR, "Address");
		addressRecord.add("Value", address);
		list.add(addressRecord);

		Result result = numberOptionalRequirement(context, list, selection,
				PHONE, "Enter Phone Number");
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, FAX,
				"Enter Fax number");
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, EMAIL,
				"Enter Email");
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, WEBSITE,
				"Enter Web Address");
		if (result != null) {
			return result;
		}

		Requirement contactReq = get(VAT_AGENCY_CONTACT);
		Set<Contact> contacts = contactReq.getValue();
		selection = context.getSelection(VAT_AGENCY_CONTACT);
		if (selection != null) {
			Result contact = contact(context, "vat agency contact",
					VAT_AGENCY_CONTACT, (ClientContact) selection);
			if (contact != null) {
				return contact;
			}
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();

		if (selection == IS_ACTIVE) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This Agency is Active";
		} else {
			activeString = "This Agency is InActive";
		}
		Record isActiveRecord = new Record(IS_ACTIVE);
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);

		result = context.makeResult();
		result.add("Tax Agency is ready to create with following values.");
		result.add(list);
		ResultList contactsList = new ResultList(VAT_AGENCY_CONTACT);
		for (Contact contact : contacts) {
			Record contactRec = new Record(contact);
			contactRec.add("primary", contact.getVersion());
			contactRec.add("contactName", contact.getName());
			contactRec.add("title", contact.getTitle());
			contactRec.add("businessPhone", contact.getBusinessPhone());
			contactRec.add("email", contact.getEmail());
			contactsList.add(contactRec);
		}

		result.add(contactsList);
		ResultList actions = new ResultList("actions");
		Record moreContacts = new Record(ActionNames.ADD_MORE_CONTACTS);
		moreContacts.add("", "Add more contacts");
		actions.add(moreContacts);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Tax Agency.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result purchaseAccountRequirement(Context context) {
		Requirement purchaseAccountReq = get(PURCHASE_ACCOUNT);
		Account purchaseAccount = context.getSelection(PURCHASE_ACCOUNT);
		if (purchaseAccount != null) {
			purchaseAccountReq.setValue(purchaseAccount);
		}
		if (!purchaseAccountReq.isDone()) {
			return getPurchaseAccountResult(context);
		}
		return null;
	}

	private Result getPurchaseAccountResult(Context context) {
		Result result = context.makeResult();
		ResultList purchseAccountsList = new ResultList(PURCHASE_ACCOUNT);

		Object last = context.getLast(RequirementType.ACCOUNT);
		if (last != null) {
			purchseAccountsList.add(createAccountRecord((Account) last));
		}

		List<Account> purchseAccounts = getVatAgencyAccounts(context);
		for (int i = 0; i < VALUES_TO_SHOW && i < purchseAccounts.size(); i++) {
			Account purchseAccount = purchseAccounts.get(i);
			if (purchseAccount != last) {
				purchseAccountsList
						.add(createAccountRecord((Account) purchseAccount));
			}
		}

		int size = purchseAccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Purchase Liability Account");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(purchseAccountsList);
		result.add(commandList);
		result.add("Select the Purchse Liability Account");

		return result;
	}

	private Result salesAccountRequirement(Context context) {
		Requirement salesAccountReq = get(SALES_ACCOUNT);
		Account salesAccount = context.getSelection(SALES_ACCOUNTS);
		if (salesAccount != null) {
			salesAccountReq.setValue(salesAccount);
		}
		if (!salesAccountReq.isDone()) {
			return getSalesAccountResult(context);
		}
		return null;
	}

	private Result getSalesAccountResult(Context context) {
		Result result = context.makeResult();
		ResultList salesAccountsList = new ResultList(SALES_ACCOUNTS);

		Object last = context.getLast(RequirementType.ACCOUNT);
		if (last != null) {
			salesAccountsList.add(createAccountRecord((Account) last));
		}

		List<Account> salesAccounts = getVatAgencyAccounts(context);
		for (int i = 0; i < VALUES_TO_SHOW && i < salesAccounts.size(); i++) {
			Account salesAccount = salesAccounts.get(i);
			if (salesAccount != last) {
				salesAccountsList
						.add(createAccountRecord((Account) salesAccount));
			}
		}

		int size = salesAccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Sales Liability Account");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(salesAccountsList);
		result.add(commandList);
		result.add("Select the Sales Liability Account");

		return result;
	}

	private List<Account> getVatAgencyAccounts(Context context) {
		ArrayList<Account> accounts = new ArrayList<Account>();

		for (Account account : context.getCompany().getAccounts()) {
			if (account.getIsActive()
					&& Arrays.asList(ClientAccount.TYPE_INCOME,
							ClientAccount.TYPE_EXPENSE,
							ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
							ClientAccount.TYPE_OTHER_CURRENT_ASSET,
							ClientAccount.TYPE_FIXED_ASSET).contains(
							account.getType()))
				accounts.add(account);
		}
		return accounts;
	}

	private Result vatReturnRequirement(Context context) {
		Requirement vatReturnReq = get(VAT_RETURN);
		String vatReturn = context.getSelection(VAT_RETURNS);
		if (vatReturn != null) {
			vatReturnReq.setValue(vatReturn);
		}
		if (!vatReturnReq.isDone()) {
			return getVatReturnResult(context);
		}
		return null;
	}

	private Result getVatReturnResult(Context context) {
		Result result = context.makeResult();
		ResultList vatReturnsList = new ResultList(VAT_RETURNS);

		Object last = context.getLast(RequirementType.VAT_RETURN);
		if (last != null) {
			vatReturnsList.add(createVatReturnRecord((String) last));
		}

		List<String> vatReturns = getVatReturns(context.getHibernateSession());
		for (int i = 0; i < VALUES_TO_SHOW && i < vatReturns.size(); i++) {
			String vatReturn = vatReturns.get(i);
			if (vatReturn != last) {
				vatReturnsList.add(createVatReturnRecord((String) vatReturn));
			}
		}

		int size = vatReturnsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Vat Return");
		}

		result.add(message.toString());
		result.add(vatReturnsList);
		result.add("Select the Vat Return");

		return result;
	}

	private Record createVatReturnRecord(String vatReturn) {
		Record record = new Record(vatReturn);
		record.add("Name", vatReturn);
		return record;
	}

	private List<String> getVatReturns(Session session) {

		ArrayList<String> vatReturnList = new ArrayList<String>();
		vatReturnList.add("UK VAT");
		vatReturnList.add("VAT 3(Ireland)");

		return vatReturnList;
	}

	private Result paymentTermsRequirement(Context context) {
		Requirement paymentTermsReq = get(PAYMENT_TERM);
		PaymentTerms paymentTerms = context.getSelection(PAYMENT_TERMS);
		if (paymentTerms != null) {
			paymentTermsReq.setValue(paymentTerms);
		}
		if (!paymentTermsReq.isDone()) {
			return getPaymentTermsResult(context);
		}
		return null;
	}

	private Result getPaymentTermsResult(Context context) {
		Result result = context.makeResult();
		ResultList paymentTermsList = new ResultList(PAYMENT_TERMS);

		Object last = context.getLast(RequirementType.PAYMENT_TERMS);
		if (last != null) {
			paymentTermsList.add(createPaymentTermRecord((PaymentTerms) last));
		}

		List<PaymentTerms> paymentTerms = getPaymentTerms(context);
		for (int i = 0; i < VALUES_TO_SHOW && i < paymentTerms.size(); i++) {
			PaymentTerms paymentTerm = paymentTerms.get(i);
			if (paymentTerm != last) {
				paymentTermsList
						.add(createPaymentTermRecord((PaymentTerms) paymentTerm));
			}
		}

		int size = paymentTermsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the PaymentTerm");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(paymentTermsList);
		result.add(commandList);
		result.add("Select the Payment Term");

		return result;
	}

	private List<PaymentTerms> getPaymentTerms(Context context) {
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		return new ArrayList<PaymentTerms>(paymentTerms);
	}

	private Record createPaymentTermRecord(PaymentTerms paymentTerm) {
		Record record = new Record(paymentTerm);
		record.add("Name", paymentTerm.getName());
		return record;
	}

}
