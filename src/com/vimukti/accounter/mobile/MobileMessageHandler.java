/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileMessageHandler {

	private static final String AUTHENTICATE_COMMAND = "authenticate";
	private static final String SELECT_COMPANY_COMMAND = "selectCompany";
	private Map<String, MobileSession> sessions = new HashMap<String, MobileSession>();

	/**
	 * @param message
	 * @return
	 * @throws AccounterMobileException
	 */
	public String messageReceived(String userId, String message,
			AdaptorType adaptorType) throws AccounterMobileException {
		Session openSession = HibernateUtil.openSession();
		try {

			MobileSession session = sessions.get(userId);

			if (session == null || session.isExpired()) {
				session = new MobileSession();
				sessions.put(userId, session);
				processWithOutAuthenticationForTest(session, openSession,
						userId);
			}
			MobileAdaptor adoptor = getAdaptor(adaptorType);
			session.refresh();

			// if (!session.isAuthenticated()
			// && !message.equals(AUTHENTICATE_COMMAND)) {
			// message = AUTHENTICATE_COMMAND;
			// } else if (session.getCompany() != null
			// && !message.equals(SELECT_COMPANY_COMMAND)) {
			// message = SELECT_COMPANY_COMMAND;
			// }

			UserMessage preProcess = adoptor.preProcess(session, message);
			Result result = getCommandProcessor().handleMessage(session,
					preProcess);
			String reply = adoptor.postProcess(result);
			session.await();

			return reply;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(e);
		} finally {
			if (openSession.isOpen()) {
				openSession.close();
			}
		}
	}

	private void processWithOutAuthenticationForTest(MobileSession session,
			Session hibernateSession, String userId) {
		ServerLocal.set(Locale.ENGLISH);
		Query namedQuery = hibernateSession
				.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", userId);
		Client client = (Client) namedQuery.uniqueResult();
		session.setCompanyID(1l);
		session.setClientID(client.getID());
		session.sethibernateSession(hibernateSession);
		User user = session.getUser();
		AccounterThreadLocal.set(user);
	}

	/**
	 * Returns the Adaptor of given Type
	 * 
	 * @param chat
	 * @return
	 */
	private MobileAdaptor getAdaptor(AdaptorType type) {
		if (type.equals(AdaptorType.CHAT)) {
			return MobileChatAdaptor.INSTANCE;
		} else {
			return MobileApplicationAdaptor.INSTANCE;
		}
	}

	/**
	 * @param userId
	 * @param session
	 * @return
	 */
	private User getUserById(String userId, Session session) {
		Company company = (Company) session.get(Company.class, 1l);
		User user = (User) session.getNamedQuery("user.by.emailid")
				.setString("emailID", userId).setEntity("company", company)
				.uniqueResult();
		return user;
	}

	/**
	 * Returns the Command Processor
	 */
	private CommandProcessor getCommandProcessor() {
		return CommandProcessor.INSTANCE;
	}
}
