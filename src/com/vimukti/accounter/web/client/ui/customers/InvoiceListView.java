package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.grids.InvoiceListGrid;

public class InvoiceListView extends BaseListView<InvoicesList> {
	CustomersMessages customerConstants = GWT.create(CustomersMessages.class);

	private List<InvoicesList> listOfInvoices;

	private static String OPEN = FinanceApplication.getCustomersMessages()
			.open();
	private static String OVER_DUE = FinanceApplication.getCustomersMessages()
			.overDue();
	private static String VOID = FinanceApplication.getVendorsMessages()
			.Voided();
	private static String ALL = FinanceApplication.getVendorsMessages().all();
	// private static String DELETE = "Deleted";
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	public String viewType;

	public InvoiceListView() {
		isDeleteDisable = true;
		startDate = FinanceApplication.getStartDate();
		endDate = Utility.getLastandOpenedFiscalYearEndDate();
	}

	public InvoiceListView(String viewType) {
		this.viewType = viewType;
		isDeleteDisable = true;
		startDate = FinanceApplication.getStartDate();
		endDate = Utility.getLastandOpenedFiscalYearEndDate();
	}

	// @Override
	// public void initData() {
	//
	// }

	@Override
	protected Action getAddNewAction() {
		return CustomersActionFactory.getNewInvoiceAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return customerConstants.addaNewInvoice();
	}

	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getCustomersMessages().invoiceList();
	}

	@Override
	public void updateInGrid(InvoicesList objectTobeModified) {

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getInvoiceList(
				startDate.getTime(), endDate.getTime(), this);
	}

	@Override
	public void onSuccess(List<InvoicesList> result) {
		super.onSuccess(result);
		listOfInvoices = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	@Override
	protected void initGrid() {
		grid = new InvoiceListGrid();
		grid.init();
	}

	String[] dateRangeArray = {
			FinanceApplication.getCustomersMessages().all(),
			FinanceApplication.getCustomersMessages().thisWeek(),
			FinanceApplication.getCustomersMessages().thisMonth(),
			FinanceApplication.getCustomersMessages().lastWeek(),
			FinanceApplication.getCustomersMessages().lastMonth(),
			FinanceApplication.getCustomersMessages().thisFinancialYear(),
			FinanceApplication.getCustomersMessages().lastFinancialYear(),
			FinanceApplication.getCustomersMessages().thisFinancialQuarter(),
			FinanceApplication.getCustomersMessages().lastFinancialQuarter(),
			FinanceApplication.getCustomersMessages().financialYearToDate(),
			FinanceApplication.getCustomersMessages().custom() };

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(FinanceApplication.getCustomersMessages()
				.currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(OVER_DUE);
		listOfTypes.add(VOID);
		listOfTypes.add(ALL);
		viewSelect.initCombo(listOfTypes);
		if (viewType != null && !viewType.equals(""))
			viewSelect.setComboItem(viewType);
		else
			viewSelect.setComboItem(OPEN);
		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("150px");
		viewSelect.setComboItem(OPEN);
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							filterList(viewSelect.getSelectedValue());
						}

					}
				});

		return viewSelect;
	}

	protected SelectCombo getDateRangeSelectItem() {
		dateRangeSelector = new SelectCombo(FinanceApplication
				.getCustomersMessages().date());
		dateRangeSelector.setValueMap(dateRangeArray);
		dateRangeSelector.setDefaultValue(ALL);

		if (UIUtils.isMSIEBrowser())
			dateRangeSelector.setWidth("150px");

		if (dateRangeSelector.getValue() != null
				&& dateRangeSelector.getValue().equals(
						FinanceApplication.getCustomersMessages().all()))
			getMinimumAndMaximumDates();

		dateRangeSelector.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (dateRangeSelector.getValue() != null
						&& !dateRangeSelector.getValue().equals(
								FinanceApplication.getCustomersMessages()
										.custom())) {
					dateRangeChanged(dateRangeSelector.getValue().toString());
					grid.setViewType(dateRangeSelector.getValue().toString());
				}

			}
		});

		return dateRangeSelector;
	}

	@SuppressWarnings("unchecked")
	private void filterList(String text) {

		grid.removeAllRecords();
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");

		for (InvoicesList invoice : listOfInvoices) {
			if (text.equals(OPEN)) {
				if (invoice.getBalance() != null
						&& DecimalUtil.isGreaterThan(invoice.getBalance(), 0)
						&& invoice.getDueDate() != null
						&& (invoice.getStatus() != ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
						&& !invoice.isVoided())
					grid.addData(invoice);
				continue;

			}
			if (text.equals(OVER_DUE)) {
				if (invoice.getBalance() != null
						&& DecimalUtil.isGreaterThan(invoice.getBalance(), 0)
						&& (invoice.getDueDate().compareTo(
								new ClientFinanceDate()) < 0)
						&& !invoice.isVoided())
					grid.addData(invoice);
				continue;
			}
			if (text.equals(VOID)) {
				if (invoice.isVoided()
				// && !invoice.isDeleted()
				)
					grid.addData(invoice);
				continue;
			}
			// if (text.equals(DELETE)) {
			// if (invoice.isDeleted())
			// grid.addData(invoice);
			// continue;
			// }
			if (text.equals(ALL)) {
				grid.addData(invoice);
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
		}
	}

	public void dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		startDate = FinanceApplication.getStartDate();
		endDate = Utility.getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(FinanceApplication.getCustomersMessages().all())) {
			getMinimumAndMaximumDates();
			return;
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.thisWeek())) {
			startDate = getWeekStartDate();
			endDate = new ClientFinanceDate();
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.thisMonth())) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			endDate = new ClientFinanceDate();
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.lastWeek())) {
			endDate = getWeekStartDate();
			endDate.setDate(endDate.getDate() - 1);
			startDate = new ClientFinanceDate(endDate.getTime());
			startDate.setDate(startDate.getDate() - 6);

		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.lastMonth())) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(), date
						.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.thisFinancialYear())) {
			startDate = Utility.getCurrentFiscalYearStartDate();
			endDate = Utility.getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.lastFinancialYear())) {
			startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.thisFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = Utility.getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.lastFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = Utility.getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(FinanceApplication.getCustomersMessages()
				.financialYearToDate())) {
			startDate = Utility.getCurrentFiscalYearStartDate();
			endDate = new ClientFinanceDate();
		}
		setStartDate(startDate);
		setEndDate(endDate);
		changeDates(startDate, endDate);
	}

	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		initListCallback();

	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay();
		ClientFinanceDate newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() - day);
		return newDate;
	}

	// public native double getWeekStartDate()/*-{
	// var date= new Date();
	// var day=date.getDay();
	// var newDate=new Date();
	// newDate.setDate(date.getDate()-day);
	// var tmp=newDate.getTime();
	// return tmp;
	// }-*/;

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void getPreviousQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}
	}

	private void getMinimumAndMaximumDates() {

		if (this.rpcUtilService == null)
			return;

		else {
			AsyncCallback<List<ClientFinanceDate>> callback = new AsyncCallback<List<ClientFinanceDate>>() {

				@Override
				public void onFailure(Throwable caught) {
					return;
				}

				@Override
				public void onSuccess(List<ClientFinanceDate> result) {

					if (result == null)
						onFailure(new Exception());
					if (result.size() > 0) {
						ClientFinanceDate startDate1 = result.get(0) == null ? new ClientFinanceDate()
								: result.get(0);
						ClientFinanceDate endDate2 = result.get(1) == null ? new ClientFinanceDate()
								: result.get(1);
						startDate = startDate1;
						endDate = endDate2;
						initListCallback();
						fromItem.setValue(startDate);
						toItem.setValue(endDate);

					} else {
						onFailure(new Exception());

					}

				}
			};
			this.rpcUtilService.getMinimumAndMaximumTransactionDate(callback);

		}
	}

	@Override
	public void customManage() {

		setStartDate(fromItem.getDate());
		setEndDate(toItem.getDate());
		changeDates(startDate, endDate);
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return UIUtils.toDate(endDate);
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public void updateGrid(IAccounterCore core) {
		initListCallback();
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}
}
