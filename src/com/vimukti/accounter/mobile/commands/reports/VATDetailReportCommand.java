package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class VATDetailReportCommand extends NewAbstractReportCommand<VATDetail> {

	private String currentsectionName;
	private double accountbalance;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATDetail record) {
		Record salesRecord = new Record(record);

		salesRecord.add("Type", record.getTransactionName());
		salesRecord.add("Date", record.getTransactionDate());
		salesRecord.add("No.", record.getTransactionNumber());
		salesRecord.add("VAT Rate", record.isPercentage() ? record.getVatRate()
				+ "%" : record.getVatRate());
		salesRecord.add("Net amount", record.getNetAmount());
		salesRecord.add("Amount", record.getTotal());
		if (!currentsectionName.equals(record.getBoxName())) {
			currentsectionName = record.getBoxName();
			accountbalance = 0.0D;
		}
		salesRecord.add("Balance", accountbalance += record.getTotal());

		return salesRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<VATDetail> getRecords() {
		ArrayList<VATDetail> vatDetails = new ArrayList<VATDetail>();
		try {
			vatDetails = new FinanceTool().getReportManager()
					.getVATDetailReport(getStartDate(), getEndDate(),
							getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetails;
	}

	@Override
	protected String addCommandOnRecordClick(VATDetail selection) {
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
		return getMessages().reportSelected(getMessages().vatDetail());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(getMessages().vatDetail());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().vatDetail());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().vatDetail());
	}

}