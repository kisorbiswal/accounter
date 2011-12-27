package com.vimukti.accounter.web.client.util;

import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.vimukti.accounter.web.client.Global;

public class DayAndMonthUtil {
	private static final DateTimeFormatInfo a = Global.get()
			.createDateTimeFormatInfo();
	private static String[] weekdaysFull = a.weekdaysFull();
	private static String[] weekdaysShort = a.weekdaysShort();
	private static String[] monthNameFull = a.monthsFull();
	private static String[] monthNameShort = a.monthsShort();
	private static String sunday = weekdaysFull[0];
	private static String monday = weekdaysFull[1];
	private static String tuesday = weekdaysFull[2];
	private static String wednesday = weekdaysFull[3];
	private static String thursday = weekdaysFull[4];
	private static String friday = weekdaysFull[5];
	private static String saturday = weekdaysFull[6];
	private static String january = monthNameFull[0];
	private static String february = monthNameFull[1];
	private static String march = monthNameFull[2];
	private static String april = monthNameFull[3];
	private static String may_full = monthNameFull[4];
	private static String june = monthNameFull[5];
	private static String july = monthNameFull[6];
	private static String august = monthNameFull[7];
	private static String september = monthNameFull[8];
	private static String october = monthNameFull[9];
	private static String november = monthNameFull[10];
	private static String december = monthNameFull[11];
	private static String jan = monthNameShort[0];
	private static String feb = monthNameShort[1];
	private static String mar = monthNameShort[2];
	private static String apr = monthNameShort[3];
	private static String may_short = monthNameShort[4];
	private static String jun = monthNameShort[5];
	private static String jul = monthNameShort[6];
	private static String aug = monthNameShort[7];
	private static String sep = monthNameShort[8];
	private static String oct = monthNameShort[9];
	private static String nov = monthNameShort[10];
	private static String dec = monthNameShort[11];

	/**
	 * @return the monday
	 */
	public static String monday() {
		return monday;
	}

	/**
	 * @param monday
	 *            the monday to set
	 */
	public static void setMonday(String monday) {
		DayAndMonthUtil.monday = monday;
	}

	/**
	 * @return the sunday
	 */
	public static String sunday() {
		return sunday;
	}

	/**
	 * @param sunday
	 *            the sunday to set
	 */
	public static void setSunday(String sunday) {
		DayAndMonthUtil.sunday = sunday;
	}

	/**
	 * @return the tuesday
	 */
	public static String tuesday() {
		return tuesday;
	}

	/**
	 * @param tuesday
	 *            the tuesday to set
	 */
	public static void setTuesday(String tuesday) {
		DayAndMonthUtil.tuesday = tuesday;
	}

	/**
	 * @return the thursday
	 */
	public static String thursday() {
		return thursday;
	}

	/**
	 * @param thursday
	 *            the thursday to set
	 */
	public static void setThursday(String thursday) {
		DayAndMonthUtil.thursday = thursday;
	}

	/**
	 * @return the december
	 */
	public static String december() {
		return december;
	}

	/**
	 * @param december
	 *            the december to set
	 */
	public static void setDecember(String december) {
		DayAndMonthUtil.december = december;
	}

	/**
	 * @return the wednesday
	 */
	public static String wednesday() {
		return wednesday;
	}

	/**
	 * @param wednesday
	 *            the wednesday to set
	 */
	public static void setWednesday(String wednesday) {
		DayAndMonthUtil.wednesday = wednesday;
	}

	/**
	 * @return the friday
	 */
	public static String friday() {
		return friday;
	}

	/**
	 * @param friday
	 *            the friday to set
	 */
	public static void setFriday(String friday) {
		DayAndMonthUtil.friday = friday;
	}

	/**
	 * @return the saturday
	 */
	public static String saturday() {
		return saturday;
	}

	/**
	 * @param saturday
	 *            the saturday to set
	 */
	public static void setSaturday(String saturday) {
		DayAndMonthUtil.saturday = saturday;
	}

	/**
	 * @return the january
	 */
	public static String january() {
		return january;
	}

	/**
	 * @param january
	 *            the january to set
	 */
	public static void setJanuary(String january) {
		DayAndMonthUtil.january = january;
	}

	/**
	 * @return the february
	 */
	public static String february() {
		return february;
	}

	/**
	 * @param february
	 *            the february to set
	 */
	public static void setFebruary(String february) {
		DayAndMonthUtil.february = february;
	}

	/**
	 * @return the march
	 */
	public static String march() {
		return march;
	}

	/**
	 * @param march
	 *            the march to set
	 */
	public static void setMarch(String march) {
		DayAndMonthUtil.march = march;
	}

	/**
	 * @return the april
	 */
	public static String april() {
		return april;
	}

	/**
	 * @param april
	 *            the april to set
	 */
	public static void setApril(String april) {
		DayAndMonthUtil.april = april;
	}

	/**
	 * @return the may
	 */
	public static String may_full() {
		return may_full;
	}

	/**
	 * @param may
	 *            the may to set
	 */
	public static void setMay_full(String may_full) {
		DayAndMonthUtil.may_full = may_full;
	}

	/**
	 * @return the june
	 */
	public static String june() {
		return june;
	}

	/**
	 * @param june
	 *            the june to set
	 */
	public static void setJune(String june) {
		DayAndMonthUtil.june = june;
	}

	/**
	 * @return the july
	 */
	public static String july() {
		return july;
	}

	/**
	 * @param july
	 *            the july to set
	 */
	public static void setJuly(String july) {
		DayAndMonthUtil.july = july;
	}

	/**
	 * @return the august
	 */
	public static String august() {
		return august;
	}

	/**
	 * @param august
	 *            the august to set
	 */
	public static void setAugust(String august) {
		DayAndMonthUtil.august = august;
	}

	/**
	 * @return the september
	 */
	public static String september() {
		return september;
	}

	/**
	 * @param september
	 *            the september to set
	 */
	public static void setSeptember(String september) {
		DayAndMonthUtil.september = september;
	}

	/**
	 * @return the october
	 */
	public static String october() {
		return october;
	}

	/**
	 * @param october
	 *            the october to set
	 */
	public static void setOctober(String october) {
		DayAndMonthUtil.october = october;
	}

	/**
	 * @return the november
	 */
	public static String november() {
		return november;
	}

	/**
	 * @param november
	 *            the november to set
	 */
	public static void setNovember(String november) {
		DayAndMonthUtil.november = november;
	}

	/**
	 * @return the jan
	 */
	public static String jan() {
		return jan;
	}

	/**
	 * @param jan
	 *            the jan to set
	 */
	public static void setJan(String jan) {
		DayAndMonthUtil.jan = jan;
	}

	/**
	 * @return the feb
	 */
	public static String feb() {
		return feb;
	}

	/**
	 * @param feb
	 *            the feb to set
	 */
	public static void setFeb(String feb) {
		DayAndMonthUtil.feb = feb;
	}

	/**
	 * @return the mar
	 */
	public static String mar() {
		return mar;
	}

	/**
	 * @param mar
	 *            the mar to set
	 */
	public static void setMar(String mar) {
		DayAndMonthUtil.mar = mar;
	}

	/**
	 * @return the apr
	 */
	public static String apr() {
		return apr;
	}

	/**
	 * @param apr
	 *            the apr to set
	 */
	public static void setApr(String apr) {
		DayAndMonthUtil.apr = apr;
	}

	/**
	 * @return the jun
	 */
	public static String jun() {
		return jun;
	}

	/**
	 * @param jun
	 *            the jun to set
	 */
	public static void setJun(String jun) {
		DayAndMonthUtil.jun = jun;
	}

	/**
	 * @return the jul
	 */
	public static String jul() {
		return jul;
	}

	/**
	 * @param jul
	 *            the jul to set
	 */
	public static void setJul(String jul) {
		DayAndMonthUtil.jul = jul;
	}

	/**
	 * @return the aug
	 */
	public static String aug() {
		return aug;
	}

	/**
	 * @param aug
	 *            the aug to set
	 */
	public static void setAug(String aug) {
		DayAndMonthUtil.aug = aug;
	}

	/**
	 * @return the sept
	 */
	public static String sep() {
		return sep;
	}

	/**
	 * @return the oct
	 */
	public static String oct() {
		return oct;
	}

	/**
	 * @param oct
	 *            the oct to set
	 */
	public static void setOct(String oct) {
		DayAndMonthUtil.oct = oct;
	}

	/**
	 * @return the nov
	 */
	public static String nov() {
		return nov;
	}

	/**
	 * @param nov
	 *            the nov to set
	 */
	public static void setNov(String nov) {
		DayAndMonthUtil.nov = nov;
	}

	/**
	 * @return the dec
	 */
	public static String dec() {
		return dec;
	}

	/**
	 * @param dec
	 *            the dec to set
	 */
	public static void setDec(String dec) {
		DayAndMonthUtil.dec = dec;
	}

	/**
	 * @return the may_short
	 */
	public static String mayS() {
		return may_short;
	}

	/**
	 * @param may_short
	 *            the may_short to set
	 */
	public static void setMay_short(String may_short) {
		DayAndMonthUtil.may_short = may_short;
	}
}
