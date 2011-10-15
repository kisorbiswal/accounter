package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.google.gwt.i18n.server.testing.Gender;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;

public class NewSalesPersonCommand extends AbstractTransactionCommand {

	private static final String SALES_PERSON_NAME = "salesPersonName";
	private static final String FILE_AS = "fileAs";
	private static final String JOB_TITLE = "jobTitle";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String E_MAIL = "eMail";
	private static final String WEB_PAGE_ADDRESS = "webPageAddress";
	private static final String GENDER = "gender";
	private static final String DO_BIRTH = "dateOfBirth";
	private static final String DO_HIRE = "dateOfHire";
	private static final String DO_LASTREVIEW = "dateOfLastReview";
	private static final String DO_RELEASE = "dateOfRelease";
	private static final int VALUES_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(SALES_PERSON_NAME, false, true));

		list.add(new Requirement(FILE_AS, true, true));
		list.add(new Requirement(JOB_TITLE, true, true));
		list.add(new Requirement(ADDRESS, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EXPENSE_ACCOUNT, true, true));
		list.add(new Requirement(E_MAIL, true, true));
		list.add(new Requirement(WEB_PAGE_ADDRESS, true, true));
		list.add(new Requirement(GENDER, true, true));
		list.add(new Requirement(DO_BIRTH, true, true));
		list.add(new Requirement(DO_HIRE, true, true));
		list.add(new Requirement(DO_LASTREVIEW, true, true));
		list.add(new Requirement(DO_RELEASE, true, true));
		list.add(new Requirement("memo", true, true));

	}

	@Override
	public Result run(Context context) {

		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		result = nameRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;

	}

	private Result nameRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(SALES_PERSON_NAME);
		String salesPersonName = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("orderNo")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			salesPersonName = order;
			req.setValue(salesPersonName);
		}

		if (selection == salesPersonName) {
			context.setAttribute(INPUT_ATTR, SALES_PERSON_NAME);
			return number(context, "Enter Sales Person Name", salesPersonName);
		}

		Record salesPersonNameRecord = new Record(salesPersonName);
		salesPersonNameRecord.add("Name", "Sales Person Name");
		salesPersonNameRecord.add("Value", salesPersonName);
		list.add(salesPersonNameRecord);
		return null;
	}

	private void completeProcess(Context context) {

		SalesPerson newSalesPerson = new SalesPerson();

		newSalesPerson.setFileAs((String) get(FILE_AS).getValue());

		newSalesPerson.setPhoneNo((String) get(PHONE).getValue());

		newSalesPerson.setFaxNo((String) get(FAX).getValue());

		create(newSalesPerson, context);
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		Result result = fileAsRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = jobTitleRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = addressRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, PHONE,
				"Enter your phone number");
		if (result != null) {
			return result;
		}

		result = expenseAmountRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = emailRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = webPageRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = genderRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_BIRTH,
				"Select ur Date of Birth", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_HIRE,
				"Select Hire date", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_LASTREVIEW,
				"Select last Review date", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_RELEASE,
				"Select your Release Date", selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, "memo",
				"Add a memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add(" Item is ready to create with following values.");
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result genderRequirement(Context context, ResultList list,
			Object selection) {
		Object genderObj = context.getSelection(PAYMENT_TERMS);
		Requirement genderReq = get("paymentTerms");
		Gender gender = (Gender) genderReq.getValue();

		if (selection == gender) {
			return genderSelected(context, gender);

		}
		if (genderObj != null) {
			gender = (Gender) genderObj;
			genderReq.setValue(gender);
		}

		Record paymentTermRecord = new Record(gender);
		paymentTermRecord.add("Name", "Gender");
		paymentTermRecord.add("Value", gender.getDeclaringClass());
		list.add(paymentTermRecord);
		return null;
	}

	private Result genderSelected(Context context, Gender gender2) {
		List<Gender> newGender = getGenders();
		Result result = context.makeResult();
		result.add("Select Gender");

		ResultList list = new ResultList(GENDER);
		int num = 0;
		for (Gender gender : newGender) {
			if (gender != gender2) {
				list.add(createGenderRecord(gender));
				num++;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Gender");
		result.add(commandList);
		return result;
	}

	private Record createGenderRecord(Gender gender2) {
		Record record = new Record(gender2);
		record.add("Name", gender2.name());
		record.add("Desc", "");
		return record;
	}

	private List<Gender> getGenders() {
		// TODO Auto-generated method stub
		return null;
	}

	private Result webPageRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(WEB_PAGE_ADDRESS);
		String webPageReq = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(WEB_PAGE_ADDRESS)) {
			String order = context.getSelection(TEXT);
			if (order == null) {
				order = context.getString();
			}
			webPageReq = order;
			req.setValue(webPageReq);
		}

		if (selection == webPageReq) {
			context.setAttribute(INPUT_ATTR, WEB_PAGE_ADDRESS);
			return number(context, "Web page address", webPageReq);
		}

		Record emailRecord = new Record(webPageReq);
		emailRecord.add("Name", "Web page address");
		emailRecord.add("Value", webPageReq);
		list.add(emailRecord);

		return null;
	}

	private Result emailRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(E_MAIL);
		String emailReq = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(E_MAIL)) {
			String order = context.getSelection(TEXT);
			if (order == null) {
				order = context.getString();
			}
			emailReq = order;
			req.setValue(emailReq);
		}

		if (selection == emailReq) {
			context.setAttribute(INPUT_ATTR, E_MAIL);
			return number(context, "Enter Email", emailReq);
		}

		Record emailRecord = new Record(emailReq);
		emailRecord.add("Name", "E- mail");
		emailRecord.add("Value", emailReq);
		list.add(emailRecord);

		return null;
	}

	private Result expenseAmountRequirement(Context context, ResultList list,
			Object selection) {

		Requirement expenseAccountReq = get(EXPENSE_ACCOUNT);
		Account expenseAccount = context.getSelection(EXPENSE_ACCOUNT);
		if (expenseAccount != null) {
			expenseAccountReq.setValue(expenseAccount);
		}
		if (!expenseAccountReq.isDone()) {
			return getExpenseAccountResult(context);
		}
		return null;
	}

	private Result getExpenseAccountResult(Context context) {
		Result result = context.makeResult();
		ResultList expenseAccountsList = new ResultList(EXPENSE_ACCOUNT);

		Object last = context.getLast(RequirementType.ACCOUNT);
		if (last != null) {
			expenseAccountsList.add(createAccountRecord((ClientAccount) last));
		}

		List<ClientAccount> expenseAccount = getAccounts();
		for (int i = 0; i < VALUES_TO_SHOW || i < expenseAccount.size(); i++) {
			ClientAccount expAccount = expenseAccount.get(i);
			if (expAccount != last) {
				expenseAccountsList
						.add(createAccountRecord((ClientAccount) expAccount));
			}
		}

		int size = expenseAccountsList.size();

		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Expense Account");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(expenseAccountsList);
		result.add(commandList);
		result.add("Select the Expense Account");

		return result;
	}

	private List<Account> getAccounts(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result addressRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(ADDRESS);
		ClientAddress address = (ClientAddress) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("billTo")) {
			ClientAddress input = context.getSelection(ADDRESS);
			if (input == null) {
				input = context.getAddress();
			}
			address = input;
			req.setValue(address);
		}

		if (selection == address) {
			context.setAttribute(INPUT_ATTR, ADDRESS);
			return address(context, "Address", ADDRESS, address);
		}

		Record addressRecord = new Record(address);
		addressRecord.add("Name", ADDRESS);
		addressRecord.add("Value", address.toString());
		list.add(addressRecord);
		return null;
	}

	private Result jobTitleRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(JOB_TITLE);
		String jobReq = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(JOB_TITLE)) {
			String order = context.getSelection(TEXT);
			if (order == null) {
				order = context.getString();
			}
			jobReq = order;
			req.setValue(jobReq);
		}

		if (selection == jobReq) {
			context.setAttribute(INPUT_ATTR, FILE_AS);
			return number(context, "Enter Job Title", jobReq);
		}

		Record jobTitleRecord = new Record(jobReq);
		jobTitleRecord.add("Name", "Job Title");
		jobTitleRecord.add("Value", jobReq);
		list.add(jobTitleRecord);
		return null;
	}

	private Result fileAsRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(FILE_AS);
		String fileAsReq = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FILE_AS)) {
			String order = context.getSelection(TEXT);
			if (order == null) {
				order = context.getString();
			}
			fileAsReq = order;
			req.setValue(fileAsReq);
		}

		if (selection == fileAsReq) {
			context.setAttribute(INPUT_ATTR, FILE_AS);
			return number(context, "Enter File As", fileAsReq);
		}

		Record fileAsRecord = new Record(fileAsReq);
		fileAsRecord.add("Name", "File As");
		fileAsRecord.add("Value", fileAsReq);
		list.add(fileAsRecord);
		return null;
	}

}
