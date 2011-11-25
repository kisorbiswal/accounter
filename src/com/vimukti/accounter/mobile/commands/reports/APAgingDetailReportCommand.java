package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.server.FinanceTool;

public class APAgingDetailReportCommand extends
		NewAbstractReportCommand<AgedDebtors> {
	private String vendorName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Record createReportRecord(AgedDebtors recordReport) {
		Record record = new Record(recordReport);
		record.add("Name", recordReport.getName());
		record.add("Date", recordReport.getDate());
		record.add("Transansaction Type",
				ReportUtility.getTransactionName(recordReport.getType()));
		record.add("Number", recordReport.getNumber());
		record.add("Aging", recordReport.getAgeing());
		record.add("Total", recordReport.getTotal());
		return record;
	}

	@Override
	protected List<AgedDebtors> getRecords() {
		FinanceDate start = getStartDate();
		FinanceDate end = getEndDate();
		ArrayList<AgedDebtors> apAgingDetailsReport = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> apAgingDetailsReportForVendor = new ArrayList<AgedDebtors>();
		try {
			if (vendorName == null) {
				apAgingDetailsReport = new FinanceTool().getReportManager()
						.getAgedCreditors(start, end, getCompanyId());
			} else if (vendorName != null) {
				apAgingDetailsReport = new FinanceTool().getReportManager()
						.getAgedCreditors(getStartDate(), getEndDate(),
								getCompanyId());
				for (AgedDebtors agDebitor : apAgingDetailsReport) {
					if (vendorName.equals(agDebitor.getName())) {
						apAgingDetailsReportForVendor.add(agDebitor);
					}
				}
			}

			if (apAgingDetailsReportForVendor != null
					&& !apAgingDetailsReportForVendor.isEmpty()) {
				return apAgingDetailsReportForVendor;
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return apAgingDetailsReport;
	}

	@Override
	protected String addCommandOnRecordClick(AgedDebtors selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return null;
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(getMessages().apAgeingDetails());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			vendorName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().apAgeingDetails());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().apAgeingDetails());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().apAgeingDetails());
	}

}