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

	private static final String LAST_RESULT = "lastResult";
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
				// TODO
				break;
			case NUMBER:
				processNumber(session, message);
				break;
			case NAME:
				processName(session, message);
				break;
			default:
				break;
			}
			Command command = message.getCommand();

			if (command != null && session.getCurrentCommand() == null
					&& !command.isDone()) {
				session.setCurrentCommand(command);
			}

			if (message.getResult() == null) {
				Result result = new Result(
						"Sorry, We are unable to find the answer for '"
								+ message.getInputs().get(0) + "'");
				message.setResult(result);
			}

			session.setAttribute(LAST_RESULT, message.getResult());
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

		Result lastResult = getLastResult(session);
		if (lastResult != null) {
			List<Object> resultParts = lastResult.getResultParts();
			Iterator<Object> iterator = resultParts.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (!(next instanceof ResultList)) {
					continue;
				}
				ResultList resultList = (ResultList) next;
				for (Record record : resultList) {
					if (message.getInputs().contains(record.getCode())) {
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
		}

		context.setInputs(message.getInputs());
		return command.run(context);
	}

	/**
	 * @return
	 */
	private Result getLastResult(MobileSession session) {
		return (Result) session.getAttribute(LAST_RESULT);
	}

	private Context getContext(MobileSession mSession) {
		long companyId = mSession.getCompanyId();
		Session session = HibernateUtil.openSession(Server.COMPANY + companyId);
		mSession.sethibernateSession(session);
		Context context = new Context(mSession);
		return context;
	}

}
