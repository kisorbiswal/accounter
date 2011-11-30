package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class BalanceSheetReportCommand extends
		NewAbstractReportCommand<TrialBalance> {
	private ClientAccount account;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeToDateRequirements(list);

		list.add(new ReportResultRequirement<TrialBalance>() {

			@Override
			protected String onSelection(TrialBalance selection, String name) {
				return "Transaction Detail By Account ,"
						+ selection.getAccountNumber();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TrialBalance> records = getRecords();

			}
		});
	}

	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		if (getCompany().getPreferences().getUseAccountNumbers() == true) {
			trialRecord.add(getMessages().categoryNumber(),
					record.getAccountNumber());
		} else {
			return null;
		}
		trialRecord.add(getMessages().accountName(), record.getAccountName());
		trialRecord
				.add(getStartDate() + "-" + getEndDate(), record.getAmount());
		trialRecord.add("", record.getAmount());
		return trialRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	protected List<TrialBalance> getRecords() {
		ArrayList<TrialBalance> trailBalanceReport = new ArrayList<TrialBalance>();
		try {
			trailBalanceReport = new FinanceTool().getReportManager()
					.getBalanceSheetReport(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trailBalanceReport;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String number = null;
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			number = split[1];
		}
		if (number != null) {
			account = CommandUtils.getAccountByNumber(getCompany(), number);
			if (account == null) {
				account = CommandUtils.getAccountByName(getCompany(), number);
			}
		}
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().balanceSheet());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().balanceSheet());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().balanceSheet());
	}

}
