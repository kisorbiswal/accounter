package com.vimukti.accounter.text;

import java.util.HashMap;

import com.vimukti.accounter.text.commands.CustomerCommand;
import com.vimukti.accounter.text.commands.ITextCommand;
import com.vimukti.accounter.text.commands.ItemCommand;
import com.vimukti.accounter.text.commands.VendorCommand;
import com.vimukti.accounter.text.commands.transaction.CashExpenseCommand;
import com.vimukti.accounter.text.commands.transaction.CompanyCommand;
import com.vimukti.accounter.text.commands.transaction.EnterBillCommand;
import com.vimukti.accounter.text.commands.transaction.InvoiceCommand;
import com.vimukti.accounter.text.commands.transaction.PayBillCommand;
import com.vimukti.accounter.text.commands.transaction.ReceivePaymentCommand;

public class CommandsFactory {

	public static final String CUSTOMER = "customer";
	public static final String INVOICE = "invoice";
	public static final String VENDOR = "vendor";
	public static final String BILL = "bill";
	public static final String ITEM = "item";
	public static final String CASH_EXPENSE = "cashexpense";
	public static final String ACCOUNT = "account";

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
		if (name == null) {
			return null;
		}
		name = name.replace(" ", "").toLowerCase().trim();
		return commands.get(name);
	}

	private static void init() {

		addCommand(CustomerCommand.class, "customer", "createcustomer",
				"newcustomer", "updatecustomer");

		addCommand(InvoiceCommand.class, "invoice", "createinvoice",
				"newinvoice", "updateinvoice");

		addCommand(VendorCommand.class, "vendor", "createvendor", "newvendor",
				"updatevendor");

		addCommand(ItemCommand.class, "item", "createitem", "neweitem",
				"updateitem");

		addCommand(CashExpenseCommand.class, "cashexpense",
				"createcashexpense", "newcashexpense", "updatecashexpense");

		addCommand(CompanyCommand.class, "company", "createcompany",
				"newcompany", "updatecompany");

		addCommand(EnterBillCommand.class, "enterbill", "createenterbill",
				"newenterbill", "updateenterbill");

		addCommand(ReceivePaymentCommand.class, "receivepayment",
				"createreceivepayment", "newreceivepayment",
				"updatereceivepayment");

		addCommand(PayBillCommand.class, "paybill", "createpaybill",
				"newpaybill", "updatepaybill");

	}

	private static void addCommand(Class<? extends ITextCommand> cls,
			String... keys) {
		for (String key : keys) {
			commands.put(key, cls);
		}
	}
}
