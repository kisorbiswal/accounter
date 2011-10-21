package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

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
		get(SUBSCRIBED_NEWSLETTER).setDefaultValue(true);

		Result makeResult = context.makeResult();
		makeResult.add("Your account is ready to create with below values :");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);
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

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish");
		actions.add(finish);
		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				completeProcess(context);
				return new Result(
						"Your account was created successfully. we hav sent an activation code to your mail. please enter activation code.");
			}
		}
		return makeResult;
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
		Client client = new Client();
		client.setActive(false);
		client.setUsers(new HashSet<User>());
		String emailId = get(EMAIL).getValue();
		client.setEmailId(emailId);

		String firstName = get(FIRST_NAME).getValue();
		client.setFirstName(firstName);

		String lastName = get(LAST_NAME).getValue();
		client.setLastName(lastName);

		client.setFullName(Global.get().messages()
				.fullName(firstName, lastName));

		String password = SecureUtils.createID(16);
		sendPasswordMail(password, emailId);
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password));
		client.setPassword(passwordWithHash);

		String phoneNumber = get(PHONE).getValue();
		client.setPhoneNo(phoneNumber);

		String country = get(COUNTRY).getValue();
		client.setCountry(country);

		Boolean isSubscribedToNewsLetter = get(SUBSCRIBED_NEWSLETTER)
				.getValue();
		client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);

		saveEntry(client, context);

		UsersMailSendar.sendActivationMail(createActivation(emailId, context),
				client);

		IMUser imUser = new IMUser();
		imUser.setClient(client);
		imUser.setNetworkId(context.getNetworkId());
		imUser.setNetworkType(context.getNetworkType());
		saveEntry(imUser, context);

	}

	private void sendPasswordMail(String token, String emailId) {
		System.out.println("Password : " + token);
		// TODO
	}

	protected String createActivation(String emailID, Context context) {
		String token = SecureUtils.createID(16);
		Activation activation = new Activation();
		activation.setEmailId(emailID);
		activation.setToken(token);
		activation.setSignUpDate(new Date());
		saveEntry(activation, context);
		return token;
	}

	private void saveEntry(Object object, Context context) {
		Transaction beginTransaction = context.getHibernateSession()
				.beginTransaction();
		context.getHibernateSession().save(object);
		beginTransaction.commit();
	}
}
