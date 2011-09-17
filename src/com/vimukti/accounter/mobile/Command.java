package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
	MobileConstants constants;
	MobileMessages messages;

	public abstract String getId();

	List<Requirement> requirements = new ArrayList<Requirement>();

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

	public void markDone() {
		// TODO
	}
}
