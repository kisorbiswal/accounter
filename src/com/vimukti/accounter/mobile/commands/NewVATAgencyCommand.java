package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewVATAgencyCommand extends AbstractCommand {

	private static final String NAME = "name";
	private static final String PAYMENT_TERM = "paymentTerm";
	private static final String VAT_RETURN = "vatReturn";
	private static final String SALES_ACCOUNT = "salesLiabilityAccount";
	private static final String PURCHASE_ACCOUNT = "purchaseLiabilityAccount";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBSITE = "webPageAddress";
	private static final String CONTACTS = "contacts";
	protected static final String CONTACT_NAME = "contactName";
	protected static final String TITLE = "title";
	protected static final String BUSINESS_PHONE = "businessPhone";
	protected static final String CONTACT_EMAIL = "contactEmail";
	private static final String IS_ACTIVE = "isActive";
	private static final String SALES_ACCOUNTS = "salesAccounts";
	private static final String VAT_RETURNS = "vatReturns";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final int VALUES_TO_SHOW = 5;

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
		if (getCompanyType() != 0) {
			list.add(new Requirement(VAT_RETURN, false, true));
			list.add(new Requirement(PURCHASE_ACCOUNT, false, true));
		}
		list.add(new Requirement(ADDRESS, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEBSITE, true, true));
		list.add(new ObjectListRequirement(CONTACTS, true, true) {

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
		}

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

		if (getCompanyType() != 0) {
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

	private Result createVatAgency(Context context) {
		TAXAgency taxAgency = new TAXAgency();
		String name = get(NAME).getValue();
		PaymentTerms paymentTerm = get(PAYMENT_TERM).getValue();
		Account salesAccount = get(SALES_ACCOUNT).getValue();
		Address address = (Address) get(ADDRESS).getDefaultValue();
		String phone = (String) get(PHONE).getDefaultValue();
		String fax = (String) get(FAX).getDefaultValue();
		String email = (String) get(EMAIL).getDefaultValue();
		String website = (String) get(WEBSITE).getDefaultValue();
		Set<Contact> contacts = (Set<Contact>) get(CONTACTS).getDefaultValue();

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
		if (getCompanyType() != 0) {
			Account purchaseAccount = get(PURCHASE_ACCOUNT).getValue();
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
		result.add(getString() + " Agency was created Successfully.");

		return result;
	}

	private Result createOptionalResult(Context context) {
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

		selection = context.getSelection("values");

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		if (name == selection) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the " + getString()
					+ " Agency Name.", name);
		}

		Requirement paymentTermReq = get(PAYMENT_TERM);
		PaymentTerms paymentTerm = (PaymentTerms) paymentTermReq.getValue();
		if (paymentTerm == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_TERM);
			return getPaymentTermsResult(context);
		}

		Requirement salesAccountReq = get(SALES_ACCOUNT);
		Account salesAccount = (Account) salesAccountReq.getValue();
		if (salesAccount == selection) {
			context.setAttribute(INPUT_ATTR, SALES_ACCOUNT);
			return getSalesAccountResult(context);
		}

		ResultList list = new ResultList("values");

		Record nameRecord = new Record(name);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

		Record paymentTermRecord = new Record(paymentTerm);
		paymentTermRecord.add(INPUT_ATTR, "Payment Term");
		paymentTermRecord.add("Value", paymentTerm.getName());
		list.add(paymentTermRecord);

		Record salesAccountRecord = new Record(salesAccount);
		salesAccountRecord.add(INPUT_ATTR, "Sales Liability Account");
		salesAccountRecord.add("Value", salesAccount.getName());
		list.add(salesAccountRecord);

		if (getCompanyType() != 0) {
			Requirement purchseAccountReq = get(PURCHASE_ACCOUNT);
			Account purchaseAccount = (Account) purchseAccountReq.getValue();
			if (purchaseAccount == selection) {
				context.setAttribute(INPUT_ATTR, PURCHASE_ACCOUNT);
				return getPurchaseAccountResult(context);
			}

			Requirement vatReturnReq = get(VAT_RETURN);
			VATReturn vatReturn = (VATReturn) vatReturnReq.getValue();
			if (vatReturn == selection) {
				context.setAttribute(INPUT_ATTR, SALES_ACCOUNT);
				return getVatReturnResult(context);
			}

			Record purchaseAccountRecord = new Record(purchaseAccount);
			purchaseAccountRecord.add(INPUT_ATTR, "Purchase Liability Account");
			purchaseAccountRecord.add("Value", purchaseAccount.getName());
			list.add(purchaseAccountRecord);
		}

		Result result = addressRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = phoneRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = faxRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = emailRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = websiteRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Requirement contactReq = get("customerContact");
		List<Contact> contacts = contactReq.getValue();
		selection = context.getSelection("customerContact");
		if (selection != null) {
			Result contact = contact(context, "customer contact",
					(Contact) selection);
			if (contact != null) {
				return contact;
			}
		}

		Requirement isActiveReq = get(IS_ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getDefaultValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, IS_ACTIVE);
			isActive = !isActive;
			isActiveReq.setDefaultValue(isActive);
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
		result.add(getString()
				+ " Agency is ready to create with following values.");
		result.add(list);
		ResultList contactsList = new ResultList("customerContact");
		for (Contact contact : contacts) {
			Record contactRec = new Record(contact);
			contactRec.add("primary", contact.getVersion());
			contactRec.add("contactName", contact.getName());
			contactRec.add("title", contact.getTitle());
			contactRec.add("businessPhone", contact.getBusinessPhone());
			contactRec.add("email", contact.getEmail());
		}

		result.add(contactsList);
		ResultList actions = new ResultList("actions");
		Record moreContacts = new Record(ActionNames.ADD_MORE_ITEMS);
		moreContacts.add("", "Add more contacts");
		actions.add(moreContacts);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create " + getString() + " Agency.");
		actions.add(finish);
		result.add(actions);

		return null;
	}

	private Result websiteRequirement(Context context, ResultList list,
			Object selection) {
		Requirement websiteReq = get(WEBSITE);
		String website = (String) websiteReq.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(WEBSITE)) {
			String input = context.getSelection(WEBSITE);
			if (input == null) {
				input = context.getString();
			}
			website = input;
			websiteReq.setDefaultValue(website);
		}

		if (selection == website) {
			context.setAttribute(INPUT_ATTR, WEBSITE);
			return text(context, "Website", website);
		}

		Record websiteRecord = new Record(website);
		websiteRecord.add("Name", "Website");
		websiteRecord.add("Value", website);
		list.add(websiteRecord);
		return null;
	}

	private Result emailRequirement(Context context, ResultList list,
			Object selection) {
		Requirement emailReq = get(EMAIL);
		String email = (String) emailReq.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(EMAIL)) {
			String input = context.getSelection(EMAIL);
			if (input == null) {
				input = context.getString();
			}
			email = input;
			emailReq.setDefaultValue(email);
		}

		if (selection == email) {
			context.setAttribute(INPUT_ATTR, EMAIL);
			return text(context, "Email", email);
		}

		Record emailRecord = new Record(email);
		emailRecord.add("Name", "Email");
		emailRecord.add("Value", email);
		list.add(emailRecord);
		return null;
	}

	private Result faxRequirement(Context context, ResultList list,
			Object selection) {
		Requirement faxReq = get(FAX);
		String fax = (String) faxReq.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FAX)) {
			String input = context.getSelection(FAX);
			if (input == null) {
				input = context.getString();
			}
			fax = input;
			faxReq.setDefaultValue(fax);
		}

		if (selection == fax) {
			context.setAttribute(INPUT_ATTR, FAX);
			return text(context, "Fax", fax);
		}

		Record faxRecord = new Record(fax);
		faxRecord.add("Name", "Fax");
		faxRecord.add("Value", fax);
		list.add(faxRecord);
		return null;
	}

	private Result phoneRequirement(Context context, ResultList list,
			Object selection) {
		Requirement phoneReq = get(PHONE);
		String phone = (String) phoneReq.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PHONE)) {
			String input = context.getSelection(PHONE);
			if (input == null) {
				input = context.getString();
			}
			phone = input;
			phoneReq.setDefaultValue(phone);
		}

		if (selection == phone) {
			context.setAttribute(INPUT_ATTR, PHONE);
			return text(context, "Phone", phone);
		}

		Record phoneRecord = new Record(phone);
		phoneRecord.add("Name", "Phone");
		phoneRecord.add("Value", phone);
		list.add(phoneRecord);
		return null;
	}

	private Result addressRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(ADDRESS);
		Address address = (Address) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ADDRESS)) {
			Address input = context.getSelection(ADDRESS);
			if (input == null) {
				input = context.getAddress();
			}
			address = input;
			req.setDefaultValue(address);
		}

		if (selection == address) {
			context.setAttribute(INPUT_ATTR, ADDRESS);
			return address(context, "Address", address);
		}

		Record addressRecord = new Record(address);
		addressRecord.add("Name", "Address");
		addressRecord.add("Value", address.toString());
		list.add(addressRecord);
		return null;
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

		List<Account> purchseAccounts = getAccounts(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < purchseAccounts.size(); i++) {
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

	private Record createAccountRecord(Account account) {
		Record record = new Record(account);
		record.add("Number", account.getNumber());
		record.add("Name", account.getName());
		record.add("Type", Utility.getAccountTypeString(account.getType()));
		return record;
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

		List<Account> salesAccounts = getAccounts(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < salesAccounts.size(); i++) {
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

	private List<Account> getAccounts(Session session) {
		// TODO Auto-generated method stub
		return null;
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

		List<String> vatReturns = getVatReturns(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < vatReturns.size(); i++) {
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

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(vatReturnsList);
		result.add(commandList);
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
		vatReturnList.add(Accounter.constants().ukVAT());
		vatReturnList.add(Accounter.constants().vat3Ireland());

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

		List<PaymentTerms> paymentTerms = getPaymentTerms(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < paymentTerms.size(); i++) {
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

	private List<PaymentTerms> getPaymentTerms(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createPaymentTermRecord(PaymentTerms paymentTerm) {
		Record record = new Record(paymentTerm);
		record.add("Name", paymentTerm.getName());
		return record;
	}

	private Result nameRequirement(Context context) {
		Requirement nameReq = get(NAME);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(NAME)) {
			input = context.getString();
			nameReq.setValue(input);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!nameReq.isDone()) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the " + getString()
					+ " Agency Name.", null);
		}

		return null;
	}

	private int getCompanyType() {
		Company company = new FinanceTool().getCompany();
		int accountingType = company.getAccountingType();
		return accountingType;
	}

	private String getString() {
		String s = "TAX";
		if (getCompanyType() == 1) {
			s = "VAT";
		}
		return s;
	}

}
