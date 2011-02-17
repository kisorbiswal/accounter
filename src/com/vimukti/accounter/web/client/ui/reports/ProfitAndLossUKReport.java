package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public class ProfitAndLossUKReport extends ProfitAndLossReport {

	@Override
	public void processRecord(TrialBalance record) {
		if (this.handler == null)
			initHandler();

		if (sectionDepth == 0) {
			addTypeSection("", FinanceApplication.getReportsMessages()
					.netProfit());
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

		if (closePrevSection(record.getParentAccount() == null ? record
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
		this.handler = new ISectionHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionAdd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {
				if (section.title == FinanceApplication.getReportsMessages()
						.grossProfit()) {
					section.data[0] = "";
				}

				if (section.title.equals(FinanceApplication
						.getReportsMessages().indirectCosts())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionEnd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {
				if (section.title == null)
					section.title = "";

				if (section.title.equals(FinanceApplication
						.getReportsMessages().salesIncome())) {
					totalincome = Double.valueOf(section.data[3].toString());
					totalincome2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().directProductsMaterialCosts())) {
					totalCGOS = Double.valueOf(section.data[3].toString());
					totalCGOS2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherDirectCosts())) {
					otherExpense = Double.valueOf(section.data[3].toString());
					otherExpense2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().indirectCosts())) {
					totalexpese = (Double) section.data[3];
					totalexpese2 = (Double) section.data[5];
				}

				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherIncome())) {
					otherIncome = Double.valueOf(section.data[3].toString());
					otherIncome2 = Double.valueOf(section.data[5].toString());
				}

				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherIncomeOrExpense())) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[3] = otherNetIncome;
					otherNetIncome2 = otherIncome2 - otherExpense2;
					section.data[5] = otherNetIncome2;
				}
				if (section.footer == null)
					section.footer = "";
				if (section.footer.equals(FinanceApplication
						.getReportsMessages().grossProfit())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[3] = grosProft;
					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
					section.data[5] = grosProft2;
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages().netProfit())) {
					netIncome = grosProft - totalexpese;
					section.data[3] = netIncome;
					netIncome2 = grosProft2 - totalexpese2;
					section.data[5] = netIncome2;
				}
				grid.getFooterTable().getCellFormatter().addStyleName(0, 1,
						"depth2");
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
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.grossProfit())) {
				addTypeSection(FinanceApplication.getReportsMessages()
						.grossProfit(), "", FinanceApplication
						.getReportsMessages().grossProfit());

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains(FinanceApplication
						.getReportsMessages().salesIncome())) {

					addTypeSection(FinanceApplication.getReportsMessages()
							.salesIncome(), FinanceApplication
							.getReportsMessages().totalsalesIncome());
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes.contains(FinanceApplication
						.getReportsMessages().directProductsMaterialCosts())) {
					closeOtherSections();
					closeSection(types.indexOf(FinanceApplication
							.getReportsMessages().salesIncome()));
					addTypeSection(FinanceApplication.getReportsMessages()
							.directProductsMaterialCosts(), FinanceApplication
							.getReportsMessages().totalDPMC());

				}

		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.otherDirectCosts())) {
				closeOtherSections();
				closeSection(types.indexOf(FinanceApplication
						.getReportsMessages().directProductsMaterialCosts()));
				addTypeSection(FinanceApplication.getReportsMessages()
						.otherDirectCosts(), FinanceApplication
						.getReportsMessages().totalotherDirectCosts());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.indirectCosts())) {
				closeAllSection();
				addTypeSection(FinanceApplication.getReportsMessages()
						.indirectCosts(), FinanceApplication
						.getReportsMessages().totalindirectCosts());
			}
		}

	}

}
