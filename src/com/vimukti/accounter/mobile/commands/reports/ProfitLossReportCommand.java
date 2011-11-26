package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility_R;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class ProfitLossReportCommand extends
		NewAbstractReportCommand<TrialBalance> {
	private double incomeTotals;
	private double grossProfitTotals;
	private double expenseTotals;
	private double netProfit;
	private double netLoss;
	private double costOfGoodsSold;
	private double costOfGoodsSoldTotal;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		if (getCompany().getPreferences().getUseAccountNumbers() == true) {
			trialRecord.add(getMessages().categoryNumber(),
					record.getAccountNumber());
		} else {
			trialRecord.add(getMessages().categoryNumber(), "");
		}
		trialRecord.add(getMessages().name(), record.getAccountName());
		trialRecord
				.add(getStartDate() + "_" + getEndDate(), record.getAmount());
		trialRecord.add(getMessages().amount(), record.getAmount());
		trialRecord.add(Utility_R.getCurrentFiscalYearStartDate(getCompany())
				+ "_" + getLastMonth(new FinanceDate()),
				record.getTotalAmount());
		trialRecord.add(getMessages().totalBalance(), record.getTotalAmount());
		return trialRecord;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {

	}

	@Override
	protected String addCommandOnRecordClick(TrialBalance selection) {
		return "Transaction Detail By Account ," + selection.getAccountNumber();
	}

	@Override
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
	protected String getEmptyString() {
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return null;
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(getMessages().profitAndLoss());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {

		}
		return null;
	}

}
