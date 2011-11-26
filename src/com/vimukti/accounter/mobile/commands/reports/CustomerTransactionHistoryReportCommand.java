package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public class CustomerTransactionHistoryReportCommand extends
		NewAbstractReportCommand<TransactionHistory> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(TransactionHistory record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add(getMessages().payeeName(Global.get().Customer()),
				"");
		transactionRecord.add(getMessages().date(), record.getDate());
		transactionRecord.add(getMessages().transactionType(),
				Utility.getTransactionName(record.getType()));
		transactionRecord.add(getMessages().number(), record.getNumber());
		transactionRecord.add(getMessages().amount(), record.getAccount());
		transactionRecord.add(getMessages().amount(), DecimalUtil.isEquals(
				record.getInvoicedAmount(), 0.0) ? record.getPaidAmount()
				: record.getInvoicedAmount());
		return transactionRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<TransactionHistory> getRecords() {
		ArrayList<TransactionHistory> transatiHistories = new ArrayList<TransactionHistory>();
		try {
			transatiHistories = new FinanceTool().getCustomerManager()
					.getCustomerTransactionHistory(getStartDate(),
							getEndDate(), getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transatiHistories;
	}

	@Override
	protected String addCommandOnRecordClick(TransactionHistory selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return "";
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(
				getMessages().customer() + getMessages().transaction()
						+ getMessages().history());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().customer() + getMessages().transaction()
						+ getMessages().history());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().customer() + getMessages().transaction()
						+ getMessages().history());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().customer() + getMessages().transaction()
						+ getMessages().history());
	}

}