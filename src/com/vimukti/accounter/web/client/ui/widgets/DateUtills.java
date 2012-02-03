package com.vimukti.accounter.web.client.ui.widgets;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class DateUtills {

	public boolean isMonth = false;

	public static String getCurrentDateAsString(String dateFormat) {

		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(dateFormat);
		return dateFormatter.format(new Date(System.currentTimeMillis()));

	}

	public static String getDateAsString(Date date) {
		DateTimeFormat dateFormatter = null;
		dateFormatter = DateTimeFormat.getFormat(Global.get().preferences()
				.getDateFormat());
		return dateFormatter.format(date);

	}

	public static String getDateAsString(ClientFinanceDate date) {
		return getDateAsString(date.getDateAsObject());
	}

	public static String getDateAsString(long date) {
		return getDateAsString(new ClientFinanceDate(date).getDateAsObject());
	}

	public int evaluateDate(String dateFormat) {
		try {
			return Integer.parseInt(dateFormat);
		} catch (Exception e) {
			isMonth = true;
			if (dateFormat.equalsIgnoreCase("Jan")
					|| dateFormat.equalsIgnoreCase("January")) {
				return 1;
			} else if (dateFormat.equalsIgnoreCase("Feb")
					|| dateFormat.equalsIgnoreCase("February")) {
				return 2;
			} else if (dateFormat.equalsIgnoreCase("Mar")
					|| dateFormat.equalsIgnoreCase("March")) {
				return 3;
			} else if (dateFormat.equalsIgnoreCase("Apr")
					|| dateFormat.equalsIgnoreCase("April")) {
				return 4;
			} else if (dateFormat.equalsIgnoreCase("May")) {
				return 5;
			} else if (dateFormat.equalsIgnoreCase("Jun")
					|| dateFormat.equalsIgnoreCase("June")) {
				return 6;
			} else if (dateFormat.equalsIgnoreCase("Jul")
					|| dateFormat.equalsIgnoreCase("July")) {
				return 7;
			} else if (dateFormat.equalsIgnoreCase("Aug")
					|| dateFormat.equalsIgnoreCase("August")) {
				return 8;
			} else if (dateFormat.equalsIgnoreCase("Sep")
					|| dateFormat.equalsIgnoreCase("September")) {
				return 9;
			} else if (dateFormat.equalsIgnoreCase("Oct")
					|| dateFormat.equalsIgnoreCase("October")) {
				return 10;
			} else if (dateFormat.equalsIgnoreCase("Nov")
					|| dateFormat.equalsIgnoreCase("November")) {
				return 11;
			} else if (dateFormat.equalsIgnoreCase("Dec")
					|| dateFormat.equalsIgnoreCase("December")) {
				return 12;
			} else {
				System.out.println("Please enter Valid Month Format DDMMYYYY ");
			}
		}
		return 0;
	}

	public int evaluateMonthFormat(String monthFormat) {
		try {
			return Integer.parseInt(monthFormat);
		} catch (Exception e) {
			if (monthFormat.equalsIgnoreCase("Jan")
					|| monthFormat.equalsIgnoreCase("January")) {
				return 1;
			} else if (monthFormat.equalsIgnoreCase("Feb")
					|| monthFormat.equalsIgnoreCase("February")) {
				return 2;
			} else if (monthFormat.equalsIgnoreCase("Mar")
					|| monthFormat.equalsIgnoreCase("March")) {
				return 3;
			} else if (monthFormat.equalsIgnoreCase("Apr")
					|| monthFormat.equalsIgnoreCase("April")) {
				return 4;
			} else if (monthFormat.equalsIgnoreCase("May")) {
				return 5;
			} else if (monthFormat.equalsIgnoreCase("Jun")
					|| monthFormat.equalsIgnoreCase("June")) {
				return 6;
			} else if (monthFormat.equalsIgnoreCase("Jul")
					|| monthFormat.equalsIgnoreCase("July")) {
				return 7;
			} else if (monthFormat.equalsIgnoreCase("Aug")
					|| monthFormat.equalsIgnoreCase("August")) {
				return 8;
			} else if (monthFormat.equalsIgnoreCase("Sep")
					|| monthFormat.equalsIgnoreCase("September")) {
				return 9;
			} else if (monthFormat.equalsIgnoreCase("Oct")
					|| monthFormat.equalsIgnoreCase("October")) {
				return 10;
			} else if (monthFormat.equalsIgnoreCase("Nov")
					|| monthFormat.equalsIgnoreCase("November")) {
				return 11;
			} else if (monthFormat.equalsIgnoreCase("Dec")
					|| monthFormat.equalsIgnoreCase("December")) {
				return 12;
			} else {
				System.out.println("Please enter Valid Month Format DDMMYYYY ");
			}
		}
		return 0;
	}

	public static String getMonthNameByNumber(int month) {

		switch (month) {
		case 1:
			return ("Jan");
		case 2:
			return ("Feb");
		case 3:
			return ("Mar");
		case 4:
			return ("Apr");
		case 5:
			return ("May");
		case 6:
			return ("Jun");
		case 7:
			return ("July");
		case 8:
			return ("Aug");
		case 9:
			return ("Sep");
		case 10:
			return ("Oct");

		case 11:
			return ("Nov");
		case 12:
			return ("Dec");
		default:
			break;

		}
		return "";

	}

	public static ClientFinanceDate getDateFromString(String value) {
		return new ClientFinanceDate(parseDate(value));

	}

	public static Date parseDate(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.isEmpty()) {
			return null;
		}
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(Global.get()
				.preferences().getDateFormat());
		return dateFormatter.parse(value);
	}

	public static ClientFinanceDate getSelectedFormatDate(String value,
			String selectedformat) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.isEmpty()) {
			return null;
		}
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(selectedformat);
		try {
			Date parse = dateFormatter.parse(value);
			return new ClientFinanceDate(parse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
