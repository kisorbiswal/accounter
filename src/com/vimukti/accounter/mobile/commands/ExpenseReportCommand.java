package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;

public class ExpenseReportCommand extends AbstractReportCommand<ExpenseList> {
	private String currentsectionName = "";
	private Double accountBalance = 0.0;

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
		list.add(new Requirement("expenseType", true, true));
	}

	@Override
	protected Result createReqReportRecord(Result reportResult, Context context) {
		ResultList resultList = new ResultList("values");

		// Checking whether expense type is there or not and returning result
		Requirement expenseTypeReq = get("expenseType");
		String expenseType = (String) expenseTypeReq.getValue();
		String selectionExpenseType = context.getSelection("values");
		if (expenseType == selectionExpenseType)
			return expenseTypeRequirement(context, resultList,
					selectionExpenseType);

		return super.createReqReportRecord(reportResult, context);
	}

	@Override
	protected Record createReportRecord(ExpenseList record) {
		Record expenseRecord = new Record(record);

		expenseRecord.add("", Utility.getTransactionName(record
				.getTransactionType()));
		expenseRecord.add("Transaction date", record.getTransactionDate());
		expenseRecord.add("Amount", record.getTotal());
		if (!currentsectionName.equals(record.getName())) {
			currentsectionName = record.getName();
			accountBalance = 0.0D;
		}
		expenseRecord.add("Balance", accountBalance += record.getTotal());
		return expenseRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<ExpenseList> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(ExpenseList selection,
			CommandList commandList) {
		commandList
				.add(Utility.getTransactionName(selection.getTransactionType()));
	}

}
