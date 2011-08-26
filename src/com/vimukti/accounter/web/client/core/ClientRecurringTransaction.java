package com.vimukti.accounter.web.client.core;

import java.util.Date;

import com.vimukti.accounter.web.client.ui.core.Calendar;

public class ClientRecurringTransaction implements IAccounterCore {
	private static final long serialVersionUID = 1L;

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

	private long id;

	private long startDate;
	private long endDate; // Optional

	private int dueDateValue;
	private int dueDateType;

	private long nextScheduleOn;
	
	//Extra client fields, will be used for displaying in grid
	private double refTransactionTotal;
	private int refTransactionType;

	/**
	 * {@link #ACTION_SAVE_DRAFT}, {@link #ACTION_APPROVE},
	 * {@link #ACTION_APPROVE_SEND}
	 */
	private int actionType;
	private String name;

	private long referringTransaction;

	private int version;

	public ClientRecurringTransaction() {
		// TODO Auto-generated constructor stub
	}

	// @Override
	// public boolean canEdit(IAccounterServerCore clientObject)
	// throws AccounterException {
	// // TODO
	// return true;
	// }

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

	private long getInitialDateForNextSchedule() {
		return nextScheduleOn == 0 ? getStartDate() : nextScheduleOn;
	}

	public long getNextScheduleOn() {
		return nextScheduleOn;
	}

	public long getReferringTransaction() {
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

	private boolean isValidScheduleTime(ClientFinanceDate date) {
		if (getEndDate() == 0) {
			return true;
		}
		// TODO need to add another condition, date may equal.
		return date.before(new ClientFinanceDate(getEndDate()));
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

	public void setNextScheduleOn(long nextScheduleOn) {
		this.nextScheduleOn = nextScheduleOn;
	}

	public void setReferringTransaction(long referringTransaction) {
		this.referringTransaction = referringTransaction;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(" - Every ");
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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
			calendar.setTime(new Date(getInitialDateForNextSchedule()));
			calendar.add(Calendar.DATE, howOftenValue);
			return isValidScheduleTime(new ClientFinanceDate(calendar.getTime())) ? calendar
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
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(getInitialDateForNextSchedule()));
			calendar.add(Calendar.MONTH, howOftenValue);
			return isValidScheduleTime(new ClientFinanceDate(calendar.getTime())) ? calendar
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
			calendar.setTime(new Date(getInitialDateForNextSchedule()));
			// 1week = 7 days.
			calendar.add(Calendar.DATE, howOftenValue * 7);
			return isValidScheduleTime(new ClientFinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECURRING_TRANSACTION;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientRecurringTransaction";
	}

	public String getFrequencyString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Every ");
		if (howOftenValue == 1) {
			// Do nothing
		} else if (howOftenValue == 2) {
			sb.append("Other ");
		}else {
			sb.append(howOftenValue).append(' ');
		}

		switch (howOftenType) {
		case HOW_OFTEN_DAY:
			sb.append("Day");
			break;
		case HOW_OFTEN_WEEK:
			sb.append("Week");
			break;
		case HOW_OFTEN_MONTH:
			sb.append("Month");
			break;
		default:
			break;
		}

		if (howOftenValue >= 3) {
			sb.append('s');
		}

		return sb.toString();
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setRefTransactionTotal(double transactionTotal) {
		this.refTransactionTotal = transactionTotal;
	}

	public double getRefTransactionTotal() {
		return refTransactionTotal;
	}

	public void setRefTransactionType(int transactionType) {
		this.refTransactionType = transactionType;
	}

	public int getRefTransactionType() {
		return refTransactionType;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

		
}
