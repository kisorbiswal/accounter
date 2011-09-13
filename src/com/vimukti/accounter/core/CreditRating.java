package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CreditRating extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the name of the Credit Rating.
	 */
	String name;

	boolean isDefault;

	transient private boolean isOnSaveProccessed;

	public CreditRating() {
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.CREDIT_RATING);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		super.onUpdate(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
