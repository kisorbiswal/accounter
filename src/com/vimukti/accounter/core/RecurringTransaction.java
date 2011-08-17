package com.vimukti.accounter.core;

import java.util.Calendar;
import java.util.Date;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class RecurringTransaction implements IAccounterCore {
	public final static int HOW_OFTEN_DAILY = 0;
	public final static int HOW_OFTEN_EVERY_OTHER_DAY = 1;
	public final static int HOW_OFTEN_WEEKLY = 2;
	public final static int HOW_OFTEN_EVERY_OTHER_WEEK = 3;
	public final static int HOW_OFTEN_EVERY_FOUR_WEEK = 4;

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

	private Date nextScheduleOn;

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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getNextScheduleOn() {
		return nextScheduleOn;
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
		switch (howOftenType) {
		case HOW_OFTEN_DAILY:
			return new DailyScheduler();
		default:
			break;
		}
		return null;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Transaction getTransaction() {
		return transaction;
	}

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

	public void setNextScheduleOn(Date nextScheduleOn) {
		this.nextScheduleOn = nextScheduleOn;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * This is for scheduling.
	 * 
	 * @author vimukti3
	 * 
	 */
	private interface ScheduleIterator {

		/**
		 * Gives next scheduling date/time.
		 * 
		 * @return
		 */
		Date next();
	}

	/**
	 * Daily scheduler.
	 * 
	 * @author vimukti3
	 * 
	 */
	private class DailyScheduler implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			if(nextScheduleOn==null){
				calendar.setTime(startDate);
			}else{
				calendar.setTime(nextScheduleOn);
			}
			calendar.add(Calendar.DATE, 1);
			return calendar.getTime();
		}

	}

}
