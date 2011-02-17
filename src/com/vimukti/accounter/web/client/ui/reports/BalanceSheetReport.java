package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class BalanceSheetReport extends AbstractReportView<TrialBalance> {

	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;
	private double currentAssetsTotal;
	private double currentLiabilityTotal;
	private double netCurrentAssetsTotal;
	private double fixedAssetsTotal;
	private double longTermLiabilityTotal;
	private double otherNominalAccounts;
	private double netAssetsTotal;
	private double dividendAmount;

	public BalanceSheetReport() {
		super(false, "");
		this.columnstoHide.add(3);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (record.getAccountId() != null) {
			UIUtils.runAction(record, ReportsActionFactory
					.getTransactionDetailByAccountAction());
		} else {
			UIUtils.runAction(record, ReportsActionFactory
					.getProfitAndLossAction());
		}

	}

	@Override
	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().financialYearToDate();
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAccountNumber();
		case 1:
			return record.getAccountName();
		case 2:
			return record.getAmount();
		case 3:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				FinanceApplication.getReportsMessages().categoryNumber(),
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()),
				"" };
	}

	@Override
	protected String[] getDynamicHeaders() {
		return new String[] {
				FinanceApplication.getReportsMessages().categoryNumber(),
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()),
				"" };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().balanceSheet();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getBalanceSheetReport(
				start.getTime(), end.getTime(), this);
		initValues();
	}

	@Override
	public void processRecord(TrialBalance record) {

		if (this.handler == null)
			iniHandler();
		if (sectionDepth == 0) {
			addTypeSection(" ");
		}
		addAssetTypes(record);
		// if (record.getBaseType() == ClientAccount.BASETYPE_ASSET) {
		// addAssetTypes(record);
		// } else if (record.getBaseType() == ClientAccount.BASETYPE_LIABILITY)
		// {
		// addLiablityTypes(record);
		// } else if (record.getBaseType() == ClientAccount.BASETYPE_EQUITY) {
		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .equity())) {
		// closeOtherSections();
		// for (int i : new int[] { types.size() - 1, types.size() - 2 }) {
		// closeSection(i);
		// }
		// if (!sectiontypes.contains("Liabilities and Equity")) {
		// addTypeSection("Liabilities and Equity");
		// }
		// addTypeSection(FinanceApplication.getReportsMessages().equity());
		//
		// }
		// }
		if (closePrevSection(record.getParentAccount() == null ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	public String getAccountNameById(String id) {
		for (TrialBalance balance : this.records)
			if (balance.getAccountId().equals(id))
				return balance.getAccountName();
		return null;
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	/**
	 * closes all Accounts sections
	 */
	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public void addAssetTypes(TrialBalance record) {
		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .asset())) {
		// closeAllSection();
		// addTypeSection("Asset");
		// }

		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.netAssets())) {
			addTypeSection(FinanceApplication.getReportsMessages().netAssets(),
					"", FinanceApplication.getReportsMessages().netAssets());
		}

		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.netCurrentAssets())) {
			addTypeSection(FinanceApplication.getReportsMessages()
					.netCurrentAssets(), "", FinanceApplication
					.getReportsMessages().netCurrentAssets());
		}
		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.currentAssets())) {
				addTypeSection(FinanceApplication.getReportsMessages()
						.currentAssets());
			}
			// if (record.getGroupType() == ClientAccount.GROUPTYPE_CASH)
			// if (!sectiontypes.contains(FinanceApplication
			// .getReportsMessages().cash())) {
			// closeOtherSections();
			// addTypeSection(FinanceApplication.getReportsMessages()
			// .cash());
			// }
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_LIABILITY) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.currentLiability())) {

				closePrevSection(FinanceApplication.getReportsMessages()
						.currentAssets());
				// closeSection(types.indexOf(FinanceApplication
				// .getReportsMessages().currentAssets()));
				addTypeSection(FinanceApplication.getReportsMessages()
						.currentLiability());
			}
		}

		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .netCurrentAssets())) {
		// addTypeSection(FinanceApplication.getReportsMessages()
		// .netCurrentAssets(), "", FinanceApplication
		// .getReportsMessages().netCurrentAssets());
		// }

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_FIXED_ASSET) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.fixedAssets())) {
				// closeAllSection();
				closePrevSection(FinanceApplication.getReportsMessages()
						.currentLiability());
				closePrevSection(FinanceApplication.getReportsMessages()
						.netCurrentAssets());
				// closePrevSection(FinanceApplication.getReportsMessages()
				// .currentAssets());
				// closePrevSection()
				// if (!sectiontypes.contains(FinanceApplication
				// .getReportsMessages().netAssets())) {
				// addTypeSection(FinanceApplication.getReportsMessages()
				// .netAssets(), "", FinanceApplication
				// .getReportsMessages().netAssets());
				// }
				// for (int i : new int[] { types.size() - 1, types.size() - 2
				// }) {
				// closeSection(i);
				// }
				addTypeSection(FinanceApplication.getReportsMessages()
						.fixedAssets());
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.longTermLiability())) {
				if (sectiontypes.contains(FinanceApplication
						.getReportsMessages().fixedAssets()))
					closePrevSection(FinanceApplication.getReportsMessages()
							.fixedAssets());
				else {
					closePrevSection(FinanceApplication.getReportsMessages()
							.currentLiability());
					closePrevSection(FinanceApplication.getReportsMessages()
							.netCurrentAssets());
				}
				addTypeSection(FinanceApplication.getReportsMessages()
						.longTermLiability());
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.otherNominalAccounts())) {
				if (sectiontypes.contains(FinanceApplication
						.getReportsMessages().longTermLiability())) {
					closePrevSection(FinanceApplication.getReportsMessages()
							.longTermLiability());
				} else if (sectiontypes.contains(FinanceApplication
						.getReportsMessages().fixedAssets())) {
					closePrevSection(FinanceApplication.getReportsMessages()
							.fixedAssets());
				} else {
					closePrevSection(FinanceApplication.getReportsMessages()
							.currentLiability());
					closePrevSection(FinanceApplication.getReportsMessages()
							.netCurrentAssets());
				}

				addTypeSection(FinanceApplication.getReportsMessages()
						.otherNominalAccounts());
			}
		}

		if (record.getBaseType() == ClientAccount.BASETYPE_EQUITY) {
			if (record.getAccountNumber() != null
					&& record.getAccountNumber().equals("3150"))
				dividendAmount = record.getAmount();
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.capitalAndReserves())) {
				closeAllSection();
				addTypeSection(FinanceApplication.getReportsMessages()
						.capitalAndReserves(), FinanceApplication
						.getReportsMessages().shareHolderFunds());
			}
		}

	}

	// public void addAssetTypes(TrialBalance record) {
	//
	// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
	// .netAssets())) {
	// addTypeSection(FinanceApplication.getReportsMessages().netAssets(),
	// "", FinanceApplication.getReportsMessages().netAssets());
	// }
	//
	// }

	public void addLiablityTypes(TrialBalance record) {

		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.liabilities())) {
			if (!sectiontypes.contains("Liabilities and Equity")) {
				closeOtherSections();
				closeAllSection();
				addTypeSection("Liabilities and Equity");
			}
			addTypeSection(FinanceApplication.getReportsMessages()
					.liabilities());
		}
		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_LIABILITY) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.currentLiability())) {
				addTypeSection(FinanceApplication.getReportsMessages()
						.currentLiability());
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.longTermLiability())) {
				closeOtherSections();
				closeSection(types.indexOf(FinanceApplication
						.getReportsMessages().currentLiability()));
				addTypeSection(FinanceApplication.getReportsMessages()
						.longTermLiability());
			}
		}

	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != null)
				if (balance.getParentAccount().equals(record.getAccountId()))
					return true;
		}
		return false;
	}

	public boolean addSection(TrialBalance record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			addSection(new String[] { "", record.getAccountName() },
					new String[] {
							"",
							record.getAccountName()
									+ " "
									+ FinanceApplication.getReportsMessages()
											.total() }, new int[] { 3 });
			return true;
		}
		return false;
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "") {
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
		} else {
			if (sectiontypes.contains(title)) {
				types.remove(types.size() - 1);
				if (types.size() > 0) {
					curentParent = types.get(types.size() - 1);
				}
				endSection();
				return true;
			}
		}
		// if (sectiontypes.contains(title)) {
		// types.remove(title);
		// sectiontypes.remove(title);
		// endSection();
		// return true;
		//
		// }
		return false;
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { "", title },
					new String[] {
							"",
							title
									+ " "
									+ FinanceApplication.getReportsMessages()
											.total() }, new int[] { 3 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { "", title }, new String[] { "",
					bottomTitle }, new int[] { 3 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void addTypeSection(String sectionType, String title,
			String bottomTitle) {
		if (!sectiontypes.contains(sectionType)) {
			addSection(new String[] { "", title, }, new String[] { "",
					bottomTitle }, new int[] { 3 });
			types.add(title);
			sectiontypes.add(sectionType);
		}
	}

	public boolean isExist(String title) {
		if (!sectiontypes.contains(title))
			return true;
		return false;
	}

	/**
	 * Reset Variables in Report,
	 */
	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
		curentParent = "";
	}

	private void iniHandler() {

		initValues();
		this.handler = new ISectionHandler() {

			private double shareHoldersFund;

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionAdd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {

				if (section.title.equals(FinanceApplication
						.getReportsMessages().capitalAndReserves())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
				}

			}

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionEnd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {
				// prevent null pointer exception with title value
				if (section.title == null)
					section.title = "";
				if (section.title.equals(FinanceApplication
						.getReportsMessages().currentAssets())) {
					currentAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().currentLiability())) {
					currentLiabilityTotal = Double.valueOf(section.data[3]
							.toString());
				}

				if (section.title.equals(FinanceApplication
						.getReportsMessages().fixedAssets())) {
					fixedAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().longTermLiability())) {
					longTermLiabilityTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherNominalAccounts())) {
					otherNominalAccounts = Double.valueOf(section.data[3]
							.toString());
				}
				// prevent null pointer exceptions with footer value
				if (section.footer == null)
					section.footer = "";

				if (section.footer.equals(FinanceApplication
						.getReportsMessages().netAssets())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
					netAssetsTotal = netCurrentAssetsTotal + fixedAssetsTotal
							- longTermLiabilityTotal + otherNominalAccounts;
					section.data[3] = netAssetsTotal;
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages().shareHolderFunds())) {
					shareHoldersFund = Double.valueOf(section.data[3]
							.toString())
							- 2 * dividendAmount;
					section.data[3] = shareHoldersFund;
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages().netCurrentAssets())) {
					netCurrentAssetsTotal = currentAssetsTotal
							- currentLiabilityTotal;
					section.data[3] = netCurrentAssetsTotal;
				}
			}

		};
	}

	protected void initValues() {
		currentAssetsTotal = 0.0D;
		currentLiabilityTotal = 0.0D;
		netCurrentAssetsTotal = 0.0D;
		fixedAssetsTotal = 0.0D;
		longTermLiabilityTotal = 0.0D;
		otherNominalAccounts = 0.0D;
		netAssetsTotal = 0.0D;

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

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");

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

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(TrialBalance obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((TrialBalance) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((TrialBalance) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((TrialBalance) object).getEndDate();
	}

	@Override
	protected int getColumnWidth(int index) {

		switch (index) {
		case 0:
			return 110;
		case 2:
			return 130;
		case 3:
			return 110;

		default:
			break;
		}
		// if (index == 1)
		// return 310;
		// else if (index == 2)
		// return 270;
		// else
		return 200;
	}
}
