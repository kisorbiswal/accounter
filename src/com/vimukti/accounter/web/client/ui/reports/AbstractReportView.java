package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ISorting;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * Subclasses must pass the Record type.
 * 
 * @author vimukti3
 * 
 * @param <I>
 * @param <R>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractReportView<R> extends ParentCanvas implements
		AsyncCallback<List<R>>, ISorting<R> {

	public static final int TOOLBAR_TYPE_DATE_RANGE = 1;
	public static final int TOOLBAR_TYPE_AS_OF = 2;
	public static final int TOOLBAR_TYPE_PRIOR_VATRETURN = 3;
	public static final int TOOLBAR_TYPE_DATE_RANGE_VATAGENCY = 4;
	public static final int TOOLBAR_TYPE_SALES_PURCAHASE = 5;
	public static final int TOOLBAR_TYPE_EXPENSE = 6;
	public static final int TOOLBAR_TYPE_CHECKDETAIl = 7;
	public static final int TOOLBAR_TYPE_CUSTOMER=8;
	public static final int TOP_MARGIN = 305;
	protected int sectionDepth = 0;
	private String[] columns;
	protected ReportToolbar toolbar;
	protected List<R> records;
	protected ISectionHandler handler;
	public boolean isVATDetailReport;
	public boolean isVATSummaryReport;

	protected List<Integer> columnstoHide = new ArrayList<Integer>();

	ReportGrid<R> grid;
	private String emptyMsg;

	public AbstractReportView() {
		emptyMsg = FinanceApplication.getVendorsMessages().norecordstoshow();
	}

	public AbstractReportView(boolean showGridFooter, String emptyMsg) {
		this.ishowGridFooter = showGridFooter;
		this.emptyMsg = emptyMsg;
	}

	public class Section {
		Section(String title, String footer, int[] sumColums) {
			this.title = title;
			this.footer = footer;
			this.sumColumnsIndexes = sumColums;
		}

		Section(String[] titles, String[] footers, int[] sumColums) {
			this.titles = titles;
			this.footers = footers;
			if (AbstractReportView.this instanceof BalanceSheetReport
					|| AbstractReportView.this instanceof ProfitAndLossReport) {
				this.title = ((titles[0] == "" || titles[0] == null) ? titles[1]
						: titles[0]);
				this.footer = ((footers[0] == "" || footers[0] == null) ? footers[1]
						: footers[0]);
			}
			this.sumColumnsIndexes = sumColums;
		}

		String title;
		String[] titles;
		String footer;
		String[] footers;
		int[] sumColumnsIndexes;
		Object[] data = new Object[columns.length];
		boolean isaddFooter = true;

		public void startSection() {
			if (handler != null) {
				handler.OnSectionAdd(this);
			}
			if (titles == null) {
				titles = new String[] { title };
			}
			if (isTitleEmpty())
				grid.addRow(null, 0, titles, true, false, false);
		}

		public void update(Object[] values) {
			for (int index : sumColumnsIndexes) {
				this.data[index] = sum((this.data[index] == ""
						|| this.data[index] == null ? 0.0 : this.data[index]),
						(values[index] == "" || values[index] == null ? 0.0
								: values[index]));
			}
		}

		private boolean isTitleEmpty() {
			for (String str : titles) {
				if (str == null)
					continue;
				str = str.trim();
				if (str.length() > 0)
					return true;
			}
			return false;
		}

		private double sum(Object a, Object b) {
			double da = a == null ? 0.0 : (Double) a;
			double db = b == null ? 0.0 : (Double) b;
			return da + db;
		}

		public void endSection() {
			updateFooterInData();

			if (handler != null) {
				handler.OnSectionEnd(this);
			}
			if (ishowGridFooter)
				grid.addFooter(data);

			if (isaddFooter)
				grid.addRow(null, 2, data, true, true, true);
		}

		private void updateFooterInData() {
			if (footers == null) {
				footers = new String[] { footer };
			}
			int j = 0;
			for (String ft : footers) {
				// if (ft != null & ft.equals("")) {
				data[j] = ft;
				j++;
				// }
			}
		}
	}

	public interface ReportGridRowHandler {
		public void OnRowClick(IsSerializable serializable);
	}

	private List<Section> sections = new ArrayList<Section>();
	private VerticalPanel mainLayout;
	private VerticalPanel printableLayout;
	private ScrollPanel tableLayout;
	private HTML reportTypeTitle;
	private HTML dateRange;

	protected boolean isShowTotal = true;
	private VerticalPanel topLayout;
	private boolean ishowGridFooter = true;
	public ClientFinanceDate startDate;
	// private DialogBox loadingDialog;
	public ClientFinanceDate endDate;
	private int fitHeight;

	public static final int COLUMN_TYPE_TEXT = 1;
	public static final int COLUMN_TYPE_AMOUNT = 2;
	public static final int COLUMN_TYPE_DATE = 3;
	public static final int COLUMN_TYPE_NUMBER = 4;
	public static final int COLUMN_TYPE_PERCENTAGE = 5;

	/**
	 * Called when there is a problem in the server side in returning the data
	 * for the report view.
	 */
	public void onFailure(Throwable caught) {
		ReportsActionFactory.getReportsHomeAction();
		grid.removeLoadingImage();
	}

	/**
	 * This method will be called when the RPC method returns. This is a async
	 * call back.
	 */
	public void onSuccess(List<R> result) {
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
			Accounter.showError(e.toString());
		}
	}

	protected void setFromAndToDate(List<R> result) {
		try {
			R obj = result.get(result.size() - 1);
			startDate = getStartDate(obj);
			endDate = getEndDate(obj);
			toolbar.setStartAndEndDates(startDate, endDate);
			setHeaderTitle();
			if (startDate != null || endDate != null) {
				result.remove(result.size() - 1);
			}
			this.records = result;
		} catch (Exception e) {
		}

	}

	private void initRecords(List<R> records) {
		grid.removeAllRows();
		this.records = records;

		for (R record : records) {
			processRecord(record);
			Object[] values = new Object[this.columns.length];
			for (int x = 0; x < this.columns.length; x++) {
				values[x] = getColumnData(record, x);
			}
			updateTotals(values);
			grid.addRow(record, 2, values, false, false, false);
		}
		endAllSections();
		sections.clear();
		endStatus();
		showRecords();
	}

	protected void setHeaderTitle() {
		String[] dynamiccolumns = getDynamicHeaders();
		if (dynamiccolumns != null) {
			for (int index = 0; index < dynamiccolumns.length; index++) {

				grid.setHeaderText(dynamiccolumns[index], index);
			}
		}
	}

	protected String[] getDynamicHeaders() {

		return null;
	}

	/**
	 * This method is to close all sections in reverse Order
	 */
	protected void endAllSections() {
		try {
			for (int i = this.sections.size() - 1; i >= 0; i--) {
				if (i == 1) {
					sections.get(0).isaddFooter = false;
					endSection();
				} else
					endSection();
			}
		} catch (Exception e) {
		}
	}

	private void endStatus() {
	}

	private void updateTotals(Object[] values) {
		for (Section sec : this.sections) {
			sec.update(values);
		}
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
			// styleTag.setId("report-style-Tag");
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

	/**
	 * In this method you need to call the RPC method by passing 'this' as
	 * callback.
	 */
	public abstract void makeReportRequest(ClientFinanceDate start,
			ClientFinanceDate end);

	public void makeReportRequest(String vatAgency, ClientFinanceDate endDate) {

	}

	public void makeReportRequest(String vatAgency,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

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

		String cmpyname = FinanceApplication.getCompany().getName();
		cmpyname = cmpyname != null && !cmpyname.isEmpty() ? cmpyname
				: FinanceApplication.getCompany().getTradingName();

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

		printableLayout = new VerticalPanel();
		printableLayout.setSize("100%", "100%");
		printableLayout.add(topLayout);

		this.tableLayout = new ScrollPanel() {
			@Override
			protected void onLoad() {
				super.onLoad();
				grid.setHeight(tableLayout.getOffsetHeight() - 25 + "px");
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

		String cmpyname = FinanceApplication.getCompany().getName();
		cmpyname = cmpyname != null && !cmpyname.isEmpty() ? cmpyname
				: FinanceApplication.getCompany().getTradingName();

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
	 * @SuppressWarnings("deprecation") private void updateDateRangeLayout(Date
	 * startDate, Date endDate) { this.dateRange.setHTML("<strong> Date Range: "
	 * + UIUtils.getDateStringByDate(startDate.toGMTString()) + " - " +
	 * UIUtils.getDateStringByDate(endDate.toGMTString()) + "</strong> "); }
	 */
	@Override
	public void initData() {
		grid.addLoadingImagePanel();
		Object data = getData();
		if (data != null) {
			String dateRange = null;
			dateRange = getPreviousReportDateRange(data);
			if (dateRange.equals(FinanceApplication.getReportsMessages()
					.custom())) {
				toolbar.setStartAndEndDates(getPreviousReportStartDate(data),
						getPreviousReportEndDate(data));
			}
			toolbar.setDefaultDateRange(dateRange);
			toolbar.setSelectedDateRange(dateRange);

		} else {
			toolbar.setDefaultDateRange(getDefaultDateRange());
			toolbar.setSelectedDateRange(getDefaultDateRange());
		}
		// makeReportRequest(toolbar.getStartDate(), toolbar.getEndDate());
	}

	protected String getPreviousReportDateRange(Object object) {
		return "";
	}

	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return null;
	}

	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return null;
	}

	private void createReportTable() {
		try {
			this.columns = getColunms();
			this.grid = new ReportGrid<R>(columns, ishowGridFooter) {
				@Override
				protected void onLoad() {
					if (UIUtils.isMSIEBrowser())
						grid.setHeight(tableLayout.getOffsetHeight() + "px");
					else
						grid.setHeight(tableLayout.getOffsetHeight() - 25
								+ "px");
					super.onLoad();
				}
			};
			this.grid.setReportView(this);
			this.grid.setColumnTypes(getColumnTypes());
			this.grid.init();
			// this.grid.setHeight((Window.getClientHeight() - TOP_MARGIN) +
			// "px");

			tableLayout.add(this.grid);
		} catch (Exception e) {
		}
	}

	private void createToolBar() {
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
			case TOOLBAR_TYPE_CHECKDETAIl:
				toolbar = new CheckDetailReportToolbar();
				break;
			case TOOLBAR_TYPE_CUSTOMER:
				toolbar=new CreateStatementToolBar(this);
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
			mainLayout.add(toolbar);
			toolbar.setView(this);
			toolbar.itemSelectionHandler = new ReportToolBarItemSelectionHandler() {

				public void onItemSelectionChanged(int type,
						ClientFinanceDate startDate, ClientFinanceDate endDate) {
					resetReport(startDate, endDate);
				}
			};
		} catch (Exception e) {
		}

	}

	public void resetReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.sectionDepth = 0;
		if (grid != null)
			grid.removeFromParent();
		// this.grid.removeAllRows();
		// if (tableLayout != null)
		// tableLayout.removeFromParent();

		createReportTable();
		resetVariables();
		// updateDateRangeLayout(startDate, endDate);
		grid.addLoadingImagePanel();
		if (UIUtils.isMSIEBrowser())
			fitToSize(fitHeight, 0);
		makeReportRequest(startDate, endDate);
	}

	/**
	 * Reset Variables in Report,
	 */
	public void resetVariables() {
		// implemented in subclass
	}

	/**
	 * Just show the status in the middle of the report view
	 * 
	 * @param string
	 */
	// private void setStatus(String status) {
	// loadingDialog = UIUtils.getLoadingMessageDialog(status
	// + ",please wait....");
	// loadingDialog.show();
	// }

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
	protected void addSection(String sectionTitle, String footerTitle,
			int[] sumColumns) {
		Section s = new Section(sectionTitle, footerTitle, sumColumns);
		s.startSection();
		sections.add(s);
		sectionDepth++;
	}

	protected void addSection(String[] sectionTitles, String[] footerTitles,
			int[] sumColumns) {
		Section s = new Section(sectionTitles, footerTitles, sumColumns);
		s.startSection();
		sections.add(s);
		sectionDepth++;
	}

	/**
	 * Report Title
	 * 
	 * @return
	 */
	@Override
	public abstract String getTitle();

	/**
	 * @return all column names in order
	 */
	public abstract String[] getColunms();

	/**
	 * 
	 * @return all column types in the same order as column names
	 */
	public abstract int[] getColumnTypes();

	/**
	 * Check and pre-process the report before it is added to the report view.
	 * You may want to check and create sections depending on the requirement of
	 * your report.
	 * 
	 * @param record
	 */
	public abstract void processRecord(R record);

	public abstract void OnRecordClick(R record);

	public boolean isWiderReport() {
		return false;
	}

	public abstract Object getColumnData(R record, int columnIndex);

	protected void endSection() {
		try {

			this.sectionDepth--;
			if (sectionDepth >= 0 && !sections.isEmpty()) {
				Section s = sections.remove(sectionDepth);
				s.endSection();
			}
		} catch (Exception e) {
			// do nothing
		}
	}

	public boolean isIshowGridFooter() {
		return ishowGridFooter;
	}

	public void setIshowGridFooter(boolean ishowGridFooter) {
		this.ishowGridFooter = ishowGridFooter;
	}

	protected void addEmptyMessage(String msg) {
		grid.addEmptyMessage(msg);
	}

	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().financialYearToDate();
	}

	protected int getColumnWidth(int index) {
		return -1;
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
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	public abstract ClientFinanceDate getStartDate(R obj);

	public abstract ClientFinanceDate getEndDate(R obj);

	@Override
	public void fitToSize(int height, int width) {

		if (UIUtils.isMSIEBrowser()) {
			if (height == 0) {
				return;
			}
			fitHeight = height;
			height = height - ViewManager.TOP_MENUBAR;
			this.setHeight(height + "px");
			mainLayout.setHeight(height - 5 + "px");
		}
	}

	public void refresh() {
		resetVariables();
		this.initRecords(records);
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
		return columnstoHide;
	}

	public void setColumnstoHide(List<Integer> columnstoHide) {
		this.columnstoHide = columnstoHide;
	}
}
