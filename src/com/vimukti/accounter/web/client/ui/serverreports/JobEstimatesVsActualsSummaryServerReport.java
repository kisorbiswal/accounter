package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobEstimatesVsActualsSummary;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class JobEstimatesVsActualsSummaryServerReport extends
		AbstractFinaneReport<JobEstimatesVsActualsSummary> {
	private String sectionName = "";
	private String jobName = "";

	public JobEstimatesVsActualsSummaryServerReport(
			IFinanceReport<JobEstimatesVsActualsSummary> reportView) {
		this.reportView = reportView;
	}

	public JobEstimatesVsActualsSummaryServerReport(long startDate,
			long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { Global.get().customer(), getMessages().jobName(),
				"Est. Cost", "Act. Cost", "Difference", "Est. Revenue",
				"Act. Revenue", "Difference" };
	}

	@Override
	public String getTitle() {
		return "Job Estimates Vs Actuals Summary";
	}

	@Override
	public String[] getColunms() {
		return new String[] { Global.get().customer(), getMessages().jobName(),
				"Est. Cost", "Act. Cost", "Difference", "Est. Revenue",
				"Act. Revenue", "Difference" };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(JobEstimatesVsActualsSummary record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { "", "", "", "",
					getMessages().total() }, new int[] { 7 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getCustomerName();
			addSection(new String[] { sectionName }, new String[] { "", "",
					getMessages().reportTotal(sectionName) }, new int[] { 7 });

		} else if (sectionDepth == 2) {
			this.jobName = record.getJobName();
			addSection(new String[] { "", jobName }, new String[] { "", "", "",
					"", "", getMessages().reportTotal(jobName) },
					new int[] { 7 });
		} else if (sectionDepth == 3) {
			if (!jobName.equals(record.getJobName())) {
				endSection();
			}
			if (!sectionName.equals(record.getCustomerName())) {
				if (!jobName.equals(record.getJobName())) {
					endSection();
				} else {
					endSection();
					endSection();
				}
			}
			if (jobName.equals(record.getJobName())
					&& sectionName.equals(record.getCustomerName())) {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(JobEstimatesVsActualsSummary record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getCustomerName();
		case 1:
			return record.getJobName();
		case 2:
			return record.getEstimatedCost();
		case 3:
			return record.getActualCost();
		case 4:
			return record.getEstimatedCost() - record.getActualCost();
		case 5:
			return record.getEstimatedRevenue();
		case 6:
			return record.getActualRevenue();
		case 7:
			return record.getEstimatedRevenue() - record.getActualRevenue();
		default:
			break;
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(JobEstimatesVsActualsSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(JobEstimatesVsActualsSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

}
