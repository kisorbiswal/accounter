package com.vimukti.accounter.mobile;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.mobile.commands.NameSearchCommand;
import com.vimukti.accounter.mobile.commands.NumberSearchCommand;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * Handles the Request from User and Executes corresponding Command
 * 
 */
public class CommandProcessor {

	public static CommandProcessor INSTANCE = new CommandProcessor();

	public synchronized Result handleMessage(MobileSession session,
			UserMessage message) throws AccounterMobileException {

		try {
			// if (!session.isAuthenticated()) {
			// Session hibernateSession = HibernateUtil
			// .openSession(Server.LOCAL_DATABASE);
			// session.sethibernateSession(hibernateSession);
			// Command authentication = new AuthenticationCommand();
			// Result result = authentication.run(new Context(session));
			// return result;
			// }

			// Parsing Message
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
			Command command = message.getCommand();

			if (command != null && !command.isDone()
					&& session.getCurrentCommand() == null) {
				session.setCurrentCommand(command);
			}

			if (message.getResult() == null) {
				Result result = new Result(
						"Sorry, We are unable to find the answer for '"
								+ message.getInputs().toString() + "'");
				message.setResult(result);
			}

			session.setLastResult(message.getResult());
			return message.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AccounterMobileException) {
				throw (AccounterMobileException) e;
			}
			throw new AccounterMobileException(e);

		} finally {
			Session currentSession = session.getHibernateSession();
			if (currentSession != null && currentSession.isOpen()) {
				currentSession.close();
			}
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
		Context context = getContext(session);
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
		Context context = getContext(session);
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
		Context context = getContext(session);
		// Getting Last Result
		Result lastResult = session.getLastResult();
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
		Result result = command.run(context);
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

	private Context getContext(MobileSession mSession) {
		long companyId = mSession.getCompanyId();
		Session session = HibernateUtil.openSession();
		mSession.sethibernateSession(session);
		Context context = new Context(mSession);
		return context;
	}

}
