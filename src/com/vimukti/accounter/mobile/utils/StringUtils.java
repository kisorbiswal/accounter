/**
 * 
 */
package com.vimukti.accounter.mobile.utils;

public class StringUtils {

	public static boolean isInteger(String string) {
		return string.matches("[-+]?\\d+(\\d+)?");
	}

	public static boolean isDouble(String string) {
		return string.matches("[-+]?\\d+(\\.\\d+)?");
	}

	public static boolean isDate(String string) {
		return false;
	}

	public static String[] getSubStrings(String input) {
		// TODO
		return new String[0];
	}

}
