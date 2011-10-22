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
	 * @param commandSender
	 * @param message2
	 * @return
	 * @throws AccounterMobileException
	 */
	public String messageReceived(String networkId, String userId,
			String message, AdaptorType adaptorType, int networkType,
			CommandSender commandSender) throws AccounterMobileException {
		Session openSession = HibernateUtil.openSession();
		try {
			MobileSession session = sessions.get(networkId);

			if (session == null || session.isExpired()) {
				session = new MobileSession();
				sessions.put(networkId, session);
			}
			ServerLocal.set(Locale.ENGLISH);
			session.sethibernateSession(openSession);
			session.reloadObjects();

			MobileAdaptor adoptor = getAdaptor(adaptorType);

			UserMessage preProcess = adoptor.preProcess(session, message,
					userId, networkId, networkType);
			Result result = getCommandProcessor().handleMessage(session,
					preProcess);
			if (preProcess.getCommand().isDone()) {
				session.refreshCurrentCommand();
				Command currentCommand = session.getCurrentCommand();
				if (currentCommand != null) {
					UserMessage lastMessage = session.getLastMessage();
					lastMessage.setResult(lastMessage.getLastResult());
					reloadCommand(networkId, userId, null, adaptorType,
							networkType, commandSender);
				}
			}

			// To check if there is only on command
			String nextCommand = result.getNextCommand();
			if (nextCommand != null) {
				reloadCommand(networkId, userId, nextCommand, adaptorType,
						networkType, commandSender);
			}
			String reply = adoptor.postProcess(result);
			session.await();
			commandSender.onReply(reply);
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

	protected void reloadCommand(final String networkId, final String userId,
			final String message, final AdaptorType adaptorType,
			final int networkType, final CommandSender commandSender) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					messageReceived(networkId, userId, message, adaptorType,
							networkType, commandSender);
				} catch (AccounterMobileException e) {
					e.printStackTrace();
				}
			}
		}).start();
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
