package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ItemGroup extends CreatableObject implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5174008021450044426L;

	
	/**
	 * Item Group Name
	 */
	String name;

	boolean isDefault;

	List<Item> items;

	transient List<Item> tempitems;

	transient private boolean isOnSaveProccessed;

	public ItemGroup() {
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
	 * return false; return false;
	 * 
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
		accounterCore.setObjectType(AccounterCoreType.ITEM_GROUP);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		this.tempitems = new ArrayList<Item>();
		if (this.items != null)
			this.tempitems.addAll(this.items);
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;

		if (this.items != null) {
			for (Item item : this.items) {
				item.setItemGroup(this);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		super.onUpdate(arg0);
		if (this.items != null) {
			for (Item item : this.items) {
				tempitems.remove(item);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
			for (Item item : this.tempitems) {
				item.setItemGroup(null);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
		}
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		ItemGroup itemGroup = (ItemGroup) clientObject;
		// Query query = session.createQuery("from ItemGroup I where I.name=?")
		// .setParameter(0, itemGroup.name);
		Query query = session.getNamedQuery("getItemGroupWithSameName")
				.setParameter("name", itemGroup.name)
				.setParameter("id", itemGroup.id);
		List list = query.list();
		if (list != null && list.size() > 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
			// "An ItemGroup already exsits with this name");
		}

		return true;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
