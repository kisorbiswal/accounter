package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;

public class SupplierTransactionHistoryReportCommand extends
		AbstractReportCommand<TransactionHistory> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(TransactionHistory record) {
		// TODO Auto-generated method stub
		return null;
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