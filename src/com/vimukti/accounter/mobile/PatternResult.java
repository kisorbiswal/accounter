/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.Collection;
import java.util.List;

import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.mobile.store.Output;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternResult extends Result {
	public String condition;
	public boolean login;
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
		if (login ? !isAuthenticated : false) {
			Result result = new Result();
			result.add("You can not do this action before login.");
			result.add("Please login");
			result.setNextCommand("login");
			return result;
		}
		if (condition != null && !checkCondition(condition)) {
			Result result = new Result();
			result.add("You do not have permission to do this action.");
			result.add("You can change permissions from Company Preferences.");
			CommandList list = new CommandList();
			list.add("Change Preferences");
			result.add(list);
			result.setNextCommand("back");
			return result;
		}
		PatternResult result = new PatternResult();
		for (Output output : outputs) {
			output.add(result);
		}
		return result;
	}

	public boolean checkCondition(String condition) {
		ClientCompanyPreferences preferences = CompanyPreferenceThreadLocal
				.get();
		if (condition.equals("trackTax")) {
			return preferences.isTrackTax();
		} else if (condition.equals("trackingQuotes")) {
			return preferences.isDoyouwantstatements();
		} else if (condition.equals("delayedCharges")) {
			return preferences.isDelayedchargesEnabled();
		} else if (condition.equals("manageBills")) {
			return preferences.isKeepTrackofBills();
		}
		return true;
	}
}
