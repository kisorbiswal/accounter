package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompany;

public abstract class Command {
	MobileConstants constants;
	MobileMessages messages;
	List<Requirement> requirements = new ArrayList<Requirement>();
	private boolean isDone;
	private String successMessage;
	private Result lastResult;
	private ClientCompany clientCompany;

	public Command() {
		addRequirements(requirements);
	}

	public abstract String getId();

	protected abstract void addRequirements(List<Requirement> list);

	protected Requirement get(String name) {
		return getRequirement(name);
	}

	protected Requirement getRequirement(String name) {
		for (Requirement requirement : requirements) {
			if (requirement.getName().equals(name)) {
				return requirement;
			}
		}
		return null;
	}

	public abstract Result run(Context context);

	public MobileConstants constants() {
		return constants;

	}

	public MobileMessages messages() {
		return messages;

	}

	public boolean isAllRequirementsFulfilled() {
		// TODO
		return true;
	}

	/**
	 * @return
	 */
	public boolean isDone() {
		return this.isDone;
	}

	public void markDone() {
		this.isDone = true;
	}

	/**
	 * 
	 * Sets the SuccessMessage
	 * 
	 * @param message
	 */
	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}

	/**
	 * 
	 * Returns the SuccessMessage
	 * 
	 * @return
	 */
	public String getSuccessMessage() {
		return successMessage;
	}

	/**
	 * @return
	 */
	public Object getResultObject() {
		// TODO Auto-generated method stub
		return null;
	}

	public Result getLastResult() {
		return lastResult;
	}

	public void setLastResult(Result lastResult) {
		this.lastResult = lastResult;
	}

	public void setClientCompany(ClientCompany clientCompany) {
		this.clientCompany = clientCompany;
	}

	public ClientCompany getClientCompany() {
		return clientCompany;
	}

}
