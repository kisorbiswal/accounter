package com.vimukti.accounter.core;

import java.util.Date;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class RecurringTransaction implements IAccounterCore, ScheduleIterator {
	public final static int HOW_OFTEN_DAILY = 0;
	public final static int HOW_OFTEN_WEEKLY = 1;

	public final static int DEU_DATE_DAYS_AFTER_THE_INVOICE = 0;
	public final static int DEU_DATE_OF_THE_FOLLOWING_MONTH = 1;

	public final static int ACTION_SAVE_DRAFT = 0;
	public final static int ACTION_APPROVE = 1;
	public final static int ACTION_APPROVE_SEND = 2;

	private int howOftenType;
	private int howOftenValue;

	private Date startDate;
	private Date endDate;

	private int dueDateValue;
	private int dueDateType;

	private Date lastScheduledOn;

	/**
	 * {@link #ACTION_SAVE_DRAFT}, {@link #ACTION_APPROVE},
	 * {@link #ACTION_APPROVE_SEND}
	 */
	private int actionType;

	private Transaction transaction;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4937393711538114447L;

	public RecurringTransaction() {

	}

	public int getActionType() {
		return actionType;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDueDateType() {
		return dueDateType;
	}

	public int getDueDateValue() {
		return dueDateValue;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getHowOftenType() {
		return howOftenType;
	}

	public int getHowOftenValue() {
		return howOftenValue;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Date getLastScheduledOn() {
		return lastScheduledOn;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gives schedule iterator by {@link #howOftenType} <br>
	 * This is a factory method to generate ScheduleIterator.
	 * 
	 * @return
	 */
	private ScheduleIterator getScheduleIterator() {
		// TODO create related sheduleIterators
		return null;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public Date next() {
		return getScheduleIterator().next();
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public void setDueDateType(int dueDateType) {
		this.dueDateType = dueDateType;
	}

	public void setDueDateValue(int dueDateValue) {
		this.dueDateValue = dueDateValue;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setHowOftenType(int howOftenType) {
		this.howOftenType = howOftenType;
	}

	public void setHowOftenValue(int howOftenValue) {
		this.howOftenValue = howOftenValue;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	public void setLastScheduledOn(Date lastScheduledOn) {
		this.lastScheduledOn = lastScheduledOn;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
