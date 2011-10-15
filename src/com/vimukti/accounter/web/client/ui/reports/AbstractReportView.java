package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ISorting;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AbstractView;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;

/**
 * Subclasses must pass the Record type.
 * 
 * @author vimukti3
 * 
 * @param <I>
 * @param <R>
 */

public abstract class AbstractReportView<R> extends AbstractView<List<R>>
		implements ISorting<R>, IFinanceReport<R>, AsyncCallback<ArrayList<R>>,
		IPrintableView {
	public final static int REPORT_TYPE_GENERAL_LEDGER_REPORT = 162;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT = 115;
	public static final int TOOLBAR_TYPE_DATE_RANGE = 1;
	public static final int TOOLBAR_TYPE_AS_OF = 2;
	public static final int TOOLBAR_TYPE_PRIOR_VATRETURN = 3;
	public static final int TOOLBAR_TYPE_DATE_RANGE_VATAGENCY = 4;
	public static final int TOOLBAR_TYPE_SALES_PURCAHASE = 5;
	public static final int TOOLBAR_TYPE_EXPENSE = 6;
	public static final int TOOLBAR_TYPE_CHECKDETAIl = 7;
	public static final int TOOLBAR_TYPE_CUSTOMER = 8;
	public static final int TOOLBAR_TYPE_BUDGET1 = 9;
	public static final int TOOLBAR_TYPE_BUDGET2 = 10;
	public static final int TOOLBAR_TYPE_BUDGET3 = 11;
	public static final int TOOLBAR_TYPE_BUDGET4 = 12;
	public static final int TOP_MARGIN = 305;

	protected ReportToolbar toolbar;

	public boolean isVATDetailReport;
	public boolean isVATSummaryReport;

	protected IFinanceReport<R> serverReport = null;

	ReportGrid<R> grid;
	private String emptyMsg;

	public interface ReportGridRowHandler {
		public void OnRowClick(IsSerializable serializable);
	}

	private VerticalPanel mainLayout;
	private VerticalPanel printableLayout;
	private ScrollPanel tableLayout;

	private HTML reportTypeTitle;

	private HTML dateRange;

	protected boolean isShowTotal = true;

	public ClientFinanceDate startDate;
	public ClientFinanceDate endDate;

	private VerticalPanel topLayout;

	private int fitHeight;

	public AbstractReportView() {
		emptyMsg = Accounter.constants().noRecordsToShow();
	}

	public AbstractReportView(boolean showGridFooter, String emptyMsg) {
		this.emptyMsg = emptyMsg;
	}

	/**
	 * Called when there is a problem in the server side in returning the data
	 * for the report view.
	 */
	@Override
	public void onFailure(Throwable exception) {
		if (exception instanceof AccounterException) {
			ActionFactory.getReportsHomeAction();
			grid.removeLoadingImage();
			return;
		}
		Accounter.showMessage(Global.get().constants().sessionExpired());
	}

	/**
	 * This method will be called when the RPC method returns. This is a async
	 * call back.
	 */
	public void onSuccess(ArrayList<R> result) {
		try {
			if (result != null && result.size() > 1) {
				grid.removeAllRows();

				setFromAndToDate(result);

				initRecords(result);

			} else {
				grid.removeAllRows();

				if (result != null && result.size() == 1)
					setFromAndToDate(result);

				endStatus();

				addEmptyMessage(emptyMsg);
			}

			showRecords();
		} catch (Exception e) {
			endStatus();
			System.err.println(e.toString());
			Accounter.showError(e.toString());
		}
	}

	public void setFromAndToDate(List<R> result) {
		try {
			R obj = result.get(result.size() - 1);

			startDate = this.serverReport.getStartDate(obj);
			endDate = this.serverReport.getEndDate(obj);

			toolbar.setStartAndEndDates(startDate, endDate);

			setHeaderTitle();

			if (startDate != null || endDate != null) {
				result.remove(result.size() - 1);
			}

			this.serverReport.setRecords(result);
		} catch (Exception e) {
		}

	}

	protected void setHeaderTitle() {
		String[] dynamiccolumns = getDynamicHeaders();
		if (dynamiccolumns != null) {
			for (int index = 0; index < dynamiccolumns.length; index++) {
				grid.setHeaderText(dynamiccolumns[index], index);
			}
		}
	}

	/**
	 * This method is to close all sections in reverse Order
	 */
	@Override
	public void endAllSections() {
		this.serverReport.endAllSections();
	}

	// public String getDateByCompanyType(ClientFinanceDate date) {
	// return this.serverReport.getDateByCompanyType(date);
	// }

	private void endStatus() {
	}

	@Override
	public void updateTotals(Object[] values) {
		this.serverReport.updateTotals(values);
	}

	/**
	 * In this method we will just make the GUI control that holds all data
	 * visible
	 */
	public void showRecords() {
		try {
			// Element styleTag = DOM.getElementById("report-style-Tag");
			// if (styleTag == null) {
			// styleTag = DOM.createElement("Style");
			// styleTag.setID("report-style-Tag");
			// RootPanel.getBodyElement().appendChild(styleTag);
			// }
			if (UIUtils.isMSIEBrowser()) {
				if (isWiderReport()) {
					this.tableLayout.setStyleName("tablelayoutInIE");
				} else {
					this.tableLayout.setStyleName("tablelayoutInIE1");
				}
			} else {
				if (isWiderReport()) {
					this.tableLayout.setStyleName("tablelayout");
				} else {
					this.tableLayout.setStyleName("tablelayout1");
				}
			}
			// int child = canAdd ? 2 : 1;
			//
			// if (isWiderReport()) {
			// styleTag.setInnerText(".depth :nth-child(" + child
			// + ") {padding-left: " + 2
			// + "%!important;} .tablelayout{padding-left: " + 4
			// + "%!important;padding-right: " + 4
			// + "%!important;width:" + 92
			// + "%!important} .ReportGrid tr td{font-size:" + 9
			// + "pt !important;}");
			// } else {
			//
			// styleTag.setInnerText(".depth :nth-child(" + child
			// + ") {padding-left: " + 2
			// + "%!important;} .tablelayout{padding-left: " + 12
			// + "%!important;padding-right: " + 12
			// + "%!important;width:" + 75
			// + "%!important} .ReportGrid tr td{font-size:" + 9
			// + "pt !important;}");
			//
			// }

			// this.grid.setHTML(this.grid.getRowCount(), this.columns.length -
			// 1,styleTag.ge);
			AccounterDOM.addStyleToparent(this.grid.getElement(),
					"reportGridParent");
		} catch (Exception e) {
		}
	}

	@Override
	public void init() {

		if (UIUtils.isMSIEBrowser())
			createControlsForIE();
		else
			createControls();

		reportTypeTitle.setHTML("<strong>" + getTitle() + "</strong>");

	}

	private void createControls() {
		mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		createToolBar();
		toolbar.setSize("100%", "10%");

		topLayout = new VerticalPanel();
		topLayout.setSize("100%", "10%");

		String cmpyname = getCompany().getName();
		cmpyname = cmpyname != null && !cmpyname.isEmpty() ? cmpyname
				: getCompany().getTradingName();

		HTML companyLabel = new HTML("<strong>" + cmpyname + "</strong");
		HTML title = new HTML("<strong>" + "<h3>" + this.getAction().getText()
				+ "</h3>" + "</strong");
		title.getElement().getStyle().setMarginLeft(10, Unit.PX);
		// topLayout.add(companyLabel);
		reportTypeTitle = new HTML();
		topLayout.add(reportTypeTitle);
		/*
		 * dateRange = new HTML(); updateDateRangeLayout(toolbar.getStartDate(),
		 * toolbar.getEndDate());
		 * 
		 * topLayout.add(dateRange);
		 */
		Label currentDate = new Label();
		topLayout.add(currentDate);

		printableLayout = new VerticalPanel();
		// printableLayout.setSize("100%", "100%");
		printableLayout.setWidth("100%");
		printableLayout.add(topLayout);
		printableLayout.add(title);
		this.tableLayout = new ScrollPanel() {
			@Override
			protected void onLoad() {
				super.onLoad();
				// grid.setHeight("450px");
			}
		};
		this.tableLayout.addStyleName("tableLayout");
		this.tableLayout.setSize("100%", "100%");
		this.printableLayout.addStyleName("printableLayout");
		printableLayout.add(tableLayout);
		mainLayout.add(printableLayout);

		createReportTable();

		AccounterDOM.setParentElementHeight(toolbar.getElement(), 5);
		AccounterDOM.setParentElementHeight(topLayout.getElement(), 7);

		add(mainLayout);
		setSize("100%", "100%");

	}

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	private void createControlsForIE() {
		mainLayout = new VerticalPanel() {
			@Override
			public void setHeight(String height) {
				long hgt = Long.parseLong(height.replace("%", "").replace("px",
						""));
				super.setHeight(height);
				printableLayout.setHeight(hgt - 38 + "px");

			}

		};
		mainLayout.setWidth("100%");
		createToolBar();

		topLayout = new VerticalPanel();
		topLayout.setSize("100%", "10%");

		String cmpyname = getCompany().getName();
		cmpyname = cmpyname != null && !cmpyname.isEmpty() ? cmpyname
				: getCompany().getTradingName();

		HTML companyLabel = new HTML("<strong>" + cmpyname + "</strong");

		// topLayout.add(companyLabel);
		reportTypeTitle = new HTML();
		topLayout.add(reportTypeTitle);
		/*
		 * dateRange = new HTML(); updateDateRangeLayout(toolbar.getStartDate(),
		 * toolbar.getEndDate());
		 * 
		 * topLayout.add(dateRange);
		 */
		Label currentDate = new Label();
		topLayout.add(currentDate);

		printableLayout = new VerticalPanel() {
			@Override
			public void setHeight(String height) {
				long hgt = Long.parseLong(height.replace("%", "").replace("px",
						""));
				super.setHeight(height);
				tableLayout.setHeight(hgt - 10 + "px");

			}

		};
		;
		printableLayout.add(topLayout);

		this.tableLayout = new ScrollPanel() {
			@Override
			public void setHeight(String height) {
				long hgt = Long.parseLong(height.replace("%", "").replace("px",
						""));
				super.setHeight(height);
				grid.setHeight(hgt - 25 + "px");
			}

		};
		printableLayout.setWidth("100%");
		createReportTable();
		this.tableLayout.addStyleName("tableLayout");
		// this.tableLayout.setSize("100%", "100%");
		this.printableLayout.addStyleName("printableLayout");
		printableLayout.add(tableLayout);
		mainLayout.add(printableLayout);

		// AccounterDOM.setParentElementHeight(toolbar.getElement(), 5);
		// AccounterDOM.setParentElementHeight(topLayout.getElement(), 7);

		add(mainLayout);
		setSize("100%", "100%");
	}

	/*
	 * private void updateDateRangeLayout(Date startDate, Date endDate) {
	 * this.dateRange.setHTML("<strong> Date Range: " +
	 * UIUtils.getDateStringByDate(startDate.toGMTString()) + " - " +
	 * UIUtils.getDateStringByDate(endDate.toGMTString()) + "</strong> "); }
	 */
	@Override
	public void initData() {
		grid.addLoadingImagePanel();
		Object data = getData();
		if (data != null) {
			String dateRange = null;
			dateRange = getPreviousReportDateRange(data);
			if (dateRange.equals(Accounter.constants().custom())) {
				toolbar.setStartAndEndDates(getPreviousReportStartDate(data),
						getPreviousReportEndDate(data));
			}
			toolbar.setDefaultDateRange(dateRange);
			toolbar.setSelectedDateRange(dateRange);

		} else {
			toolbar.setDefaultDateRange(getDefaultDateRange());
			toolbar.setSelectedDateRange(getDefaultDateRange());
			this.serverReport.setStartAndEndDates(toolbar.getStartDate(),
					toolbar.getEndDate());

		}
		// makeReportRequest(toolbar.getStartDate(), toolbar.getEndDate());
	}

	private void createReportTable() {
		try {
			this.grid = new ReportGrid<R>(getColunms(),
					this.serverReport.isIshowGridFooter()) {
				@Override
				protected void onLoad() {
					if (UIUtils.isMSIEBrowser())
						grid.setHeight(tableLayout.getOffsetHeight() + "px");
					else
						// grid.setHeight(tableLayout.getOffsetHeight() - 20
						// + "px");
						super.onLoad();
				}
			};
			this.grid.setReportView(this);
			this.grid.setColumnTypes(getColumnTypes());
			this.grid.init();
			// grid.setHeight("450px");
			// this.grid.setHeight((Window.getClientHeight() - TOP_MARGIN) +
			// "px");

			tableLayout.add(this.grid);
		} catch (Exception e) {
		}
	}

	public void createToolBar() {
		try {
			int type = getToolbarType();
			switch (type) {
			case TOOLBAR_TYPE_DATE_RANGE:
				toolbar = new DateRangeReportToolbar();
				break;
			case TOOLBAR_TYPE_PRIOR_VATRETURN:
				toolbar = new PriorVATReturnToolBar();
				if (isVATDetailReport || isVATSummaryReport)
					((PriorVATReturnToolBar) toolbar).setDateRangeHide(true);
				break;
			case TOOLBAR_TYPE_DATE_RANGE_VATAGENCY:
				toolbar = new DateRangeVATAgencyToolbar();
				break;
			case TOOLBAR_TYPE_SALES_PURCAHASE:
				toolbar = new SalesPurchasesReportToolbar();
				break;
			case TOOLBAR_TYPE_EXPENSE:
				toolbar = new ExpenseReportToolbar();
				break;
			case TOOLBAR_TYPE_BUDGET1:
				toolbar = new BudgetReportToolbar(1);
				break;
			case TOOLBAR_TYPE_BUDGET2:
				toolbar = new BudgetReportToolbar(2);
				break;
			case TOOLBAR_TYPE_BUDGET3:
				toolbar = new BudgetReportToolbar(3);
				break;
			case TOOLBAR_TYPE_BUDGET4:
				toolbar = new BudgetReportToolbar(4);
				break;
			case TOOLBAR_TYPE_CUSTOMER:
				toolbar = new CreateStatementToolBar(this);
				break;
			default:
				toolbar = new AsOfReportToolbar();

				break;
			}
			toolbar.setStyleName("report-toolbar");
			if (this instanceof ARAgingDetailReport
					|| this instanceof APAgingDetailReport
					|| this instanceof ARAgingSummaryReport
					|| this instanceof APAgingSummaryReport) {
				toolbar.setVisible(false);
			}
			add(toolbar);
			toolbar.setView(this);
			toolbar.itemSelectionHandler = new ReportToolBarItemSelectionHandler() {

				public void onItemSelectionChanged(int type,
						ClientFinanceDate startDate, ClientFinanceDate endDate) {
					if (data != null && data instanceof BaseReport) {
						((BaseReport) data).setStartDate(startDate);
						((BaseReport) data).setEndDate(endDate);
					}
					resetReport(startDate, endDate);
				}
			};
		} catch (Exception e) {
		}

	}

	public void resetReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (grid != null)
			grid.removeFromParent();

		createReportTable();
		resetVariables();

		grid.addLoadingImagePanel();
		// if (UIUtils.isMSIEBrowser())
		fitToSize(fitHeight, 0);
		this.serverReport.setStartAndEndDates(toolbar.getStartDate(),
				toolbar.getEndDate());
		setHeaderTitle();
		makeReportRequest(startDate, endDate);
	}

	public String[] getDynamicHeaders() {

		return this.serverReport.getDynamicHeaders();
	}

	public void initRecords(List<R> records) {
		this.serverReport.initRecords(records);
	}

	protected String getPreviousReportDateRange(Object object) {
		if (object instanceof BaseReport) {
			return ((BaseReport) object).getDateRange();
		} else {
			return "";
		}
	}

	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return null;
	}

	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return null;
	}

	/**
	 * In this method you need to call the RPC method by passing 'this' as
	 * callback.
	 */
	public abstract void makeReportRequest(ClientFinanceDate start,
			ClientFinanceDate end);

	public void makeReportRequest(long vatAgency, ClientFinanceDate endDate) {

	}

	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate, int value) {

	}

	/**
	 * This method is used for Sales and Purchase order reports by sending the
	 * status.This method is override in the SalesOpenOrderReport and
	 * PurchaseOpenOrderReport
	 */
	public void makeReportRequest(int status, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	/**
	 * Must return one of the TOOLBAR_TYPE_* available in this class.
	 * 
	 * @return
	 */
	public abstract int getToolbarType();

	public abstract void OnRecordClick(R record);

	/**
	 * Reset Variables in Report,
	 */
	public void resetVariables() {
		this.serverReport.resetVariables();
	}

	/**
	 * This method creates a section
	 * 
	 * @param sectionTitle
	 *            section title
	 * @param footerTitle
	 *            footerTitle if available, null otherwise.
	 * @param sumColumns
	 *            indexes of the columns that needs to totalled
	 */
	@Override
	public void addSection(String sectionTitle, String footerTitle,
			int[] sumColumns) {
		this.serverReport.addSection(sectionTitle, footerTitle, sumColumns);
	}

	@Override
	public void addSection(String[] sectionTitles, String[] footerTitles,
			int[] sumColumns) {
		this.serverReport.addSection(sectionTitles, footerTitles, sumColumns);
	}

	public boolean isWiderReport() {
		return this.serverReport.isWiderReport();
	}

	@Override
	public void endSection() {
		this.serverReport.endSection();
	}

	public boolean isIshowGridFooter() {
		return this.serverReport.isIshowGridFooter();
	}

	protected void addEmptyMessage(String msg) {
		grid.addEmptyMessage(msg);
	}

	@Override
	public String getDefaultDateRange() {
		return this.serverReport.getDefaultDateRange();
	}

	@Override
	public int getColumnWidth(int index) {
		return this.serverReport.getColumnWidth(index);
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {

	}

	protected void removeLoadingImage() {
		grid.removeLoadingImage();
		grid.addEmptyMessage(this.emptyMsg);
	}

	@Override
	public void fitToSize(int height, int width) {

	}

	public void refresh() {
		resetVariables();
		this.initRecords(this.serverReport.getRecords());
	}

	@Override
	public void addFooter(Object[] values) {
		grid.addFooter(values);
	}

	@Override
	public void addRow(R record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		grid.addRow(record, depth, values, bold, underline, border);
	}

	@Override
	public R getObject(R parent, R child) {
		return null;
	}

	@Override
	public int sort(R obj1, R obj2, int col) {
		return 0;
	}

	public List<Integer> getColumnstoHide() {
		return this.serverReport.getColumnstoHide();
	}

	@Override
	public boolean isServerSide() {
		return false;
	}

	public ISectionHandler getSectionHanlder() {
		return this.serverReport.getSectionHanlder();
	}

	@Override
	public Object getColumnData(R record, int columnIndex) {
		return this.serverReport.getColumnData(record, columnIndex);
	}

	@Override
	public int[] getColumnTypes() {
		return this.serverReport.getColumnTypes();
	}

	@Override
	public String[] getColunms() {
		return this.serverReport.getColunms();
	}

	@Override
	public ClientFinanceDate getEndDate(R obj) {
		return this.serverReport.getEndDate(obj);
	}

	@Override
	public ClientFinanceDate getStartDate(R obj) {
		return this.serverReport.getStartDate(obj);
	}

	public void processRecord(R record) {
		this.serverReport.processRecord(record);
	}

	@Override
	public List<R> getRecords() {
		return this.serverReport.getRecords();
	}

	@Override
	public void setColumnstoHide(List<Integer> columnstoHide) {
		this.serverReport.setColumnstoHide(columnstoHide);
	}

	@Override
	public void setHandler(ISectionHandler<R> handler) {
		this.serverReport.setHandler(handler);
	}

	@Override
	public void setIshowGridFooter(boolean ishowGridFooter) {
		this.serverReport.setIshowGridFooter(ishowGridFooter);
	}

	@Override
	public void setRecords(List<R> records) {
		this.serverReport.setRecords(records);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	public void exportToCsv() {
	}

	@Override
	public void removeAllRows() {
		this.grid.removeAllRows();
	}

	@Override
	public ClientFinanceDate getCurrentFiscalYearStartDate() {
		return getCompany().getCurrentFiscalYearStartDate();
	}

	@Override
	public ClientFinanceDate getCurrentFiscalYearEndDate() {
		return getCompany().getCurrentFiscalYearEndDate();
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public boolean canPrint() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeEmptyStyle() {
		this.grid.removeEmptyStyle();
	}
}
