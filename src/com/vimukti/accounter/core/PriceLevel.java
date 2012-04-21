package com.vimukti.accounter.core;

import java.io.Serializable;

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

public class PriceLevel extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5616443741745926670L;

	/**
	 * This field stores the name of the Price Level
	 */
	String name;

	/**
	 * Price Level Percentage
	 */
	double percentage;

	boolean isPriceLevelDecreaseByThisPercentage;

	boolean isDefault;

	public PriceLevel() {

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

	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * @return the isPriceLevelDecreaseByThisPercentage
	 */
	public boolean isPriceLevelDecreaseByThisPercentage() {
		return isPriceLevelDecreaseByThisPercentage;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.PRICE_LEVEL);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
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
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// Session session = HibernateUtil.getCurrentSession();
		// PriceLevel priceLevel = (PriceLevel) clientObject;
		// Query query = session.getNamedQuery("getPriceLevel.by.Name")
		// .setParameter(0, priceLevel.name);
		// List list = query.list();
		// if (list != null && list.size() > 0) {
		// PriceLevel newPriceLevel = (PriceLevel) list.get(0);
		// if (priceLevel.id != newPriceLevel.id) {
		// throw new AccounterException(
		// AccounterException.ERROR_NAME_CONFLICT);
		// // "PriceLevel already exists with this name");
		// }
		// }

		return true;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.PRICE_LEVEL;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.priceLevel()).gap();

		w.put(messages.name(), this.name);
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
