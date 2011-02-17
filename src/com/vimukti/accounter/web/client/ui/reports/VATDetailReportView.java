package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

/**
 * @author Murali.A
 * 
 */
public class VATDetailReportView extends AbstractReportView<VATDetail> {

	String sectionTitle = "";
	private String currentsectionName = "";
	private double accountBalance = 0.0D;

	public VATDetailReportView() {
		super(false, FinanceApplication.getReportsMessages().noRecordsToShow());
		isVATDetailReport = true;

	}

	@Override
	public void init() {
		super.init();
	}

	// @SuppressWarnings("deprecation")
	// @Override
	// public void initData() {
	// // Make rpc request for default VAT Agency and default DateRange
	// List<ClientVATAgency> vatAgencies = FinanceApplication.getCompany()
	// .getVatAgencies();
	// for (ClientVATAgency vatAgency : vatAgencies) {
	// if (vatAgency.getName().equalsIgnoreCase(
	// "HM Customs & Excise - VAT")) {
	// ClientFinanceDate date = new ClientFinanceDate();
	// int month = (date.getMonth()) % 3;
	// int startMonth = date.getMonth() - month;
	// ClientFinanceDate startDate = new ClientFinanceDate(date.getYear(),
	// startMonth, 1);
	// @SuppressWarnings("unused")
	// String start = UIUtils.dateToString(startDate);
	// String end = UIUtils.dateToString(date);
	// makeReportRequest(vatAgency.getStringID(), end);
	// break;
	// }
	// }
	// super.initData();
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#OnRecordClick
	 * (java.lang.Object)
	 */
	@Override
	public void OnRecordClick(VATDetail record) {
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getTransactionId());
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getColumnData
	 * (java.lang.Object, int)
	 */
	@Override
	public Object getColumnData(VATDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getTransactionName();

		case 1:
			return UIUtils.getDateByCompanyType(record.getTransactionDate());
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
			if (!currentsectionName.equals(record.getBoxName())) {
				currentsectionName = record.getBoxName();
				accountBalance = 0.0D;
			}
			return accountBalance += record.getTotal();

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
		if (toolbar.isToolBarComponentChanged) {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
					COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		} else {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
					COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		}
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getColunms
	 * ()
	 */
	@Override
	public String[] getColunms() {
		if (toolbar.isToolBarComponentChanged) {
			return new String[] {
					FinanceApplication.getReportsMessages().type(),
					FinanceApplication.getReportsMessages().date(),
					FinanceApplication.getReportsMessages().number(),
					FinanceApplication.getReportsMessages().name(),
					FinanceApplication.getReportsMessages().vatRate(),
					FinanceApplication.getReportsMessages().netAmount(),
					FinanceApplication.getReportsMessages().amount() };

		} else {
			return new String[] {
					FinanceApplication.getReportsMessages().type(),
					FinanceApplication.getReportsMessages().date(),
					FinanceApplication.getReportsMessages().number(),
					FinanceApplication.getReportsMessages().name(),
					FinanceApplication.getReportsMessages().vatRate(),
					FinanceApplication.getReportsMessages().netAmount(),
					FinanceApplication.getReportsMessages().amount(),
					FinanceApplication.getReportsMessages().balance() };
		}
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getTitle()
	 */
	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().vatDetail();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#getToolbarType
	 * ()
	 */
	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	protected int getColumnWidth(int index) {
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

	/*
	 * @seecom.vimukti.accounter.web.client.ui.reports.AbstractReportView#
	 * makeReportRequest(java.util.ClientFinanceDate,
	 * java.util.ClientFinanceDate)
	 */
	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService()
				.getPriorVATReturnVATDetailReport(start.getTime(),
						end.getTime(), this);
	}

	// @Override
	// public void makeReportRequest(String vatAgency, String end) {
	// sectionDepth = 0;
	// FinanceApplication.createReportService().getPriorVATReturnReport(
	// vatAgency, end, this);
	//
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.AbstractReportView#processRecord
	 * (java.lang.Object)
	 */
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
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();

		gridhtml = gridhtml.replaceAll(headerhtml, "");

		headerhtml = headerhtml.replaceAll("td", "th");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
				headerhtml.indexOf("</tbody>"));

		String firsRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		headerhtml = headerhtml + firsRow;
		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
				+ this.toolbar.getStartDate()
				+ " - "
				+ this.toolbar.getEndDate() + "</strong></div>";

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(VATDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATDetail obj) {
		return obj.getStartDate();
	}

	@Override
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
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionTitle = "";
		this.currentsectionName = "";
		this.accountBalance = 0.0;
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

}
