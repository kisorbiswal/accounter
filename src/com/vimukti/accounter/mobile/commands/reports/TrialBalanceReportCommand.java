package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class TrialBalanceReportCommand extends
		NewAbstractReportCommand<TrialBalance> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeToDateRequirements(list);
		list.add(new ReportResultRequirement<TrialBalance>() {

			@Override
			protected String onSelection(TrialBalance selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TrialBalance> records = getRecords();
			}
		});
	}

	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);

		trialRecord.add(getMessages().accountName(), record.getAccountName());
		if (getCompany().getPreferences().getUseAccountNumbers())
			trialRecord.add(getMessages().accountNumber(),
					record.getAccountNumber());
		else
			trialRecord.add("", "");
		trialRecord.add(getMessages().debit(), record.getDebitAmount());
		trialRecord.add(getMessages().credit(), record.getCreditAmount());
		return trialRecord;
	}

	protected List<TrialBalance> getRecords() {
		List<TrialBalance> trialBalanceDetails = new ArrayList<TrialBalance>();
		try {
			trialBalanceDetails = new FinanceTool().getReportManager()
					.getTrialBalance(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
			trialBalanceDetails = new ArrayList<TrialBalance>();
		}
		return trialBalanceDetails;
	}

	protected String addCommandOnRecordClick(TrialBalance selection) {
		return "Transaction Detail By Account ," + selection.getAccountNumber();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return "Trial balance report details";
	}

	@Override
	public String getSuccessMessage() {
		return "Trial balance report command closed successfully";
	}
}
