package com.vimukti.accounter.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class LocalInfoCache {
	private static Map<Locale, DayAndMonthUtil> map = new HashMap<Locale, DayAndMonthUtil>();

	public static DayAndMonthUtil get(Locale locale) {
		DayAndMonthUtil dayAndMonthUtil = map.get(locale);
		if (dayAndMonthUtil == null) {
			dayAndMonthUtil = createDayAndMonthUtil(locale);
			map.put(locale, dayAndMonthUtil);
		}
		return dayAndMonthUtil;
	}

	/**
	 * 
	 * @param locale
	 * @return
	 */
	private static DayAndMonthUtil createDayAndMonthUtil(Locale locale) {
		String bundleName = toBundleName(
				"com.vimukti.accounter.web.server.i18n.constants.DateTimeConstantsImpl",
				locale);
		String resourceName = toResourceName(bundleName, "properties");

		try {
			InputStream openStream = new LocalInfoCache().getClass()
					.getClassLoader().getResource(resourceName).openStream();
			DataInputStream in = new DataInputStream(openStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			DummyDateTimeFormatInfo dateTimeFormatInfo = new DummyDateTimeFormatInfo();
			while ((strLine = br.readLine()) != null) {
				if (strLine.length() == 0) {
					continue;
				}
				if (strLine.charAt(0) == '#') {
					continue;
				}
				String[] split = strLine.split("=");
				String key = split[0].trim();
				String[] values = split[1].trim().split(",");
				createDateTimeFormatInfoObj(dateTimeFormatInfo, key, values);
			}
			DayAndMonthUtil dayAndMonthUtil = new DayAndMonthUtil(
					dateTimeFormatInfo);
			return dayAndMonthUtil;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creating DateTimeFormatInfo Object depending on Keys
	 * 
	 * @param dateTimeFormatInfo
	 * @param key
	 * @param values
	 * @return
	 */
	private static DummyDateTimeFormatInfo createDateTimeFormatInfoObj(
			DummyDateTimeFormatInfo dateTimeFormatInfo, String key,
			String[] values) {
		if (key.equalsIgnoreCase("eras")) {
			dateTimeFormatInfo.seterasShort(values);
		} else if (key.equalsIgnoreCase("eraNames")) {
			dateTimeFormatInfo.setErasFull(values);
		} else if (key.equalsIgnoreCase("narrowMonths")) {
		} else if (key.equalsIgnoreCase("months")) {
			dateTimeFormatInfo.setMonthsFull(values);
		} else if (key.equalsIgnoreCase("shortMonths")) {
			dateTimeFormatInfo.setMonthsShort(values);
		} else if (key.equalsIgnoreCase("standaloneNarrowMonths")) {
			dateTimeFormatInfo.setMonthsNarrowStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneMonths")) {
			dateTimeFormatInfo.setMonthsFullStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneShortMonths")) {
			dateTimeFormatInfo.setMonthsShortStandalone(values);
		} else if (key.equalsIgnoreCase("weekdays")) {
			dateTimeFormatInfo.setWeekdaysFull(values);
		} else if (key.equalsIgnoreCase("shortWeekdays")) {
			dateTimeFormatInfo.setWeekdaysShort(values);
		} else if (key.equalsIgnoreCase("narrowWeekdays")) {
			dateTimeFormatInfo.setWeekdaysNarrow(values);
		} else if (key.equalsIgnoreCase("standaloneWeekdays")) {
			dateTimeFormatInfo.setWeekdaysFullStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneShortWeekdays")) {
			dateTimeFormatInfo.setWeekdaysShortStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneNarrowWeekdays")) {
			dateTimeFormatInfo.setWeekdaysNarrowStandalone(values);
		} else if (key.equalsIgnoreCase("shortQuarters")) {
			dateTimeFormatInfo.setQuartersShort(values);
		} else if (key.equalsIgnoreCase("quarters")) {
			dateTimeFormatInfo.setQuartersFull(values);
		} else if (key.equalsIgnoreCase("ampms")) {
			dateTimeFormatInfo.setAmpms(values);
		} else if (key.equalsIgnoreCase("dateFormats")) {
			dateTimeFormatInfo.setDateFormat(values[0]);
		} else if (key.equalsIgnoreCase("timeFormats")) {
			dateTimeFormatInfo.setTimeFormatFull(values[0]);
		} else if (key.equalsIgnoreCase("firstDayOfTheWeek")) {
			dateTimeFormatInfo
					.setFirstDayOfTheWeek(Integer.parseInt(values[0]));
		} else if (key.equalsIgnoreCase("weekendRange")) {
			dateTimeFormatInfo.setWeekendEnd(Integer.parseInt(values[0]));
		}
		return dateTimeFormatInfo;

	}

	/**
	 * Converts the given <code>baseName</code> and <code>locale</code> to the
	 * bundle name. This method is called from the default implementation of the
	 * {@link #newBundle(String, Locale, String, ClassLoader, boolean)
	 * newBundle} and
	 * {@link #needsReload(String, Locale, String, ClassLoader, ResourceBundle, long)
	 * needsReload} methods.
	 * 
	 * @param baseName
	 * @param locale
	 * @return
	 */
	private static String toBundleName(String baseName, Locale locale) {
		if (locale == Locale.ROOT) {
			return baseName;
		}

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (language == "" && country == "" && variant == "") {
			return baseName;
		}

		StringBuilder sb = new StringBuilder(baseName);
		sb.append('_');
		if (variant != "") {
			sb.append(language).append('_').append(country).append('_')
					.append(variant);
		} else if (country != "") {
			sb.append(language).append('_').append(country);
		} else {
			sb.append(language);
		}
		return sb.toString();

	}

	/**
	 * Converts the given <code>bundleName</code> to the form required by the
	 * {@link ClassLoader#getResource ClassLoader.getResource} method by
	 * replacing all occurrences of <code>'.'</code> in <code>bundleName</code>
	 * with <code>'/'</code> and appending a <code>'.'</code> and the given file
	 * <code>suffix</code>. For example, if <code>bundleName</code> is
	 * <code>"foo.bar.MyResources_ja_JP"</code> and <code>suffix</code> is
	 * <code>"properties"</code>, then
	 * <code>"foo/bar/MyResources_ja_JP.properties"</code> is returned.
	 * 
	 * @param bundleName
	 * @param suffix
	 * @return
	 */
	public final static String toResourceName(String bundleName, String suffix) {
		StringBuilder sb = new StringBuilder(bundleName.length() + 1
				+ suffix.length());
		sb.append(bundleName.replace('.', '/')).append('.').append(suffix);
		return sb.toString();
	}
}
