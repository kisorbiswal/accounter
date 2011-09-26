/**
 * 
 */
package com.vimukti.accounter.mobile;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternResult extends Result {

	public void setTitle(String message) {
		super.add(message);
	}

	public void setCommands(CommandList commands) {
		super.add(commands);
	}

	/**
	 * @return
	 */
	public CommandList getCommands() {
		for (Object obj : resultParts) {
			if (obj instanceof CommandList) {
				return (CommandList) obj;
			}
		}
		return null;
	}

}
