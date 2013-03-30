package com.vimukti.accounter.text;

import com.google.gwt.dev.util.collect.HashMap;
import com.vimukti.accounter.text.commands.CustomerCommand;
import com.vimukti.accounter.text.commands.ITextCommand;

public class CommandsFactory {

	public static final String CUSTOMER = "customer";
	public static final String INVOICE = "invoice";
	public static final String VENDOR = "vendor";
	public static final String BILL = "bill";
	public static final String ITEM = "item";
	public static final String CASH_EXPENSE = "cashexpense";

	static HashMap<String, Class<? extends ITextCommand>> commands = new HashMap<String, Class<? extends ITextCommand>>();
	static {
		init();
	}

	/**
	 * Getting the Command on Name
	 * 
	 * @param name
	 * @return {@link ITextCommand}
	 */
	public static Class<? extends ITextCommand> getCommand(String name) {
		return commands.get(name);
	}

	private static void init() {

		addCommand(CustomerCommand.class, "customer", "create customer",
				"update customer");

		addCommand(CustomerCommand.class, "invoice", "create invoice",
				"update invoice");

		addCommand(CustomerCommand.class, "vendor", "create vendor",
				"update vendor");

		addCommand(CustomerCommand.class, "item", "create item", "update item");

		addCommand(CustomerCommand.class, "cashexpense", "create cashexpense",
				"update cashexpense");

	}

	private static void addCommand(Class<CustomerCommand> cls, String... keys) {
		for (String key : keys) {
			commands.put(key, cls);
		}
	}
}
