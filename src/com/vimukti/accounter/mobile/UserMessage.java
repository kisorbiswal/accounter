/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Prasanna Kumar G
 * 
 */
public class UserMessage {

	public enum Type {
		COMMAND, HELP, NUMBER, NAME, UNKNOWN;
	}

	private Type type = Type.UNKNOWN;
	private Result result;
	private Command command;
	private List<String> inputs = new ArrayList<String>();

	/**
	 * Returns the Type of the Message
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param command2
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Return Command if type is COMMAND
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * 
	 */
	public List<String> getInputs() {
		return this.inputs;
	}

	/**
	 * @param currentCommand
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * @param reply
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Returns the Result for this UserMessage
	 */
	public Result getResult() {
		return this.result;
	}

	/**
	 * @param split
	 */
	public void setInputs(String[] split) {
		this.inputs = Arrays.asList(split);
	}

}
