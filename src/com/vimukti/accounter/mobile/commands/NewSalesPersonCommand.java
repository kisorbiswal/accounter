package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;

public class NewSalesPersonCommand extends AbstractTransactionCommand {

	private static final String SALES_PERSON_NAME = "salesPersonName";
	private static final String FILE_AS = "fileAs";
	private static final String JOB_TITLE = "jobTitle";
	private static final String FAX = "fax";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String E_MAIL = "eMail";
	private static final String WEB_PAGE_ADDRESS = "webPageAddress";
	private static final String GENDER = "gender";
	private static final String DO_BIRTH = "dateOfBirth";
	private static final String DO_HIRE = "dateOfHire";
	private static final String DO_LASTREVIEW = "dateOfLastReview";
	private static final String DO_RELEASE = "dateOfRelease";

	@Override
	public String getId() {
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
		list.add(new Requirement(MEMO, true, true));

	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult.add(getMessages()
				.readyToCreate(getConstants().salesPerson()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, SALES_PERSON_NAME,
				getConstants().salesPersonName(),
				getMessages().pleaseEnter(getConstants().salesPersonName()));
		if (result != null) {
			return result;
		}
		setDefaultValues();

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return completeProcess(context);

	}

	private void setDefaultValues() {
		String name = get(SALES_PERSON_NAME).getValue();
		if (get(FILE_AS).getValue() == null)
			get(FILE_AS).setDefaultValue(name);
		get(JOB_TITLE).setDefaultValue("");
		get(ADDRESS).setDefaultValue(new ClientAddress());
		get(PHONE).setDefaultValue("");
		get(FAX).setDefaultValue("");
		get(EXPENSE_ACCOUNT).setDefaultValue(null);
		get(E_MAIL).setDefaultValue("");
		get(WEB_PAGE_ADDRESS).setDefaultValue("");
		get(GENDER).setDefaultValue(getConstants().unspecified());
		get(DO_BIRTH).setDefaultValue(new ClientFinanceDate());
		get(DO_HIRE).setDefaultValue(new ClientFinanceDate());
		get(DO_LASTREVIEW).setDefaultValue(new ClientFinanceDate());
		get(DO_RELEASE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
	}

	private Result completeProcess(Context context) {

		ClientSalesPerson salesPerson = new ClientSalesPerson();

		String name = get(SALES_PERSON_NAME).getValue();
		salesPerson.setFirstName(name);

		String fileAs = get(FILE_AS).getValue();
		salesPerson.setFileAs(fileAs);

		String jobTitle = get(JOB_TITLE).getValue();
		salesPerson.setJobTitle(jobTitle);

		ClientAddress address = get(ADDRESS).getValue();
		salesPerson.setAddress(address);

		String phone = get(PHONE).getValue();
		salesPerson.setPhoneNo(phone);

		String fax = get(FAX).getValue();
		salesPerson.setFaxNo(fax);

		ClientAccount value = get(EXPENSE_ACCOUNT).getValue();
		salesPerson.setExpenseAccount(value != null ? value.getID() : 0);

		String email = get(E_MAIL).getValue();
		salesPerson.setFaxNo(email);

		String webPage = get(WEB_PAGE_ADDRESS).getValue();
		salesPerson.setFaxNo(webPage);

		String gender = get(GENDER).getValue();
		salesPerson.setGender(gender);

		ClientFinanceDate do_birth = get(DO_BIRTH).getValue();
		salesPerson.setDateOfBirth(do_birth);

		ClientFinanceDate do_hire = get(DO_HIRE).getValue();
		salesPerson.setDateOfHire(do_hire);

		ClientFinanceDate do_lastreview = get(DO_LASTREVIEW).getValue();
		salesPerson.setDateOfLastReview(do_lastreview);

		ClientFinanceDate do_release = get(DO_RELEASE).getValue();
		salesPerson.setDateOfRelease(do_release);

		String memo = get(MEMO).getValue();
		salesPerson.setMemo(memo);

		create(salesPerson, context);

		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().salesPerson()));
		return result;
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

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

		Result result = stringOptionalRequirement(context, list, selection,
				FILE_AS, getConstants().fileAs(),
				getMessages().pleaseEnter(getConstants().fileAs()));
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, JOB_TITLE,
				getConstants().jobTitle(),
				getMessages().pleaseEnter(getConstants().jobTitle()));
		if (result != null) {
			return result;
		}

		result = addressOptionalRequirement(context, list, selection, ADDRESS,
				getConstants().address(),
				getMessages().pleaseEnter(getConstants().address()));
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, PHONE,
				getConstants().phone(),
				getMessages().pleaseEnter(getConstants().phone()));
		if (result != null) {
			return result;
		}

		result = accountsOptionalRequirement(context, list, selection,
				EXPENSE_ACCOUNT);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, E_MAIL,
				getConstants().email(),
				getMessages().pleaseEnter(getConstants().email()));
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection,
				WEB_PAGE_ADDRESS, getConstants().webSite(), getMessages()
						.pleaseEnter(getConstants().webSite()));
		if (result != null) {
			return result;
		}

		result = genderRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_BIRTH,
				getConstants().dateofBirth(),
				getMessages().pleaseSelect(getConstants().dateofBirth()),
				selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_HIRE, getConstants()
				.date(),
				getMessages().selectDateOfHire(getConstants().salesPerson()),
				selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_LASTREVIEW,
				getConstants().dateofLastReview(),
				getMessages().pleaseSelect(getConstants().dateofLastReview()),
				selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DO_RELEASE,
				getConstants().dateofRelease(),
				getMessages().pleaseSelect(getConstants().dateofRelease()),
				selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().addMemo(),
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().salesPerson()));
		actions.add(finish);

		return makeResult;
	}

	private Result genderRequirement(Context context, ResultList list,
			Object selection) {
		Object salesPersonObj = context.getSelection(GENDER);
		Requirement salesPersonReq = get(GENDER);
		String gender = (String) salesPersonReq.getValue();
		if (salesPersonObj != null) {
			gender = (String) salesPersonObj;
			salesPersonReq.setValue(gender);
		}
		if (selection != null)
			if (selection == GENDER) {
				context.setAttribute(INPUT_ATTR, GENDER);
				return genderSelected(context, gender);

			}

		Record salesPersonRecord = new Record(GENDER);
		salesPersonRecord.add("", getConstants().gender());
		salesPersonRecord.add("", gender);
		list.add(salesPersonRecord);
		return null;
	}

	private Result genderSelected(Context context, String gender) {
		List<String> newGender = getGenders();
		Result result = context.makeResult();
		result.add(getMessages().pleaseSelect(getConstants().gender()));

		ResultList list = new ResultList(GENDER);
		for (String gende : newGender) {
			// if (!gende.equals(gender)) {
			list.add(createGenderRecord(gende));
			// }
		}
		result.add(list);

		return result;
	}

	private Record createGenderRecord(String gender) {
		Record record = new Record(gender);
		record.add("Name", gender);
		record.add("Desc", "");
		return record;
	}

	private List<String> getGenders() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getConstants().unspecified());
		list.add(getConstants().male());
		list.add(getConstants().female());
		return list;
	}

}
