package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class BalanceSheetServerReport extends
		AbstractFinaneReport<TrialBalance> {

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
	
	

	public BalanceSheetServerReport(long startDate, long endDate,int generationType) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
	}

	public BalanceSheetServerReport(IFinanceReport<TrialBalance> reportView) {
		this.columnstoHide.add(3);
		this.reportView = reportView;
	}

	@Override
	public void initData() {
		this.makeReportRequest(startDate.getTime(), endDate.getTime());
	}

	@Override
	public String getDefaultDateRange() {
		return "Financial Year To Date";
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
				"Category Number",
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()), "" };
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				"Category Number",
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()), "" };
	}

	@Override
	public String getTitle() {
		return "Balance Sheet";
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		//
		// initValues();
		// try {
		// onSuccess(this.financeTool.getBalanceSheetReport(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }

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

		if (!sectiontypes.contains("Net Assets")) {
			addTypeSection("Net Assets", "", "Net Assets");
		}

		if (!sectiontypes.contains("Net Current Assets")) {
			addTypeSection("Net Current Assets", "", "Net Current Assets");
		}
		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET) {
			if (!sectiontypes.contains("Current Assets")) {
				addTypeSection("Current Assets");
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
			if (!sectiontypes.contains("Current Liabilities")) {

				closePrevSection("Current Assets");
				// closeSection(types.indexOf(FinanceApplication
				// .getReportsMessages().currentAssets()));
				addTypeSection("Current Liabilities");
			}
		}

		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .netCurrentAssets())) {
		// addTypeSection(FinanceApplication.getReportsMessages()
		// .netCurrentAssets(), "", FinanceApplication
		// .getReportsMessages().netCurrentAssets());
		// }

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_FIXED_ASSET) {
			if (!sectiontypes.contains("Fixed Assets")) {
				// closeAllSection();
				closePrevSection("Current Liabilities");
				closePrevSection("Net Current Assets");
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
				addTypeSection("Fixed Assets");
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY) {
			if (!sectiontypes.contains("Long Term  Liabilities")) {
				if (sectiontypes.contains("Fixed Assets"))
					closePrevSection("Fixed Assets");
				else {
					closePrevSection("Current Liabilities");
					closePrevSection("Net Current Assets");
				}
				addTypeSection("Long Term  Liabilities");
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
			if (!sectiontypes.contains("Other Nominal Finance Categories")) {
				if (sectiontypes.contains("Long Term  Liabilities")) {
					closePrevSection("Long Term  Liabilities");
				} else if (sectiontypes.contains("Fixed Assets")) {
					closePrevSection("Fixed Assets");
				} else {
					closePrevSection("Current Liabilities");
					closePrevSection("Net Current Assets");
				}

				addTypeSection("Other Nominal Finance Categories");
			}
		}

		if (record.getBaseType() == ClientAccount.BASETYPE_EQUITY) {
			if (record.getAccountNumber() != null
					&& record.getAccountNumber().equals("3150"))
				dividendAmount = record.getAmount();
			if (!sectiontypes.contains("Capital And Reserves")) {
				closeAllSection();
				addTypeSection("Capital And Reserves", "Shareholder Funds");
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

		if (!sectiontypes.contains("Liabilities")) {
			if (!sectiontypes.contains("Liabilities and Equity")) {
				closeOtherSections();
				closeAllSection();
				addTypeSection("Liabilities and Equity");
			}
			addTypeSection("Liabilities");
		}
		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_LIABILITY) {
			if (!sectiontypes.contains("Current Liabilities")) {
				addTypeSection("Current Liabilities");
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY) {
			if (!sectiontypes.contains("Long Term  Liabilities")) {
				closeOtherSections();
				closeSection(types.indexOf("Current Liabilities"));
				addTypeSection("Long Term  Liabilities");
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
			addSection(
					new String[] { "", record.getAccountName() },
					new String[] { "", record.getAccountName() + " " + "Total" },
					new int[] { 3 });
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
			addSection(new String[] { "", title }, new String[] { "",
					title + " " + "Total" }, new int[] { 3 });
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
		initValues();
	}

	private void iniHandler() {

		initValues();
		ISectionHandler<TrialBalance> sectionHandler = new ISectionHandler<TrialBalance>() {

			private double shareHoldersFund;

			@Override
			public void OnSectionAdd(Section<TrialBalance> section) {

				if (section.title.equals("Capital And Reserves")) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
				}
			}

			@Override
			public void OnSectionEnd(Section<TrialBalance> section) {
				// prevent null pointer exception with title value
				if (section.title == null)
					section.title = "";
				if (section.title.equals("Current Assets")) {
					currentAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals("Current Liabilities")) {
					currentLiabilityTotal = Double.valueOf(section.data[3]
							.toString());
				}

				if (section.title.equals("Fixed Assets")) {
					fixedAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals("Long Term  Liabilities")) {
					longTermLiabilityTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals("Other Nominal Finance Categories")) {
					otherNominalAccounts = Double.valueOf(section.data[3]
							.toString());
				}
				// prevent null pointer exceptions with footer value
				if (section.footer == null)
					section.footer = "";

				if (section.footer.equals("Net Assets")) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
					netAssetsTotal = netCurrentAssetsTotal + fixedAssetsTotal
							- longTermLiabilityTotal + otherNominalAccounts;
					section.data[3] = netAssetsTotal;
				}
				if (section.footer.equals("Shareholder Funds")) {
					shareHoldersFund = Double.valueOf(section.data[3]
							.toString())
							- 2 * dividendAmount;
					section.data[3] = shareHoldersFund;
				}
				if (section.footer.equals("Net Current Assets")) {
					netCurrentAssetsTotal = currentAssetsTotal
							- currentLiabilityTotal;
					section.data[3] = netCurrentAssetsTotal;
				}
			}

		};
		setHandler(sectionHandler);

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
	public int getColumnWidth(int index) {

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
