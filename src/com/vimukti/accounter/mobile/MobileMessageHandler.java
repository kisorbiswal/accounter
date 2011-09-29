/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileMessageHandler {

	private static final String AUTHENTICATE_COMMAND = "authenticate";
	private Map<String, MobileSession> sessions = new HashMap<String, MobileSession>();

	/**
	 * @param message
	 * @return
	 * @throws AccounterMobileException
	 */
	public String messageReceived(String userId, String message,
			AdaptorType adaptorType) throws AccounterMobileException {
		try {
			MobileSession session = sessions.get(userId);
			if (session == null || session.isExpired()) {
				session = new MobileSession(getUserById(userId));
				sessions.put(userId, session);
			}
			MobileAdaptor adoptor = getAdaptor(adaptorType);
			session.refresh();

			// if (!session.isAuthenticated()
			// && !message.equals(AUTHENTICATE_COMMAND)) {
			// message = AUTHENTICATE_COMMAND;
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
		}
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
	 * @return
	 */
	private User getUserById(String userId) {
		User user = new User();
		user.setEmail(userId);
		return user;
	}

	/**
	 * Returns the Command Processor
	 */
	private CommandProcessor getCommandProcessor() {
		return CommandProcessor.INSTANCE;
	}
}
