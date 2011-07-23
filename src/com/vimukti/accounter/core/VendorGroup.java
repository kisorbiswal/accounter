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

/**
 * VendorGroup refers to the category of the vendors. By this we can know to
 * which group a particular vendor belong to.
 * 
 * @author Chandan
 * 
 */

public class VendorGroup extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315811900319594794L;
	int version;
	/**
	 * VendorGroup Category Name
	 */
	String name;

	transient boolean isImported;

	boolean isDefault;
	transient private boolean isOnSaveProccessed;

	/**
	 * @return the version
	 */
	public VendorGroup() {
		// TODO
	}

	public int getVersion() {
		return version;
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the vendorGroupName
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
		accounterCore.setObjectType(AccounterCoreType.VENDOR_GROUP);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (isImported) {
			return false;
		}
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
	public long getID() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public boolean equals(VendorGroup obj) {
		if (this.id == obj.id && this.name == obj.name) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		VendorGroup vendorGroup = (VendorGroup) clientObject;
		Query query = session.createQuery(
				"from com.vimukti.accounter.core.VendorGroup V where V.name=?")
				.setParameter(0, vendorGroup.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			VendorGroup newVendorGroup = (VendorGroup) list.get(0);
			if (vendorGroup.id != newVendorGroup.id) {
				throw new InvalidOperationException(
						"SupplierGroup already exists with this name");
			}
		}
		return true;
	}

}
