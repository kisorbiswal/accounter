package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author vimukti2
 * 
 */
public class CashFlowReportCommand extends
		NewAbstractReportCommand<TrialBalance> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);

		list.add(new ReportResultRequirement<TrialBalance>() {

			@Override
			protected String onSelection(TrialBalance selection, String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {

				List<TrialBalance> records = getRecords();
				double netIncomeTotal = 0.0;
				TrialBalance trialBalance = records.get(0);
				if (trialBalance.getAccountName().equals(
						getMessages().netIncome())) {
					netIncomeTotal = trialBalance.getAmount();

				}
				records.remove(0);
				Map<String, List<TrialBalance>> recordGroups = new HashMap<String, List<TrialBalance>>();
				for (TrialBalance trailBalanceRecord : records) {
					String subBaseTypeName = getCashFlowCategoryName(trailBalanceRecord
							.getCashFlowCategory());
					List<TrialBalance> group = recordGroups
							.get(subBaseTypeName);
					if (group == null) {
						group = new ArrayList<TrialBalance>();
						recordGroups.put(subBaseTypeName, group);
					}
					group.add(trailBalanceRecord);
				}

				ResultList operatingActivitieList = new ResultList(
						getMessages().operatingActivities());
				operatingActivitieList
						.setTitle("Adjustments to reconcile net income to net cash");
				double operatingActivitieAmount = fillResultList(
						operatingActivitieList,
						recordGroups.get(getMessages().operatingActivities()));

				ResultList invetList = new ResultList(getMessages()
						.investingActivities());
				invetList.setTitle(getMessages().investingActivities());
				double investingActivitieAmount = fillResultList(invetList,
						recordGroups.get(getMessages().investingActivities()));

				ResultList financingActivitieList = new ResultList(
						getMessages().financingActivities());
				financingActivitieList.setTitle(getMessages()
						.financingActivities());
				double financingActivitieAmount = fillResultList(
						financingActivitieList,
						recordGroups.get(getMessages().financingActivities()));

				ResultList netIncomeList = new ResultList("Net Income");
				netIncomeList.setTitle(getMessages().operatingActivities());
				Record record = new Record("Net Income");
				record.add(" ", "Net Income");
				record.add(" ", netIncomeTotal);
				netIncomeList.add(record);
				makeResult.add(netIncomeList);

				makeResult.add(operatingActivitieList);
				makeResult.add("End of aR-NI-NC :" + operatingActivitieAmount);
				double operatingActivitieNetAmount = netIncomeTotal
						+ operatingActivitieAmount;
				makeResult.add("Net cash provided by OPERATING ACTIVITIES  :"
						+ operatingActivitieNetAmount);

				if (netIncomeList != null && !invetList.isEmpty()) {
					makeResult.add(invetList);
					makeResult
							.add("Net cash provided by INVESTING ACTIVITIES  :"
									+ investingActivitieAmount);
				}
				double netAmountTotal = operatingActivitieNetAmount
						+ financingActivitieAmount + investingActivitieAmount;

				if (financingActivitieList != null
						&& !financingActivitieList.isEmpty()) {
					makeResult.add(financingActivitieList);
					makeResult
							.add("Net cash provided by FINANCING ACTIVITIES  :"
									+ financingActivitieAmount);
				}
				makeResult.add("Cash at End of Period   :" + netAmountTotal);

			}

			private double fillResultList(ResultList list,
					List<TrialBalance> records) {
				double total = 0.0;
				for (TrialBalance record : records) {
					Record createReportRecord = createReportRecord(record);
					total += record.getAmount();
					list.add(createReportRecord);
				}
				return total;
			}

			private String getCashFlowCategoryName(int type) {
				String cashFlowCategoryName = null;
				switch (type) {
				case ClientAccount.CASH_FLOW_CATEGORY_OPERATING:
					cashFlowCategoryName = getMessages().operatingActivities();
					break;
				case ClientAccount.CASH_FLOW_CATEGORY_FINANCING:
					cashFlowCategoryName = getMessages().financingActivities();
					break;
				case ClientAccount.CASH_FLOW_CATEGORY_INVESTING:
					cashFlowCategoryName = getMessages().investingActivities();
					break;
				}
				return cashFlowCategoryName;
			}
		});
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

	/**
	 * getting the cash flow Report Records
	 * 
	 * @return {@link List<TrialBalance>}
	 */
	private List<TrialBalance> getRecords() {
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
}
