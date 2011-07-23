package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
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

public class ItemGroup implements IAccounterServerCore, Lifecycle,
		CreatableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5174008021450044426L;

	/**
	 * Object's Version
	 */
	int version;

	/**
	 * A Unique Number Assigned by Hibernate
	 */
	long id;

	/**
	 * A Secure Random unique 40 digit Number
	 */
	public long id;

	/**
	 * Item Group Name
	 */
	String name;

	transient boolean isImported;

	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	boolean isDefault;

	List<Item> items;

	transient List<Item> tempitems;

	transient private boolean isOnSaveProccessed;

	public ItemGroup() {
		// TODO
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
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
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
		accounterCore.setid(this.id);
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
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;

		if (this.items != null) {
			for (Item item : this.items) {
				item.setItemGroup(this);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
		}
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
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
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;

	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setCreatedDate(FinanceDate createdDate) {
		this.createdDate = createdDate;
	}

	public FinanceDate getCreatedDate() {
		return createdDate;
	}

	public void setLastModifiedDate(FinanceDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public FinanceDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		ItemGroup itemGroup = (ItemGroup) clientObject;
		// Query query = session.createQuery("from ItemGroup I where I.name=?")
		// .setParameter(0, itemGroup.name);
		Query query = session.getNamedQuery("getItemGroupWithSameName")
				.setParameter("name", itemGroup.name).setParameter("id",
						itemGroup.id);
		List list = query.list();
		if (list != null && list.size() > 0) {
			throw new InvalidOperationException(
					"An ItemGroup already exsits with this name");
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
