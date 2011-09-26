/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.List;

import com.vimukti.accounter.mobile.UserMessage.Type;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileChatAdaptor implements MobileAdaptor {

	public static MobileAdaptor INSTANCE = new MobileChatAdaptor();

	/**
	 * PreProcess the UserMessage
	 * 
	 * @param userMessage
	 * @return
	 * @throws AccounterMobileException
	 */
	public UserMessage preProcess(MobileSession session, String message) {
		UserMessage userMessage = new UserMessage();

		if (message == null || message.isEmpty()) {

		}

		Command command = null;
		Result lastResult = session.getLastResult();
		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			command = getCommand(patternResult.getCommands(), message);
			userMessage.setInputs(message.split(" "));
		}
		if (command == null) {
			command = session.getCurrentCommand();
			userMessage.setInputs(message.split(" "));
		}
		if (command == null) {
			// for (String subStr : StringUtils.getSubStrings(message)) {
			command = CommandsFactory.INSTANCE.searchCommand(message);
			// if (command != null) {
			// break;
			// }
			// }
		}
		if (command != null) {
			userMessage.setType(Type.COMMAND);
			userMessage.setCommand(command);
			return userMessage;
		}

		Result result = PatternStore.INSTANCE.find(message);
		if (result != null) {
			userMessage.setType(Type.HELP);
			userMessage.setResult(result);
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

	private Command getCommand(CommandList commands, String input) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		String commandString = commands.get(ch - 97);
		if (commandString == null) {
			return null;
		}
		return CommandsFactory.INSTANCE.getCommand(commandString);
	}

	/**
	 * PostProcess the Result
	 * 
	 * @param result
	 * @return
	 */
	public String postProcess(Result result) {
		if (result == null) {
			return null;
		}
		List<Object> resultParts = result.getResultParts();
		StringBuffer reply = new StringBuffer();
		int recordsCount = 1;
		int commandIndex = 97;
		for (Object part : resultParts) {
			if (part instanceof ResultList) {
				ResultList resultList = (ResultList) part;
				for (int x = 0; x < resultList.size(); x++, recordsCount++) {
					Record record = resultList.get(x);
					record.setCode(recordsCount);
					reply.append(record.toString());
				}
			}
			if (part instanceof CommandList) {
				CommandList commandList = (CommandList) part;
				for (int x = 0; x < commandList.size(); x++, commandIndex++) {
					reply.append('(');
					reply.append((char) commandIndex);
					reply.append(") ");
					reply.append(commandList.get(x));
					reply.append('\n');
				}
			} else {
				reply.append((String) part);
			}
			reply.append('\n');
		}
		return reply.toString();
	}
}
