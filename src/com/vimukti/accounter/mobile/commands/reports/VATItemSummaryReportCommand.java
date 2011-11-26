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
		vatItemRecord.add(getMessages().name(), record.getName());
		vatItemRecord.add(getMessages().amount(), record.getAmount());
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
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return "";
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(getMessages().vatItemSummary());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().vatItemSummary());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().vatItemSummary());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().vatItemSummary());
	}

}