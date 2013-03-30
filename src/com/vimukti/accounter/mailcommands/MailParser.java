package com.vimukti.accounter.mailcommands;

import java.util.List;

import net.sf.json.JSONObject;

public class MailParser {

	private List<MailCommand> commands;

	public MailParser(JSONObject jsonObj) {
		String emailBody = (String) jsonObj.get("body");
	}

	public List<MailCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<MailCommand> commands) {
		this.commands = commands;
	}
}
