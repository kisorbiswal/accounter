package com.vimukti.accounter.text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.EMailJob;
import com.vimukti.accounter.mail.EMailMessage;
import com.vimukti.accounter.mail.EMailSenderAccount;
import com.vimukti.accounter.mail.EmailManager;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class TextRequestProcessor {

	private Logger logger = Logger.getLogger(TextRequestProcessor.class);

	public static final String EMAIL_DOMAIL = "email.accounterlive.com";

	public static final String SIGNUP_EMAIL = "signup@email.accounterlive.com";

	private static TextRequestProcessor processor;

	public static TextRequestProcessor getInstance() {
		if (processor == null) {
			processor = new TextRequestProcessor();
		}
		return processor;
	}

	public void process(Mail request) {
		try {
			logger.info("Got request " + request.shortForm());
			executeRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
			sendResponse(request,
					"Internal Error occured while processing your request");
		}

	}

	@SuppressWarnings("rawtypes")
	private void executeRequest(Mail request) throws IOException,
			AccounterException {
		logger.info("Processing request");
		String to = request.getTo();
		// Check it sign up request
		if (SIGNUP_EMAIL.equals(to)) {
			signup(request);
			return;
		}

		Session session = HibernateUtil.getCurrentSession();

		// Check Client Existance
		Criteria criteria = session.createCriteria(Client.class);
		criteria.add(Restrictions.eq("uniqueId", to));
		Client client = (Client) criteria.uniqueResult();
		if (client == null) {
			logger.info("Don't have clients with unique email " + to);
			sendResponse(request,
					"You are not yet signed up. please send an email to "
							+ SIGNUP_EMAIL + " to signup");
			return;
		}

		logger.info("Getting companies for user");
		Query query = session.getNamedQuery("get.users.by.unique.email")
				.setParameter("email", to);
		List list = query.list();
		if (list.isEmpty()) {
			logger.info("Don't have comapnies yet for this user");
			// No companies yet, Need to create a new company before doing this
			// operation
			sendResponse(request,
					"You don't have companies yet, please create a company");
			return;
		}

		// Set User to Thread
		User user = (User) list.get(0);
		AccounterThreadLocal.set(user);

		// Set Company Preferences to thread local
		ClientCompanyPreferences preferences = new FinanceTool()
				.getCompanyManager().getClientCompanyPreferences(
						user.getCompany());
		CompanyPreferenceThreadLocal.set(preferences);

		// Server Global
		Global.set(new ServerGlobal());

		// Text body
		String textBody = request.getTextBody();

		logger.info("Parsing email body");
		ArrayList<ITextData> datas = TextCommandParser.parse(textBody);

		CommandsQueue queue = new CommandsQueue(datas);

		logger.info("Processing command");
		ArrayList<CommandResponseImpl> responses = CommandProcessor
				.getInstance().processCommands(queue);

		logger.info("Completed processing commands!");
		sendResponse(request, responses);
	}

	private void sendResponse(Mail request,
			ArrayList<CommandResponseImpl> responses) {
		logger.info("Making response to send email");
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
			ArrayList<String> errors = response.getErrors();

			// Add Errors first
			buff.append("\t");
			buff.append("Errors : ");
			if (errors.isEmpty()) {
				buff.append("No Errors");
			}
			for (String error : errors) {
				buff.append("\n\t\t");
				buff.append(error);
			}

			ArrayList<String> messages = response.getMessages();
			// Add then messages
			buff.append("\n\t");
			buff.append("Messages : ");
			if (messages.isEmpty()) {
				buff.append("No Messages");
			}
			for (String msg : messages) {
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
		logger.info("Creating new client to signup");
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

		String random = UUID.randomUUID().toString();
		random = random.replace("-", "");
		client.setUniqueId(random + "@" + EMAIL_DOMAIL);
		logger.info("Generated unique emailId " + client.getUniqueId());

		logger.info("Creating subscription");
		ClientSubscription clientSubscription = new ClientSubscription();
		clientSubscription.setCreatedDate(new Date());
		clientSubscription.setLastModified(new Date());
		clientSubscription.setSubscription(Subscription
				.getInstance(Subscription.FREE_CLIENT));
		session.saveOrUpdate(clientSubscription);

		client.setClientSubscription(clientSubscription);
		session.saveOrUpdate(client);

		sendResponse(request, "Signp Success");
	}

	private void sendResponse(Mail request, String message) {
		sendResponse(request, message, (File[]) null);
	}

	private void sendResponse(Mail request, String message, File... files) {
		logger.info("Sending response email for " + request.shortForm());
		EMailMessage msg = new EMailMessage();
		msg.setSubject(request.getSubject() + " Response");
		// msg.setFrom(request.getFrom());
		String replyTo = request.getReplyTo();
		if (replyTo == null || replyTo.isEmpty()) {
			replyTo = request.getFrom();
		}
		msg.setRecepeant(replyTo);
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

		// TODO HAS TO REMOVE THIS
		EMailSenderAccount sender = sampleSendeAccount();
		job.setSender(sender);

		logger.info("Adding email job to Queue");
		EmailManager.getInstance().addJob(job);
	}

	private EMailSenderAccount sampleSendeAccount() {
		EMailSenderAccount sender = new EMailSenderAccount();
		sender.setOutGoingMailServer("smtp.gmail.com");
		sender.setPortNumber(587);
		sender.setProtocol("smtp");
		sender.setEnableTls(true);
		sender.setSslAutheticationRequired(true);
		sender.setSmtpAuthentication(true);
		sender.setSenderEmailID("***REMOVED***");
		sender.setAccountName("Accounter Support");
		sender.setSenderPassword("vimukti***REMOVED***");
		return sender;
	}
}
