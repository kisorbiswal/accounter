package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

public class ShippingMethod extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5964725345397949514L;

	/**
	 * Object Version
	 */
	int version;

	/**
	 * Shipping Method Name
	 */
	String name;

	/**
	 * Shipping Method Description
	 */
	String description;

	boolean isDefault;

	transient private boolean isOnSaveProccessed;

	public ShippingMethod() {

	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
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

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.SHIPPING_METHOD);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		ShippingMethod shippingMethod = (ShippingMethod) clientObject;
		Query query = session
				.createQuery(
						"from com.vimukti.accounter.core.ShippingMethod S where S.name=?")
				.setParameter(0, shippingMethod.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			ShippingMethod newShippingMethod = (ShippingMethod) list.get(0);
			if (shippingMethod.id != newShippingMethod.id) {
				throw new InvalidOperationException(
						"A ShippingMethod already exists with this name");
			}
		}
		return true;
	}
}
