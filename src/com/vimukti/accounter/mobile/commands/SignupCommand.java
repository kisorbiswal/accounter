package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

public class SignupCommand extends NewCommand {
	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String SUBSCRIBED_NEWSLETTER = "subscribed";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	private static final String COUNTRY = "country";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(FIRST_NAME, "Enter First Name",
				"First Name", false, true));

		list.add(new NameRequirement(LAST_NAME, "Enter Last Name", "Last Name",
				false, true));

		list.add(new NameRequirement(EMAIL, "Enter Email", "Emial", false, true));

		list.add(new NumberRequirement(PHONE, "Eneter Phone number",
				"Phone No", false, true));

		list.add(new CountryRequirement(COUNTRY, "Enter Country Name",
				"Country", false, true, null));

		list.add(new BooleanRequirement(SUBSCRIBED_NEWSLETTER, true) {

			@Override
			protected String getFalseString() {
				return "Not subscribed";
			}

			@Override
			protected String getTrueString() {
				return "Subscribed";
			}

		});
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

	@Override
	protected String getDetailsMessage() {
		return "Your account is ready to be create with below details :";
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	protected Result onCompleteProcess(Context context) {
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
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return "Your account was created successfully. We have sent an activation code to your mail";
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}
}
