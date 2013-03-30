package com.vimukti.accounter.text;

import java.util.ArrayList;
import java.util.Iterator;

import com.vimukti.accounter.text.commands.ITextCommand;

public class CommandProcessor {

	public void processCommands(ArrayList<ITextData> datas) throws Exception {

		Iterator<ITextData> iterator = datas.iterator();

		Class<? extends ITextCommand> commandClass = null;

		while (iterator.hasNext()) {
			ITextData data = iterator.next();
			if (commandClass == null) {
				commandClass = CommandsFactory.getCommand(data.getType());
			}
			ITextCommand command = commandClass.newInstance();
			ITextResponse response = newResponse();
			boolean isSameCommand = command.parse(data, response);
			if (isSameCommand) {
				continue;
			}
			data.reset();
			command.process(response);
		}
	}

	private ITextResponse newResponse() {
		return new CommandResponse();
	}

}
