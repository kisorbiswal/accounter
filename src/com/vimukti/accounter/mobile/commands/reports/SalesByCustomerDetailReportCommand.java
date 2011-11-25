package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesByCustomerDetailReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {
	private String customerName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add("Customer Name", "");
		transactionRecord.add("Date", record.getDate());
		transactionRecord.add("Type",
				Utility.getTransactionName(record.getType()));
		transactionRecord.add("No.", record.getNumber());
		transactionRecord.add("Amount", record.getAmount());
		return transactionRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			if (customerName == null && customerName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getReportManager()
						.getSalesByCustomerDetailReport(getStartDate(),
								getEndDate(), getCompanyId());
			} else if (!customerName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getReportManager()
						.getSalesByCustomerDetailReport(customerName,
								getStartDate(), getEndDate(), getCompanyId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
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
				getMessages().salesByCustomerDetail(Global.get().Customer()));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			customerName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().salesByCustomerDetail(Global.get().Customer()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().salesByCustomerDetail(Global.get().Customer()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().salesByCustomerDetail(Global.get().Customer()));
	}

}