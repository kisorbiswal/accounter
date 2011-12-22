package com.vimukti.accounter.web.client.i18n;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.i18n.client.impl.cldr.DateTimeFormatInfoImpl;

public class AccounterDateTimeFormatInfoImpl extends DateTimeFormatInfoImpl {

	@Override
	public String[] ampms() {
		return toArray(_ampms());
	}

	private String[] toArray(JsArrayString array) {
		String[] str = new String[array.length()];
		for (int x = 0; x < str.length; x++) {
			str[x] = array.get(x);
		}
		return str;
	}

	public native JsArrayString _ampms() /*-{
		return $wnd['accounter_locale']['ampms'];
	}-*/;

	@Override
	public native String dateFormat() /*-{

		return $wnd['accounter_locale']['dateFormat'];
	}-*/;

	@Override
	public native String dateFormatFull() /*-{

		return $wnd['accounter_locale']['dateFormatFull'];
	}-*/;

	@Override
	public native String dateFormatLong() /*-{

		return $wnd['accounter_locale']['dateFormatLong'];
	}-*/;

	@Override
	public native String dateFormatMedium() /*-{

		return $wnd['accounter_locale']['dateFormatMedium'];
	}-*/;

	@Override
	public native String dateFormatShort() /*-{

		return $wnd['accounter_locale']['dateFormatShort'];
	}-*/;

	@Override
	public String[] erasFull() {
		return toArray(_erasFull());
	}
	public native JsArrayString _erasFull() /*-{
		return $wnd['accounter_locale']['erasFull'];
	}-*/;

	@Override
	public String[] erasShort() {
		return toArray(_erasShort());
	}
	public native JsArrayString _erasShort() /*-{

		return $wnd['accounter_locale']['erasShort'];
	}-*/;

	@Override
	public native int firstDayOfTheWeek() /*-{

		return $wnd['accounter_locale']['firstDayOfTheWeek'];
	}-*/;

	@Override
	public native String formatDay() /*-{

		return $wnd['accounter_locale']['formatDay'];
	}-*/;

	@Override
	public native String formatHour12Minute() /*-{

		return $wnd['accounter_locale']['formatHour12Minute'];
	}-*/;

	@Override
	public native String formatHour12MinuteSecond() /*-{

		return $wnd['accounter_locale']['formatHour12MinuteSecond'];
	}-*/;

	@Override
	public native String formatHour24Minute() /*-{

		return $wnd['accounter_locale']['formatHour24Minute'];
	}-*/;

	@Override
	public native String formatHour24MinuteSecond() /*-{

		return $wnd['accounter_locale']['formatHour24MinuteSecond'];
	}-*/;

	@Override
	public native String formatMinuteSecond() /*-{

		return $wnd['accounter_locale']['formatMinuteSecond'];
	}-*/;

	@Override
	public native String formatMonthAbbrev() /*-{

		return $wnd['accounter_locale']['formatMonthAbbrev'];
	}-*/;

	@Override
	public native String formatMonthAbbrevDay() /*-{

		return $wnd['accounter_locale']['formatMonthAbbrevDay'];
	}-*/;

	@Override
	public native String formatMonthFull() /*-{

		return $wnd['accounter_locale']['formatMonthFull'];
	}-*/;

	@Override
	public native String formatMonthFullDay() /*-{

		return $wnd['accounter_locale']['formatMonthFullDay'];
	}-*/;

	@Override
	public native String formatMonthFullWeekdayDay() /*-{

		return $wnd['accounter_locale']['formatMonthFullWeekdayDay'];
	}-*/;

	@Override
	public native String formatMonthNumDay() /*-{

		return $wnd['accounter_locale']['formatMonthNumDay'];
	}-*/;

	@Override
	public native String formatYear() /*-{

		return $wnd['accounter_locale']['formatYear'];
	}-*/;

	@Override
	public native String formatYearMonthAbbrev() /*-{

		return $wnd['accounter_locale']['formatYearMonthAbbrev'];
	}-*/;

	@Override
	public native String formatYearMonthAbbrevDay() /*-{

		return $wnd['accounter_locale']['formatYearMonthAbbrevDay'];
	}-*/;

	@Override
	public native String formatYearMonthFull() /*-{

		return $wnd['accounter_locale']['formatYearMonthFull'];
	}-*/;

	@Override
	public native String formatYearMonthFullDay() /*-{

		return $wnd['accounter_locale']['formatYearMonthFullDay'];
	}-*/;

	@Override
	public native String formatYearMonthNum() /*-{

		return $wnd['accounter_locale']['formatYearMonthNum'];
	}-*/;

	@Override
	public native String formatYearMonthNumDay() /*-{

		return $wnd['accounter_locale']['formatYearMonthNumDay'];
	}-*/;

	@Override
	public native String formatYearMonthWeekdayDay() /*-{

		return $wnd['accounter_locale']['formatYearMonthWeekdayDay'];
	}-*/;

	@Override
	public native String formatYearQuarterFull() /*-{

		return $wnd['accounter_locale']['formatYearQuarterFull'];
	}-*/;

	@Override
	public native String formatYearQuarterShort() /*-{

		return $wnd['accounter_locale']['formatYearQuarterShort'];
	}-*/;

	@Override
	public String[] monthsFull() {
		return toArray(_monthsFull());
	}
	public native JsArrayString _monthsFull() /*-{

		return $wnd['accounter_locale']['monthsFull'];
	}-*/;
	
	@Override
	public String[] monthsFullStandalone() {
		return toArray(_monthsFullStandalone());
	}
	
	public native JsArrayString _monthsFullStandalone() /*-{

		return $wnd['accounter_locale']['monthsFullStandalone'];
	}-*/;

	@Override
	public String[] monthsNarrow() {
		return toArray(_monthsNarrow());
	}
	public native JsArrayString _monthsNarrow() /*-{

		return $wnd['accounter_locale']['monthsNarrow'];
	}-*/;

	@Override
	public String[] monthsNarrowStandalone() {
		return toArray(_monthsNarrowStandalone());
	}
	public native JsArrayString _monthsNarrowStandalone() /*-{

		return $wnd['accounter_locale']['monthsNarrowStandalone'];
	}-*/;

	@Override
	public String[] monthsShort() {
		return toArray(_monthsShort());
	}
	public native JsArrayString _monthsShort() /*-{

		return $wnd['accounter_locale']['monthsShort'];
	}-*/;

	@Override
	public String[] monthsShortStandalone() {
		return toArray(_monthsShortStandalone());
	}
	public native JsArrayString _monthsShortStandalone() /*-{

		return $wnd['accounter_locale']['monthsShortStandalone'];
	}-*/;

	@Override
	public String[] quartersFull() {
		return toArray(_quartersFull());
	}
	public native JsArrayString _quartersFull() /*-{

		return $wnd['accounter_locale']['quartersFull'];
	}-*/;

	@Override
	public String[] quartersShort() {
		return toArray(_quartersShort());
	}
	public native JsArrayString _quartersShort() /*-{

		return $wnd['accounter_locale']['quartersShort'];
	}-*/;

	@Override
	public native String timeFormat() /*-{

		return $wnd['accounter_locale']['timeFormat'];
	}-*/;

	@Override
	public native String timeFormatFull() /*-{

		return $wnd['accounter_locale']['timeFormatFull'];
	}-*/;

	@Override
	public native String timeFormatLong() /*-{

		return $wnd['accounter_locale']['timeFormatLong'];
	}-*/;

	@Override
	public native String timeFormatMedium() /*-{

		return $wnd['accounter_locale']['timeFormatMedium'];
	}-*/;

	@Override
	public native String timeFormatShort() /*-{

		return $wnd['accounter_locale']['timeFormatShort'];
	}-*/;

	@Override
	public String[] weekdaysFull() {
		return toArray(_weekdaysFull());
	}
	public native JsArrayString _weekdaysFull() /*-{

		return $wnd['accounter_locale']['weekdaysFull'];
	}-*/;

	@Override
	public String[] weekdaysFullStandalone() {
		return toArray(_weekdaysFullStandalone());
	}
	public native JsArrayString _weekdaysFullStandalone() /*-{

		return $wnd['accounter_locale']['weekdaysFullStandalone'];
	}-*/;

	@Override
	public String[] weekdaysNarrow() {
		return toArray(_weekdaysNarrow());
	}
	public native JsArrayString _weekdaysNarrow() /*-{

		return $wnd['accounter_locale']['weekdaysNarrow'];
	}-*/;

	@Override
	public String[] weekdaysNarrowStandalone() {
		return toArray(_weekdaysNarrowStandalone());
	}
	public native JsArrayString _weekdaysNarrowStandalone() /*-{

		return $wnd['accounter_locale']['weekdaysNarrowStandalone'];
	}-*/;

	@Override
	public String[] weekdaysShort() {
		return toArray(_weekdaysShort());
	}
	public native JsArrayString _weekdaysShort() /*-{

		return $wnd['accounter_locale']['shortWeekdays'];
	}-*/;

	@Override
	public String[] weekdaysShortStandalone() {
		return toArray(_weekdaysShortStandalone());
	}
	public native JsArrayString _weekdaysShortStandalone() /*-{

		return $wnd['accounter_locale']['weekdaysShortStandalone'];
	}-*/;

	@Override
	public native int weekendEnd() /*-{

		return $wnd['accounter_locale']['weekendEnd'];
	}-*/;

	@Override
	public native int weekendStart() /*-{

		return $wnd['accounter_locale']['weekendStart'];
	}-*/;

}
