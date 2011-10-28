package com.vimukti.accounter.mobile;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.mobile.commands.NameSearchCommand;
import com.vimukti.accounter.mobile.commands.NumberSearchCommand;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * Handles the Request from User and Executes corresponding Command
 * 
 */
public class CommandProcessor {

	Logger log = Logger.getLogger(CommandProcessor.class);

	public static CommandProcessor INSTANCE = new CommandProcessor();

	public synchronized Result handleMessage(MobileSession session,
			UserMessage message) throws AccounterMobileException {

		try {
			switch (message.getType()) {
			case COMMAND:
				Result reply = processCommand(session, message);
				message.setResult(reply);
				break;
			case HELP:
				break;
			case NUMBER:
				processNumber(session, message);
				break;
			case NAME:
				processName(session, message);
				break;
			}

			if (message.getResult() == null) {
				Result result = new Result(
						"Sorry, We are unable to find the answer for '"
								+ message.getOriginalMsg() + "'");
				message.setResult(result);
			}

			session.setLastMessage(message);
			return message.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AccounterMobileException) {
				throw (AccounterMobileException) e;
			}
			throw new AccounterMobileException(e);

		}
	}

	/**
	 * @param session
	 * @param userMessage
	 * @throws AccounterMobileException
	 */
	private void processName(MobileSession session, UserMessage userMessage)
			throws AccounterMobileException {
		NameSearchCommand command = new NameSearchCommand();
		Context context = getContext(session, userMessage);
		context.setInputs(userMessage.getInputs());
		Result result = command.run(context);
		userMessage.setCommand(command);
		userMessage.setResult(result);
	}

	/**
	 * @param session
	 * @param userMessage
	 * @throws AccounterMobileException
	 */
	private void processNumber(MobileSession session, UserMessage userMessage)
			throws AccounterMobileException {
		NumberSearchCommand command = new NumberSearchCommand();
		Context context = getContext(session, userMessage);
		context.setInputs(userMessage.getInputs());
		Result result = command.run(context);
		userMessage.setCommand(command);
		userMessage.setResult(result);
	}

	/**
	 * 
	 * Creates And Runs the Command
	 * 
	 * @param message
	 * @return
	 * @throws AccounterMobileException
	 */
	private Result processCommand(MobileSession session, UserMessage message)
			throws AccounterMobileException {
		Command command = message.getCommand();
		Context context = getContext(session, message);
		// Getting Last Result
		UserMessage lastMessage = session.getLastMessage();
		Result lastResult = lastMessage == null ? null : lastMessage
				.getResult();
		if (lastResult != null) {
			List<Object> resultParts = lastResult.getResultParts();
			Iterator<Object> iterator = resultParts.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof ResultList) {
					// Setting Selections
					setSelections((ResultList) next, message.getInputs(),
							context);
				}
			}
		}

		context.setInputs(message.getInputs());
		context.setString(message.getOriginalMsg());
		Result result = null;
		try {
			if (session.getCompanyID() != 0) {
				context.setClientCompany(new FinanceTool().getCompanyManager()
						.getClientCompany(session.getUserEmail(),
								session.getCompanyID()));
			}
			result = command.run(context);
			result = processResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			result = context.makeResult();
			e.printStackTrace();
			result.add("You got an Exception....@@@@@@@");
		}
		return result;
	}

	private Result processResult(Result result) {
		PatternResult patternResult = new PatternResult();
		patternResult.setNextCommand(result.getNextCommand());
		boolean isCommandList = false;
		for (Object obj : result.resultParts) {
			if (obj instanceof String) {
				patternResult.add((String) obj);
			} else if (obj instanceof ResultList) {
				patternResult.add((ResultList) obj);
			} else if (obj instanceof CommandList) {
				isCommandList = true;
				patternResult.setCommands((CommandList) obj);
			}
		}
		if (isCommandList) {
			return patternResult;
		}
		return result;
	}

	/**
	 * @param next
	 * @param inputs
	 * @param context
	 */
	private void setSelections(ResultList next, List<String> inputs,
			Context context) {
		ResultList resultList = (ResultList) next;
		for (Record record : resultList) {
			if (inputs.contains(record.getCode())) {
				if (!resultList.isMultiSelection()) {
					context.putSelection(resultList.getName(),
							record.getObject());
					// FIXME GOT MULTIPLE SELECTION FROM USER EVEN
					// THOUGH SINGLE SELECTION ENABLED
					break;
				} else {
					context.putSelection(resultList.getName(),
							record.getObject());
				}
			}
		}
	}

	private Context getContext(MobileSession mSession, UserMessage message) {
		Session session = HibernateUtil.getCurrentSession();
		mSession.sethibernateSession(session);
		Context context = new Context(mSession);
		context.setNetworkId(message.getNetworkId());
		context.setNetworkType(message.getNetworkType());
		return context;
	}
}
