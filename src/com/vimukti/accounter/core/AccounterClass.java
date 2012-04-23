package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class AccounterClass extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	private String className;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!goingToBeEdit) {
			checkNullValues();
		}
		return false;
	}

	private void checkNullValues() throws AccounterException {
		if (className == null || className.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().accounterClass());
		}
	}

	public String getclassName() {
		return className;
	}

	public void setclassName(String trackingClassName) {
		this.className = trackingClassName;
	}

	@Override
	public String getName() {
		return className;
	}

	@Override
	public void setName(String name) {
		this.className = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.ACCOUNTER_CLASS;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.name(), this.className);
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.ACCOUNTER_CLASS);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (className == null || className.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().accounterClass());
		}
	}
}
