package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.server.FinanceTool;

public class UncategorisedVATAmountsReportCommand extends
		NewAbstractReportCommand<UncategorisedAmountsReport> {

	private double balance;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);

	}

	@Override
	protected Record createReportRecord(UncategorisedAmountsReport record) {
		Record uncategoryRecord = new Record(record);

		uncategoryRecord.add(getMessages().transactionType(),
				Utility.getTransactionName(record.getTransactionType()));
		if (record.getDate() != null)
			uncategoryRecord.add(getMessages().date(), record.getDate());
		else
			uncategoryRecord.add("", "");
		uncategoryRecord.add(getMessages().number(),
				record.getTransactionNumber());
		uncategoryRecord
				.add(getMessages().sourceName(), record.getSourceName());
		balance += record.getAmount();
		uncategoryRecord.add(getMessages().amount(), record.getAmount());
		uncategoryRecord.add(getMessages().balance(), balance);

		return uncategoryRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<UncategorisedAmountsReport> getRecords() {
		ArrayList<UncategorisedAmountsReport> uncategorisedAmountsReports = new ArrayList<UncategorisedAmountsReport>();
		try {
			uncategorisedAmountsReports = new FinanceTool().getReportManager()
					.getUncategorisedAmountsReport(getStartDate(),
							getEndDate(), getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uncategorisedAmountsReports;
	}

	@Override
	protected String addCommandOnRecordClick(
			UncategorisedAmountsReport selection) {
		return "update transaction " + selection.getTransactionNumber();
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
				getMessages().uncategorisedVATAmounts());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().uncategorisedVATAmounts());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().uncategorisedVATAmounts());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().uncategorisedVATAmounts());
	}

}