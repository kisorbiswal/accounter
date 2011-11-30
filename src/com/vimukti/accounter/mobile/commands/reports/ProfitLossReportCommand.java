package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class ProfitLossReportCommand extends
		NewAbstractReportCommand<TrialBalance> {
	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		if (getCompany().getPreferences().getUseAccountNumbers() == true) {
			trialRecord.add("", record.getAccountNumber());
			trialRecord.add(getMessages().categoryNumber(),
					record.getAccountNumber());
		}
		trialRecord.add(getMessages().name(), record.getAccountName());
		trialRecord.add(getMessages().amount(), record.getAmount());
		trialRecord.add(getMessages().totalBalance(), record.getTotalAmount());
		return trialRecord;
	}

	protected String addCommandOnRecordClick(TrialBalance selection) {
		return "Transaction Detail By Account ," + selection.getAccountNumber();
	}

	protected List<TrialBalance> getRecords() {
		FinanceDate start = getStartDate();
		FinanceDate end = getEndDate();
		ArrayList<TrialBalance> profitAndLossReport = new ArrayList<TrialBalance>();
		try {
			profitAndLossReport = new FinanceTool().getReportManager()
					.getProfitAndLossReport(start, end, getCompanyId());
			// for (TrialBalance trialBalance : profitAndLossReport) {
			// if (trialBalance.getAccountType() == ClientAccount.TYPE_INCOME) {
			// incomeTotals += trialBalance.getTotalAmount();
			// } else if (trialBalance.getAccountType() ==
			// ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			// costOfGoodsSoldTotal += trialBalance.getTotalAmount();
			// }
			// }// else if (trialBalance.getAccountType() == ClientAccount.)
			// // }
		} catch (DAOException e) {
			e.printStackTrace();
		}
		// addCategories(profitAndLossReport,context);
		return profitAndLossReport;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().profitAndLoss());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().profitAndLoss());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().profitAndLoss());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {

		}
		return null;
	}

}
