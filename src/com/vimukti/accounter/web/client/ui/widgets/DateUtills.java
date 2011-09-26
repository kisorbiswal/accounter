package com.vimukti.accounter.web.client.ui.widgets;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;

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
		dateFormatter = DateTimeFormat.getFormat(Accounter.getCompany()
				.getPreferences().getDateFormat());
		return dateFormatter.format(date);

	}

	public static String getDateAsString(long date) {
		return getDateAsString(new ClientFinanceDate(date).getDateAsObject());
	}

	public Date parseDate(String date) {
		String[] split = date.contains("/") ? date.split("/") : date
				.contains("-") ? date.split("-") : spilitString(date);

		if (split.length == 3) {

			if (Accounter.getCompany().getPreferences().getDateFormat()
					.equals("MM/dd/yyyy")) {
				return processDate(split[1], split[0], split[2]);
			} else {
				return processDate(split[0], split[1], split[2]);
			}

		}
		return null;
	}

	public String[] spilitString(String date) {

		String str[] = new String[3];
		try {
			Integer.parseInt(date);
			int i = 0;
			if ((i = date.getBytes().length) <= 8
					&& date.getBytes().length >= 4) {
				switch (i) {
				case 8:
					str[0] = date.substring(0, 2);
					str[1] = date.substring(2, 4);
					str[2] = date.substring(4);
					return str;
				case 6:
					str[0] = date.substring(0, 2);
					str[1] = date.substring(2, 4);
					str[2] = date.substring(4);
					return str;
				case 4:
					str[0] = date.substring(0, 1);
					str[1] = date.substring(1, 2);
					str[2] = date.substring(2);
					return str;
				}
			} else {
				System.out.println("Please Enter Valid Date Format");
			}
		} catch (Exception e) {
			byte[] bytes = date.getBytes();
			int length = bytes.length;
			switch (length) {
			case 7:
			case 9:
				if ((bytes[0] >= 48) && (bytes[0] <= 57)) {
					str[0] = date.substring(0, 2);
					str[1] = date.substring(2, 5);
					str[2] = date.substring(5);
					return str;

				} else {
					str[0] = date.substring(0, 3);
					str[1] = date.substring(3, 5);
					str[2] = date.substring(5);
					return str;
				}
			default:
				// Window.alert("Please enter valid date");
				break;
			}

		}
		return null;
	}

	public Date processDate(String dateFormat, String monthFormat,
			String yearFormat) {
		int temp = 0;
		try {
			// int day = evaluateMonthFormat(dateFormat);
			int day = evaluateDate(dateFormat);
			int month = evaluateMonthFormat(monthFormat);
			if (isMonth) {
				temp = day;
				day = month;
				month = temp;
			}
			int year = Integer.parseInt(yearFormat);
			switch (month) {
			case 2:
				if (year % 4 == 0) {
					if (day >= 1 && day <= 29) {
						if (yearFormat.getBytes().length <= 3) {
							return new Date(year + 100, month - 1, day);
						}
						return new Date(year - 1900, month - 1, day);
					} else {
						System.out
								.println("Please Enter Valid Date for That Month");
						break;
					}
				} else {
					if (day >= 1 && day <= 28) {
						if (yearFormat.getBytes().length <= 3) {
							return new Date(year + 100, month - 1, day);
						}
						return new Date(year - 1900, month - 1, day);
					} else {
						System.out
								.println("Please Enter Valid Date for That Month");
						break;
					}
				}
			case 4:
			case 6:
			case 9:
			case 11:
				if (day >= 1 && day <= 30) {
					if (yearFormat.getBytes().length <= 3) {
						return new Date(year + 100, month - 1, day);
					}
					return new Date(year - 1900, month - 1, day);
				} else {
					System.out
							.println("Please Enter Valid Date for That Month");
					break;
				}
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				if (day >= 1 && day <= 31) {
					if (yearFormat.getBytes().length <= 3) {
						return new Date(year + 100, month - 1, day);
					}
					return new Date(year - 1900, month - 1, day);
				} else {
					System.out
							.println("Please Enter Valid Date for That Month");
					break;
				}
			default:

				break;
			}
		} catch (Exception e) {

			Window.alert("Enter valid date");
			return null;
		}
		return null;
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

}
