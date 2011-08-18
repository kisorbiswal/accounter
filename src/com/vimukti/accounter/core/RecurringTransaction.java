package com.vimukti.accounter.core;

import java.util.Calendar;
import java.util.Date;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Contains a referring transaction and has information for scheduling
 * procedure.
 * 
 * @author vimukti3
 * 
 */
public class RecurringTransaction extends CreatableObject implements
		IAccounterServerCore {
	public final static int HOW_OFTEN_DAY = 0;
	public final static int HOW_OFTEN_WEEK = 1;
	public final static int HOW_OFTEN_MONTH = 2;

	public final static int DUE_DATE_DAYS_AFTER_THE_INVOICE = 0;
	public final static int DUE_DATE_OF_THE_FOLLOWING_MONTH = 1;
	public final static int DUE_DATE_OF_THE_CURRENT_MONTH = 2;

	public final static int ACTION_SAVE_DRAFT = 0;
	public final static int ACTION_APPROVE = 1;
	public final static int ACTION_APPROVE_SEND = 2;

	private int howOftenType;
	private int howOftenValue;

	private FinanceDate startDate;
	private FinanceDate endDate; // Optional

	private int dueDateValue;
	private int dueDateType;

	private FinanceDate nextScheduleOn;

	/**
	 * {@link #ACTION_SAVE_DRAFT}, {@link #ACTION_APPROVE},
	 * {@link #ACTION_APPROVE_SEND}
	 */
	private int actionType;

	private Transaction referringTransaction;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4937393711538114447L;

	public RecurringTransaction() {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO
		return true;
	}

	public int getActionType() {
		return actionType;
	}

	public int getDueDateType() {
		return dueDateType;
	}

	public int getDueDateValue() {
		return dueDateValue;
	}

	public int getHowOftenType() {
		return howOftenType;
	}

	public int getHowOftenValue() {
		return howOftenValue;
	}

	private FinanceDate getInitialDateForNextSchedule() {
		return nextScheduleOn == null ? startDate : nextScheduleOn;
	}

	public FinanceDate getNextScheduleOn() {
		return nextScheduleOn;
	}

	public Transaction getReferringTransaction() {
		return referringTransaction;
	}

	/**
	 * Gives schedule iterator by {@link #howOftenType} <br>
	 * This is a factory method to generate ScheduleIterator.
	 * 
	 * @return
	 */
	private ScheduleIterator getScheduleIterator() {
		switch (howOftenType) {
		case HOW_OFTEN_DAY:
			return new DaySchedular();
		case HOW_OFTEN_WEEK:
			return new WeekShceduler();
		case HOW_OFTEN_MONTH:
			return new MonthScheduler();
		default:
			break;
		}
		return null;
	}

	private boolean isValidScheduleTime(FinanceDate date) {
		if (endDate == null) {
			return true;
		}
		// TODO need to add another condition, date may equal.
		return date.before(endDate);
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

	public void setHowOftenType(int howOftenType) {
		this.howOftenType = howOftenType;
	}

	public void setHowOftenValue(int howOftenValue) {
		this.howOftenValue = howOftenValue;
	}

	public void setNextScheduleOn(FinanceDate nextScheduleOn) {
		this.nextScheduleOn = nextScheduleOn;
	}

	public void setReferringTransaction(Transaction referringTransaction) {
		this.referringTransaction = referringTransaction;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Every ");
		sb.append(howOftenValue).append(' ');
		switch (howOftenType) {
		case HOW_OFTEN_DAY:
			sb.append("Day(s)");
			break;
		case HOW_OFTEN_MONTH:
			sb.append("Month(s)");
			break;
		case HOW_OFTEN_WEEK:
			sb.append("Week(s)");
			break;

		default:
			sb.append("-");
			break;
		}
		return sb.toString();
	}

	/**
	 * Daily scheduler.
	 * 
	 * @author vimukti3
	 * 
	 */
	private class DaySchedular implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());
			calendar.add(Calendar.DATE, howOftenValue);
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}
	}

	/**
	 * Month scheduler
	 * 
	 * @author vimukti3
	 * 
	 */
	private class MonthScheduler implements ScheduleIterator {

		@Override
		public Date next() {
			java.util.Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());
			calendar.add(Calendar.MONTH, howOftenValue);
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

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
		 * @return date for next schedule. null if there is no next schedule.
		 */
		Date next();
	}

	/**
	 * Week scheduler.
	 * 
	 * @author vimukti3
	 * 
	 */
	private class WeekShceduler implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());
			// 1week = 7 days.
			calendar.add(Calendar.DATE, howOftenValue * 7);
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}
	}
}
