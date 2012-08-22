package com.vimukti.accounterbb.ui;

import com.vimukti.accounterbb.result.Command;
import com.vimukti.accounterbb.result.CommandList;

public interface CommandSelectionListener {
	public void commandSelected(CommandList list, Command command);
}
