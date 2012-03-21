package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientReminder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.grids.RemindersListGrid;

public class RemindersListView extends BaseListView<ClientReminder> implements
		IPrintableView {

	public static String ALL = messages.all();
	public static String BILL = messages.bill();
	public static String CASH_EXPENSE = messages.cashExpense();
	public static String CASH_SALES = messages.cashSale();
	public static String CASH_PURCHASE = messages.cashPurchase();
	public static String CREDIT_CARD_EXPENSE = messages.creditCardExpense();
	public static String CUSTOMER_CREDIT_MEMO = messages
			.customerCreditNote(Global.get().Customer());
	public static String DEPOSIT_TRANSFER_FUNDS = messages
			.depositTransferFunds();
	public static String INVOICE = messages.invoice();
	public static String QUOTE = messages.quote();
	public static String VENDOR_CREDIT_MEMO = messages.vendorCreditMemo();
	public static String WRITE_CHECK = messages.writeCheck();

	Map<String, Integer> typesMap = new HashMap<String, Integer>();

	Button createButton, skipButton;

	List<ClientReminder> reminders = new ArrayList<ClientReminder>();
	public String viewType;

	public RemindersListView() {
		super();
		this.getElement().setId("RemindersListView");
	}
	
	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getRemindersList(this);
		filterList(viewSelect.getSelectedValue());
	}

	@Override
	public void onSuccess(PaginationList<ClientReminder> result) {
		super.onSuccess(result);
		if (result == null) {
			result = new PaginationList<ClientReminder>();
		}
		reminders = result;
		filterList(viewSelect.getSelectedValue());
		grid.setViewType(viewSelect.getSelectedValue());
	}

	@Override
	public void updateInGrid(ClientReminder objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		super.createControls();

		StyledPanel panel = new StyledPanel("panel");

		createButton = new Button(messages.createSelected());
		createButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createOrSkipTransactions(true);
			}
		});

		skipButton = new Button(messages.skipSelected());
		skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createOrSkipTransactions(false);
			}
		});

		panel.add(createButton);
		panel.add(skipButton);

		add(panel);
	}

	protected void createOrSkipTransactions(boolean isCreate) {
		List<ClientReminder> records = grid.getSelectedRecords();
		if (!records.isEmpty()) {
			if (isCreate) {
				for (ClientReminder reminder : records) {
					if (!reminder.isValid()) {
						Accounter.showError(messages
								.someSelectedRecordsAreNeedToBeEdited());
						return;
					}
				}
			}
			createButton.setEnabled(false);
			skipButton.setEnabled(false);
			AccounterAsyncCallback<Boolean> callBack = new AccounterAsyncCallback<Boolean>() {

				@Override
				public void onException(AccounterException exception) {
					createButton.setEnabled(true);
					skipButton.setEnabled(true);
					Accounter.showError(exception.getMessage());
				}

				@Override
				public void onResultSuccess(Boolean result) {
					createButton.setEnabled(true);
					skipButton.setEnabled(true);
					initListCallback();
				}
			};
			Accounter.createCRUDService().createOrSkipTransactions(records,
					isCreate, callBack);
		} else {
			Accounter.showError(messages.pleaseSelectAtLeastOneRecord());
		}
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		map.put("selectedOption", selectedValue);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		start = (Integer) viewDate.get("start");
		String object = (String) viewDate.get("selectedOption");
		if (object.equalsIgnoreCase(ALL)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(BILL)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CASH_EXPENSE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CASH_PURCHASE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CASH_SALES)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CREDIT_CARD_EXPENSE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CUSTOMER_CREDIT_MEMO)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(DEPOSIT_TRANSFER_FUNDS)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(INVOICE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(QUOTE)) {
			viewSelect.setComboItem(VENDOR_CREDIT_MEMO);
		} else if (object.equalsIgnoreCase(WRITE_CHECK)) {
			viewSelect.setComboItem(object);
		}
	}

	@Override
	protected void initGrid() {
		grid = new RemindersListGrid(true);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.remindersList();
	}

	@Override
	protected Action getAddNewAction() {
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		// No need to create add new label
		return null;
	}

	@Override
	protected String getViewTitle() {
		return messages.remindersList();
	}

	@Override
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		initTypesMap();
		List<String> listOfTypes = new ArrayList<String>(typesMap.keySet());
		viewSelect.initCombo(listOfTypes);
		if (viewType != null && !viewType.equals(""))
			viewSelect.setComboItem(viewType);
		else
			viewSelect.setComboItem(ALL);

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

	protected void filterList(String selectedValue) {
		grid.removeAllRecords();

		if (!selectedValue.equalsIgnoreCase(ALL)) {
			for (ClientReminder reminder : reminders) {
				if (reminder.getRecurringTransaction().getTransaction()
						.getType() == typesMap.get(selectedValue)) {
					grid.addData(reminder);
				}
			}
		} else {
			grid.setRecords(reminders);
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	private void initTypesMap() {
		typesMap.put(BILL, ClientTransaction.TYPE_ENTER_BILL);
		typesMap.put(CASH_EXPENSE, ClientTransaction.TYPE_CASH_EXPENSE);
		typesMap.put(CASH_SALES, ClientTransaction.TYPE_CASH_SALES);
		typesMap.put(CASH_PURCHASE, ClientTransaction.TYPE_CASH_PURCHASE);
		typesMap.put(CREDIT_CARD_EXPENSE,
				ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		typesMap.put(CUSTOMER_CREDIT_MEMO,
				ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		typesMap.put(DEPOSIT_TRANSFER_FUNDS,
				ClientTransaction.TYPE_TRANSFER_FUND);
		typesMap.put(INVOICE, ClientTransaction.TYPE_INVOICE);
		typesMap.put(QUOTE, ClientTransaction.TYPE_ESTIMATE);
		typesMap.put(VENDOR_CREDIT_MEMO,
				ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		typesMap.put(WRITE_CHECK, ClientTransaction.TYPE_WRITE_CHECK);
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getRemindersListExportCsv(
				getExportCSVCallback(messages.remindersList()));
	}
}
