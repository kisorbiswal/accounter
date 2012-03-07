package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractView;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.AccountRegisterOtherListGrid;

public class AccountRegisterOthersView extends AbstractView<AccountRegister> {
	AccountRegisterOtherListGrid grid;

	private String selectedDateRange;
	private String selectedOption;
	private SelectCombo showTransactionSelect;

	private final ClientAccount takenaccount;

	private ClientAccount account;
	protected List<AccountRegister> accountRegister;
	private AccountRegister accRegister;
	private StyledPanel mainVLay;
	private StyledPanel hlayTop;
	private Label lab1;

	private ClientFinanceDate startDate, todaydate;
	private Label totalLabel;
	private double total = 0.0;

	private final int TOP = 120;
	private final int FOOTER = 25;
	private final int BORDER = 20;

	String[] dateRangeArray = { messages.all(), messages.today(),
			messages.last30Days(), messages.last45Days() };
	private List<String> listOfDateRanges;

	private ClientFinanceDate endDate;

	public AccountRegisterOthersView(ClientAccount account2) {
		super();
		this.takenaccount = account2;

		selectedDateRange = messages.today();
		selectedOption = messages.all();
	}

	protected void createControls() {

		showTransactionSelect = new SelectCombo(messages.showTransactions());
		listOfDateRanges = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			listOfDateRanges.add(dateRangeArray[i]);
		}
		// if (UIUtils.isMSIEBrowser())
		// showTransactionSelect.setWidth("120px");
		showTransactionSelect.initCombo(listOfDateRanges);
		showTransactionSelect.setComboItem(dateRangeArray[0]);
		showTransactionSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!showTransactionSelect.getSelectedValue().equals(
								messages.custom()))
							dateRangeChanged();

					}
				});
		DynamicForm form = new DynamicForm("form");
		form.add(showTransactionSelect);
		hlayTop = new StyledPanel("hlayTop");
		hlayTop.add(form);
		// hlayTop.setCellHorizontalAlignment(form, ALIGN_RIGHT);

		lab1 = new Label(messages.accountRegister() + " - "
				+ takenaccount.getName());
		lab1.setStyleName("label-title");
		StyledPanel lableHpanel = new StyledPanel("lableHpanel");
		lableHpanel.add(lab1);
		lableHpanel.add(hlayTop);

		grid = new AccountRegisterOtherListGrid(false);
		// grid.addStyleName("listgrid-tl");
		grid.init();
		grid.setView(this);
		StyledPanel gridLayout = new StyledPanel("gridLayout");
		// gridLayout.setWidth("100%");
		// gridLayout.setHeight("100%");
		gridLayout.add(grid);

		totalLabel = new Label();
		totalLabel.setText(messages.totalEndingBalance()
				+ DataUtils.getAmountAsStringInPrimaryCurrency(total));
		mainVLay = new StyledPanel("mainVLay");
		mainVLay.setHeight("100%");
		mainVLay.setWidth("100%");
		mainVLay.add(lableHpanel);
		AccounterDOM.setParentElementHeight(lableHpanel.getElement(), 4);
		mainVLay.add(gridLayout);
		int pageSize = getPageSize();
		if (pageSize != -1) {
			grid.addRangeChangeHandler2(new Handler() {

				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onPageChange(event.getNewRange().getStart(), event
							.getNewRange().getLength());
				}
			});
			SimplePager pager = new SimplePager(TextLocation.CENTER,
					(Resources) GWT.create(Resources.class), false,
					pageSize * 2, true);
			pager.setDisplay(grid);
			updateRecordsCount(0, 0, 0);
			mainVLay.add(pager);
		}

		add(mainVLay);

		setSize("100%", "100%");

	}

	private int getPageSize() {
		return 25;
	}

	private void updateRecordsCount(int start, int length, int total) {
		grid.updateRange(new Range(start, getPageSize()));
		grid.setRowCount(total, (start + length) == total);

	}

	private void onPageChange(int start, int length) {
		accountSelected(takenaccount, start, length);
	}

	protected void dateRangeChanged() {
		todaydate = new ClientFinanceDate();
		selectedOption = showTransactionSelect.getSelectedValue();
		if (!selectedDateRange.equals(messages.all())
				&& selectedOption.equals(messages.all())) {
			startDate = new ClientFinanceDate(0);
			endDate = new ClientFinanceDate(0);
			selectedDateRange = messages.all();

		} else if (!selectedDateRange.equals(messages.today())
				&& selectedOption.equals(messages.today())) {
			startDate = todaydate;
			endDate = todaydate;
			selectedDateRange = messages.today();

		} else if (!selectedDateRange.equals(messages.last30Days())
				&& selectedOption.equals(messages.last30Days())) {
			selectedDateRange = messages.last30Days();
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 1, todaydate.getDay());
			endDate = todaydate;

		} else if (!selectedDateRange.equals(messages.last45Days())
				&& selectedOption.equals(messages.last45Days())) {

			selectedDateRange = messages.last45Days();
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 2, todaydate.getDay() + 16);
			endDate = todaydate;
		}
		onPageChange(0, getPageSize());

	}

	protected void accountSelected(final ClientAccount takenaccount, int start,
			int length) {

		if (takenaccount == null) {
			accountRegister = null;
			return;
		}

		this.account = takenaccount;
		grid.setAccount(takenaccount);
		Accounter.createReportService().getAccountRegister(startDate, endDate,
				takenaccount.getID(), start, length,
				new AccounterAsyncCallback<PaginationList<AccountRegister>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedtoGetListofAccounts(takenaccount
										.getName()));

					}

					@Override
					public void onResultSuccess(
							PaginationList<AccountRegister> result) {
						grid.removeAllRecords();
						if (result.isEmpty()) {
							updateRecordsCount(result.getStart(),
									grid.getTableRowCount(),
									result.getTotalCount());
							grid.addEmptyMessage(messages.noRecordsToShow());
							return;
						}
						grid.sort(10, false);
						grid.setRecords(result);
						Window.scrollTo(0, 0);
						updateRecordsCount(result.getStart(),
								grid.getTableRowCount(), result.getTotalCount());

					}

				});

	}

	@Override
	public void init() {
		createControls();
	}

	@Override
	public void initData() {
		dateRangeChanged();
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.showTransactionSelect.setFocus();
	}

	@Override
	public void fitToSize(int height, int width) {

		if (grid.isShowFooter())
			grid.setHeight(height - TOP - FOOTER + "px");
		else
			grid.setHeight(height - TOP + "px");

		grid.setWidth(width - BORDER + "px");

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

	public long getCurrency() {
		return takenaccount.getCurrency();
	}
}
