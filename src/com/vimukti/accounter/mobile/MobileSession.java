/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileSession {

	public static final long SESSION_TIME_OUT_PERIOD = 2 * 60 * 1000;

	private static final String LAST_RESULT = "lastResult";

	private Client client;
	private Company company;
	private Map<Object, Object> attributes = new HashMap<Object, Object>();
	private Command currentCommand;
	private boolean isExpired;

	private TimerTask task;

	private Session hibernateSession;

	/**
	 * Creates new Instance
	 */
	public MobileSession() {
	}

	public long getUserId() {
		return this.client.getID();
	}

	public String getUserEmail() {
		return this.client.getEmailId();
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

	/**
	 * @return
	 */
	public boolean isAuthenticated() {
		return client != null;
	}

	/**
	 * @return
	 */
	public User getUser() {
		return company.getUserByUserEmail(client.getEmailId());
	}

	public Company getCompany() {
		return this.company;
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
	public NetworkUser getFrom() {
		// TODO Auto-generated method stub
		return null;
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

}
