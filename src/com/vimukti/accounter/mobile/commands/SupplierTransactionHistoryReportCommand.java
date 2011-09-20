package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class SupplierTransactionHistoryReportCommand extends
		AbstractReportCommand<TransactionHistory> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(TransactionHistory record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add("Supplier", "");
		transactionRecord.add("Date", record.getDate());
		transactionRecord.add("Type", Utility.getTransactionName(record
				.getType()));
		transactionRecord.add("No.", record.getNumber());
		transactionRecord.add("Account", record.getAccount());
		transactionRecord.add("Amount", DecimalUtil.isEquals(record
				.getInvoicedAmount(), 0.0) ? record.getPaidAmount() : record
				.getInvoicedAmount());
		return transactionRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<TransactionHistory> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}