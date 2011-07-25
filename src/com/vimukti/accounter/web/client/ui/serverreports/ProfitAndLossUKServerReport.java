package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class ProfitAndLossUKServerReport extends ProfitAndLossServerReport {

	public ProfitAndLossUKServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public void processRecord(TrialBalance record) {
		if (this.handler == null)
			initHandler();

		if (sectionDepth == 0) {
			addTypeSection("", "Net Profit");
		}
		addOrdinaryIncomeOrExpenseTypes(record);
		// if (record.getBaseType() ==
		// ClientAccount.BASETYPE_ORDINARY_INCOME_OR_EXPENSE) {
		// addOrdinaryIncomeOrExpenseTypes(record);
		// } else if (record.getBaseType() ==
		// ClientAccount.BASETYPE_OTHER_INCOME_OR_EXPENSE) {
		// addOtherIncomeOrExpenseTypes(record);
		// } else {
		// closeAllSection();
		// }

		if (closePrevSection(record.getParentAccount() == 0 ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	protected void initHandler() {

		initValues();
		this.handler = new ISectionHandler<TrialBalance>() {

			@Override
			public void OnSectionAdd(Section<TrialBalance> section) {
				if (section.title == "GrossProfit") {
					section.data[0] = "";
				}

				if (section.title.equals("Indirect Costs")) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
				}
			}

			public void OnSectionEnd(Section<TrialBalance> section) {
				if (section.title == null)
					section.title = "";

				if (section.title.equals("Revenue/Income")) {
					totalincome = Double.valueOf(section.data[3].toString());
					totalincome2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals("Direct Products And Material Costs")) {
					totalCGOS = Double.valueOf(section.data[3].toString());
					totalCGOS2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals("Other Direct Costs")) {
					otherExpense = Double.valueOf(section.data[3].toString());
					otherExpense2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals("Indirect Costs")) {
					totalexpese = (Double) section.data[3];
					totalexpese2 = (Double) section.data[5];
				}

				if (section.title.equals("Other Income")) {
					otherIncome = Double.valueOf(section.data[3].toString());
					otherIncome2 = Double.valueOf(section.data[5].toString());
				}

				if (section.title.equals("Other Income or Expense")) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[3] = otherNetIncome;
					otherNetIncome2 = otherIncome2 - otherExpense2;
					section.data[5] = otherNetIncome2;
				}
				if (section.footer == null)
					section.footer = "";
				if (section.footer.equals("GrossProfit")) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[3] = grosProft;
					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
					section.data[5] = grosProft2;
				}
				if (section.footer.equals("Net Profit")) {
					netIncome = grosProft - totalexpese;
					section.data[3] = netIncome;
					netIncome2 = grosProft2 - totalexpese2;
					section.data[5] = netIncome2;
				}
				// FIXME
				// if(!isServerSide())
				// reportView.updateTotals(values)
				// .getCellFormatter().addStyleName(0, 1,
				// "depth2");
				// if (section.footer == FinanceApplication.getReportsMessages()
				// .netIncome()) {
				// section.data[3] = netIncome + otherNetIncome;
				// }
			}
		};

	}

	public void addOrdinaryIncomeOrExpenseTypes(TrialBalance record) {
		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .ordinaryIncomeOrExpense())) {
		// closeAllSection();
		// addTypeSection(FinanceApplication.getReportsMessages()
		// .ordinaryIncomeOrExpense(), FinanceApplication
		// .getReportsMessages().netOrdinaryIncome());
		// }
		if (record.getAccountType() == ClientAccount.TYPE_INCOME
				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			if (!sectiontypes.contains("GrossProfit")) {
				addTypeSection("GrossProfit", "", "GrossProfit");

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains("Revenue/Income")) {

					addTypeSection("Revenue/Income", "Revenue/Income Total");
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes
						.contains("Direct Products And Material Costs")) {
					closeOtherSections();
					closeSection(types.indexOf("Revenue/Income"));
					addTypeSection("Direct Products And Material Costs",
							"Direct Products And Material Costs Total");

				}

		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains("Other Direct Costs")) {
				closeOtherSections();
				closeSection(types
						.indexOf("Direct Products And Material Costs"));
				addTypeSection("Other Direct Costs", "Other Direct Costs Total");
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains("Indirect Costs")) {
				closeAllSection();
				addTypeSection("Indirect Costs", "Indirect Costs Total");
			}
		}

	}

}
