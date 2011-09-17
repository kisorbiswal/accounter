package com.vimukti.accounter.mobile;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.commands.NameSearchCommand;
import com.vimukti.accounter.mobile.commands.NumberSearchCommand;

/**
 * Handles the Request from User and Executes corresponding Command
 * 
 */
public class CommandProcessor {

	public String handleMessage(String message) throws AccounterMobileException {

		// TODO Open Hibernate Session

		// Parsing Message
		UserMessage userMessage = parseMessage(message.trim());

		switch (userMessage.getType()) {
		case COMMAND:
			if (userMessage.getCommand().isDone()) {
				break;
			}
			Result reply = processCommand(userMessage);
			userMessage.setResult(reply);
			break;
		case HELP:
			// TODO
			break;
		case NUMBER:
			userMessage.setResult(processNumber(userMessage));
			break;
		case NAME:
			userMessage.setResult(processName(userMessage));
			break;
		default:
			// TODO Return Default Result
			break;
		}

		Session currentSession = getCurrentSession();
		if (currentSession != null && currentSession.isOpen()) {
			currentSession.close();
		}
		setLastResult(userMessage.getResult());
		return buildResult(userMessage.getResult());
	}

	/**
	 * @param userMessage
	 */
	private Result processName(UserMessage userMessage) {
		NameSearchCommand command = new NameSearchCommand();
		Context context = new Context(getCurrentSession());
		context.setInputs(userMessage.getInputs());
		return command.run(context);
	}

	/**
	 * @param userMessage
	 */
	private Result processNumber(UserMessage userMessage) {
		NumberSearchCommand command = new NumberSearchCommand();
		Context context = new Context(getCurrentSession());
		context.setInputs(userMessage.getInputs());
		return command.run(context);
	}

	/**
	 * Builds the Result String from Result
	 * 
	 * @param result
	 * @return
	 */
	private String buildResult(Result result) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * Creates And Runs the Command
	 * 
	 * @param message
	 * @return
	 */
	private Result processCommand(UserMessage message) {
		Command command = message.getCommand();
		Context context = new Context(getCurrentSession());

		Result lastResult = getLastResult();
		List<ResultList> resultList = lastResult.getResultList();
		for (ResultList result : resultList) {
			for (Record record : result) {
				if (message.getInputs().contains(record.getCode())) {
					if (!result.isMultiSelection()) {
						context.putSelection(result.getName(),
								record.getObject());
						// FIXME GOT MULTIPLE SELECTION FROM USER EVEN THOUGH
						// SINGLE SELECTION ENABLED
						break;
					} else {
						context.putSelection(result.getName(),
								record.getObject());
					}
				}
			}
		}

		context.setInputs(message.getInputs());
		return command.run(context);
	}

	/**
	 * Parse the UserMessage
	 * 
	 * @param message
	 * @return
	 * @throws AccounterMobileException
	 */
	private UserMessage parseMessage(String message)
			throws AccounterMobileException {
		UserMessage userMessage = new UserMessage();
		Command command = null;
		if (isProcessingAnyCommand()) {
			command = getCurrentCommand();
		} else {
			command = getCommandFactory().searchCommand(message);
		}
		if (command != null) {
			userMessage.setCommand(command);
			String name = command.getName();
			userMessage.setInputs(message.replace(name, "").split(" "));
			return userMessage;
		}

		Result result = searchForPatterns(message);
		if (result != null) {
			userMessage.setResult(result);
			return userMessage;
		}

		if (message.startsWith("#")) {
			userMessage.setInputs(message.replaceAll("#", "").split(" "));
			return userMessage;
		}

		// TODO Check is Name

		userMessage.setInputs(message.split(" "));

		return userMessage;
	}

	/**
	 * @return
	 * 
	 */
	private Command getCurrentCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param message
	 */
	private Result searchForPatterns(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	private CommandFactory getCommandFactory() {
		return CommandFactory.INSTANCE;
	}

	public Session getCurrentSession() {
		return null;
	}

	/**
	 * Returns true if Processing Any Command
	 * 
	 * @return
	 */
	public boolean isProcessingAnyCommand() {
		return false;
	}

	public Result getLastResult() {
		// TODO
		return null;
	}

	private void setLastResult(Result result) {
		// TODO
	}

}
