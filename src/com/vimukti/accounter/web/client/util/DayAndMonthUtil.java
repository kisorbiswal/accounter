package com.vimukti.accounter.web.client.util;

import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.vimukti.accounter.web.client.Global;

public class DayAndMonthUtil {
	private final DateTimeFormatInfo a;
	private String[] weekdaysFull;
	private String[] weekdaysShort;
	private String[] monthNameFull;
	private String[] monthNameShort;
	private String sunday = weekdaysFull[0];
	private String monday = weekdaysFull[1];
	private String tuesday = weekdaysFull[2];
	private String wednesday = weekdaysFull[3];
	private String thursday = weekdaysFull[4];
	private String friday = weekdaysFull[5];
	private String saturday = weekdaysFull[6];
	private String january = monthNameFull[0];
	private String february = monthNameFull[1];
	private String march = monthNameFull[2];
	private String april = monthNameFull[3];
	private String may_full = monthNameFull[4];
	private String june = monthNameFull[5];
	private String july = monthNameFull[6];
	private String august = monthNameFull[7];
	private String september = monthNameFull[8];
	private String october = monthNameFull[9];
	private String november = monthNameFull[10];
	private String december = monthNameFull[11];
	private String jan = monthNameShort[0];
	private String feb = monthNameShort[1];
	private String mar = monthNameShort[2];
	private String apr = monthNameShort[3];
	private String may_short = monthNameShort[4];
	private String jun = monthNameShort[5];
	private String jul = monthNameShort[6];
	private String aug = monthNameShort[7];
	private String sep = monthNameShort[8];
	private String oct = monthNameShort[9];
	private String nov = monthNameShort[10];
	private String dec = monthNameShort[11];

	public DayAndMonthUtil(DateTimeFormatInfo a) {
		this.a = a;
		weekdaysFull = a.weekdaysFull();
		weekdaysShort = a.weekdaysShort();
		monthNameFull = a.monthsFull();
		monthNameShort = a.monthsShort();
	}

	/**
	 * @return the monday
	 */
	public static String monday() {
		return Global.get().getDayAndMonthUtil().monday;
	}

	/**
	 * @param monday
	 *            the monday to set
	 */
	public static void setMonday(String monday) {
		Global.get().getDayAndMonthUtil().monday = monday;
	}

	/**
	 * @return the sunday
	 */
	public static String sunday() {
		return Global.get().getDayAndMonthUtil().sunday;
	}

	/**
	 * @param sunday
	 *            the sunday to set
	 */
	public static void setSunday(String sunday) {
		Global.get().getDayAndMonthUtil().sunday = sunday;
	}

	/**
	 * @return the tuesday
	 */
	public static String tuesday() {
		return Global.get().getDayAndMonthUtil().tuesday;
	}

	/**
	 * @param tuesday
	 *            the tuesday to set
	 */
	public static void setTuesday(String tuesday) {
		Global.get().getDayAndMonthUtil().tuesday = tuesday;
	}

	/**
	 * @return the thursday
	 */
	public static String thursday() {
		return Global.get().getDayAndMonthUtil().thursday;
	}

	/**
	 * @param thursday
	 *            the thursday to set
	 */
	public static void setThursday(String thursday) {
		Global.get().getDayAndMonthUtil().thursday = thursday;
	}

	/**
	 * @return the december
	 */
	public static String december() {
		return Global.get().getDayAndMonthUtil().december;
	}

	/**
	 * @param december
	 *            the december to set
	 */
	public static void setDecember(String december) {
		Global.get().getDayAndMonthUtil().december = december;
	}

	/**
	 * @return the wednesday
	 */
	public static String wednesday() {
		return Global.get().getDayAndMonthUtil().wednesday;
	}

	/**
	 * @param wednesday
	 *            the wednesday to set
	 */
	public static void setWednesday(String wednesday) {
		Global.get().getDayAndMonthUtil().wednesday = wednesday;
	}

	/**
	 * @return the friday
	 */
	public static String friday() {
		return Global.get().getDayAndMonthUtil().friday;
	}

	/**
	 * @param friday
	 *            the friday to set
	 */
	public static void setFriday(String friday) {
		Global.get().getDayAndMonthUtil().friday = friday;
	}

	/**
	 * @return the saturday
	 */
	public static String saturday() {
		return Global.get().getDayAndMonthUtil().saturday;
	}

	/**
	 * @param saturday
	 *            the saturday to set
	 */
	public static void setSaturday(String saturday) {
		Global.get().getDayAndMonthUtil().saturday = saturday;
	}

	/**
	 * @return the january
	 */
	public static String january() {
		return Global.get().getDayAndMonthUtil().january;
	}

	/**
	 * @param january
	 *            the january to set
	 */
	public static void setJanuary(String january) {
		Global.get().getDayAndMonthUtil().january = january;
	}

	/**
	 * @return the february
	 */
	public static String february() {
		return Global.get().getDayAndMonthUtil().february;
	}

	/**
	 * @param february
	 *            the february to set
	 */
	public static void setFebruary(String february) {
		Global.get().getDayAndMonthUtil().february = february;
	}

	/**
	 * @return the march
	 */
	public static String march() {
		return Global.get().getDayAndMonthUtil().march;
	}

	/**
	 * @param march
	 *            the march to set
	 */
	public static void setMarch(String march) {
		Global.get().getDayAndMonthUtil().march = march;
	}

	/**
	 * @return the april
	 */
	public static String april() {
		return Global.get().getDayAndMonthUtil().april;
	}

	/**
	 * @param april
	 *            the april to set
	 */
	public static void setApril(String april) {
		Global.get().getDayAndMonthUtil().april = april;
	}

	/**
	 * @return the may
	 */
	public static String may_full() {
		return Global.get().getDayAndMonthUtil().may_full;
	}

	/**
	 * @param may
	 *            the may to set
	 */
	public static void setMay_full(String may_full) {
		Global.get().getDayAndMonthUtil().may_full = may_full;
	}

	/**
	 * @return the june
	 */
	public static String june() {
		return Global.get().getDayAndMonthUtil().june;
	}

	/**
	 * @param june
	 *            the june to set
	 */
	public static void setJune(String june) {
		Global.get().getDayAndMonthUtil().june = june;
	}

	/**
	 * @return the july
	 */
	public static String july() {
		return Global.get().getDayAndMonthUtil().july;
	}

	/**
	 * @param july
	 *            the july to set
	 */
	public static void setJuly(String july) {
		Global.get().getDayAndMonthUtil().july = july;
	}

	/**
	 * @return the august
	 */
	public static String august() {
		return Global.get().getDayAndMonthUtil().august;
	}

	/**
	 * @param august
	 *            the august to set
	 */
	public static void setAugust(String august) {
		Global.get().getDayAndMonthUtil().august = august;
	}

	/**
	 * @return the september
	 */
	public static String september() {
		return Global.get().getDayAndMonthUtil().september;
	}

	/**
	 * @param september
	 *            the september to set
	 */
	public static void setSeptember(String september) {
		Global.get().getDayAndMonthUtil().september = september;
	}

	/**
	 * @return the october
	 */
	public static String october() {
		return Global.get().getDayAndMonthUtil().october;
	}

	/**
	 * @param october
	 *            the october to set
	 */
	public static void setOctober(String october) {
		Global.get().getDayAndMonthUtil().october = october;
	}

	/**
	 * @return the november
	 */
	public static String november() {
		return Global.get().getDayAndMonthUtil().november;
	}

	/**
	 * @param november
	 *            the november to set
	 */
	public static void setNovember(String november) {
		Global.get().getDayAndMonthUtil().november = november;
	}

	/**
	 * @return the jan
	 */
	public static String jan() {
		return Global.get().getDayAndMonthUtil().jan;
	}

	/**
	 * @param jan
	 *            the jan to set
	 */
	public static void setJan(String jan) {
		Global.get().getDayAndMonthUtil().jan = jan;
	}

	/**
	 * @return the feb
	 */
	public static String feb() {
		return Global.get().getDayAndMonthUtil().feb;
	}

	/**
	 * @param feb
	 *            the feb to set
	 */
	public static void setFeb(String feb) {
		Global.get().getDayAndMonthUtil().feb = feb;
	}

	/**
	 * @return the mar
	 */
	public static String mar() {
		return Global.get().getDayAndMonthUtil().mar;
	}

	/**
	 * @param mar
	 *            the mar to set
	 */
	public static void setMar(String mar) {
		Global.get().getDayAndMonthUtil().mar = mar;
	}

	/**
	 * @return the apr
	 */
	public static String apr() {
		return Global.get().getDayAndMonthUtil().apr;
	}

	/**
	 * @param apr
	 *            the apr to set
	 */
	public static void setApr(String apr) {
		Global.get().getDayAndMonthUtil().apr = apr;
	}

	/**
	 * @return the jun
	 */
	public static String jun() {
		return Global.get().getDayAndMonthUtil().jun;
	}

	/**
	 * @param jun
	 *            the jun to set
	 */
	public static void setJun(String jun) {
		Global.get().getDayAndMonthUtil().jun = jun;
	}

	/**
	 * @return the jul
	 */
	public static String jul() {
		return Global.get().getDayAndMonthUtil().jul;
	}

	/**
	 * @param jul
	 *            the jul to set
	 */
	public static void setJul(String jul) {
		Global.get().getDayAndMonthUtil().jul = jul;
	}

	/**
	 * @return the aug
	 */
	public static String aug() {
		return Global.get().getDayAndMonthUtil().aug;
	}

	/**
	 * @param aug
	 *            the aug to set
	 */
	public static void setAug(String aug) {
		Global.get().getDayAndMonthUtil().aug = aug;
	}

	/**
	 * @return the sept
	 */
	public static String sep() {
		return Global.get().getDayAndMonthUtil().sep;
	}

	/**
	 * @return the oct
	 */
	public static String oct() {
		return Global.get().getDayAndMonthUtil().oct;
	}

	/**
	 * @param oct
	 *            the oct to set
	 */
	public static void setOct(String oct) {
		Global.get().getDayAndMonthUtil().oct = oct;
	}

	/**
	 * @return the nov
	 */
	public static String nov() {
		return Global.get().getDayAndMonthUtil().nov;
	}

	/**
	 * @param nov
	 *            the nov to set
	 */
	public static void setNov(String nov) {
		Global.get().getDayAndMonthUtil().nov = nov;
	}

	/**
	 * @return the dec
	 */
	public static String dec() {
		return Global.get().getDayAndMonthUtil().dec;
	}

	/**
	 * @param dec
	 *            the dec to set
	 */
	public static void setDec(String dec) {
		Global.get().getDayAndMonthUtil().dec = dec;
	}

	/**
	 * @return the may_short
	 */
	public static String mayS() {
		return Global.get().getDayAndMonthUtil().may_short;
	}

	/**
	 * @param may_short
	 *            the may_short to set
	 */
	public static void setMay_short(String may_short) {
		Global.get().getDayAndMonthUtil().may_short = may_short;
	}
}
