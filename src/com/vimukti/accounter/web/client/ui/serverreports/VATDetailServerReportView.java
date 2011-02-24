package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * @author Murali.A
 * 
 */
public class VATDetailServerReportView extends AbstractFinaneReport<VATDetail> {

	String sectionTitle = "";

	public VATDetailServerReportView(IFinanceReport<VATDetail> reportView) {
		isVATDetailReport = true;
		this.reportView = reportView;
	}

	public VATDetailServerReportView(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public ClientFinanceDate getEndDate(VATDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATDetail obj) {
		return obj.getStartDate();
	}

	public int sort(VATDetail obj1, VATDetail obj2, int col) {
		int ret = obj1.getBoxName().compareTo(obj2.getBoxName());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getTransactionName().compareTo(
					obj2.getTransactionName());
		case 1:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 2:
			return UIUtils.compareInt(Integer.parseInt(obj1
					.getTransactionNumber()), Integer.parseInt(obj2
					.getTransactionNumber()));
		case 3:
			return obj1.getPayeeName().compareTo(obj2.getPayeeName());
		case 4:
			return UIUtils.compareDouble(obj1.getVatRate(), obj2.getVatRate());
		case 5:
			return UIUtils.compareDouble(obj1.getNetAmount(), obj2
					.getNetAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 230;
		case 1:
			return 85;
		case 2:
			return 65;
		case 3:
			return 140;
		case 4:
			return 75;

		default:
			return 100;
		}
	}

	@Override
	public Object getColumnData(VATDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getTransactionName();

		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getPayeeName() != null ? record.getPayeeName() : "";
		case 4:
			return record.isPercentage() ? record.getVatRate() + "%" : record
					.getVatRate();
		case 5:
			return record.getNetAmount();
		case 6:
			return record.getTotal();
		case 7:
		}
		return null;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getColumnTypes
	 * ()
	 */
	@Override
	public int[] getColumnTypes() {
		// if (toolbar.isToolBarComponentChanged) {
		// return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
		// COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
		// COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		// } else {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		// }
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getColunms
	 * ()
	 */
	@Override
	public String[] getColunms() {
		return new String[] { "Type", "Date", "No.", "VAT Rate", "Net Amount",
				"Amount", "Balance" };
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getTitle()
	 */
	@Override
	public String getTitle() {
		return "VAT Detail";
	}

	@Override
	public void processRecord(VATDetail record) {
		if (sectionDepth == 0) {
			sectionTitle = record.getBoxName();
			addSection(sectionTitle, sectionTitle, new int[] { 6 });
		} else if (!sectionTitle.equals(record.getBoxName())) {
			endSection();
			sectionTitle = record.getBoxName();
			addSection(sectionTitle, sectionTitle, new int[] { 6 });
		} else {
			return;
		}

		processRecord(record);

	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "Type", "Date", "No.", "VAT Rate", "Net Amount",
				"Amount", "Balance" };
	}

}
