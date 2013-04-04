package com.vimukti.accounter.text.commands.utils;

public class CommandUtils {
	public final static String[] MONTHS = new String[] { "january", "february",
			"march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december" };

	/**
	 * Get the Month number by Name
	 * 
	 * @param monthName
	 * @return
	 */
	public static int getMonthInNumber(String monthName) {
		// fiscalStartsList = null;
		for (int i = 0; i < MONTHS.length; i++) {
			if (monthName.toLowerCase().equals(MONTHS[i])) {
				return i;
			}
		}
		return 0;
	}

}
