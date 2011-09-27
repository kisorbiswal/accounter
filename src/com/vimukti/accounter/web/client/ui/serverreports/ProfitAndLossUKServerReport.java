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
			addTypeSection("", getConstants().netProfit());
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
				if (section.title == getConstants().grossProfit()) {
					section.data[0] = "";
				}

				if (section.title.equals(getConstants().indirectCosts())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
				}
			}

			public void OnSectionEnd(Section<TrialBalance> section) {
				if (section.title == null)
					section.title = "";

				if (section.title.equals(getConstants().revenueIncome())) {
					totalincome = Double.valueOf(section.data[3].toString());
					totalincome2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(getConstants()
						.directProductsMaterialCosts())) {
					totalCGOS = Double.valueOf(section.data[3].toString());
					totalCGOS2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(getConstants().otherDirectCosts())) {
					otherExpense = Double.valueOf(section.data[3].toString());
					otherExpense2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(getConstants().indirectCosts())) {
					totalexpese = (Double) section.data[3];
					totalexpese2 = (Double) section.data[5];
				}

				if (section.title.equals(getConstants().otherIncome())) {
					otherIncome = Double.valueOf(section.data[3].toString());
					otherIncome2 = Double.valueOf(section.data[5].toString());
				}

				if (section.title.equals(getConstants().otherIncomeOrExpense())) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[3] = otherNetIncome;
					otherNetIncome2 = otherIncome2 - otherExpense2;
					section.data[5] = otherNetIncome2;
				}
				if (section.footer == null)
					section.footer = "";
				if (section.footer.equals(getConstants().grossProfit())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[3] = grosProft;
					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
					section.data[5] = grosProft2;
				}
				if (section.footer.equals(getConstants().netProfit())) {
					netProfit = grosProft - totalexpese;
					section.data[3] = netProfit;
					netProfit2 = grosProft2 - totalexpese2;
					section.data[5] = netProfit2;
				}
			}
		};

	}

	public void addOrdinaryIncomeOrExpenseTypes(TrialBalance record) {
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .ordinaryIncomeOrExpense())) {
		// closeAllSection();
		// addTypeSection(FinanceApplication.constants()
		// .ordinaryIncomeOrExpense(), FinanceApplication
		// .constants().netOrdinaryIncome());
		// }
		if (record.getAccountType() == ClientAccount.TYPE_INCOME
				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			if (!sectiontypes.contains(getConstants().grossProfit())) {
				addTypeSection(getConstants().grossProfit(), "", getConstants()
						.grossProfit());

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains(getConstants().revenueIncome())) {

					addTypeSection(getConstants().revenueIncome(),
							getConstants().revenueIncomeTotal());
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes.contains(getConstants()
						.directProductsMaterialCosts())) {
					closeOtherSections();
					closeSection(types.indexOf(getConstants().revenueIncome()));
					addTypeSection(
							getConstants().directProductsMaterialCosts(),
							getConstants()
									.directProductsAndMaterialCostsTotal());

				}

		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains(getConstants().otherDirectCosts())) {
				closeOtherSections();
				closeSection(types.indexOf(getConstants()
						.directProductsMaterialCosts()));
				addTypeSection(getConstants().otherDirectCosts(),
						getConstants().otherDirectCostsTotal());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains(getConstants().indirectCosts())) {
				closeAllSection();
				addTypeSection(getConstants().indirectCosts(), getConstants()
						.indirectCostsTotal());
			}
		}

	}

}
