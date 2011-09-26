package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
	MobileConstants constants;
	MobileMessages messages;

	public abstract String getId();

	List<Requirement> requirements = new ArrayList<Requirement>();
	private boolean isDone;
	private String successMessage;

	protected abstract void addRequirements(List<Requirement> list);

	protected Requirement get(String name) {
		return getRequirement(name);
	}

	protected Requirement getRequirement(String name) {
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

}
