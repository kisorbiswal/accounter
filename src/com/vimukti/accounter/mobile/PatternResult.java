/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.Collection;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternResult extends Result {
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

}
