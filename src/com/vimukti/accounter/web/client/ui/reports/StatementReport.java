package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;

public class StatementReport extends AbstractReportView<PayeeStatementsList> {
	private String sectionName = "";
	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;
	public int precategory = 1001;
	public String customerId;

//	@Override
//	public int[] getColumnTypes() {
//		return new int[] { COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT,
//				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT };
//	}

	@Override
	public String[] getColunms() {
		return new String[] { FinanceApplication.getReportsMessages().date(),
				FinanceApplication.getReportsMessages().type(),
				FinanceApplication.getReportsMessages().num(),
				FinanceApplication.getReportsMessages().ageing(),
				FinanceApplication.getReportsMessages().amount()

		};
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().customerStatement();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_CUSTOMER;
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 160;
		else if (index == 0)
			return 70;
		else if (index == 2)
			return 70;
		else if (index == 3)
			return 100;
		else if (index == 4)
			return 150;
		else
			return -1;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void makeReportRequest(String customer, ClientFinanceDate endDate) {
		resetReport(endDate, endDate);
		FinanceApplication.createHomeService().getStatements(customer,
				new ClientFinanceDate().getTime(),
				new ClientFinanceDate().getTime(),
				new ClientFinanceDate().getTime(), 0, false, false, 0.00,
				false, false, this);
		customerId = customer;
	}

	@Override
	public void OnRecordClick(PayeeStatementsList record) {

	}

	@Override
	public Object getColumnData(PayeeStatementsList record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return UIUtils.getDateByCompanyType(record.getTransactionDate());
		case 1:
			return Utility.getTransactionName(record.getTransactiontype());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getAgeing();
		case 4:
			return record.getTotal();
		}
		return null;
	}

	@Override
	public void processRecord(PayeeStatementsList record) {
		if (precategory == 1001 || precategory == record.getCategory()) {
			if (addCategoryTypes(record)) {
				return;
			}
		} else if (precategory != 1001 && record.getCategory() != precategory) {
			precategory = record.getCategory();
			endSection();
		} else {
			return;
		}
		processRecord(record);
	}

	private boolean addCategoryTypes(PayeeStatementsList record) {

		if (record.getCategory() == 1) {
			precategory = record.getCategory();
			return addOneTothirty(record);
		} else if (record.getCategory() == 2) {
			precategory = record.getCategory();
			return addThirtyToSixty(record);
		} else if (record.getCategory() == 3) {
			precategory = record.getCategory();
			return addSixtyTo90(record);
		} else if (record.getCategory() == 4) {
			precategory = record.getCategory();
			return addGreaterThan90(record);
		}
		return true;
	}

	private boolean addOneTothirty(PayeeStatementsList record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.days30())) {
			addTypeSection(FinanceApplication.getReportsMessages().days30(), "");
			return false;
		}
		return true;
	}

	private boolean addThirtyToSixty(PayeeStatementsList record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.days60())) {
			addTypeSection(FinanceApplication.getReportsMessages().days60(), "");
			return false;
		}
		return true;

	}

	private boolean addSixtyTo90(PayeeStatementsList record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.days90())) {
			addTypeSection(FinanceApplication.getReportsMessages().days90(), "");
			return false;
		}
		return true;

	}

	private boolean addGreaterThan90(PayeeStatementsList record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.older())) {
			addTypeSection(FinanceApplication.getReportsMessages().older(), "");
			return false;
		}
		return true;

	}

	private boolean addTotalBalance(PayeeStatementsList record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.totalBalance())) {
			addTypeSection(FinanceApplication.getReportsMessages()
					.totalBalance(), "");
			return false;
		}
		return true;
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { title }, new String[] { "", "", "",
					FinanceApplication.getReportsMessages().total() },
					new int[] { 4 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "")
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
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

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();
	}

	private void printDataForOtherBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(grid.getFooter(), "");

		headerhtml = headerhtml.replaceAll("td", "th");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
				headerhtml.indexOf("</tbody>"));

		String firsRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = null;

		UIUtils.generateStatementPDF(this.getTitle(), gridhtml, dateRangeHtml,
				customerId);
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerHtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");
		footerHtml = footerHtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerHtml, "");

		headerhtml = headerhtml.replaceAll("TD", "TH");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
				headerhtml.indexOf("</TBODY>"));

		String firsRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		firsRow = firsRow.replaceAll("\r\n", "");
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

		String dateRangeHtml = null;

		UIUtils.generateStatementPDF(this.getTitle(), gridhtml, dateRangeHtml,
				customerId);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(PayeeStatementsList obj) {
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(PayeeStatementsList obj) {
		return null;
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public PayeeStatementsList getObject(PayeeStatementsList parent,
			PayeeStatementsList child) {
		// TODO Auto-generated method stub
		return super.getObject(parent, child);
	}

	@Override
	public int sort(PayeeStatementsList obj1, PayeeStatementsList obj2, int col) {
		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getTransactionNumber(), obj2
						.getTransactionNumber());

		case 1:
			return UIUtils.compareInt(obj1.getTransactiontype(), obj2
					.getTransactiontype());

		case 3:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 0:
			return UIUtils.compareTo(obj1.getTransactionDate(), obj2
					.getTransactionDate());

		case 4:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());

		}
		return 0;
	}

	@Override
	public void resetVariables() {
//		sectionDepth = 0;
		sectionName = "";
		precategory = 1001;
		types.clear();
		sectiontypes.clear();
		curentParent = "";
		super.resetVariables();
	}

	@Override
	public String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().all();
	}
}
