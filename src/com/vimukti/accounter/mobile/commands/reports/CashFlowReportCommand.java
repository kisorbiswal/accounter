package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class CashFlowReportCommand extends
		NewAbstractReportCommand<TrialBalance> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		trialRecord.add(
				"",
				record.getAccountNumber() != null ? record.getAccountNumber()
						+ "-" + record.getAccountName() : ""
						+ record.getAccountName());
		trialRecord
				.add(getStartDate() + "_" + getEndDate(), record.getAmount());
		return trialRecord;
	}

	protected List<TrialBalance> getRecords() {
		List<TrialBalance> cashFlowStatements = new ArrayList<TrialBalance>();
		try {
			cashFlowStatements = new FinanceTool().getReportManager()
					.getCashFlowReport(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (AccounterException e) {
			e.printStackTrace();
			cashFlowStatements = new ArrayList<TrialBalance>();
		}
		return cashFlowStatements;
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
		return "Cash flow report command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "Cash flow report details as follows:";
	}

	@Override
	public String getSuccessMessage() {
		return "Cash flow Report command closed successfully";
	}

}
