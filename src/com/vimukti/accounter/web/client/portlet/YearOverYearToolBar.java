package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.reports.PortletToolBar;

public abstract class YearOverYearToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	protected List<String> dateRangesList;
	protected AccountCombo accountCombo;
	private int chartType;
	protected long accountId;

	public YearOverYearToolBar(int chartType) {
		this.chartType = chartType;
		init();
	}

	protected void init() {
		createControls();
	}

	private void createControls() {
		dateRangesList = new ArrayList<String>();
		for (int i = 0; i < monthDateRangeArray.length; i++) {
			dateRangesList.add(monthDateRangeArray[i]);
		}
		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.addStyleName("date_range_combo");
		dateRangeItemCombo.initCombo(dateRangesList);
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						refreshPortletData(selectItem, accountId);
					}
				});

		accountCombo = new AccountCombo(messages.Account()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				if (chartType == YearOverYearPortlet.YEAR_OVER_YEAR_EXPENSE) {
					return Accounter.getCompany().getAccounts(
							ClientAccount.TYPE_EXPENSE);
				} else if (chartType == YearOverYearPortlet.YEAR_OVER_YEAR_INCOME) {
					return Accounter.getCompany().getAccounts(
							ClientAccount.TYPE_INCOME);
				}
				return null;
			}
		};
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						if (YearOverYearToolBar.this.getSelectedDateRange() == null) {
							YearOverYearToolBar.this
									.setSelectedDateRange(messages.thisMonth());
						}
						accountId = selectItem.getID();
						refreshPortletData(
								YearOverYearToolBar.this.getSelectedDateRange(),
								selectItem.getID());
					}
				});
		if (getSelectedItem().size() != 1 && getSelectedItem().get(1) != null) {
			accountCombo.setSelected(Accounter.getCompany()
					.getAccount(Long.parseLong(getSelectedItem().get(1)))
					.getName());
		}
		addItems(dateRangeItemCombo, accountCombo);
		setDefaultDateRange(getSelectedItem().get(0));
	}

	protected abstract void refreshPortletData(String selectItem, long accountId);

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	/**
	 * get default date range for date range combo
	 */
	protected abstract List<String> getSelectedItem();
}
