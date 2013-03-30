package com.vimukti.accounter.text;

import com.vimukti.accounter.text.commands.CustomerCommand;
import com.vimukti.accounter.text.commands.ITextCommand;

public class CommandsFactory {

	public static final String CUSTOMER = "customer";
	public static final String INVOICE = "invoice";
	public static final String VENDOR = "vendor";
	public static final String BILL = "bill";
	public static final String ITEM = "item";

	public static Class<? extends ITextCommand> getCommand(String name) {
		if (CUSTOMER.equals(name)) {
			return CustomerCommand.class;
		}
		return null;
	}
}
