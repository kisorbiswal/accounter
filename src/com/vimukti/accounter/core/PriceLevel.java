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

public class PriceLevel implements IAccounterServerCore, Lifecycle,
		ICreatableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5616443741745926670L;

	/**
	 * {@link PriceLevel} Object Version
	 */
	int version;

	long id;

	/**
	 * A unique Secure 40 Digit, Random Number
	 */
	public long id;

	/**
	 * This field stores the name of the Price Level
	 */
	String name;

	/**
	 * Price Level Percentage
	 */
	double percentage;

	boolean isPriceLevelDecreaseByThisPercentage;

	transient boolean isImported;

	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	boolean isDefault;

	transient private boolean isOnSaveProccessed;

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
		accounterCore.setid(this.id);
		accounterCore.setObjectType(AccounterCoreType.PRICE_LEVEL);
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
	public long getID(){
		// TODO Auto-generated method stub
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
		PriceLevel priceLevel = (PriceLevel) clientObject;
		Query query = session.createQuery(
				"from com.vimukti.accounter.core.PriceLevel P where P.name=?")
				.setParameter(0, priceLevel.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			PriceLevel newPriceLevel = (PriceLevel) list.get(0);
			if (priceLevel.id != newPriceLevel.id) {
				throw new InvalidOperationException(
						"PriceLevel already exists with this name");
			}
		}

		return true;
	}
}
