package com.vimukti.accounter.mobile.commands;

import java.io.IOException;

import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public abstract class NewAbstractCommand extends NewCommand {

	private IGlobal global;
	private AccounterConstants constants;
	private AccounterMessages messages;
	protected static final String AMOUNTS_INCLUDE_TAX = "Amounts Include tax";
	protected static final String COUNTRY = "country";
	protected static final String PHONE = "phone";
	protected static final String EMAIL = "email";
	protected static final String ACTIONS = "actions";
	protected static final int VALUES_TO_SHOW = 5;
	protected static final int COUNTRIES_TO_SHOW = 5;

	public NewAbstractCommand() {

	}

	@Override
	public void init() {
		try {
			global = new ServerGlobal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		constants = global.constants();
		messages = global.messages();
		super.init();
	}

	protected AccounterConstants getConstants() {
		return constants;
	}

	protected AccounterMessages getMessages() {
		return messages;
	}

	protected void create(IAccounterCore coreObject, Context context) {
		try {
			if (coreObject.getID() == 0) {
				String clientClassSimpleName = coreObject.getObjectType()
						.getClientClassSimpleName();

				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), coreObject, context
						.getIOSession().getUserEmail());

				opContext.setArg2(clientClassSimpleName);

				new FinanceTool().create(opContext);
			} else {
				String serverClassFullyQualifiedName = coreObject
						.getObjectType().getServerClassFullyQualifiedName();

				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), coreObject, context
						.getIOSession().getUserEmail(),
						String.valueOf(coreObject.getID()),
						serverClassFullyQualifiedName);

				new FinanceTool().update(opContext);
			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	protected long getNumberFromString(String string) {
		if (string.isEmpty()) {
			return 0;
		}
		if (string.charAt(0) != '#') {
			return 0;
		}
		string = string.substring(1);
		return Long.parseLong(string);
	}
}
