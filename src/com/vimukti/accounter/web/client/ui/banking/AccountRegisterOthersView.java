package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.AbstractView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.AccountRegisterOtherListGrid;

public class AccountRegisterOthersView extends AbstractView<AccountRegister> {
	AccountRegisterOtherListGrid grid;

	private String selectedDateRange;
	private String selectedOption;
	private SelectCombo showTransactionSelect;

	private ClientAccount takenaccount;

	private ClientAccount account;
	protected List<AccountRegister> accountRegister;
	private AccountRegister accRegister;
	private VerticalPanel mainVLay;
	private HorizontalPanel hlayTop;
	private Label lab1;

	private ClientFinanceDate startDate, todaydate;
	private Label totalLabel;
	private double total = 0.0;

	private final int TOP = 120;
	private final int FOOTER = 25;
	private final int BORDER = 20;

	String[] dateRangeArray = { Accounter.constants().all(),
			Accounter.constants().today(), Accounter.constants().last30Days(),
			Accounter.constants().last45Days() };
	private List<String> listOfDateRanges;

	private ClientFinanceDate endDate;

	public AccountRegisterOthersView(ClientAccount account2) {
		super();
		this.takenaccount = account2;

		selectedDateRange = Accounter.constants().today();
		selectedOption = Accounter.constants().all();
	}

	protected void createControls() {

		showTransactionSelect = new SelectCombo(Accounter.constants()
				.showTransactions());
		listOfDateRanges = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			listOfDateRanges.add(dateRangeArray[i]);
		}
		if (UIUtils.isMSIEBrowser())
			showTransactionSelect.setWidth("120px");
		showTransactionSelect.initCombo(listOfDateRanges);
		showTransactionSelect.setComboItem(dateRangeArray[0]);
		showTransactionSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!showTransactionSelect.getSelectedValue().equals(
								Accounter.constants().custom()))
							dateRangeChanged();

					}
				});
		DynamicForm form = new DynamicForm();
		form.setFields(showTransactionSelect);
		hlayTop = new HorizontalPanel();
		hlayTop.setWidth("100%");
		hlayTop.add(form);
		hlayTop.setCellHorizontalAlignment(form, ALIGN_RIGHT);

		lab1 = new Label(Accounter.messages().accountRegister(
				Global.get().Account())
				+ " - " + takenaccount.getName());
		lab1.setStyleName(Accounter.constants().labelTitle());
		HorizontalPanel lableHpanel = new HorizontalPanel();
		lableHpanel.setWidth("100%");
		lableHpanel.add(lab1);
		lableHpanel.add(hlayTop);

		grid = new AccountRegisterOtherListGrid(false);
		// grid.addStyleName("listgrid-tl");
		grid.init();
		HorizontalPanel gridLayout = new HorizontalPanel() {
			@Override
			protected void onAttach() {

				grid.setHeight(this.getOffsetHeight() - FOOTER + "px");

				super.onAttach();
			}
		};
		gridLayout.setWidth("100%");
		gridLayout.setHeight("100%");

		gridLayout.add(grid);

		totalLabel = new Label();
		totalLabel.setText(Accounter.constants().totalEndingBalance()
				+ DataUtils.getAmountAsString(total));
		mainVLay = new VerticalPanel();
		mainVLay.setHeight("100%");
		mainVLay.setWidth("100%");
		mainVLay.add(lableHpanel);
		AccounterDOM.setParentElementHeight(lableHpanel.getElement(), 4);
		mainVLay.add(gridLayout);

		add(mainVLay);

		setSize("100%", "100%");

	}

	protected void dateRangeChanged() {
		todaydate = new ClientFinanceDate();
		selectedOption = showTransactionSelect.getSelectedValue();
		if (!selectedDateRange.equals(Accounter.constants().all())
				&& selectedOption.equals(Accounter.constants().all())) {
			startDate = Accounter.getStartDate();
			endDate = Accounter.getCompany()
					.getLastandOpenedFiscalYearEndDate();
			if (endDate == null)
				endDate = new ClientFinanceDate();
			selectedDateRange = Accounter.constants().all();

		} else if (!selectedDateRange.equals(Accounter.constants().today())
				&& selectedOption.equals(Accounter.constants().today())) {
			startDate = todaydate;
			endDate = todaydate;
			selectedDateRange = Accounter.constants().today();

		} else if (!selectedDateRange
				.equals(Accounter.constants().last30Days())
				&& selectedOption.equals(Accounter.constants().last30Days())) {
			selectedDateRange = Accounter.constants().last30Days();
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 1, todaydate.getDay());
			endDate = todaydate;

		} else if (!selectedDateRange
				.equals(Accounter.constants().last45Days())
				&& selectedOption.equals(Accounter.constants().last45Days())) {

			selectedDateRange = Accounter.constants().last45Days();
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 2, todaydate.getDay() + 16);
			endDate = todaydate;
		}
		accountSelected(takenaccount);

	}

	public void getAccountRegisterGrid(List<AccountRegister> result) {

		grid.removeAllRecords();
		grid.removeLoadingImage();
		grid.balance = 0.0;
		grid.totalBalance = 0.0;
		if (accountRegister != null) {
			for (int i = 0; i < accountRegister.size(); i++) {
				accRegister = this.accountRegister.get(i);
				grid.addData(accRegister);
				// this.total += accRegister.getBalance();
			}
			if (accountRegister.size() == 0) {
				grid.addEmptyMessage(Accounter.constants().noRecordsToShow());
			}
		} else {
			grid.addEmptyMessage(Accounter.constants().noRecordsToShow());
		}
		// grid.updateFooterValues(FinanceApplication.constants()
		// .endingbalance(), 6);
		// grid.addFooterValue(DataUtils.getAmountAsString(takenaccount
		// .getCurrentBalance()), 7);
		this.total = 0;
	}

	protected void accountSelected(final ClientAccount takenaccount) {

		if (takenaccount == null) {
			accountRegister = null;
			return;
		}

		this.account = takenaccount;

		Accounter.createReportService().getAccountRegister(startDate, endDate,
				takenaccount.getID(),
				new AccounterAsyncCallback<ArrayList<AccountRegister>>() {

					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.messages()
								.failedtoGetListofAccounts(
										Global.get().account())
								+ takenaccount.getName());

					}

					public void onResultSuccess(
							ArrayList<AccountRegister> result) {
						accountRegister = result;
						getAccountRegisterGrid(result);

					}

				});
		grid.removeAllRecords();
		grid.addLoadingImagePanel();

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

}
