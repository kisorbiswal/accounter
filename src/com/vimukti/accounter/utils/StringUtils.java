package com.vimukti.accounter.utils;

import java.util.Collection;

/**
 * This class is a util class with different String operations.
 * 
 * @author Administrator
 */
public class StringUtils {

	// private static Logger LOG = Logger.getLogger(StringUtils.class);

	/**
	 * Method is useful for checking the given string is empty or not if it is
	 * empty then return true otherwise return false.
	 * 
	 * @param tmp
	 * @return boolean
	 */

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String sizeToString(long val, boolean isAbbreviated) {
		String unity = ""; //$NON-NLS-1$
		double size = val;

		if (isAbbreviated)
			unity = "b";
		else
			unity = "bytes";

		if (size > 1024.) {
			size /= 1024.;
			if (isAbbreviated)
				unity = "Kb";
			else
				unity = "Kilo-Bytes";
		}
		if (size > 1024.) {
			size /= 1024.;
			if (isAbbreviated)
				unity = "Mb";
			else
				unity = "Mega-Bytes";
		}
		if (size > 1024.) {
			size /= 1024.;
			if (isAbbreviated)
				unity = "Gb";
			else
				unity = "Giga-Bytes";

		}
		size = ((double) (long) (size * 10)) / 10d;
		return size + unity;
	}

	/****
	 * Returns the comma seprated string example:-a@b.com,c@d.com,d@e.com
	 * 
	 * @param coll
	 * @param delimiter
	 * @return String
	 */
	public static String join(Collection<String> coll, String delimiter) {
		if (coll.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();

		for (String x : coll)
			sb.append(x + delimiter);

		sb.delete(sb.length() - delimiter.length(), sb.length());

		return sb.toString();
	}

	public static boolean hasLength(String username) {
		if (username.length() != 0) {
			return true;
		}
		return false;
	}

	public static String stringAppenderForIndex(String... strings) {

		if (strings == null)
			return "";
		StringBuffer buffer = new StringBuffer();
		for (String string : strings) {
			if (!(string == null))
				buffer.append(" " + string + " ");
		}
		return buffer.toString();

	}

}
