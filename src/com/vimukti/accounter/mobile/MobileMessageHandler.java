/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.IMActivation;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

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
	 * @param message2
	 * @return
	 * @throws AccounterMobileException
	 */
	public String messageReceived(String networkId, String userId,
			String message, AdaptorType adaptorType, int networkType)
			throws AccounterMobileException {
		Session openSession = HibernateUtil.openSession();
		try {

			// /Checking authentication
			IMUser imUser = getIMUser(networkId, networkType);
			if (imUser == null) {
				Client client = getClient(userId);
				if (client == null) {
					IMActivation activation = getImActivationByTocken(message);
					if (activation == null) {
						List<IMActivation> activationList = getImActivationByNetworkId(networkId);
						if (activationList == null
								|| activationList.size() == 0) {
							client = getClient(message);
							if (client != null) {
								sendActivationMail(networkId, message);
								return "Activation code has sent to your email '"
										+ message + "'.";
							} else {
								return "Enter valid Accounter emailId. or Press 'S' for signup";
							}
						} else {
							return "Wrong Activation code";
						}
					} else {
						createIMUser(networkType, activation.getNetworkId(),
								getClient(activation.getEmailId()));
						return "Activation Success";
					}
				} else {
					imUser = createIMUser(networkType, networkId, client);
				}
			}
			userId = imUser.getClient().getEmailId();
			MobileSession session = sessions.get(userId);

			if (session == null || session.isExpired()) {
				session = new MobileSession();
				sessions.put(userId, session);
				ServerLocal.set(Locale.ENGLISH);
				session.setClient(imUser.getClient());
				session.sethibernateSession(openSession);
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

	private Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	private IMUser createIMUser(int networkType, String networkId, Client client) {
		Session currentSession = HibernateUtil.getCurrentSession();
		IMUser imUser = new IMUser();
		imUser.setClient(client);
		imUser.setNetworkId(networkId);
		imUser.setNetworkType(networkType);
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(imUser);
		List<IMActivation> imActivationByNetworkId = getImActivationByNetworkId(networkId);
		for (IMActivation activation : imActivationByNetworkId) {
			currentSession.delete(activation);
		}
		beginTransaction.commit();
		return imUser;
	}

	private void sendActivationMail(String networkId, String emailId) {
		String activationCode = SecureUtils.createID(16);
		System.out.println("NetWorkID: " + networkId);
		System.out.println("EmailId: " + emailId);
		System.out.println("Activation Code: " + activationCode);

		UsersMailSendar.sendMobileActivationMail(activationCode, emailId);

		Session currentSession = HibernateUtil.getCurrentSession();
		IMActivation activation = new IMActivation();
		activation.setEmailId(emailId);
		activation.setNetworkId(networkId);
		activation.setTocken(activationCode);
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(activation);
		beginTransaction.commit();
	}

	private List<IMActivation> getImActivationByNetworkId(String networkId) {
		Session session = HibernateUtil.getCurrentSession();
		List<IMActivation> activationList = (List<IMActivation>) session
				.getNamedQuery("activation.by.networkId")
				.setString("networkId", networkId).list();
		return activationList;
	}

	private IMActivation getImActivationByTocken(String tocken) {
		Session session = HibernateUtil.getCurrentSession();
		IMActivation activation = (IMActivation) session
				.getNamedQuery("activation.by.tocken")
				.setString("tocken", tocken).uniqueResult();
		return activation;
	}

	private IMUser getIMUser(String networkId, int networkType) {
		Session session = HibernateUtil.getCurrentSession();
		IMUser user = (IMUser) session.getNamedQuery("imuser.by.networkId")
				.setString("networkId", networkId)
				.setInteger("networkType", networkType).uniqueResult();
		return user;
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
	private User getUserById(String userId) {
		Session session = HibernateUtil.getCurrentSession();
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
