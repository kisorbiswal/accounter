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

	public void setCommands(CommandList commands) {
		super.add(commands);
	}

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

}
