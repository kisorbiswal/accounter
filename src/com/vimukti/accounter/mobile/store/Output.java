package com.vimukti.accounter.mobile.store;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.PatternResult;

public abstract class Output {

	public void add(PatternResult result, Company company) {
		if (this instanceof Text) {
			result.add(((Text) this).text);
		} else {
			PCommand command = (PCommand) this;
			CommandList commandList = new CommandList();
			commandList.add(command.command);
			if (command.condition == null
					|| result.checkCondition(command.condition, company)) {
				result.add(commandList);
			}
		}
	}
}

class Text extends Output {
	String text;
}

class PCommand extends Output {
	String condition;
	String command;
}