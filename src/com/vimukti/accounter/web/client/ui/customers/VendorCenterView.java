package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.VendorSelectionListener;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionsHistoryGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorsListGrid;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;

public class VendorCenterView<T> extends BaseView<ClientVendor> {

	private static final int TYPE_CASH_PURCHASE = 2;
	private static final int TYPE_ENTER_BILL = 6;
	public static final int TYPE_PAY_BILL = 11;
	public static final int TYPE_WRITE_CHECK = 15;
	public static final int TYPE_PURCHASE_ORDER = 22;
	private static final int TYPE_ALL_TRANSACTION = 100;
	public static final int TYPE_ISSUE_PAYMENT = 9;
	public static final int TYPE_VENDOR_CREDIT_MEMO = 14;
	public static final int TYPE_EXPENSE = 18;

	public ClientVendor selectedVendor;
	private List<PayeeList> listOfVendors;
	protected ArrayList<ClientFinanceDate> startEndDates;
	protected ArrayList<TransactionHistory> records;

	VendorDetailsPanel detailsPanel;
	VendorsListGrid vendorlistGrid;
	protected SelectCombo activeInActiveSelect, trasactionViewSelect,
			trasactionViewTypeSelect, dateRangeSelector;
	VerticalPanel transactionGridpanel;
	VendorTransactionsHistoryGrid vendHistoryGrid;
	List<String> dateRangeList, typeList;
	ClientFinanceDate startDate, endDate;
	Map<Integer, String> transactiontypebyStatusMap;

	public VendorCenterView() {

	}

	@Override
	public boolean canEdit() {
		return selectedVendor == null ? false : true;
	}

	@Override
	public void onEdit() {
		NewVendorAction newVendorAction = ActionFactory.getNewVendorAction();
		newVendorAction.setisVendorViewEditable(true);
		newVendorAction.run(selectedVendor, false);
	}

	@Override
	public void init() {
		super.init();
		creatControls();
	}

	private void creatControls() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		VerticalPanel leftVpPanel = new VerticalPanel();
		viewTypeCombo();
		DynamicForm viewform = new DynamicForm();
		viewform.setFields(activeInActiveSelect);
		leftVpPanel.add(viewform);
		viewform.setNumCols(2);
		viewform.getElement().getParentElement().setAttribute("align", "left");
		vendorlistGrid = new VendorsListGrid();
		vendorlistGrid.init();
		leftVpPanel.add(vendorlistGrid);

		vendorlistGrid.getElement().getParentElement()
				.setAttribute("width", "15%");
		vendorlistGrid.setStyleName("cusotmerCentrGrid");
		VerticalPanel rightVpPanel = new VerticalPanel();
		detailsPanel = new VendorDetailsPanel(selectedVendor);
		rightVpPanel.add(detailsPanel);
		vendorlistGrid
				.setVendorSelectionListener(new VendorSelectionListener() {

					@Override
					public void vendorSelected() {
						onVendorSelected();

					}
				});
		transactionViewSelectCombo();
		transactionViewTypeSelectCombo();
		transactionDateRangeSelector();
		DynamicForm transactionViewform = new DynamicForm();

		transactionViewform.setNumCols(6);
		transactionViewform.setFields(trasactionViewSelect,
				trasactionViewTypeSelect, dateRangeSelector);

		transactionGridpanel = new VerticalPanel();
		transactionGridpanel.add(transactionViewform);
		vendHistoryGrid = new VendorTransactionsHistoryGrid();
		vendHistoryGrid.init();
		vendHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
				.get().Vendor()));
		rightVpPanel.add(transactionGridpanel);
		rightVpPanel.add(vendHistoryGrid);
		vendHistoryGrid.setHeight("494px");
		mainPanel.add(leftVpPanel);
		mainPanel.add(rightVpPanel);
		add(mainPanel);

	}

	private void viewTypeCombo() {
		if (activeInActiveSelect == null) {
			activeInActiveSelect = new SelectCombo(Accounter.messages().show());
			activeInActiveSelect.setHelpInformation(true);

			List<String> activetypeList = new ArrayList<String>();
			activetypeList.add(Accounter.messages().active());
			activetypeList.add(Accounter.messages().inActive());
			activeInActiveSelect.initCombo(activetypeList);
			activeInActiveSelect.setComboItem(Accounter.messages().active());
			activeInActiveSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (activeInActiveSelect.getSelectedValue() != null) {
								if (activeInActiveSelect.getSelectedValue()
										.toString().equalsIgnoreCase("Active")) {
									onActiveChangedListener();
								} else {
									onInActiveChangedlistener();
								}
							}

						}

					});
		}
	}

	private void onActiveChangedListener() {
		vendorlistGrid.setSelectedVendor(null);
		detailsPanel.vendName.setText(messages.no() + " "
				+ messages.payeeSelected(Global.get().Vendor()));
		this.selectedVendor = null;
		onVendorSelected();
		vendorlistGrid.filterList(true);

	}

	private void onInActiveChangedlistener() {
		vendorlistGrid.setSelectedVendor(null);
		detailsPanel.vendName.setText(messages.no() + " "
				+ messages.payeeSelected(Global.get().Vendor()));
		this.selectedVendor = null;
		onVendorSelected();
		vendorlistGrid.filterList(false);

	}

	private void transactionViewSelectCombo() {
		if (trasactionViewSelect == null) {
			trasactionViewSelect = new SelectCombo(Accounter.messages()
					.currentView());
			trasactionViewSelect.setHelpInformation(true);

			List<String> transactionTypeList = new ArrayList<String>();
			transactionTypeList.add(Accounter.messages().allTransactions());
			transactionTypeList.add(Accounter.messages().cashPurchases());
			transactionTypeList.add(Accounter.messages().bills());
			transactionTypeList.add(Accounter.messages().payBills());
			transactionTypeList.add(Accounter.messages().cheques());
			transactionTypeList.add(Accounter.messages().payeeCreditNotes(
					Global.get().Vendor()));
			transactionTypeList.add(Accounter.messages().expenses());
			transactionTypeList.add(Accounter.messages().purchaseOrders());
			trasactionViewSelect.initCombo(transactionTypeList);
			trasactionViewSelect.setComboItem(Accounter.messages()
					.allTransactions());
			trasactionViewSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewSelect.getSelectedValue() != null) {
								getMessagesList();
								callRPC();
							}

						}

					});
		}

	}

	private void transactionViewTypeSelectCombo() {
		if (trasactionViewTypeSelect == null) {
			trasactionViewTypeSelect = new SelectCombo(Accounter.messages()
					.type());
			trasactionViewTypeSelect.setHelpInformation(true);
			getMessagesList();
			trasactionViewTypeSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (trasactionViewTypeSelect.getSelectedValue() != null) {
								callRPC();
							}

						}

					});
		}

	}

	private void getMessagesList() {
		transactiontypebyStatusMap = new HashMap<Integer, String>();
		if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.allTransactions())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_TRANSACTIONS,
					messages.allTransactions());
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cashPurchases())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_CASH_PURCHASES,
					messages.allCashPurchases());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.bills())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_BILLS,
					messages.allBills());
			transactiontypebyStatusMap.put(TransactionHistory.OPEND_BILLS,
					messages.all() + " " + messages.openedBills());
			transactiontypebyStatusMap.put(TransactionHistory.OVERDUE_BILLS,
					messages.all() + " " + messages.overDueBills());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payBills())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_PAYBILLS,
					messages.all() + " " + messages.payBills());
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cheques())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CHEQUES,
					messages.allcheques());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payeeCreditNotes(Global.get().Vendor()))) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_VENDOR_CREDITNOTES,
					messages.all() + " "
							+ messages.payeeCreditNotes(Global.get().Vendor()));

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().expenses())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_EXPENSES,
					messages.allExpenses());
			transactiontypebyStatusMap.put(
					TransactionHistory.CREDIT_CARD_EXPENSES,
					messages.creditCardExpenses());
			transactiontypebyStatusMap.put(TransactionHistory.CASH_EXPENSES,
					messages.cashExpenses());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().purchaseOrders())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_PURCHASE_ORDERS,
					messages.allPurchaseOrders());
			transactiontypebyStatusMap.put(
					TransactionHistory.OPEN_PURCHASE_ORDERS,
					messages.openPurchaseOrders());

		}
		List<String> typeList = new ArrayList<String>(
				transactiontypebyStatusMap.values());
		Collections.sort(typeList, new Comparator<String>() {

			@Override
			public int compare(String entry1, String entry2) {
				return entry1.compareTo(entry2);
			}

		});
		trasactionViewTypeSelect.initCombo(typeList);
		trasactionViewTypeSelect.setComboItem(typeList.get(0));
	}

	private void transactionDateRangeSelector() {
		dateRangeSelector = new SelectCombo(Accounter.messages().date());

		dateRangeList = new ArrayList<String>();
		String[] dateRangeArray = { Accounter.messages().all(),
				Accounter.messages().thisWeek(),
				Accounter.messages().thisMonth(),
				Accounter.messages().lastWeek(),
				Accounter.messages().lastMonth(),
				Accounter.messages().thisFinancialYear(),
				Accounter.messages().lastFinancialYear(),
				Accounter.messages().thisFinancialQuarter(),
				Accounter.messages().lastFinancialQuarter(),
				Accounter.messages().financialYearToDate() };
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeSelector.initCombo(dateRangeList);

		dateRangeSelector.setComboItem(Accounter.messages().all());
		dateRangeSelector
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRangeSelector.setComboItem(selectItem);
						if (dateRangeSelector.getValue() != null) {
							dateRangeChanged(selectItem);
							callRPC();

						}
					}
				});

	}

	private void onVendorSelected() {
		this.selectedVendor = vendorlistGrid.getSelectedVendor();
		detailsPanel.showVendorDetails(selectedVendor);
		vendHistoryGrid.setSelectedVendor(selectedVendor);
		MainFinanceWindow.getViewManager().updateButtons();
		callRPC();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().payees(Global.get().Vendor());
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		Iterator<PayeeList> iterator = listOfVendors.iterator();
		while (iterator.hasNext()) {
			PayeeList next = iterator.next();
			if (next.getID() == result.getID()) {
				iterator.remove();
			}
		}
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	public void setSelectedVendor(ClientVendor selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	private void callRPC() {
		vendHistoryGrid.clear();
		if (selectedVendor != null) {
			Accounter.createReportService().getVendorTransactionsList(
					selectedVendor.getID(), getTransactionType(),
					getTransactionStatusType(), getStartDate(), getEndDate(),
					new AsyncCallback<ArrayList<TransactionHistory>>() {

						@Override
						public void onFailure(Throwable caught) {
							Accounter.showError(messages
									.unableToPerformTryAfterSomeTime());
						}

						@Override
						public void onSuccess(
								ArrayList<TransactionHistory> result) {
							records = result;
							vendHistoryGrid.clear();
							if (records != null) {
								vendHistoryGrid.addRecords(records);
							}
							if (records.size() == 0) {
								vendHistoryGrid.addEmptyMessage(messages
										.thereAreNo(messages.transactions()));
							}
						}
					});

		} else {
			vendHistoryGrid.clear();
			vendHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
					.transactions()));
		}
	}

	protected int getTransactionStatusType() {
		if (trasactionViewTypeSelect.getSelectedValue() != null) {
			Set<Integer> keySet = transactiontypebyStatusMap.keySet();
			for (Integer integerKey : keySet) {
				String entrystring = transactiontypebyStatusMap.get(integerKey);
				if (trasactionViewTypeSelect.getSelectedValue().equals(
						entrystring)) {
					return integerKey;
				}
			}
		}
		return TransactionHistory.ALL_INVOICES;

	}

	protected int getTransactionType() {

		if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cashPurchases())) {

			return TYPE_CASH_PURCHASE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.bills())) {
			return TYPE_ENTER_BILL;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.payBills())) {
			return TYPE_PAY_BILL;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.cheques())) {
			return TYPE_WRITE_CHECK;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().payeeCreditNotes(Global.get().Vendor()))) {
			return TYPE_VENDOR_CREDIT_MEMO;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().expenses())) {
			return TYPE_EXPENSE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().purchaseOrders())) {
			return TYPE_PURCHASE_ORDER;
		}
		return TYPE_ALL_TRANSACTION;

	}

	public void dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		startDate = Accounter.getStartDate();
		endDate = getCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(Accounter.messages().thisWeek())) {
			startDate = getWeekStartDate();
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals(Accounter.messages().thisMonth())) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());

		}
		if (dateRange.equals(Accounter.messages().lastWeek())) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals(Accounter.messages().lastMonth())) {
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
		if (dateRange.equals(Accounter.messages().thisFinancialYear())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(Accounter.messages().lastFinancialYear())) {

			startDate = Accounter.getCompany().getCurrentFiscalYearStartDate();
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(Accounter.getCompany().getCurrentFiscalYearEndDate()
					.getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);

		}
		if (dateRange.equals(Accounter.messages().thisFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(Accounter.messages().lastFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(Accounter.messages().financialYearToDate())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = new ClientFinanceDate();
		}
		setStartDate(startDate);
		setEndDate(endDate);
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}
		return newDate;
	}

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

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

}
