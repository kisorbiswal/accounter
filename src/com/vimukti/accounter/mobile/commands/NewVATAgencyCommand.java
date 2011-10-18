package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.vimukti.accounter.web.client.core.ClientTAXAgency;

public class NewVATAgencyCommand extends AbstractVATCommand {

	private static final String PAYMENT_TERM = "paymentTerm";
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
		Result makeResult = context.makeResult();
		makeResult.add("VatAgency  is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		setOptionalValues();

		result = nameRequirement(context, list, NAME, "Enter VatAgency Name");
		if (result != null) {
			return result;
		}

		result = paymentTermsRequirement(context, list, PAYMENT_TERM,
				"Enter VatAgency Name");
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, SALES_ACCOUNT);
		if (result != null) {
			return result;
		}

		if (getCompanyType(context) != ACCOUNTING_TYPE_US) {
			result = accountRequirement(context, list, SALES_ACCOUNT);
			if (result != null) {
				return result;
			}
			result = vatReturnRequirement(context, list, VAT_RETURN);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context, list, actions, makeResult);
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

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		// context.setAttribute(INPUT_ATTR, "optional");

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

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Vat Agency.");
		actions.add(finish);
		return makeResult;
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

	private Result salesAccountRequirement(Context context, ResultList list,
			String name, String dispalyString) {
		Requirement salesAccountReq = get(name);
		Account salesAccount = context.getSelection(name);
		if (salesAccount != null) {
			salesAccountReq.setValue(salesAccount);
		}

		Account account = salesAccountReq.getValue();
		Object selection = context.getSelection("values");

		if (!salesAccountReq.isDone() || (account == selection)) {
			return getSalesAccountResult(context);
		}
		Record paymentTermsRecord = new Record(account);
		paymentTermsRecord.add("", name);
		paymentTermsRecord.add("", account.getName());
		return null;
	}

	private Result getSalesAccountResult(Context context) {
		Result result = context.makeResult();
		ResultList salesAccountsList = new ResultList(SALES_ACCOUNTS);

		Object last = context.getLast(RequirementType.ACCOUNT);
		List<Account> skipAccount = new ArrayList<Account>();
		if (last != null) {
			salesAccountsList.add(createAccountRecord((Account) last));
			skipAccount.add((Account) last);
		}

		List<Account> salesAccounts = getVatAgencyAccounts(context);

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<Account> pagination = pagination(context, selection, actions,
				salesAccounts, skipAccount, VALUES_TO_SHOW);

		for (Account account : pagination) {
			salesAccountsList.add(createAccountRecord(account));
		}

		int size = salesAccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a salesAcount");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create New SaleAcount");

		result.add(message.toString());
		result.add(salesAccountsList);
		result.add(actions);
		result.add(commandList);
		return result;

		// for (int i = 0; i < VALUES_TO_SHOW && i < salesAccounts.size(); i++)
		// {
		// Account salesAccount = salesAccounts.get(i);
		// if (salesAccount != last) {
		// salesAccountsList
		// .add(createAccountRecord((Account) salesAccount));
		// }
		// }
		//
		// int size = salesAccountsList.size();
		// StringBuilder message = new StringBuilder();
		// if (size > 0) {
		// message.append("Please Select the Sales Liability Account");
		// }
		//
		// CommandList commandList = new CommandList();
		// commandList.add("create");
		//
		// result.add(message.toString());
		// result.add(salesAccountsList);
		// result.add(commandList);
		// result.add("Select the Sales Liability Account");
		//
		// return result;
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

}
