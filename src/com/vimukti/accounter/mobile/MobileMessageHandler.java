/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;
import com.vimukti.accounter.mobile.UserMessage.Type;
import com.vimukti.accounter.mobile.commands.AuthenticationCommand;
import com.vimukti.accounter.mobile.commands.SelectCompanyCommand;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileMessageHandler extends Thread {
	private static MobileMessageHandler instance;
	private Map<String, MobileSession> sessions = new HashMap<String, MobileSession>();
	private LinkedBlockingQueue<MobileChannelContext> queue = new LinkedBlockingQueue<MobileChannelContext>();

	private MobileMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		while (true) {
			try {
				MobileChannelContext take = queue.take();
				messageReceived(take);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void messageReceived(MobileChannelContext context) {
		if (context.getMessage().isEmpty()) {
			return;
		}
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		String processMessage;
		try {
			processMessage = processMessage(context.getNetworkId(),
					context.getMessage(), context.getAdaptorType(),
					context.getNetworkType(), null, context);
		} catch (AccounterMobileException e) {
			e.printStackTrace();
			processMessage = "Exception: " + e.getMessage();
		}

		if (context.getNetworkType() == AccounterChatServer.NETWORK_TYPE_GTALK) {
			sessions.get(context.getNetworkId()).await(this,
					context.getNetworkId());
		}
		context.send(processMessage);
	}

	private String processMessage(String networkId, String message,
			AdaptorType adaptorType, int networkType, Result oldResult,
			MobileChannelContext context) throws AccounterMobileException {
		Session openSession = HibernateUtil.openSession();
		try {
			MobileSession session = sessions.get(networkId);
			if (session == null || session.isExpired()) {
				sessions.remove(networkId);
				if (networkType != AccounterChatServer.NETWORK_TYPE_MOBILE) {
					message = networkId;
				}
				String[] split = message.split(" ");
				String cookie = split.length > 0 ? split[0] : "";
				String language = split.length > 1 ? split[split.length - 1]
						: "";
				message = cookie;
				session = sessions.get(cookie);
				if (session != null && !session.isExpired()) {
					session.setLanguage(language);
					networkId = cookie;
					context.changeNetworkId(cookie);
					return session.getLastReply();
				} else {
					session = new MobileSession();
					sessions.put(networkId, session);
					session.setLanguage(language);
				}
			}
			ServerLocal.set(getLocal(session.getLanguage()));
			session.sethibernateSession(openSession);
			session.reloadObjects();

			MobileAdaptor adoptor = getAdaptor(adaptorType);
			UserMessage userMessage = preProcess(session, message, networkId,
					networkType);

			Command command = userMessage.getCommand();
			session.addCommand(command, userMessage);

			Result result = getCommandProcessor().handleMessage(session,
					userMessage);
			if (oldResult != null) {
				List<Object> resultParts = oldResult.getResultParts();
				for (int i = resultParts.size(); i > 0; i--) {
					result.resultParts.add(0, resultParts.get(i - 1));
				}
				String cookie = oldResult.getCookie();
				if (result.getCookie() == null) {
					result.setCookie(cookie);
				}
				result.setHideCancel(oldResult.isHideCancel());
			}
			boolean hasNextCommand = true;
			if (command != null && command.isDone()) {
				session.refreshCurrentCommand();
				String nextCommand = result.getNextCommand();
				if (nextCommand == null) {
					session.refreshLastMessage();
					UserMessage lastMessage = session.getLastMessage();
					if (lastMessage != null) {
						lastMessage.setResult(lastMessage.getLastResult());
						return processMessage(networkId, null, adaptorType,
								networkType, result, context);
					} else {
						hasNextCommand = false;
					}
				}
			}

			String nextCommand = result.getNextCommand();
			if (nextCommand != null) {
				result.setNextCommand(null);
				return processMessage(networkId, nextCommand, adaptorType,
						networkType, result, context);
			}

			if (!hasNextCommand) {
				return processMessage(networkId, "back", adaptorType,
						networkType, result, context);
			}

			Command currentCommand = session.getCurrentCommand();
			if (currentCommand == null) {
				result.setHideCancel(true);
			} else {
				if (currentCommand instanceof AuthenticationCommand) {
					if (currentCommand.getAttribute("select") == null) {
						result.setHideCancel(true);
					}
				}
			}

			result.setTitle(userMessage.getCommandString());
			String postProcess = adoptor.postProcess(result);
			session.setLastReply(postProcess);
			if (session.isExpired()) {
				sessions.remove(networkId);
			}
			return postProcess;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(e);
		} finally {
			if (openSession.isOpen()) {
				openSession.close();
			}
		}
	}

	private Locale getLocal(String language) {
		if (language.length() == 2) {
			return new Locale(language);
		}

		Locale[] availableLocales = Locale.getAvailableLocales();
		for (Locale locale : availableLocales) {
			String lang = "";
			try {
				language = locale.getISO3Language();
			} catch (Exception e) {
				language = "eng";
			}
			if (lang.equalsIgnoreCase(language)) {
				return locale;
			}
		}
		return Locale.getDefault();
	}

	/**
	 * PreProcess the UserMessage
	 * 
	 * @param userMessage
	 * @return
	 * @throws AccounterMobileException
	 */
	private UserMessage preProcess(MobileSession session, String clientMessage,
			String networkId, int networkType) throws AccounterMobileException {
		String message = clientMessage;
		UserMessage userMessage = new UserMessage(message, networkId,
				networkType);

		if (message == null || message.isEmpty()) {
			userMessage = session.getLastMessage();
			message = userMessage.getOriginalMsg();
			clientMessage = message;
		}
		Command command = null;

		if (command == null) {
			command = session.getCurrentCommand();
		}

		if (command == null && !session.isAuthenticated()) {
			// Result result = PatternStore.INSTANCE.find("login");
			// if (result != null) {
			// userMessage.setType(Type.HELP);
			// userMessage.setResult(result);
			// return userMessage;
			// }
			command = new AuthenticationCommand();
			userMessage.setCommandString("Login");
			if (networkType != AccounterChatServer.NETWORK_TYPE_MOBILE) {
				userMessage.setOriginalMsg("");// To know it is first
				message = "";
			}
		}
		if (command == null) {
			long companyId = session.getCompanyID();
			if (companyId == 0) {
				command = new SelectCompanyCommand();
				userMessage.setCommandString("Company Selection");
			}
		}

		String commandString = message;
		Command matchedCommand = null;
		if (session.isAuthenticated() || message.equalsIgnoreCase("cancel")) {
			while (!commandString.isEmpty()) {
				matchedCommand = CommandsFactory.INSTANCE
						.getCommand(commandString);
				if (matchedCommand != null) {
					break;
				}
				int lastIndexOf = commandString.lastIndexOf(" ");
				if (lastIndexOf > 0) {
					commandString = commandString.substring(0, lastIndexOf);
				}
				if (lastIndexOf < 0) {
					break;
				}
			}
		}
		Company company = session.getCompany();
		if (matchedCommand != null) {
			message = message.replaceAll(commandString.trim(), "").trim();
			userMessage.setOriginalMsg(message);
			command = matchedCommand;
			userMessage.setCommandString(commandString.trim());
		} else {
			Result result = PatternStore.INSTANCE.find(clientMessage,
					session.isAuthenticated(), company);
			if (result != null) {
				result.setShowBack(session.getLastMessage() != null);
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				userMessage.setCommandString(message);
				userMessage.setOriginalMsg(message);
				return userMessage;
			}
		}

		UserMessage lastMessage = session.getLastMessage();
		Result lastResult = lastMessage == null ? null : lastMessage
				.getResult();
		if (lastMessage != null && userMessage.getCommandString() == null) {
			userMessage.setCommandString(lastMessage.getCommandString());
		}
		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;

			commandString = getPatternResultString(patternResult.getCommands(),
					clientMessage);

			if (commandString != null) {
				Result result = PatternStore.INSTANCE.find(commandString,
						session.isAuthenticated(), company);
				if (result != null) {
					result.setShowBack(session.getLastMessage() != null);
					userMessage.setType(Type.HELP);
					userMessage.setResult(result);
					userMessage.setCommandString(commandString);
					userMessage.setOriginalMsg(commandString);
					return userMessage;
				}
			}
		}

		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			Command selectCommand = getCommand(patternResult.getCommands(),
					message, userMessage);
			if (selectCommand != null) {
				command = selectCommand;
			}
		}
		if (command == null && message.equalsIgnoreCase("back")) {
			session.refreshLastMessage();
			UserMessage lastMessage2 = session.getLastMessage();
			if (lastMessage2 != null) {
				message = lastMessage2.getCommandString();
			} else {
				message = "Menu";
			}
			Result result = PatternStore.INSTANCE.find(message,
					session.isAuthenticated(), company);
			if (lastMessage2 != null) {
				Result lastResult2 = lastMessage2.getResult();
				result.setShowBack(lastResult2 != null ? lastResult2
						.isShowBack() : false);
				lastMessage2.setResult(result);
				return lastMessage2;
			}

			if (result != null) {
				result.setShowBack(session.getLastMessage() != null);
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				userMessage.setCommandString(message);
				userMessage.setOriginalMsg(message);
				return userMessage;
			}

		}
		userMessage.setLastResult(lastResult);

		if (command != null) {
			userMessage.setType(Type.COMMAND);
			userMessage.setCommand(command);
			userMessage.setInputs(message.split(" "));
			return userMessage;
		}

		if (message.startsWith("#")) {
			userMessage.setType(Type.NUMBER);
			userMessage.setInputs(message.replaceAll("#", "").split(" "));
			return userMessage;
		}

		// TODO Check for isName
		if (!message.contains(" ")) {
			userMessage.setType(Type.NAME);
			userMessage.setInputs(message.split(" "));
		}

		userMessage.setInputs(new String[] { message });

		return userMessage;
	}

	private String getPatternResultString(CommandList commands, String input) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		int index = ch - 97;// If ch is number
		if (index < 0) {
			return null;
		}
		if (index >= commands.size()) {
			return null;
		}
		UserCommand userCommand = commands.get(index);
		String commandString = userCommand.getCommandName();
		if (commandString == null) {
			return null;
		}
		return commandString;
	}

	private Command getCommand(CommandList commands, String input,
			UserMessage userMessage) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		int index = ch - 97;// If ch is number
		if (index < 0) {
			return null;
		}
		if (index >= commands.size()) {
			return null;
		}
		UserCommand userCommand = commands.get(index);
		Command command = CommandsFactory.INSTANCE.getCommand(userCommand
				.getCommandName());
		if (command == null) {
			return null;
		}
		userMessage.setOriginalMsg(userCommand.getInputs());
		userMessage.setCommandString(userCommand.getCommandName());
		return command;
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
		} else if (type.equals(AdaptorType.MOBILE)) {
			return MobileApplicationAdaptor.INSTANCE;
		} else {
			return null;
		}
	}

	/**
	 * Returns the Command Processor
	 */
	private CommandProcessor getCommandProcessor() {
		return CommandProcessor.INSTANCE;
	}

	public void logout(String networkId) {
		sessions.remove(networkId);
	}

	public void putMessage(MobileChannelContext context) {
		try {
			queue.put(context);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static MobileMessageHandler getInstance() {
		if (instance == null) {
			instance = new MobileMessageHandler();
			instance.start();
		}
		return instance;
	}
}
