package com.vimukti.accounter.text;

import java.util.ArrayList;
import java.util.Iterator;

import com.vimukti.accounter.text.commands.ITextCommand;

public class CommandProcessor {

	private static CommandProcessor processor;

	public static CommandProcessor getInstance() {
		if (processor == null) {
			processor = new CommandProcessor();
		}
		return processor;
	}

	public void processCommands(ArrayList<ITextData> datas,
			ITextResponse response) {

		Iterator<ITextData> iterator = datas.iterator();

		while (iterator.hasNext()) {
			// get next
			ITextData data = iterator.next();

			Class<? extends ITextCommand> commandClass = CommandsFactory
					.getCommand(data.getType());
			ITextCommand command = null;
			try {
				command = commandClass.newInstance();
			} catch (ReflectiveOperationException e) {
				// Send response as Invalid command
				response.addError("Invalid command");
				continue;
			}

			// Parse command
			parseRequest(command, data, response, iterator);

			if (response.hasErrors()) {
				// SEND ERRORS AS RESPONSE
				continue;
			}

			// PROCESS COMMAND
			command.process(response);

			if (response.hasErrors()) {
				// SEND ERRORS AS RESPONSE
				continue;
			}
		}
	}

	private void parseRequest(ITextCommand command, ITextData data,
			ITextResponse response, Iterator<ITextData> iterator) {
		// PARSE COMMAND DATA
		boolean isPaseSuccess = command.parse(data, response);

		// Is Same as Previous, then parse next command
		if (isPaseSuccess) {
			parseRequest(command, iterator.next(), response, iterator);
		} else {
			// If not same as Previous, then just process it as first data
			// already read by this command
			data.reset();
		}
	}

}
