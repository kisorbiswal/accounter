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
	}

	@Override
	public String getId() {
		return null;
	}

	protected Record createReportRecord(AgedDebtors recordReport) {
		Record record = new Record(recordReport);
		record.add(getMessages().name(), recordReport.getName());
		record.add(getMessages().date(), recordReport.getDate());
		record.add(getMessages().transactionType(),
				ReportUtility.getTransactionName(recordReport.getType()));
		record.add(getMessages().number(), recordReport.getNumber());
		record.add(getMessages().ageing(), recordReport.getAgeing());
		record.add(getMessages().total(), recordReport.getTotal());
		return record;
	}

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