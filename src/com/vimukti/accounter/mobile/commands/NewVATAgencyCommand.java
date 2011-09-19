package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.PaymentTerms;
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
import com.vimukti.accounter.web.server.FinanceTool;

public class NewVATAgencyCommand extends AbstractCommand {

	private static final String INPUT_ATTR = "input";

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
		if (getCompanyType() == 0) {
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

		if (getCompanyType() == 0) {
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

		return result;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CONTACTS:
				// return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		return null;
	}

	private Result purchaseAccountRequirement(Context context) {
		Requirement purchaseAccountReq = get(PURCHASE_ACCOUNT);
		Account purchaseAccount = context.getSelection(PURCHASE_ACCOUNT);
		if (purchaseAccount != null) {
			purchaseAccountReq.setValue(purchaseAccount);
		}
		if (!purchaseAccountReq.isDone()) {
			return getPurchseAccountResult(context);
		}
		return null;
	}

	private Result getPurchseAccountResult(Context context) {
		Result result = context.makeResult();
		ResultList purchseAccountsList = new ResultList(PURCHASE_ACCOUNT);

		Object last = context.getLast(RequirementType.ACCOUNT);
		if (last != null) {
			purchseAccountsList.add(createPurchAccountRecord((Account) last));
		}

		List<Account> purchseAccounts = getAccounts(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < purchseAccounts.size(); i++) {
			Account purchseAccount = purchseAccounts.get(i);
			if (purchseAccount != last) {
				purchseAccountsList
						.add(createSalesAccountRecord((Account) purchseAccount));
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

	private Record createPurchAccountRecord(Account last) {
		// TODO Auto-generated method stub
		return null;
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
			salesAccountsList.add(createSalesAccountRecord((Account) last));
		}

		List<Account> salesAccounts = getAccounts(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < salesAccounts.size(); i++) {
			Account salesAccount = salesAccounts.get(i);
			if (salesAccount != last) {
				salesAccountsList
						.add(createSalesAccountRecord((Account) salesAccount));
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

	private Record createSalesAccountRecord(Account last) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result vatReturnRequirement(Context context) {
		Requirement vatReturnReq = get(VAT_RETURN);
		VATReturn vatReturn = context.getSelection(VAT_RETURNS);
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
			vatReturnsList.add(createVatReturnRecord((VATReturn) last));
		}

		List<VATReturn> vatReturns = getVatReturns(context.getSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < vatReturns.size(); i++) {
			VATReturn vatReturn = vatReturns.get(i);
			if (vatReturn != last) {
				vatReturnsList
						.add(createVatReturnRecord((VATReturn) vatReturn));
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

	private Record createVatReturnRecord(VATReturn last) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<VATReturn> getVatReturns(Session session) {
		// TODO Auto-generated method stub
		return null;
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

	private Record createPaymentTermRecord(PaymentTerms last) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result nameRequirement(Context context) {
		Requirement nameReq = get(NAME);
		if (!nameReq.isDone()) {
			String string = context.getString();
			if (string != null) {
				nameReq.setValue(string);
			} else {
				return text(context, "Please Enter the " + getString()
						+ " Agency Name.", null);
			}
		}
		String input = (String) context.getAttribute("input");
		if (input.equals(NAME)) {
			nameReq.setValue(input);
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
