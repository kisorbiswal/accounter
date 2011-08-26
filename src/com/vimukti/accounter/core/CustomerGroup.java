package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CustomerGroup extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the Group
	 */
	String name;
	boolean isDefault;

	transient private boolean isOnSaveProccessed;

	public CustomerGroup() {
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.CUSTOMER_GROUP);
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
		super.onSave(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		CustomerGroup customerGroup = (CustomerGroup) clientObject;
		Query query = session
				.getNamedQuery("getListofNames.from.customerGroup")
				.setParameter(0, customerGroup.name);
		List list = query.list();
		if (list.size() > 0 && list != null) {
			CustomerGroup newCustomerGroup = (CustomerGroup) list.get(0);
			if (customerGroup.id != newCustomerGroup.id) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A CustomerGroup already exists with this name");
			}
		}

		return true;

	}

}
