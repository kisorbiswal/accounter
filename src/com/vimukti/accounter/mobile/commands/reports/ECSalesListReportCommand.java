package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.server.FinanceTool;

public class ECSalesListReportCommand extends
		NewAbstractReportCommand<ECSalesList> {

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	protected Record createReportRecord(ECSalesList record) {
		Record salesRecord = new Record(record);
		salesRecord.add("", record.getName());
		salesRecord
				.add(getStartDate() + "_" + getEndDate(), record.getAmount());
		return salesRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<ECSalesList> getRecords() {
		ArrayList<ECSalesList> ecSalesLists = new ArrayList<ECSalesList>();
		try {
			ecSalesLists = new FinanceTool().getReportManager()
					.getECSalesListReport(getStartDate(), getEndDate(),
							getCompany());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecSalesLists;
	}

	@Override
	protected String addCommandOnRecordClick(ECSalesList selection) {
		return "EC Sales List Detail," + selection.getName();
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
		return getMessages().reportSelected(getMessages().ecSalesList());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages()
				.reportCommondActivated(getMessages().ecSalesList());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().ecSalesList());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().ecSalesList());
	}

}