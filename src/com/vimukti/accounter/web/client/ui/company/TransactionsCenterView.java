package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.options.TreeListPanel;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.ISavableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundListView;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;
import com.vimukti.accounter.web.client.ui.customers.QuoteListView;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentListView;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentsListView;
import com.vimukti.accounter.web.client.ui.vendors.BillListView;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesListView;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListView;

public class TransactionsCenterView<T> extends AbstractBaseView<T> implements
		IPrintableView, ISavableView<String> {

	public TransactionsListView<T> baseListView;
	private final HorizontalPanel mainPanel;
	private String selectedItem = null;
	private TreeListPanel listPanel;
	private ClientFinanceDate startDate;
	private ClientFinanceDate endDate;
	public static final int VIEW_ALL = 0;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_DRAFT = 4;
	public static final int TYPE_ALL = 1000;
	int transactionType = 0;

	// private String selectedType;
	//
	// private String searchString;

	public TransactionsCenterView() {
		mainPanel = new HorizontalPanel();
		mainPanel.setStyleName("Transactions_center");
		createTreeItems();
		this.add(mainPanel);
		setSize("100%", "100%");
		mainPanel.setWidth("100");
	}

	@SuppressWarnings("unchecked")
	private void createTreeItems() {
		listPanel = new TreeListPanel() {

			@Override
			protected void onMenuClick(String menuTitle) {
			}

			@Override
			protected void onMenuItemClick(String menuItemName) {
				initGridData(menuItemName);
			}

		};
		mainPanel.add(listPanel);

		listPanel.addMenuPanel(Global.get().Customers(),
				getCustomerCenterItems());

		listPanel.addMenuPanel(Global.get().Vendors(), getVendorCenterItems());

		listPanel.addMenuPanel(messages.banking(), getBankCenterItems());

		listPanel.addMenuPanel(messages.others(), getOtherCenterItems());
		baseListView = (TransactionsListView<T>) new InvoiceListView();
		initGridData(getMessages().invoices());
	}

	private List<String> getBankCenterItems() {
		List<String> bankItems = new ArrayList<String>();
		bankItems.add(getMessages().payments());
		bankItems.add(getMessages().deposits());
		bankItems.add(getMessages().transferFunds());
		return bankItems;
	}

	@SuppressWarnings("unchecked")
	protected void initGridData(String itemName) {
		selectedItem = itemName;
		baseListView.clear();
		mainPanel.remove(baseListView);
		// Customer Menu
		if (itemName.equalsIgnoreCase(messages.customerchecks())) {
			transactionType = PaymentListView.TYPE_CUSTOMER_CHECKS;
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_CUSTOMER_CHECKS);

		} else if (itemName.equalsIgnoreCase(getMessages().cashSales())) {
			transactionType = ClientTransaction.TYPE_CASH_SALES;
			baseListView = (TransactionsListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_CASH_SALES);

		} else if (itemName.equalsIgnoreCase(getMessages()
				.customerCreditNotes())) {
			transactionType = ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO;
			baseListView = (TransactionsListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

		} else if (itemName.equalsIgnoreCase(getMessages().quotes())) {
			transactionType = ClientEstimate.QUOTES;
			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.QUOTES);

		} else if (itemName.equalsIgnoreCase(getMessages().Charges())) {
			transactionType = ClientEstimate.CHARGES;
			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.CHARGES);

		} else if (itemName.equalsIgnoreCase(getMessages().credits())) {
			transactionType = ClientEstimate.CREDITS;
			baseListView = (TransactionsListView<T>) new QuoteListView(
					ClientEstimate.CREDITS);

		} else if (itemName.equalsIgnoreCase(getMessages().invoices())) {
			transactionType = ClientTransaction.TYPE_INVOICE;
			baseListView = (TransactionsListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_INVOICE);
		} else if (itemName.equalsIgnoreCase(getMessages().receivedPayments())) {
			transactionType = ClientTransaction.TYPE_RECEIVE_PAYMENT;
			baseListView = (TransactionsListView<T>) new ReceivedPaymentListView(
					ClientTransaction.TYPE_RECEIVE_PAYMENT);

		} else if (itemName.equals(getMessages().payeePayments(
				Global.get().Customers()))) {
			transactionType = ClientTransaction.TYPE_CUSTOMER_PREPAYMENT;
			baseListView = (TransactionsListView<T>) new ReceivedPaymentListView(
					ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);

		} else if (itemName.equalsIgnoreCase(getMessages().customerRefunds(
				Global.get().Customer()))) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new CustomerRefundListView();

		} else if (itemName.equalsIgnoreCase(getMessages().billCredits())) {
			transactionType = ClientTransaction.TYPE_VENDOR_CREDIT_MEMO;
			baseListView = (TransactionsListView<T>) new BillListView(
					getMessages().all(),
					ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);

		} else if (itemName.equalsIgnoreCase(getMessages().billPayments())) {
			transactionType = ClientTransaction.TYPE_VENDOR_PAYMENT;
			baseListView = (TransactionsListView<T>) new VendorPaymentsListView(
					ClientTransaction.TYPE_VENDOR_PAYMENT);

		} else if (itemName.equalsIgnoreCase(getMessages().bills())) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new BillListView(
					getMessages().all());

		} else if (itemName.equalsIgnoreCase(messages.vendorchecks())) {
			transactionType = PaymentListView.TYPE_VENDOR_CHECKS;
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_VENDOR_CHECKS);

		} else if (itemName
				.equalsIgnoreCase(getMessages().creditCardExpenses())) {
			transactionType = ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
			baseListView = (TransactionsListView<T>) new ExpensesListView(
					getMessages().creditCard(),
					ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		} else if (itemName.equalsIgnoreCase(getMessages().cashExpenses())) {
			transactionType = ClientTransaction.TYPE_CASH_EXPENSE;
			baseListView = (TransactionsListView<T>) new ExpensesListView(
					getMessages().cash(), ClientTransaction.TYPE_CASH_EXPENSE);

		} else if (itemName.equalsIgnoreCase(getMessages().deposits())) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new DepositsTransfersListView(
					0);

		} else if (itemName.equalsIgnoreCase(getMessages()
				.inventoryAdjustments())) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new StockAdjustmentsListView();

		} else if (itemName.equalsIgnoreCase(getMessages().journalEntries())) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new JournalEntryListView();

		} else if (itemName.equalsIgnoreCase(getMessages().transferFunds())) {
			transactionType = 1;
			baseListView = (TransactionsListView<T>) new DepositsTransfersListView(
					1);

		} else if (itemName.equalsIgnoreCase(getMessages().payments())) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new PaymentListView();
		} else if (itemName.equalsIgnoreCase(messages.otherChecks())) {
			transactionType = 0;
			baseListView = (TransactionsListView<T>) new PaymentListView(
					PaymentListView.TYPE_WRITE_CHECKS);

		}

		mainPanel.add(baseListView);
		mainPanel.setCellWidth(baseListView, "100%");
		MainFinanceWindow.getViewManager().updateButtons();
		baseListView.init();
		baseListView.initData();
		baseListView.removeStyleName("abstract_base_view");

		setStartDate(baseListView.getStartDate());
		setEndDate(baseListView.getEndDate());
	}

	private List<String> getVendorCenterItems() {
		List<String> vendorItems = new ArrayList<String>();
		vendorItems.add(getMessages().billCredits());
		vendorItems.add(getMessages().billPayments());
		if (getPreferences().isKeepTrackofBills()) {
			vendorItems.add(getMessages().bills());
		}
		vendorItems.add(getMessages().vendorchecks());
		vendorItems.add(getMessages().creditCardExpenses());
		vendorItems.add(getMessages().cashExpenses());
		return vendorItems;
	}

	private AccounterMessages getMessages() {
		return messages;
	}

	private List<String> getCustomerCenterItems() {
		List<String> customerItems = new ArrayList<String>();
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			customerItems.add(getMessages().invoices());
			customerItems.add(getMessages().customerchecks());
			customerItems.add(getMessages().customerCreditNotes());
			if (getPreferences().isDoyouwantEstimates()) {
				customerItems.add(getMessages().quotes());
			}

			if (getPreferences().isDelayedchargesEnabled()) {
				customerItems.add(getMessages().Charges());
				customerItems.add(getMessages().credits());
			}
			customerItems.add(getMessages().cashSales());
		}

		if (Accounter.getUser().canSeeBanking()) {
			customerItems.add(getMessages().receivedPayments());
			customerItems.add(getMessages().payeePayments(
					Global.get().Customers()));
			customerItems.add(getMessages().customerRefunds(
					Global.get().Customer()));
		}
		return customerItems;
	}

	private List<String> getOtherCenterItems() {
		List<String> otherItems = new ArrayList<String>();
		// otherItems.add(getMessages().deposits());
		if (getCompany().getPreferences().isInventoryEnabled()
				&& getCompany().getPreferences().iswareHouseEnabled()) {
			otherItems.add(getMessages().inventoryAdjustments());
		}
		otherItems.add(getMessages().journalEntries());
		otherItems.add(getMessages().otherChecks());
		// otherItems.add(getMessages().transferFunds());
		return otherItems;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
	}

	@Override
	protected String getViewTitle() {
		return messages.transactionscenter();
	}

	@Override
	public void setFocus() {
	}

	@Override
	public boolean canPrint() {
		// if (baseListView instanceof IPrintableView) {
		// return ((IPrintableView) baseListView).canPrint();
		// }
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		int viewId = 0;
		if (!selectedItem
				.equalsIgnoreCase(getMessages().inventoryAdjustments())) {
			viewId = checkViewType(baseListView.getViewType());
		}

		Accounter.createExportCSVService().getExportListCsv(
				getStartDate().getDate(), getEndDate().getDate(),
				transactionType, viewId, selectedItem,
				baseListView.getExportCSVCallback(selectedItem));

	}

	@Override
	public String saveView() {
		return selectedItem;
	}

	@Override
	public void restoreView(String viewDate) {
		if (viewDate != null) {
			initGridData(viewDate);
			listPanel.setMenuSelected(viewDate);
		}
	}

	private int checkViewType(String view) {
		int id = 0;
		if (view.equalsIgnoreCase(messages.open())) {
			id = VIEW_OPEN;
		} else if (view.equalsIgnoreCase(messages.voided())) {
			id = VIEW_VOIDED;
		} else if (view.equalsIgnoreCase(messages.overDue())) {
			id = VIEW_OVERDUE;
		} else if (view.equalsIgnoreCase(messages.all())) {
			id = VIEW_ALL;
		} else if (view.equalsIgnoreCase(messages.drafts())) {
			id = VIEW_DRAFT;
		}

		return id;
	}

	// @Override
	// public void onSearch(String searchString) {
	// this.searchString = searchString;
	// }
	//
	// @Override
	// public void setSelectionType(String selectedType) {
	// this.selectedType = selectedType;
	// }
	//
	// private String getSelectedSearchType() {
	// return selectedType;
	// }
	//
	// private String getSearchString() {
	// return searchString;
	// }

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}
}
