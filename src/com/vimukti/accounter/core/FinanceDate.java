package com.vimukti.accounter.core;

import java.util.Calendar;
import java.util.Date;

public class FinanceDate {

	String stringDate;
	StringBuffer stringBuffer;
	String year, month, date, dates;
	long time;

	public FinanceDate(long date) {
		initDate(date);
	}

	// public long getStringAsLong(String stringdate) {
	// String string[] = stringdate.split("-");
	// this.year = string[0];
	// this.month = string[1];
	// this.day = string[2];
	//
	// return Long.parseLong(this.year + this.month + this.day);
	//
	// }

	public FinanceDate(String stringdate) {
		this(new Date(stringdate));
	}

	public FinanceDate() {
		initDate(new Date());
	}

	public void initDate(long date) {
		if (date == 0) {
			this.time = date;
			return;
		}
		this.time = date;
		stringBuffer = new StringBuffer(Long.toString(date));
		this.year = stringBuffer.substring(0, 4);
		this.month = stringBuffer.substring(4, 6);
		this.date = stringBuffer.substring(6, 8);
	}

	public FinanceDate(Date date) {
		initDate(date);
	}

	public void initDate(Date date) {
		if (date != null) {
			this.year = "" + (date.getYear() + 1900);
			this.month = "" + (date.getMonth() + 1);
			this.date = "" + date.getDate();

			this.month = this.month.length() == 1 ? "0" + this.month
					: this.month;
			this.date = this.date.length() == 1 ? "0" + this.date : this.date;

			this.time = Long.parseLong(this.year + this.month + this.date);
		}
	}

	public int getYear() {
		return Integer.parseInt(this.year) - 1900;
	}

	public void setYear(long year) {
		this.year = String.valueOf(year + 1900);
		synchornizeTime();
	}

	public int getMonth() {
		return (Integer.parseInt(this.month) - 1);
	}

	public void setMonth(long month) {
		this.month = String.valueOf(month);
		Date dt = new Date(Integer.parseInt(this.year) - 1900, 0, Integer
				.parseInt(this.date));
		dt.setMonth(Integer.parseInt(this.month));

		synchornizeDate(dt);
	}

	public int getDay() {
		return getAsDateObject().getDay();
	}

	public void setDate(long day) {
		this.date = String.valueOf(day);

		Date dt = new Date(Integer.parseInt(this.year) - 1900, Integer
				.parseInt(this.month), 0);
		dt.setDate(Integer.parseInt(this.date));

		synchornizeDate(dt);
	}

	private void synchornizeDate(Date dt) {
		this.year = "" + (dt.getYear() + 1900);
		this.month = "" + (dt.getMonth() + 1);
		this.date = "" + dt.getDate();

		synchornizeTime();
	}

	public long getDate() {
		return this.time;
	}

	@SuppressWarnings("deprecation")
	public Date getAsDateObject() {
		Calendar calendar = Calendar.getInstance();
		calendar.set((Integer.parseInt(this.year)), (Integer
				.parseInt(this.month) - 1), (Integer.parseInt(this.date)));
		Date financeDate = calendar.getTime();
		financeDate.setHours(00);
		financeDate.setMinutes(00);
		financeDate.setSeconds(00);
		return financeDate;

	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents midnight, local time, at the beginning of the day specified by
	 * the <code>year</code>, <code>month</code>, and <code>date</code>
	 * arguments.
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @see java.util.Calendar
	 */
	public FinanceDate(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents the instant at the start of the minute specified by the
	 * <code>year</code>, <code>month</code>, <code>date</code>,
	 * <code>hrs</code>, and <code>min</code> arguments, in the local time zone.
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @param hrs
	 *            the hours between 0-23.
	 * @param min
	 *            the minutes between 0-59.
	 * @see java.util.Calendar
	 */
	public FinanceDate(int year, int month, int date, int hrs, int min) {
		this(year, month, date, hrs, min, 0);
	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents the instant at the start of the second specified by the
	 * <code>year</code>, <code>month</code>, <code>date</code>,
	 * <code>hrs</code>, <code>min</code>, and <code>sec</code> arguments, in
	 * the local time zone.
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @param hrs
	 *            the hours between 0-23.
	 * @param min
	 *            the minutes between 0-59.
	 * @param sec
	 *            the seconds between 0-59.
	 */
	public FinanceDate(int year, int month, int date, int hrs, int min, int sec) {
		this(new Date(year, month, date, hrs, min, sec));
	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents the date and time indicated by the string <code>s</code>,
	 * which is interpreted as if by the {@link Date#parse} method.
	 * 
	 * @param s
	 *            a string representation of the date.
	 * @see java.text.DateFormat
	 * @see java.util.Date#parse(java.lang.String)
	 */
	// public FinanceDate(String s) {
	// this(parse(s));
	// }
	//
	// private static long parse(String s) {
	// return 0;
	// }

	/**
	 * Determines the date and time based on the arguments. The arguments are
	 * interpreted as a year, month, day of the month, hour of the day, minute
	 * within the hour, and second within the minute, exactly as for the
	 * <tt>Date</tt> constructor with six arguments, except that the arguments
	 * are interpreted relative to UTC rather than to the local time zone. The
	 * time indicated is returned represented as the distance, measured in
	 * milliseconds, of that time from the epoch (00:00:00 GMT on January 1,
	 * 1970).
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @param hrs
	 *            the hours between 0-23.
	 * @param min
	 *            the minutes between 0-59.
	 * @param sec
	 *            the seconds between 0-59.
	 * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 *         for the date and time specified by the arguments.
	 * @see java.util.Calendar
	 */
	public static long UTC(int year, int month, int date, int hrs, int min,
			int sec) {

		return 0;
	}

	/**
	 * Sets the day of the month of this <tt>Date</tt> object to the specified
	 * value. This <tt>Date</tt> object is modified so that it represents a
	 * point in time within the specified day of the month, with the year,
	 * month, hour, minute, and second the same as before, as interpreted in the
	 * local time zone. If the date was April 30, for example, and the date is
	 * set to 31, then it will be treated as if it were on May 1, because April
	 * has only 30 days.
	 * 
	 * @param date
	 *            the day of the month value between 1-31.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.set(Calendar.DAY_OF_MONTH, int date)</code>.
	 */
	// @Deprecated
	// public void setDate(int date) {
	// getCalendarDate().setDayOfMonth(date);
	// }

	/**
	 * Returns the day of the week represented by this date. The returned value
	 * (<tt>0</tt> = Sunday, <tt>1</tt> = Monday, <tt>2</tt> = Tuesday,
	 * <tt>3</tt> = Wednesday, <tt>4</tt> = Thursday, <tt>5</tt> = Friday,
	 * <tt>6</tt> = Saturday) represents the day of the week that contains or
	 * begins with the instant in time represented by this <tt>Date</tt> object,
	 * as interpreted in the local time zone.
	 * 
	 * @return the day of the week represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.DAY_OF_WEEK)</code>.
	 */
	// @Deprecated
	// public int getDay() {
	// return normalize().getDayOfWeek() - gcal.SUNDAY;
	// }

	/**
	 * Returns the hour represented by this <tt>Date</tt> object. The returned
	 * value is a number (<tt>0</tt> through <tt>23</tt>) representing the hour
	 * within the day that contains or begins with the instant in time
	 * represented by this <tt>Date</tt> object, as interpreted in the local
	 * time zone.
	 * 
	 * @return the hour represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.HOUR_OF_DAY)</code>.
	 */

	/**
	 * Sets the hour of this <tt>Date</tt> object to the specified value. This
	 * <tt>Date</tt> object is modified so that it represents a point in time
	 * within the specified hour of the day, with the year, month, date, minute,
	 * and second the same as before, as interpreted in the local time zone.
	 * 
	 * @param hours
	 *            the hour value.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.set(Calendar.HOUR_OF_DAY, int hours)</code>.
	 */

	/**
	 * Returns the number of minutes past the hour represented by this date, as
	 * interpreted in the local time zone. The value returned is between
	 * <code>0</code> and <code>59</code>.
	 * 
	 * @return the number of minutes past the hour represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.MINUTE)</code>.
	 */

	/**
	 * Sets the minutes of this <tt>Date</tt> object to the specified value.
	 * This <tt>Date</tt> object is modified so that it represents a point in
	 * time within the specified minute of the hour, with the year, month, date,
	 * hour, and second the same as before, as interpreted in the local time
	 * zone.
	 * 
	 * @param minutes
	 *            the value of the minutes.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.set(Calendar.MINUTE, int minutes)</code>.
	 */

	/**
	 * Returns the number of seconds past the minute represented by this date.
	 * The value returned is between <code>0</code> and <code>61</code>. The
	 * values <code>60</code> and <code>61</code> can only occur on those Java
	 * Virtual Machines that take leap seconds into account.
	 * 
	 * @return the number of seconds past the minute represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.SECOND)</code>.
	 */

	/**
	 * Sets this <code>Date</code> object to represent a point in time that is
	 * <code>time</code> milliseconds after January 1, 1970 00:00:00 GMT.
	 * 
	 * @param time
	 *            the number of milliseconds.
	 */

	/**
	 * Tests if this date is before the specified date.
	 * 
	 * @param when
	 *            a date.
	 * @return <code>true</code> if and only if the instant of time represented
	 *         by this <tt>Date</tt> object is strictly earlier than the instant
	 *         represented by <tt>when</tt>; <code>false</code> otherwise.
	 * @exception NullPointerException
	 *                if <code>when</code> is null.
	 */
	public boolean before(FinanceDate when) {
		return this.time < when.time;
	}

	/**
	 * Tests if this date is after the specified date.
	 * 
	 * @param when
	 *            a date.
	 * @return <code>true</code> if and only if the instant represented by this
	 *         <tt>Date</tt> object is strictly later than the instant
	 *         represented by <tt>when</tt>; <code>false</code> otherwise.
	 * @exception NullPointerException
	 *                if <code>when</code> is null.
	 */
	public boolean after(FinanceDate when) {
		return this.time > when.time;
	}

	/**
	 * Compares two dates for equality. The result is <code>true</code> if and
	 * only if the argument is not <code>null</code> and is a <code>Date</code>
	 * object that represents the same point in time, to the millisecond, as
	 * this object.
	 * <p>
	 * Thus, two <code>Date</code> objects are equal if and only if the
	 * <code>getTime</code> method returns the same <code>long</code> value for
	 * both.
	 * 
	 * @param obj
	 *            the object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 * @see java.util.Date#getTime()
	 */
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}

	/**
	 * Compares two Dates for ordering.
	 * 
	 * @param anotherDate
	 *            the <code>Date</code> to be compared.
	 * @return the value <code>0</code> if the argument Date is equal to this
	 *         Date; a value less than <code>0</code> if this Date is before the
	 *         Date argument; and a value greater than <code>0</code> if this
	 *         Date is after the Date argument.
	 * @since 1.2
	 * @exception NullPointerException
	 *                if <code>anotherDate</code> is null.
	 */
	public int compareTo(FinanceDate anotherDate) {
		return (this.time < anotherDate.time ? -1
				: (this.time == anotherDate.time ? 0 : 1));

	}

	public int hashCode() {
		return (int) this.time;
	}

	public String toString() {
		return this.date + "/" + this.month + "/" + this.year;
	}

	public void setTime(FinanceDate time) {
		this.time = time.time;
		this.year = time.year;
		this.month = time.month;
		this.date = time.date;

	}

	public long getTime() {
		return this.time;
	}

	public void initDate(long year, long month, long day) {
		this.year = String.valueOf(year);
		this.month = String.valueOf(month);
		this.date = String.valueOf(day);
		synchornizeTime();
	}

	public final void clear() {
		this.time = 0l;
	}

	private void synchornizeTime() {

		this.month = this.month.length() == 1 ? "0" + this.month : this.month;
		this.date = this.date.length() == 1 ? "0" + this.date : this.date;

		this.time = Long.parseLong(this.year + this.month + this.date);
	}

}
