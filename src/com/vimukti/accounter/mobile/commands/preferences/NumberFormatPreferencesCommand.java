package com.vimukti.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class NumberFormatPreferencesCommand extends
		AbstractCompanyPreferencesCommand {

	private static final String DATE_FORMAT = "dateFormat";
	private static final String USE_CUSTOMER_NUMBERS = "usecustomernumbers";
	private static final String USE_VENDOR_NUMBERS = "usevendornumbers";
	private static final String USE_ACCOUNT_NUMBERS = "useaccountnumbers";
	private static final String TIMEZONE = "timezone";
	private static final String NEGATIVE_NUMBER_FORMAT = "negativenumberformat";
	private static final String DECIMAL_DIGIT_LIMIT = "decimaldigitlimit";
	private static final String FISCAL_MONTH = "fiscalamonth";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		ClientCompanyPreferences preferences = context.getPreferences();
		get(DATE_FORMAT).setValue(preferences.getDateFormat());
		get(USE_CUSTOMER_NUMBERS).setValue(preferences.getUseCustomerId());
		get(USE_VENDOR_NUMBERS).setValue(preferences.getUseVendorId());
		get(USE_ACCOUNT_NUMBERS).setValue(preferences.getUseAccountNumbers());
		get(TIMEZONE).setValue(preferences.getTimezone());
		get(NEGATIVE_NUMBER_FORMAT).setValue(
				getNegativeNumFormats().get(
						preferences.getNegativeNumberShownType()));
		get(DECIMAL_DIGIT_LIMIT).setValue(preferences.getDecimalNumber());
		// get(FISCAL_MONTH).setValue(
		// getFiscalYearMonths()
		// .get(preferences.getFiscalYearFirstMonth()));
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(DATE_FORMAT, getMessages()
				.pleaseSelect("Date Format"), "Date Format", true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect("Date Format");
			}

			@Override
			protected String getSelectString() {
				return getMessages().hasSelected("Date Format");
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> dateFormates = new ArrayList<String>();
				dateFormates.add("ddMMyy");
				dateFormates.add("MM/dd/yy");
				dateFormates.add("dd/MM/yy");
				dateFormates.add("ddMMyyyy");
				dateFormates.add("MMddyyyy");
				dateFormates.add("MMM-dd-yy");
				dateFormates.add("MMMddyyyy");
				dateFormates.add("dd/MM/yyyy");
				dateFormates.add("MM/dd/yyyy");
				dateFormates.add("dd/MMMM/yyyy");
				dateFormates.add("MMMMddyyyy");
				dateFormates.add("dd-MM-yyyy");
				dateFormates.add("MM-dd-yyyy");
				dateFormates.add("dd/MMM/yyyy");
				dateFormates.add("MMM/dd/yyyy");
				return dateFormates;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		list.add(new BooleanRequirement(USE_CUSTOMER_NUMBERS, true) {

			@Override
			protected String getTrueString() {
				return "Use Customer Numbers";
			}

			@Override
			protected String getFalseString() {
				return "Do not use customer numbers";
			}
		});

		list.add(new BooleanRequirement(USE_VENDOR_NUMBERS, true) {

			@Override
			protected String getTrueString() {
				return "Use Vendor Numbers";
			}

			@Override
			protected String getFalseString() {
				return "Do not use vendor numbers";
			}
		});

		list.add(new BooleanRequirement(USE_ACCOUNT_NUMBERS, true) {

			@Override
			protected String getTrueString() {
				return "Use Account Numbers";
			}

			@Override
			protected String getFalseString() {
				return "Do not use account numbers";
			}
		});

		// list.add(new StringListRequirement(FISCAL_MONTH, getMessages()
		// .pleaseSelect(getMessages().FirstFiscalMonth()), getMessages()
		// .FirstFiscalMonth(), true, true, null) {
		//
		// @Override
		// protected String getSelectString() {
		// return getMessages().pleaseSelect(
		// getMessages().FirstFiscalMonth());
		// }
		//
		// @Override
		// protected List<String> getLists(Context context) {
		// return getFiscalYearMonths();
		// }
		//
		// @Override
		// protected String getSetMessage() {
		// return getMessages().hasSelected(
		// getMessages().FirstFiscalMonth());
		// }
		//
		// @Override
		// protected String getEmptyString() {
		// return null;
		// }
		// });

		list.add(new StringListRequirement(TIMEZONE, getMessages()
				.pleaseSelect(getMessages().timezone()), getMessages()
				.timezone(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().timezone());
			}

			@Override
			protected List<String> getLists(Context context) {
				return CoreUtils.getTimeZonesAsList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().timezone());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(NEGATIVE_NUMBER_FORMAT,
				getMessages()
						.pleaseSelect(getMessages().negativeNumberFormat()),
				getMessages().negativeNumberFormat(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(
						getMessages().negativeNumberFormat());
			}

			@Override
			protected String getSelectString() {
				return getMessages().hasSelected(
						getMessages().negativeNumberFormat());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getNegativeNumFormats();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new ListRequirement<Integer>(DECIMAL_DIGIT_LIMIT,
				getMessages().pleaseSelect(getMessages().decimalDigitLimit()),
				getMessages().decimalDigitLimit(), true, true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(
						getMessages().decimalDigitLimit());
			}

			@Override
			protected Record createRecord(Integer value) {
				Record record = new Record(value);
				record.add(getMessages().decimalDigitLimit(), value);
				return record;
			}

			@Override
			protected String getDisplayValue(Integer value) {
				return String.valueOf(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().decimalDigitLimit());
			}

			@Override
			protected boolean filter(Integer e, String name) {
				return false;
			}

			@Override
			protected List<Integer> getLists(Context context) {
				List<Integer> lists = new ArrayList<Integer>();
				for (int i = 0; i < 10; i++) {
					lists.add(i);
				}
				return lists;
			}
		});
	}

	protected List<String> getNegativeNumFormats() {
		List<String> negativeNumberFormats = new ArrayList<String>();
		negativeNumberFormats.add("Normal Format. Example: (-200)");
		negativeNumberFormats
				.add("Negative number with in the parathesis.Example: (200)");
		negativeNumberFormats
				.add("Negative number minus with in the parathesis.Example: (-)200");
		negativeNumberFormats.add("Number with trailing minus.Example: 200-");
		return negativeNumberFormats;

	}

	private List<String> getFiscalYearMonths() {

		String[] names = new String[] { /*
										 * DayAndMonthUtil.january(),
										 * DayAndMonthUtil.february(),
										 * DayAndMonthUtil.march(),
										 * DayAndMonthUtil.april(),
										 * DayAndMonthUtil.may_full(),
										 * DayAndMonthUtil.june(),
										 * DayAndMonthUtil.july(),
										 * DayAndMonthUtil.august(),
										 * DayAndMonthUtil.september(),
										 * DayAndMonthUtil.october(),
										 * DayAndMonthUtil.november(),
										 * DayAndMonthUtil.december()
										 */};
		List<String> fiscalYearMonths = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			fiscalYearMonths.add(names[i]);
		}
		return fiscalYearMonths;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		String dateFormat = get(DATE_FORMAT).getValue();
		Boolean useCustomerNos = get(USE_CUSTOMER_NUMBERS).getValue();
		Boolean useVendorNos = get(USE_VENDOR_NUMBERS).getValue();
		Boolean useAccountNos = get(USE_ACCOUNT_NUMBERS).getValue();
		String timeZone = get(TIMEZONE).getValue();
		String negativeNumFormat = get(NEGATIVE_NUMBER_FORMAT).getValue();
		Integer decimalLimit = get(DECIMAL_DIGIT_LIMIT).getValue();
		// String fiscalMonth = get(FISCAL_MONTH).getValue();
		preferences.setDateFormat(dateFormat);
		preferences.setUseCustomerId(useCustomerNos);
		preferences.setUseVendorId(useVendorNos);
		preferences.setUseAccountNumbers(useAccountNos);
		// preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
		// fiscalMonth));
		preferences.setTimezone(timeZone);
		preferences.setNegativeNumberShownType(getNegativeNumFormats().indexOf(
				negativeNumFormat) + 1);
		preferences.setDecimalNumber(decimalLimit);
		savePreferences(context, preferences);
		return null;
	}
}
