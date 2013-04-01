package com.vimukti.accounter.text;

import java.util.ArrayList;

import com.vimukti.accounter.text.commands.ITextCommand;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CommandProcessor {

	private static CommandProcessor processor;

	public static CommandProcessor getInstance() {
		if (processor == null) {
			processor = new CommandProcessor();
		}
		return processor;
	}

	public ArrayList<CommandResponseImpl> processCommands(CommandsQueue queue) {

		ArrayList<CommandResponseImpl> responses = new ArrayList<CommandResponseImpl>();

		while (queue.hasNext()) {
			CommandResponseImpl cResponse = new CommandResponseImpl();
			processCommand(queue, cResponse);
			responses.add(cResponse);
			if (!cResponse.hasErrors()) {
				cResponse.addMessage("Request successfull!!");
			}
		}

		return responses;
	}

	private void processCommand(CommandsQueue queue,
			CommandResponseImpl response) {
		// get next
		ITextData data = queue.take();
		response.addData(data);

		Class<? extends ITextCommand> commandClass = CommandsFactory
				.getCommand(data.getType());
		ITextCommand command = null;
		try {
			command = commandClass.newInstance();
		} catch (ReflectiveOperationException e) {
			// Send response as Invalid command
			response.addError("Invalid command");
		}

		// Parse command
		parseRequest(command, data, response, queue);

		// Got errors in parsing, don't process this
		if (response.hasErrors()) {
			return;
		}

		// PROCESS COMMAND
		try {
			command.process(response);
		} catch (AccounterException e) {
			// Add Error to response
			response.addError(e.getMessage());
		}

	}

	private void parseRequest(ITextCommand command, ITextData data,
			CommandResponseImpl response, CommandsQueue queue) {
		// PARSE COMMAND DATA
		boolean isParseSuccess = command.parse(data, response);

		// If Has errors, just return
		if (response.hasErrors()) {
			return;
		}

		// Is Same as Previous, then parse next command
		if (isParseSuccess && queue.hasNext()) {
			response.addData(data);
			parseRequest(command, queue.take(), response, queue);
		} else {
			// If not same as Previous, then just process it as first data
			// already read by this command
			data.reset();
			if (!isParseSuccess) {
				queue.revertPrevious();
			}
		}
	}
}
