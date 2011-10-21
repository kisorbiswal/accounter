/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TimerTask;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.core.User;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileSession {

	public static final long SESSION_TIME_OUT_PERIOD = 2 * 60 * 1000;

	private static final String LAST_RESULT = "lastResult";

	private Client client;
	private long companyID;
	private Map<Object, Object> attributes = new HashMap<Object, Object>();
	private Command currentCommand;
	private boolean isExpired;
	private Stack<Command> commandStack = new Stack<Command>();
	private TimerTask task;
	private User user;

	private Session hibernateSession;

	private boolean isAuthenicated;

	/**
	 * Creates new Instance
	 */
	public MobileSession() {
	}

	public String getUserEmail() {
		return getClient().getEmailId();
	}

	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Object getLast(RequirementType customer) {
		return attributes.get(customer);
	}

	public void setLast(RequirementType customer, Object obj) {
		this.attributes.put(customer, obj);
	}

	/**
	 * Returns the Current Processing Command
	 * 
	 * @return
	 */
	public Command getCurrentCommand() {
		if (currentCommand != null && !currentCommand.isDone()) {
			return this.currentCommand;
		}
		return null;
	}

	/**
	 * Sets the CurrentCommand
	 * 
	 * @param command
	 */
	public void setCurrentCommand(Command command) {
		this.currentCommand = command;
		if (command == null) {
			setLastResult(null);
		} else {
			setLastResult(command.getLastResult());
		}
	}

	public boolean isExpired() {
		return this.isExpired;
	}

	public void setExpired(boolean value) {
		this.isExpired = value;
	}

	public void await() {
		refresh();
		this.task = new TimerTask() {

			@Override
			public void run() {
				setExpired(true);
			}
		};
		AccounterTimer.INSTANCE.schedule(task, SESSION_TIME_OUT_PERIOD);
	}

	public void refresh() {
		if (this.task != null) {
			task.cancel();
		}
	}

	public void refreshCurrentCommand() {
		// Set the Present Command From the stack
		while (!commandStack.isEmpty()) {
			Command pop = commandStack.pop();
			if (pop == null) {
				break;
			}
			if (!pop.isDone()) {
				setCurrentCommand(pop);
				return;
			}
		}
		setCurrentCommand(null);
	}

	/**
	 * @return
	 */
	public User getUser() {
		return user;
	}

	public Company getCompany() {
		return (Company) hibernateSession.get(Company.class, companyID);
	}

	/**
	 * @param session
	 */
	public void sethibernateSession(Session session) {
		this.hibernateSession = session;
	}

	public Session getHibernateSession() {
		return this.hibernateSession;
	}

	/**
	 * @return
	 */
	public Result getLastResult() {
		return (Result) getAttribute(LAST_RESULT);
	}

	/**
	 * @param result
	 */
	public void setLastResult(Result result) {
		setAttribute(LAST_RESULT, result);
	}

	/**
	 * Return the from field of the Message
	 * 
	 * @return
	 */
	public IMUser getFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCompanyID() {
		return companyID;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompanyID(long company) {
		this.companyID = company;
	}

	public void addCommand(Command command) {
		if (currentCommand != command && !command.isDone()) {
			if (this.currentCommand != null) {
				this.currentCommand.setLastResult(getLastResult());
			}
			commandStack.push(command);
			refreshCurrentCommand();
		}
	}

	/**
	 * Removes an Attribute from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void reloadObjects() {
		if (client != null) {
			client = (Client) hibernateSession
					.get(Client.class, client.getID());
		}
		if (user != null) {
			user = (User) hibernateSession.get(User.class, user.getID());
		}
	}

	public boolean isAuthenticated() {
		return isAuthenicated;
	}

	public void setAuthentication(boolean b) {
		isAuthenicated = b;
	}
}
