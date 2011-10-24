package com.vimukti.accounter.mobile.commands;

import java.io.IOException;

import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.mobile.Context;
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
			String clientClassSimpleName = coreObject.getObjectType()
					.getClientClassSimpleName();

			OperationContext opContext = new OperationContext(context
					.getCompany().getID(), coreObject, context.getIOSession()
					.getUserEmail());
			opContext.setArg2(clientClassSimpleName);
			new FinanceTool().create(opContext);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}
}
