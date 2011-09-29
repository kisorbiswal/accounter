package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;

public class TransactionDetailByAccountReportCommand extends
		AbstractReportCommand<TransactionDetailByAccount> {

	private double accountBalance;
	private String currentsectionName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(TransactionDetailByAccount record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add("", "");
		transactionRecord.add("Name", record.getName());
		transactionRecord.add("Date", record.getTransactionDate());
		transactionRecord.add("", Utility.getTransactionName(getType(record)));
		transactionRecord.add("No.", record.getTransactionNumber());
		transactionRecord.add("Amount", record.getTotal());
		if (!currentsectionName.equals(record.getAccountName())) {
			currentsectionName = record.getAccountName();
			accountBalance = 0.0D;
		}
		transactionRecord.add("Balance", accountBalance += record.getTotal());
		return transactionRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<TransactionDetailByAccount> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(
			TransactionDetailByAccount selection, CommandList commandList) {
		commandList.add(ReportUtility.getTransactionName(selection
				.getTransactionType()));
	}

}
