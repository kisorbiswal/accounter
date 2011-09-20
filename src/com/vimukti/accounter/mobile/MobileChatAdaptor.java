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
		Command command = session.getCurrentCommand();
		if (command == null) {
			command = CommandsFactory.INSTANCE.searchCommand(message);
		}
		if (command != null) {
			userMessage.setType(Type.COMMAND);
			userMessage.setCommand(command);
			String name = command.getName();
			userMessage.setInputs(message.replace(name, "").split(" "));
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
					reply.append((char) commandIndex);
					reply.append(commandList.get(x));
				}
			} else {
				reply.append((String) part);
			}
			reply.append('\n');
		}
		return reply.toString();
	}
}
