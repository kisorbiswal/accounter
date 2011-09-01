package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.grids.InvoiceListGrid;

public class InvoiceListView extends BaseListView<InvoicesList> implements
		IPrintableView {
	AccounterConstants customerConstants = Accounter.constants();

	private List<InvoicesList> listOfInvoices;

	private List<String> dateRangeList;

	public static String OPEN = Accounter.constants().open();
	public static String OVER_DUE = Accounter.constants().overDue();
	public static String VOID = Accounter.constants().voided();
	public static String ALL = Accounter.constants().all();
	// private static String DELETE = "Deleted";
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	public String viewType;

	public InvoiceListView() {
		isDeleteDisable = true;
		startDate = Accounter.getStartDate();
		endDate = getCompany().getLastandOpenedFiscalYearEndDate();
	}

	public InvoiceListView(String viewType) {
		this.viewType = viewType;
		isDeleteDisable = true;
		startDate = Accounter.getStartDate();
		endDate = getCompany().getLastandOpenedFiscalYearEndDate();
	}

	// @Override
	// public void initData() {
	//
	// }

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewInvoiceAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return customerConstants.addaNewInvoice();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().invoiceList();
	}

	@Override
	public void updateInGrid(InvoicesList objectTobeModified) {

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getInvoiceList(
				startDate == null ? 0 : startDate.getDate(), endDate.getDate(),
				this);
	}

	@Override
	public void onSuccess(ArrayList<InvoicesList> result) {
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

	String[] dateRangeArray = { Accounter.constants().all(),
			Accounter.constants().thisWeek(),
			Accounter.constants().thisMonth(),
			Accounter.constants().lastWeek(),
			Accounter.constants().lastMonth(),
			Accounter.constants().thisFinancialYear(),
			Accounter.constants().lastFinancialYear(),
			Accounter.constants().thisFinancialQuarter(),
			Accounter.constants().lastFinancialQuarter(),
			Accounter.constants().financialYearToDate(),
			Accounter.constants().custom() };

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(Accounter.constants().currentView());
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
			viewSelect.setWidth("105px");

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
		viewSelect.addStyleName("invoiceListCombo");

		return viewSelect;
	}

	protected SelectCombo getDateRangeSelectItem() {
		dateRangeSelector = new SelectCombo(Accounter.constants().date());
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeSelector.initCombo(dateRangeList);
		dateRangeSelector.setDefaultValue(ALL);

		dateRangeSelector.setComboItem(ALL);
		if (UIUtils.isMSIEBrowser())
			dateRangeSelector.setWidth("105px");

		if (dateRangeSelector.getValue() != null
				&& dateRangeSelector.getValue().equals(
						Accounter.constants().all()))
			getMinimumAndMaximumDates();

		dateRangeSelector
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (dateRangeSelector.getValue() != null
								&& !dateRangeSelector.getValue().equals(
										Accounter.constants().custom())) {
							dateRangeChanged(dateRangeSelector
									.getSelectedValue());
							grid.setViewType(dateRangeSelector
									.getSelectedValue());
						}
					}
				});
		dateRangeSelector.addStyleName("invoiceListCombo");

		return dateRangeSelector;
	}

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
						&& invoice.getDueDate() != null
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
		startDate = Accounter.getStartDate();
		endDate = getCompany().getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(Accounter.constants().all())) {
			getMinimumAndMaximumDates();
			return;
		}
		if (dateRange.equals(Accounter.constants().thisWeek())) {
			startDate = getWeekStartDate();
			endDate = new ClientFinanceDate();
		}
		if (dateRange.equals(Accounter.constants().thisMonth())) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			endDate = new ClientFinanceDate();
		}
		if (dateRange.equals(Accounter.constants().lastWeek())) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals(Accounter.constants().lastMonth())) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(Accounter.constants().thisFinancialYear())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(Accounter.constants().lastFinancialYear())) {
			startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
		}
		if (dateRange.equals(Accounter.constants().thisFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(Accounter.constants().lastFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(Accounter.constants().financialYearToDate())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
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
		newDate.setDay(date.getDay() - day);
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
			AccounterAsyncCallback<ArrayList<ClientFinanceDate>> callback = new AccounterAsyncCallback<ArrayList<ClientFinanceDate>>() {

				@Override
				public void onException(AccounterException caught) {
					return;
				}

				@Override
				public void onResultSuccess(ArrayList<ClientFinanceDate> result) {

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
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}


	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
	}

	@Override
	public void print() {

		Vector<Integer> v = new Vector<Integer>();

		boolean isWriteCheck_cashsale = false;
		for (InvoicesList invoice : listOfInvoices) {

			if (invoice.isPrint()) {
				if (invoice.getType() == ClientTransaction.TYPE_INVOICE) {

					if (!v.contains(ClientTransaction.TYPE_INVOICE))

						v.add(ClientTransaction.TYPE_INVOICE);
				} else if (invoice.getType() == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {

					if (!v.contains(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO))
						v.add(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

				} else if (invoice.getType() == ClientTransaction.TYPE_CASH_SALES) {

					if (!v.contains(ClientTransaction.TYPE_CASH_SALES))
						v.add(ClientTransaction.TYPE_CASH_SALES);
					isWriteCheck_cashsale = true;
				} else if (invoice.getType() == ClientTransaction.TYPE_WRITE_CHECK) {

					if (!v.contains(ClientTransaction.TYPE_WRITE_CHECK))
						v.add(ClientTransaction.TYPE_WRITE_CHECK);
					isWriteCheck_cashsale = true;

				}
			}
		}

		String errorMessage = Global.get().constants()
				.pleaseSelectReportsOfSameType();
		String emptymsg = Global.get().constants()
				.pleaseSelectAtLeastOneReport();

		String cashsalemsg = Global.get().constants()
				.PrintIsNotProvidedForCashSale();
		if (v.size() == 0) {// no reports are selected
			showDialogBox(emptymsg);
		} else if (v.size() > 1) {
			// if one than one report type is selected
			showDialogBox(errorMessage);
		} else {
			if (!isWriteCheck_cashsale) {
				ActionFactory.getBrandingThemeComboAction().run(listOfInvoices);
			} else {
				// if other reports are selected cash sale or write check
				showDialogBox(cashsalemsg);
			}
			// ActionFactory.getInvoiceListViewAction().run(listOfInvoices);
		}

	}

	public void showDialogBox(String description) {
		InvoicePrintDialog printDialog = new InvoicePrintDialog(Accounter
				.constants().selectReports(), "", description);
		printDialog.show();
		printDialog.center();
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().invoices();
	}

	@Override
	public boolean canPrint() {

		return true;
	}

	@Override
	public boolean canExportToCsv() {

		return false;
	}
}
