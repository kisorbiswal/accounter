/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Session;

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
public class MobileMessageHandler {

	private Map<String, MobileSession> sessions = new HashMap<String, MobileSession>();

	/**
	 * @param message
	 * @param commandSender
	 * @param message2
	 * @return
	 * @throws AccounterMobileException
	 */
	public String messageReceived(String networkId, String message,
			AdaptorType adaptorType, int networkType)
			throws AccounterMobileException {
		String processMessage = processMessage(networkId, message, adaptorType,
				networkType, null);
		if (networkType == AccounterChatServer.NETWORK_TYPE_GTALK) {
			sessions.get(networkId).await(this, networkId);
		}
		return processMessage;
	}

	private String processMessage(String networkId, String message,
			AdaptorType adaptorType, int networkType, String oldReplay)
			throws AccounterMobileException {
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
			UserMessage userMessage = preProcess(session, message, networkId,
					networkType);
			Result result = getCommandProcessor().handleMessage(session,
					userMessage);
			String reply = adoptor.postProcess(result, oldReplay);
			boolean hasNextCommand = true;
			if (userMessage.getCommand() != null
					&& userMessage.getCommand().isDone()) {
				String nextCommand = result.getNextCommand();
				if (nextCommand == null) {
					session.refreshCurrentCommand();
					Command currentCommand = session.getCurrentCommand();
					if (currentCommand != null) {
						UserMessage lastMessage = session.getLastMessage();
						lastMessage.setResult(lastMessage.getLastResult());

						return processMessage(networkId, null, adaptorType,
								networkType, reply);
					} else {
						hasNextCommand = false;
					}
				}
			}

			String nextCommand = result.getNextCommand();
			if (nextCommand != null) {
				result.setNextCommand(null);
				return processMessage(networkId, nextCommand, adaptorType,
						networkType, reply);
			}

			if (!hasNextCommand) {
				reply += "\nEnter Command";
			}
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

	/**
	 * PreProcess the UserMessage
	 * 
	 * @param userMessage
	 * @return
	 * @throws AccounterMobileException
	 */
	public UserMessage preProcess(MobileSession session, String message,
			String networkId, int networkType) throws AccounterMobileException {
		UserMessage userMessage = new UserMessage(message, networkId,
				networkType);

		if (message == null || message.isEmpty()) {
			userMessage = session.getLastMessage();
			message = userMessage.getOriginalMsg();
		}
		Command command = null;

		if (command == null) {
			command = session.getCurrentCommand();
		}

		if (command == null && !session.isAuthenticated()) {
			command = new AuthenticationCommand();
			userMessage.setOriginalMsg("");// To know it is first
			message = "";
			userMessage.setCommandString("");
		}
		if (command == null) {
			long companyId = session.getCompanyID();
			if (companyId == 0) {
				command = new SelectCompanyCommand();
				userMessage.setCommandString("");
			}
		}

		if (session.isAuthenticated()) {
			String commandString = "";
			Command matchedCommand = null;
			for (String str : message.split(" ")) {
				commandString += str;
				matchedCommand = CommandsFactory.INSTANCE
						.getCommand(commandString);
				if (matchedCommand != null) {
					break;
				}
				commandString += ' ';
			}

			if (matchedCommand != null) {
				message = message.replaceAll(commandString.trim(), "").trim();
				userMessage.setOriginalMsg(message);
				command = matchedCommand;
				userMessage.setCommandString(commandString.trim());
			}

			Result result = PatternStore.INSTANCE.find(message);
			if (result != null) {
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				return userMessage;
			}
		}

		UserMessage lastMessage = session.getLastMessage();
		Result lastResult = lastMessage == null ? null : lastMessage
				.getResult();
		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			Result result = getPatternResult(patternResult.getCommands(),
					message);
			if (result != null) {
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				return userMessage;
			}
		}

		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			Command selectCommand = getCommand(patternResult.getCommands(),
					message, userMessage);
			if (selectCommand != null) {
				command = selectCommand;
				UserMessage lastMessage2 = session.getLastMessage();
				if (lastMessage2 != null) {
					lastMessage2.setOriginalMsg("");
				}
			}
		}

		userMessage.setLastResult(lastResult);

		if (command != null && !command.isDone()) {
			session.addCommand(command);
		}

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

	private Result getPatternResult(CommandList commands, String input) {
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
		UserCommand userCommand = commands.get(index);
		String commandString = userCommand.getCommandName();
		if (commandString == null) {
			return null;
		}
		return PatternStore.INSTANCE.find(commandString);
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
		UserCommand userCommand = commands.get(index);
		String commandString = userCommand.getCommandName();
		if (commandString == null) {
			return null;
		}
		Command command = null;
		String str = "";
		for (String s : commandString.split(" ")) {
			str += s;
			command = CommandsFactory.INSTANCE.getCommand(str);
			if (command != null) {
				break;
			}
			str += " ";
		}
		commandString = commandString.replaceAll(str.trim(), "").trim();
		userMessage.setOriginalMsg(commandString);
		userMessage.setCommandString(str);
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
}
