package com.vimukti.accounter.text;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class TextRequestProcessor {

	public static final String SIGNUP_EMAIL = "signup@accounterlive.com";

	private static TextRequestProcessor processor;

	public static TextRequestProcessor getInstance() {
		if (processor == null) {
			processor = new TextRequestProcessor();
		}
		return processor;
	}

	public void process(Mail request) {
		// Check it sign up request
		if (SIGNUP_EMAIL.equals(request.getTo())) {
			signup(request);
			return;
		}

		// TODO AUTHENTICATE

		String textBody = request.getTextBody();

		ArrayList<ITextData> datas = TextCommandParser.parse(textBody);

		ITextResponse response = newResponse();

		CommandProcessor.getInstance().processCommands(datas, response);

	}

	private ITextResponse newResponse() {
		return new CommandResponse();
	}

	/**
	 * Signup User
	 * 
	 * @param request
	 */
	private void signup(Mail request) {
		String from = request.getFrom();
		String fromName = request.getFromName();

		Session session = HibernateUtil.getCurrentSession();

		// Create new Client
		Client client = new Client();
		client.setEmailId(from);
		String[] split = fromName.split(" ");
		client.setFirstName(split[0]);
		if (split.length > 1) {
			client.setLastName(split[1]);
		}
		client.setFullName(fromName);
		client.setActive(true);

		ClientSubscription subscription = new ClientSubscription();
		subscription.setCreatedDate(new Date());

		ClientSubscription clientSubscription = new ClientSubscription();
		clientSubscription.setCreatedDate(new Date());
		clientSubscription.setLastModified(new Date());
		if (!ServerConfiguration.isDesktopApp()) {
			clientSubscription.setSubscription(Subscription
					.getInstance(Subscription.FREE_CLIENT));
		} else {
			clientSubscription.setSubscription(Subscription
					.getInstance(Subscription.PREMIUM_USER));
		}
		session.saveOrUpdate(clientSubscription);

		client.setClientSubscription(clientSubscription);

	}
}
