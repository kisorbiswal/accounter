package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

public class ProfitLossReportCommand extends
		NewAbstractReportCommand<TrialBalance> {
	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<TrialBalance>() {

			@Override
			protected String onSelection(TrialBalance selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TrialBalance> records = getRecords();
				Map<String, List<TrialBalance>> recordGroups = new HashMap<String, List<TrialBalance>>();
				for (TrialBalance transactionDetailByAccount : records) {
					String taxItemName = ProfitLossReportCommand.this
							.getAccountTypeString(transactionDetailByAccount
									.getAccountType());
					List<TrialBalance> group = recordGroups.get(taxItemName);
					if (group == null) {
						group = new ArrayList<TrialBalance>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				double[] incomeTotals = addResultList(recordGroups
						.get(getMessages().income()), getMessages().income(),
						makeResult);
				if (recordGroups.get(getMessages().income()) != null) {
					makeResult.add(getMessages().incomeTotals() + " : "
							+ incomeTotals[0] + " \t" + incomeTotals[1]);
				}
				double[] cogsTotals = addResultList(
						recordGroups.get(getMessages().costOfGoodSold()),
						getMessages().costOfGoodSold(), makeResult);
				if (recordGroups.get(getMessages().costOfGoodSold()) != null) {
					makeResult.add(getMessages().cogsTotal() + " : "
							+ cogsTotals[0] + " \t" + cogsTotals[1]);
				}
				double[] otherExpTotals = addResultList(
						recordGroups.get(getMessages().otherExpense()),
						getMessages().otherExpense(), makeResult);
				if (recordGroups.get(getMessages().otherExpense()) != null) {
					makeResult.add(getMessages().otherExpenseTotals() + " : "
							+ otherExpTotals[0] + " \t" + otherExpTotals[1]);
				}
				Double grosProfitAmount = incomeTotals[0] + otherExpTotals[0]
						+ cogsTotals[0];
				Double grosProfitTotal = incomeTotals[1] + otherExpTotals[1]
						+ cogsTotals[1];
				makeResult.add(getMessages().grossProfit() + " : "
						+ grosProfitAmount + " \t" + grosProfitTotal);
				double[] expTotals = addResultList(
						recordGroups.get(getMessages().expense()),
						getMessages().expense(), makeResult);
				if (recordGroups.get(getMessages().expense()) != null) {
					makeResult.add(getMessages().expenseTotals() + " : "
							+ expTotals[0] + " \t" + expTotals[1]);
				}
				double[] otherIncTotals = addResultList(
						recordGroups.get(getMessages().otherExpense()),
						getMessages().otherExpense(), makeResult);
				if (recordGroups.get(getMessages().otherExpense()) != null) {
					makeResult.add(getMessages().otherIncomeTotal() + " : "
							+ otherIncTotals[0] + " \t" + otherIncTotals[1]);
				}
				Double netProfitAmount = grosProfitAmount
						- (otherIncTotals[0] + expTotals[0]);
				Double netProfitTotal = grosProfitTotal
						- (otherIncTotals[1] + expTotals[1]);
				makeResult.add(getMessages().netProfit() + " : "
						+ netProfitAmount + " \t" + netProfitTotal);
			}

			private double[] addResultList(List<TrialBalance> group,
					String accountName, Result makeResult) {
				addSelection(accountName);
				ResultList resultList = new ResultList(accountName);
				resultList.setTitle(accountName);
				double amount = 0.0;
				double total = 0.0;
				if (group != null) {
					for (TrialBalance rec : group) {
						resultList.add(createReportRecord(rec));
						total += rec.getTotalAmount();
						amount += rec.getAmount();
					}
					makeResult.add(resultList);
				}
				return new double[] { amount, total };
			}
		});
	}

	protected String getAccountTypeString(int accountType) {
		if (accountType == ClientAccount.TYPE_INCOME) {
			return getMessages().income();
		}
		if (accountType == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			return getMessages().costOfGoodSold();
		}

		if (accountType == ClientAccount.TYPE_OTHER_EXPENSE) {
			return getMessages().otherExpense();
		}

		if (accountType == ClientAccount.TYPE_EXPENSE) {
			return getMessages().expense();
		}

		if (accountType == ClientAccount.TYPE_OTHER_INCOME) {
			return getMessages().otherIncome();
		}
		return null;
	}

	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		if (getCompany().getPreferences().getUseAccountNumbers() == true) {
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
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {

		}
		return null;
	}

}
