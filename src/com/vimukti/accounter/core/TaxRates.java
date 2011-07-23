package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.InvalidOperationException;

public class TaxRates implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5076472320042972936L;

	int version;

	long id;

	public long id;

	/**
	 * The amount of rate which should be imposed on any transaction at the
	 * given asOf FinanceDate.
	 */
	double rate;

	/**
	 * The date from which the given TaxRate should be imposed on the
	 * transaction.
	 */
	FinanceDate asOf;

	transient boolean isImported;

	transient private boolean isOnSaveProccessed;

	public TaxRates() {
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
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @return the asOf
	 */
	public FinanceDate getAsOf() {
		return asOf;
	}

	public void setRate(double rate) {

		this.rate = rate;
	}

	public void setAsOf(FinanceDate asOf) {
		this.asOf = asOf;

	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
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

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
