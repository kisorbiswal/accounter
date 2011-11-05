package com.vimukti.accounter.mobile.xtream;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.UserCommand;

public class JCommandList {
	List<JCommand> commandNames = new ArrayList<JCommand>();

	public void addAll(CommandList object) {
		int code = 97;
		for (UserCommand command : object) {
			JCommand jCommand = new JCommand();
			jCommand.set(command.getDisplayName(), code++);
			commandNames.add(jCommand);
		}
	}
}
