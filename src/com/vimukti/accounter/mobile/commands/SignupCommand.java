package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class SignupCommand extends AbstractCommand {
	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	private static final String COUNTRY = "country";
	private static final String SUBSCRIBED_NEWSLETTER = "subscribed";
	private static final String COUNTRIES = "countries";
	private static final int COUNTRIES_TO_SHOW = 10;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(FIRST_NAME, false, true));
		list.add(new Requirement(LAST_NAME, false, true));
		list.add(new Requirement(EMAIL, false, true));
		list.add(new Requirement(PHONE, false, true));
		list.add(new Requirement(COUNTRY, false, true));
		list.add(new Requirement(SUBSCRIBED_NEWSLETTER, true, true));
	}

	@Override
	public Result run(Context context) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			}
		}
		Result makeResult = context.makeResult();
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);
		makeResult.add("Your account is ready to create with below values :");
		Result result = nameRequirement(context, list, FIRST_NAME,
				getConstants().firstName(),
				getMessages().pleaseEnter(getConstants().firstName()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, LAST_NAME, getConstants()
				.lastName(),
				getMessages().pleaseEnter(getConstants().lastName()));
		if (result != null) {
			return result;
		}

		result = emailRequirement(context, list, EMAIL, getConstants().email(),
				getMessages().pleaseEnter(getConstants().email()));
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, PHONE, getConstants()
				.phoneNumber(),
				getMessages().pleaseEnter(getConstants().phoneNumber()));
		if (result != null) {
			return result;
		}

		result = countryListRequiremnt(context, list, COUNTRY, getConstants()
				.country(), getMessages()
				.pleaseSelect(getConstants().country()));

		if (result != null) {
			return result;
		}

		booleanOptionalRequirement(context, context.getSelection("values"),
				list, SUBSCRIBED_NEWSLETTER, "Subscribed", "Not subscribed");

		Record inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		completeProcess(context);
		result = new Result("Your account was created successfully");
		return result;
	}

	private Result countryListRequiremnt(Context context, ResultList list,
			String requirementName, String name, String displayingString) {
		Requirement countryReq = get(requirementName);
		String country = context.getSelection(COUNTRIES);
		if (country != null) {
			countryReq.setValue(country);
		}

		String value = countryReq.getValue();
		Object selection = context.getSelection("values");
		if (!countryReq.isDone() || (value == selection)) {
			return countries(context);
		}

		Record supplierRecord = new Record(value);
		supplierRecord.add("", name);
		supplierRecord.add("", value);
		list.add(supplierRecord);

		return null;
	}

	private Result countries(Context context) {
		Result result = context.makeResult();

		ResultList countriesList = new ResultList(COUNTRIES);

		List<String> skipCountries = new ArrayList<String>();
		List<String> countries = getCountries();

		ResultList actions = new ResultList(ACTIONS);
		ActionNames selection = context.getSelection("actions");

		List<String> pagination = pagination(context, selection, actions,
				countries, skipCountries, COUNTRIES_TO_SHOW);

		for (String country : pagination) {
			Record countryRecord = new Record(country);
			countryRecord.add("", "Country Name");
			countryRecord.add("", country);
			countriesList.add(countryRecord);
		}

		int size = countriesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Country");
		}
		result.add(message.toString());
		result.add(countriesList);
		result.add(actions);
		return result;
	}

	private List<String> getCountries() {
		List<String> countries = new ArrayList<String>();
		countries.add("United Kingdom");
		countries.add("US");
		countries.add("India");
		return countries;
	}

	private Result emailRequirement(Context context, ResultList list,
			String reqName, String name, String displayString) {
		Requirement requirement = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(reqName)) {
			input = context.getString();
			if (isValidEmail(input)) {
				requirement.setValue(input);
				context.setAttribute(INPUT_ATTR, "");
			}
		}
		if (!requirement.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, null);
		}

		Object selection = context.getSelection("values");
		String emailId = requirement.getValue();
		if (selection != null && selection.equals(reqName)) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, emailId);
		}

		Record nameRecord = new Record(reqName);
		nameRecord.add("", name);
		nameRecord.add("", emailId);
		list.add(nameRecord);
		return null;
	}

	private void completeProcess(Context context) {
		// TODO Auto-generated method stub

	}
}
