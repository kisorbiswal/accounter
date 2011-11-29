/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.Collection;
import java.util.List;

import com.vimukti.accounter.mobile.store.Output;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternResult extends Result {
	public String condition;
	public boolean needAuthentication;
	public List<Output> outputs;

	/**
	 * @return
	 */
	public CommandList getCommands() {
		CommandList commandList = new CommandList();
		for (Object obj : resultParts) {
			if (obj instanceof CommandList) {
				commandList.addAll((Collection<? extends UserCommand>) obj);
			}
		}
		return commandList;
	}

	public void add(Object obj) {
		resultParts.add(obj);
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

	public Result render(boolean isAuthenticated) {
		if (needAuthentication ? !isAuthenticated : false) {
			return null;
		}
		if (condition != null && !checkCondition(condition)) {
			return null;
		}
		PatternResult result = new PatternResult();
		for (Output output : outputs) {
			output.add(result);
		}
		return result;
	}

	public boolean checkCondition(String condition) {
		return true;
	}
}
