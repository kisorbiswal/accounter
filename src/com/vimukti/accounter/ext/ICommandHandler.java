/**
 * 
 */
package com.vimukti.accounter.ext;

/**
 * @author Fernandez
 * 
 */
public interface ICommandHandler {

	Object processCommand(Command command) throws CommandException;

}
