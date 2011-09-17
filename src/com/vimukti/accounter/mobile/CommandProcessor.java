package com.vimukti.accounter.mobile;

import java.util.List;

import org.hibernate.Session;

/**
 * Handles the Request from User and Executes corresponding Command
 * 
 */
public class CommandProcessor {

	public String handleMessage(String message) {

		// TODO Open Hibernate Session

		// Parsing Message
		UserMessage userMessage = parseMessage(message);

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
			processNumber(userMessage);
			break;
		case NAME:
			processName(userMessage);
			break;
		default:
			// TODO Return Default Result
			break;
		}

		Session currentSession = getCurrentSession();
		if (currentSession != null && currentSession.isOpen()) {
			currentSession.close();
		}

		return buildResult(userMessage.getResult());
	}

	/**
	 * @param userMessage
	 */
	private void processName(UserMessage userMessage) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param userMessage
	 */
	private void processNumber(UserMessage userMessage) {
		// TODO Auto-generated method stub

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
	 */
	private UserMessage parseMessage(String message) {
		return null;
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
		return null;
	}
}
