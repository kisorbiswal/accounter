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
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
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
import com.vimukti.accounter.web.client.ui.grids.CustomerSelectionListener;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionsHistoryGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomersListGrid;

public class CustomerCenterView<T> extends BaseView<ClientCustomer> {

	private static final int TYPE_INVOICE = 8;
	private static final int TYPE_CAHSSALE = 1;
	private static final int TYPE_RECEIVE_PAYMENT = 12;
	private static final int TYPE_CREDITNOTE = 4;
	private static final int TYPE_CUSTOMER_REFUND = 5;
	private static final int TYPE_ALL_TRANSACTION = 100;
	public ClientCustomer selectedCustomer;
	private List<PayeeList> listOfCustomers;
	protected ArrayList<ClientFinanceDate> startEndDates;
	protected ArrayList<TransactionHistory> records;

	CustomerDetailsPanel detailsPanel;
	CustomersListGrid custGrid;
	protected SelectCombo activeInActiveSelect, trasactionViewSelect,
			trasactionViewTypeSelect, dateRangeSelector;
	VerticalPanel transactionGridpanel;
	CustomerTransactionsHistoryGrid custHistoryGrid;
	List<String> dateRangeList, typeList;
	ClientFinanceDate startDate, endDate;
	Map<Integer, String> transactiontypebyStatusMap;

	public CustomerCenterView() {

	}

	@Override
	public boolean canEdit() {
		return selectedCustomer == null ? false : true;
	}

	@Override
	public void onEdit() {
		NewCustomerAction newCustomerAction = ActionFactory
				.getNewCustomerAction();
		newCustomerAction.setisCustomerViewEditable(true);
		newCustomerAction.run(selectedCustomer, false);
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
		custGrid = new CustomersListGrid();
		custGrid.init();
		leftVpPanel.add(custGrid);
		leftVpPanel.setSpacing(5);

		custGrid.getElement().getParentElement().setAttribute("width", "15%");
		custGrid.setStyleName("cusotmerCentrGrid");
		VerticalPanel rightVpPanel = new VerticalPanel();
		detailsPanel = new CustomerDetailsPanel(selectedCustomer);
		rightVpPanel.add(detailsPanel);
		custGrid.setCustomerSelectionListener(new CustomerSelectionListener() {
			@Override
			public void cusotmerSelected() {
				OncusotmerSelected();
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
		custHistoryGrid = new CustomerTransactionsHistoryGrid();
		custHistoryGrid.init();
		custHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
				.get().Customer()));
		rightVpPanel.add(transactionGridpanel);
		rightVpPanel.add(custHistoryGrid);
		custHistoryGrid.setHeight("494px");
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
									custGrid.filterList(true);
								} else {
									custGrid.filterList(false);
								}
								detailsPanel.setCustomer(null);
								selectedCustomer = null;
								callRPC();
							}

						}

					});
		}
	}

	private void transactionViewSelectCombo() {
		if (trasactionViewSelect == null) {
			trasactionViewSelect = new SelectCombo(Accounter.messages()
					.currentView());
			trasactionViewSelect.setHelpInformation(true);

			List<String> transactionTypeList = new ArrayList<String>();
			transactionTypeList.add(Accounter.messages().allTransactions());
			transactionTypeList.add(Accounter.messages().invoices());
			transactionTypeList.add(Accounter.messages().allcashSales());
			transactionTypeList.add(Accounter.messages().receivedPayments());
			transactionTypeList.add(Accounter.messages().CustomerCreditNotes());
			transactionTypeList.add(Accounter.messages().customerRefunds(
					Global.get().Customer()));
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
				messages.invoices())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_INVOICES,
					messages.getallInvoices());
			transactiontypebyStatusMap.put(TransactionHistory.OPENED_INVOICES,
					messages.getOpendInvoices());
			transactiontypebyStatusMap.put(
					TransactionHistory.OVER_DUE_INVOICES,
					messages.getOverdueInvoices());
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.allcashSales())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CASHSALES,
					messages.all() + " " + messages.allcashSales());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.receivedPayments())) {
			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_RECEIVEDPAYMENTS, messages.all()
							+ " " + messages.receivedPayments());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CASH,
					messages.receivedPaymentsbyCash());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CHEQUE,
					messages.receivedPaymentsbyCheque());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_CREDITCARD,
					messages.receivedPaymentsbyCreditCard());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_DIRECT_DEBIT,
					messages.receivedPaymentsbyDirectDebit());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_MASTERCARD,
					messages.receivedPaymentsbyMastercard());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_ONLINE,
					messages.receivedPaymentsbyOnlineBanking());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_STANDING_ORDER,
					messages.receivedPaymentsbyStandingOrder());
			transactiontypebyStatusMap.put(
					TransactionHistory.RECEV_PAY_BY_MAESTRO,
					messages.receivedPaymentsbySwitchMaestro());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.CustomerCreditNotes())) {
			transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITMEMOS,
					messages.allCreditMemos());
			transactiontypebyStatusMap.put(
					TransactionHistory.OPEND_CREDITMEMOS,
					messages.openCreditMemos());

		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().customerRefunds(Global.get().Customer()))) {
			transactiontypebyStatusMap.put(
					TransactionHistory.REFUNDS_BY_CREDITCARD,
					messages.refundsByCreditCard());
			transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCASH,
					messages.refundsByCash());
			transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCHEQUE,
					messages.refundsByCheck());

			transactiontypebyStatusMap.put(
					TransactionHistory.ALL_CUSTOMER_REFUNDS,
					messages.allCustomerRefunds());
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

	private void OncusotmerSelected() {
		this.selectedCustomer = custGrid.getSelectedCustomer();
		detailsPanel.showCustomerDetails(selectedCustomer);
		custHistoryGrid.setSelectedCustomer(selectedCustomer);
		MainFinanceWindow.getViewManager().updateButtons();
		callRPC();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().payees(Global.get().Customer());
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		Iterator<PayeeList> iterator = listOfCustomers.iterator();
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

	public void setSelectedCustomer(ClientCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public ClientCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	private void callRPC() {
		if (selectedCustomer != null) {
			Accounter.createReportService().getCustomerTransactionsList(
					selectedCustomer.getID(), getTransactionType(),
					getTransactionStatusType(), getStartDate(), getEndDate(),
					new AsyncCallback<ArrayList<TransactionHistory>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(
								ArrayList<TransactionHistory> result) {
							records = result;
							custHistoryGrid.clear();
							if (records != null) {
								custHistoryGrid.addRecords(records);
							}
							if (records.size() == 0) {
								custHistoryGrid.addEmptyMessage(messages
										.thereAreNo(messages.transactions()));
							}
						}
					});

		} else {
			custHistoryGrid.clear();
			custHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
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
				messages.invoices())) {

			return TYPE_INVOICE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.allcashSales())) {
			return TYPE_CAHSSALE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.receivedPayments())) {
			return TYPE_RECEIVE_PAYMENT;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				messages.CustomerCreditNotes())) {
			return TYPE_CREDITNOTE;
		} else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
				Accounter.messages().customerRefunds(Global.get().Customer()))) {
			return TYPE_CUSTOMER_REFUND;
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
