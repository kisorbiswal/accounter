/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Session;

import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileMessageHandler {

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
		Session openSession = null;
		Result previous = null;
		try {
			while (true) {
				if (openSession != null && openSession.isOpen()) {
					openSession.close();
				}
				openSession = HibernateUtil.openSession();
				MobileSession session = sessions.get(networkId);

				if (session == null || session.isExpired()) {
					session = new MobileSession();
					sessions.put(networkId, session);
					ServerLocal.set(Locale.ENGLISH);
				}
				session.sethibernateSession(openSession);
				session.reloadObjects();

				MobileAdaptor adoptor = getAdaptor(adaptorType);

				UserMessage preProcess = adoptor.preProcess(session, message,
						userId, networkId, networkType);
				int i = 0;
				Result result = getCommandProcessor().handleMessage(session,
						preProcess);
				if (previous != null) {
					List<Object> resultParts = previous.getResultParts();
					result.addAll(0, resultParts);
				}
				if (preProcess.getCommand().isDone()) {
					session.refreshCurrentCommand();
					Command currentCommand = session.getCurrentCommand();
					if (currentCommand != null) {
						UserMessage lastMessage = session.getLastMessage();
						lastMessage.setResult(lastMessage.getLastResult());
						message = null;
						continue;
					}
				}
				if (result instanceof PatternResult) {
					if (result.resultParts.size() == 1) {
						CommandList object = (CommandList) result.resultParts
								.get(0);
						if (object.size() == 1) {
							message = object.get(0);
							continue;
						}
					}
				}
				String reply = adoptor.postProcess(result);
				session.await();
				return reply;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(e);
		} finally {
			if (openSession.isOpen()) {
				openSession.close();
			}
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
	 * Returns the Command Processor
	 */
	private CommandProcessor getCommandProcessor() {
		return CommandProcessor.INSTANCE;
	}
}
