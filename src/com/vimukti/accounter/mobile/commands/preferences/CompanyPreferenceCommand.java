package com.vimukti.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CompanyPreferenceCommand extends AbstractCompanyPreferencesCommand {
	// protected static final String COMPANY_DATEFORMAT = "companydate";
	private static final String TAX_ID = "taxid";
	protected static final String FISCAL_MONTH = "fiscalamonth";
	private static final String PREVENT_POSTING_DATE = "preventdate";
	private static final String USE_CUSTOMER_NUMBER = "usecustomernumber";
	private static final String USE_VENDOR_NUMBER = "usevendornumber";
	private static final String USE_ACCOUNT_NUMBER = "primarycurrency";
	protected static final String TIMEZONE = "timezone";
	

	@Override
	protected void addRequirements(List<Requirement> list) {

		// list.add(new ShowListRequirement<String>("Date Formats",
		// "Please Enter format", 10) {
		//
		// @Override
		// protected String getEmptyString() {
		// return getMessages().noRecordsToShow();
		// }
		//
		// @Override
		// protected void setCreateCommand(CommandList list) {
		// // TODO Auto-generated method stub
		// }
		//
		// @Override
		// protected List<String> getLists(Context context) {
		// String[] dateFormates;
		//
		// dateFormates = new String[] { "ddMMyy", "MM/dd/yy", "dd/MM/yy",
		// "ddMMyyyy", "MMddyyyy", "MMM-dd-yy", "MMMddyyyy",
		// "dd/MM/yyyy", "MM/dd/yyyy", "dd/MMMM/yyyy",
		// "MMMMddyyyy", "dd-MM-yyyy", "MM-dd-yyyy",
		// "dd/MMM/yyyy", "MMM/dd/yyyy", };
		// List<String> formatList = new ArrayList<String>();
		// for (int i = 0; i < dateFormates.length; i++) {
		// formatList.add(dateFormates[i]);
		// }
		// return formatList;
		//
		// }
		//
		// @Override
		// protected String getShowMessage() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// protected String onSelection(String value) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// protected Record createRecord(String value) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// protected boolean filter(String e, String name) {
		// // TODO Auto-generated method stub
		// return false;
		// }
		// });

		list.add(new StringRequirement(TAX_ID, getMessages().pleaseEnter(
				getMessages().taxId()), getMessages().taxId(), true, true));

		list.add(new StringListRequirement(FISCAL_MONTH, getMessages()
				.pleaseSelect(getMessages().FirstFiscalMonth()), getMessages()
				.FirstFiscalMonth(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().FirstFiscalMonth());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getFiscalYearMonths();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().FirstFiscalMonth());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new DateRequirement(PREVENT_POSTING_DATE, getMessages()
				.pleaseEnter(getMessages().preventPostBefore()), getMessages()
				.preventPostBefore(), true, true));

		list.add(new BooleanRequirement(USE_CUSTOMER_NUMBER, true) {

			@Override
			protected String getTrueString() {
				return getMessages().useCustomersNumbers() + " :"
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().useCustomersNumbers() + " :"
						+ getMessages().inActive();
			}
		});

		list.add(new BooleanRequirement(USE_VENDOR_NUMBER, true) {

			@Override
			protected String getTrueString() {
				return getMessages().useVendorNumbers() + " :"
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().useVendorNumbers() + " :"
						+ getMessages().inActive();
			}
		});

		list.add(new BooleanRequirement(USE_ACCOUNT_NUMBER, true) {

			@Override
			protected String getTrueString() {
				return getMessages().useAccountNos() + " :"
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().useAccountNos() + " :"
						+ getMessages().inActive();
			}
		});

		

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

	}

	private List<String> getFiscalYearMonths() {

		String[] names = new String[] { getMessages().january(),
				getMessages().february(), getMessages().march(),
				getMessages().april(), getMessages().may(),
				getMessages().june(), getMessages().july(),
				getMessages().august(), getMessages().september(),
				getMessages().october(), getMessages().november(),
				getMessages().december() };
		List<String> fiscalYearMonths = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			fiscalYearMonths.add(names[i]);
		}
		return fiscalYearMonths;

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();
		// get(COMPANY_DATEFORMAT).setValue(preferences.getDateFormat());
		get(TAX_ID).setValue(preferences.getTaxId());
		get(FISCAL_MONTH).setValue(
				getFiscalYearMonths()
						.get(preferences.getFiscalYearFirstMonth()));
		if (preferences.getPreventPostingBeforeDate() != 0) {
			get(PREVENT_POSTING_DATE).setValue(
					new ClientFinanceDate(preferences
							.getPreventPostingBeforeDate()));
		}
		get(USE_CUSTOMER_NUMBER).setValue(preferences.getUseCustomerId());
		get(USE_VENDOR_NUMBER).setValue(preferences.getUseVendorId());
		get(USE_ACCOUNT_NUMBER).setValue(preferences.getUseAccountNumbers());
		get(TIMEZONE).setValue(preferences.getTimezone());
		

		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		// String dateformat = get(COMPANY_DATEFORMAT).getValue();
		String taxid = get(TAX_ID).getValue();
		String fiscalmonth = get(FISCAL_MONTH).getValue();
		ClientFinanceDate preventpostingdate = get(PREVENT_POSTING_DATE)
				.getValue();
		boolean customernumber = get(USE_CUSTOMER_NUMBER).getValue();
		boolean vendornumber = get(USE_VENDOR_NUMBER).getValue();
		boolean accountnumber = get(USE_ACCOUNT_NUMBER).getValue();
		String timezone = get(TIMEZONE).getValue();
		
		// preferences.setDateFormat(dateformat);
		preferences.setTaxId(taxid);
		preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
				fiscalmonth) + 1);
		preferences.setPreventPostingBeforeDate(preventpostingdate.getDate());
		preferences.setUseCustomerId(customernumber);
		preferences.setUseVendorId(vendornumber);
		preferences.setUseAccountNumbers(accountnumber);
		preferences.setTimezone(timezone);
		

		savePreferences(context, preferences);
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Updating Preferences";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToUpdate(getMessages().companyPreferences());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().updateSuccessfully(
				getMessages().companyPreferences());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
