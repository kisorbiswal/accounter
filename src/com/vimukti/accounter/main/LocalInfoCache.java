package com.vimukti.accounter.main;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class LocalInfoCache {
	private static Map<Locale, DayAndMonthUtil> map = new HashMap<Locale, DayAndMonthUtil>();

	public static DayAndMonthUtil get(Locale locale) {
		DayAndMonthUtil dayAndMonthUtil = map.get(locale);
		if (map == null) {
			dayAndMonthUtil = createDayAndMonthUtil(locale);
			map.put(locale, dayAndMonthUtil);
		}
		return dayAndMonthUtil;
	}

	private static DayAndMonthUtil createDayAndMonthUtil(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}
}
