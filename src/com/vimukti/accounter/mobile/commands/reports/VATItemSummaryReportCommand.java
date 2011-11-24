package com.vimukti.accounter.mobile.commands.reports;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.server.FinanceTool;

public class VATItemSummaryReportCommand extends
		NewAbstractReportCommand<VATItemSummary> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATItemSummary record) {
		Record vatItemRecord = new Record(record);
		vatItemRecord.add("Name", record.getName());
		vatItemRecord.add("Amout", record.getAmount());
		return vatItemRecord;
	}

	@Override
	protected List<VATItemSummary> getRecords() {
		try {
			return new FinanceTool().getReportManager()
					.getVATItemSummaryReport(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ArrayList<VATItemSummary>();
	}

	@Override
	protected String addCommandOnRecordClick(VATItemSummary selection) {
		return "VAT Item Detail ," + selection.getName();
	}

	@Override
	protected String getEmptyString() {
		return "You don't have any VAT Item summary reports";
	}

	@Override
	protected String getShowMessage() {
		return null;
	}

	@Override
	protected String getSelectRecordString() {
		return "Select a report to see details";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "VAT Item summary reprot command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "VAT item summary report details";
	}

	@Override
	public String getSuccessMessage() {
		return "VAT Item summary report command closed succesfully";
	}

}