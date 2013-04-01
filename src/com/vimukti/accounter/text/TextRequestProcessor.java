package com.vimukti.accounter.text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.EMailJob;
import com.vimukti.accounter.mail.EMailMessage;
import com.vimukti.accounter.mail.EmailManager;
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

	@SuppressWarnings("rawtypes")
	public void process(Mail request) throws IOException {
		// Check it sign up request
		if (SIGNUP_EMAIL.equals(request.getTo())) {
			signup(request);
			return;
		}

		// TODO AUTHENTICATE
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.users.by.email").setParameter(
				"email", request.getFrom());
		List list = query.list();
		if (list.isEmpty()) {
			// No companies yet, Need to create a new company before doing this
			// operation
			sendResponse(request,
					"You don't have companies yet, please create a company");
			return;
		}

		// Set User to Thread
		User user = (User) list.get(0);
		AccounterThreadLocal.set(user);

		String textBody = request.getTextBody();

		ArrayList<ITextData> datas = TextCommandParser.parse(textBody);

		CommandsQueue queue = new CommandsQueue(datas);

		ArrayList<CommandResponseImpl> responses = CommandProcessor
				.getInstance().processCommands(queue);

		sendResponse(request, responses);

	}

	private void sendResponse(Mail request,
			ArrayList<CommandResponseImpl> responses) {
		StringBuffer buff = new StringBuffer();
		ArrayList<File> files = new ArrayList<File>();
		for (CommandResponseImpl response : responses) {

			// Add input commands first
			ArrayList<ITextData> cData = response.getData();
			for (ITextData data : cData) {
				buff.append(data.toString());
				buff.append("\n");
			}

			// Then It's response
			// Add Errors first
			buff.append("\t");
			buff.append("Errors : ");
			for (String error : response.getErrors()) {
				buff.append("\n\t\t");
				buff.append(error);
			}

			// Add then messages
			buff.append("\n\t");
			buff.append("Messages : ");
			for (String msg : response.getMessages()) {
				buff.append("\n\t\t");
				buff.append(msg);
			}

			// Finally files
			for (String file : response.getFiles()) {
				files.add(new File(file));
			}

			// Finaly new line
			buff.append("\n\n");
		}
		sendResponse(request, buff.toString(), files.toArray(new File[] {}));
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
		session.saveOrUpdate(client);

		sendResponse(request, "Signp Success");

	}

	private void sendResponse(Mail request, String message) {
		sendResponse(request, message, (File[]) null);
	}

	private void sendResponse(Mail request, String message, File... files) {
		EMailMessage msg = new EMailMessage();
		msg.setFrom(request.getFrom());
		msg.setRecepeant(request.getFrom());
		msg.isPlain = true;

		// Set content
		msg.setContent(message);
		if (files != null) {
			// Add Attachments
			for (File file : files) {
				msg.setAttachment(file);
			}
		}

		ArrayList<EMailMessage> messages = new ArrayList<EMailMessage>();
		messages.add(msg);

		EMailJob job = new EMailJob();
		job.setMessages(messages);

		EmailManager.getInstance().addJob(job);
	}
}
