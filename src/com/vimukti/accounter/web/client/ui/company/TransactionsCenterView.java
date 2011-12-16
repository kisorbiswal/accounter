package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.options.TreeListPanel;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundListView;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;
import com.vimukti.accounter.web.client.ui.customers.QuoteListView;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentListView;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentsListView;
import com.vimukti.accounter.web.client.ui.vendors.BillListView;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesListView;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListView;

public class TransactionsCenterView<T> extends AbstractBaseView<T> {

	AccounterMessages messages = Accounter.messages();

	BaseListView<T> baseListView;
	private HorizontalPanel mainPanel;

	// private String selectedType;
	//
	// private String searchString;

	public TransactionsCenterView() {
		mainPanel = new HorizontalPanel();
		mainPanel.setStyleName("Transactions_center");
		createTreeItems();
		this.add(mainPanel);
		setSize("100%", "100%");
	}

	@SuppressWarnings("unchecked")
	private void createTreeItems() {
		TreeListPanel listPanel = new TreeListPanel() {

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

		listPanel.addMenuPanel(messages.others(), getOtherCenterItems());
		baseListView = (BaseListView<T>) new InvoiceListView();
		initGridData(getMessages().invoices());
	}

	@SuppressWarnings("unchecked")
	protected void initGridData(String itemName) {
		baseListView.clear();
		mainPanel.remove(baseListView);
		// Customer Menu
		if (itemName.equalsIgnoreCase(messages.customerchecks())) {

			baseListView = (BaseListView<T>) new PaymentListView(
					PaymentListView.TYPE_CUSTOMER_CHECKS);

		} else if (itemName.equalsIgnoreCase(getMessages().cashSalesList())) {

			baseListView = (BaseListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_CASH_SALES);

		} else if (itemName.equalsIgnoreCase(getMessages()
				.customerCreditNotes())) {

			baseListView = (BaseListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

		} else if (itemName.equalsIgnoreCase(getMessages().quotes())) {

			baseListView = (BaseListView<T>) new QuoteListView(
					ClientEstimate.QUOTES);

		} else if (itemName.equalsIgnoreCase(getMessages().Charges())) {

			baseListView = (BaseListView<T>) new QuoteListView(
					ClientEstimate.CHARGES);

		} else if (itemName.equalsIgnoreCase(getMessages().credits())) {

			baseListView = (BaseListView<T>) new QuoteListView(
					ClientEstimate.CREDITS);

		} else if (itemName.equalsIgnoreCase(getMessages().invoices())) {

			baseListView = (BaseListView<T>) new InvoiceListView(
					ClientTransaction.TYPE_INVOICE);

		} else if (itemName.equalsIgnoreCase(getMessages().receivedPayments())) {

			baseListView = (BaseListView<T>) new ReceivedPaymentListView(
					ClientTransaction.TYPE_RECEIVE_PAYMENT);

		} else if (itemName.equals(getMessages().payeePayments(
				Global.get().Customers()))) {

			baseListView = (BaseListView<T>) new ReceivedPaymentListView(
					ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);

		} else if (itemName.equalsIgnoreCase(getMessages().customerRefunds(
				Global.get().Customer()))) {

			baseListView = (BaseListView<T>) new CustomerRefundListView(
					ClientTransaction.TYPE_CUSTOMER_REFUNDS);

		} else if (itemName.equalsIgnoreCase(getMessages().billCredits())) {

			baseListView = (BaseListView<T>) new BillListView(getMessages()
					.all(), ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);

		} else if (itemName.equalsIgnoreCase(getMessages().billPayments())) {

			baseListView = (BaseListView<T>) new VendorPaymentsListView(
					ClientTransaction.TYPE_VENDOR_PAYMENT);

		} else if (itemName.equalsIgnoreCase(getMessages().bills())) {

			baseListView = (BaseListView<T>) new BillListView(getMessages()
					.all());

		} else if (itemName.equalsIgnoreCase(messages.vendorchecks())) {

			baseListView = (BaseListView<T>) new PaymentListView(
					PaymentListView.TYPE_VENDOR_CHECKS);

		} else if (itemName
				.equalsIgnoreCase(getMessages().creditCardExpenses())) {

			baseListView = (BaseListView<T>) new ExpensesListView(getMessages()
					.creditCard());

		} else if (itemName.equalsIgnoreCase(getMessages().cashExpenses())) {

			baseListView = (BaseListView<T>) new ExpensesListView(getMessages()
					.cash());

		} else if (itemName.equalsIgnoreCase(getMessages().deposits())) {
			// TODO
		} else if (itemName.equalsIgnoreCase(getMessages()
				.inventoryAdjustments())) {

			baseListView = (BaseListView<T>) new StockAdjustmentsListView();

		} else if (itemName.equalsIgnoreCase(getMessages().journalEntries())) {

			baseListView = (BaseListView<T>) new JournalEntryListView();

		} else if (itemName.equalsIgnoreCase(getMessages().transferFunds())) {
			// TODO
		}
		mainPanel.add(baseListView);
		baseListView.init();
		baseListView.initData();
		baseListView.removeStyleName("abstract_base_view");
	}

	private List<String> getVendorCenterItems() {
		List<String> vendorItems = new ArrayList<String>();
		vendorItems.add(getMessages().billCredits());
		vendorItems.add(getMessages().billPayments());
		vendorItems.add(getMessages().bills());
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
			customerItems.add(getMessages().cashSalesList());
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
		otherItems.add(getMessages().inventoryAdjustments());
		otherItems.add(getMessages().journalEntries());
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
		return "Transactions Center";
	}

	@Override
	public void setFocus() {
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
}
