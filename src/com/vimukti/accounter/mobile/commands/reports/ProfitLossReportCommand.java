package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class ProfitLossReportCommand extends
		NewAbstractReportCommand<TrialBalance> {
	protected Double totalincome = 0.0D;
	protected Double totalCGOS = 0.0D;
	protected Double grosProft = 0.0D;
	protected Double totalexpese = 0.0D;
	protected Double netProfit = 0.0D;
	protected Double otherIncome = 0.0D;
	protected Double otherExpense = 0.0D;
	protected Double otherNetIncome = 0.0D;

	protected Double totalincome2 = 0.0D;
	protected Double totalCGOS2 = 0.0D;
	protected Double grosProft2 = 0.0D;
	protected Double totalexpese2 = 0.0D;
	protected Double netProfit2 = 0.0D;
	protected Double otherIncome2 = 0.0D;
	protected Double otherExpense2 = 0.0D;
	protected Double otherNetIncome2 = 0.0D;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	private void initValues() {
		totalincome = 0.0D;
		totalCGOS = 0.0D;
		grosProft = 0.0D;
		totalexpese = 0.0D;
		netProfit = 0.0D;
		otherIncome = 0.0D;
		otherExpense = 0.0D;
		otherNetIncome = 0.0D;

		totalincome2 = 0.0D;
		totalCGOS2 = 0.0D;
		grosProft2 = 0.0D;
		totalexpese2 = 0.0D;
		netProfit2 = 0.0D;
		otherIncome2 = 0.0D;
		otherExpense2 = 0.0D;
		otherNetIncome2 = 0.0D;
	}

	@Override
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
		// addCategories(profitAndLossReport);
		return profitAndLossReport;
	}

	private void addCategories(ArrayList<TrialBalance> profitAndLossReport) {
		initValues();
		for (TrialBalance trialBalance : profitAndLossReport) {
			String accountTypeSting = getAccountTypeSting(trialBalance
					.getAccountType());
			if (categories.isEmpty()) {
				ResultList newCategory = new ResultList(accountTypeSting);
				newCategory.add(createReportRecord(trialBalance));
				categories.add(newCategory);
			} else {
				for (ResultList categiry : categories) {
					if (categiry != null
							&& categiry.getName().equals(accountTypeSting)) {
						categiry.add(createReportRecord(trialBalance));
					} else {
						ResultList newCategory = new ResultList(
								accountTypeSting);
						newCategory.add(createReportRecord(trialBalance));
						categories.add(newCategory);
					}
				}
			}
		}
	}

	private String getAccountTypeSting(int accountType) {
		if (accountType == ClientAccount.TYPE_INCOME
				|| accountType == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			// calculate getMessages().grossProfit());

			if (accountType == ClientAccount.TYPE_INCOME) {
				return getMessages().income();
				// calculateIncomTotals
			}
			if (accountType == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
				return getMessages().costOfGoodSold();
				// calculate getMessages().cogsTotal()
			}
		}
		if (accountType == ClientAccount.TYPE_OTHER_EXPENSE) {
			return getMessages().otherExpense();
			// calculate getMessages().otherExpenseTotals();
		}

		if (accountType == ClientAccount.TYPE_EXPENSE) {
			return getMessages().expense();
			// calculate getMessages().expenseTotals());
		}

		if (accountType == ClientAccount.TYPE_OTHER_INCOME) {
			return getMessages().otherIncome();
			// calculate getMessages().otherIncomeTotal();
		}

		return null;
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
