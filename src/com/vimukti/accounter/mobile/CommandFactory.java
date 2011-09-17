/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import com.google.gwt.dev.util.collect.HashMap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CommandFactory {

	private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();

	public static CommandFactory INSTANCE = new CommandFactory();

	/**
	 * Creates new Instance
	 */
	private CommandFactory() {
		try {
			loadCommands();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Search for the Command in the CommandFactory and Returns the Command if
	 * Exists
	 * 
	 * @param command
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Command searchCommand(String command)
			throws AccounterMobileException {
		try {

			Class<?> className = commands.get(command.toLowerCase());
			if (className == null) {
				return null;
			}

			return (Command) className.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(
					AccounterMobileException.ERROR_UNKNOWN_COMMAND, e);
		}

	}

	/**
	 * Loads All Commands
	 * 
	 * @throws AccounterMobileException
	 */
	private void loadCommands() throws AccounterMobileException {
		try {
			XStream xStream = new XStream(new DomDriver());

			xStream.alias("commands", List.class);

			xStream.alias("command", TemplateCommand.class);
			xStream.useAttributeFor(TemplateCommand.class, "name");
			xStream.useAttributeFor(TemplateCommand.class, "class");

			xStream.aliasField("class", TemplateCommand.class, "className");

			xStream.alias("name", String.class);

			File file = getFile("");

			Object object = xStream.fromXML(new FileInputStream(file));

			List<TemplateCommand> commands = (List<TemplateCommand>) object;
			for (TemplateCommand command : commands) {
				Class<?> forName = Class.forName(command.className);
				this.commands.put(command.name.toLowerCase(), forName);
				for (String alias : command.alias) {
					this.commands.put(alias.toLowerCase(), forName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(
					AccounterMobileException.ERROR_INTERNAL, e);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private File getFile(String language) {
		// TODO Auto-generated method stub
		return null;
	}
}

class TemplateCommand {
	String name;
	String className;
	List<String> alias;
}