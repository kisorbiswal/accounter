package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.tables.StockAdjustmentTable;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class StockAdjustmentView extends BaseView<ClientStockAdjustment>
		implements ICurrencyProvider {

	private VerticalPanel mainPanel;
	private WarehouseCombo wareHouseCombo;
	private StockAdjustmentTable table;
	private AddNewButton addButton;
	private DynamicForm form;
	private List<DynamicForm> listForms;
	private ClientWarehouse wareHouse;

	AccountCombo adjustmentAccountCombo;

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		listForms = new ArrayList<DynamicForm>();

		wareHouseCombo = new WarehouseCombo(messages.wareHouse());
		wareHouseCombo.setDisabled(!getCompany().getPreferences()
				.iswareHouseEnabled() || isInViewMode());
		wareHouseCombo.setRequired(true);
		wareHouse = getCompany().getWarehouse(
				getCompany().getDefaultWarehouse());
		wareHouseCombo.setComboItem(wareHouse);
		wareHouseCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientWarehouse>() {

					@Override
					public void selectedComboBoxItem(ClientWarehouse selectItem) {
						wareHouseSelected(selectItem);
					}
				});

		adjustmentAccountCombo = new AccountCombo(messages.adjustmentAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getActiveAccounts();
			}
		};
		adjustmentAccountCombo.setRequired(true);
		adjustmentAccountCombo.setDisabled(isInViewMode());
		table = new StockAdjustmentTable(this) {

			@Override
			protected String getAvailableQuantity(long item) {
				return StockAdjustmentView.this.getAvailableQuantity(item);
			}

			@Override
			protected boolean isInViewMode() {
				return StockAdjustmentView.this.isInViewMode();
			}

			@Override
			protected ClientWarehouse getSelectedWareHouse() {
				ClientWarehouse selectedValue = wareHouseCombo
						.getSelectedValue();
				if (selectedValue == null) {
					selectedValue = getCompany().getWarehouse(
							getCompany().getDefaultWarehouse());
					wareHouseCombo.setComboItem(selectedValue);
				}
				return selectedValue;
			}

		};
		table.setDisabled(isInViewMode());

		addButton = new AddNewButton();
		addButton.setEnabled(!isInViewMode());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				table.add(new ClientTransactionItem());
			}
		});

		form = new DynamicForm();
		form.setFields(wareHouseCombo, adjustmentAccountCombo);
		listForms.add(form);

		mainPanel.add(form);
		mainPanel.add(table);
		mainPanel.add(addButton);
		mainPanel.setSize("100%", "100%");

		this.add(mainPanel);
	}

	protected void wareHouseSelected(ClientWarehouse selectItem) {
		if (wareHouse.getID() == selectItem.getID()) {
			return;
		}
		wareHouse = selectItem;
		table.refresh();
	}

	protected String getAvailableQuantity(long itemId) {
		StringBuffer result = new StringBuffer();
		ClientItem item = getCompany().getItem(itemId);
		if (item != null) {
			ClientWarehouse warehouse = wareHouseCombo.getSelectedValue();
			if (warehouse != null) {
				ClientItemStatus itemStatus = null;
				if (warehouse.getItemStatuses() != null) {
					for (ClientItemStatus is : warehouse.getItemStatuses()) {
						if (is.getItem() == itemId) {
							itemStatus = is;
							break;
						}
					}
				}
				if (itemStatus != null) {
					ClientUnit unit = getCompany().getUnitById(
							itemStatus.getQuantity().getUnit());
					result.append(itemStatus.getQuantity().getValue());
					result.append(" ");
					result.append(unit.getName());
				} else {
					ClientMeasurement measurement = getCompany()
							.getMeasurement(item.getMeasurement());
					result.append(0.0);
					result.append(" ");
					if (measurement != null) {
						result.append(measurement.getDefaultUnit().getName());
					}
				}
			}
		}
		return result.toString();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.stockAdjustment();
	}

	@Override
	public List<DynamicForm> getForms() {
		return listForms;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(data);
	}

	private void updateData() {
		if (wareHouseCombo.getSelectedValue() != null) {
			data.setWareHouse(wareHouseCombo.getSelectedValue().getID());
		}
		data.setTransactionItems(getStockAdjustmentItems());
		if (adjustmentAccountCombo.getSelectedValue() != null) {
			data.setAdjustmentAccount(adjustmentAccountCombo.getSelectedValue()
					.getID());
		}
		data.setTransactionDate(new ClientFinanceDate().getDate());
	}

	@Override
	public void initData() {
		getAssetValuesForItems();
		if (data == null) {
			setData(new ClientStockAdjustment());
			table.addEmptyMessage(messages.noRecordsToShow());
		} else {
			ClientWarehouse warehouse = getCompany().getWarehouse(
					data.getWareHouse());
			wareHouseCombo.setComboItem(warehouse);
			List<ClientTransactionItem> stockAdjustmentItems = data
					.getTransactionItems();
			if (stockAdjustmentItems != null && !stockAdjustmentItems.isEmpty()) {
				table.setAllRows(stockAdjustmentItems);
			} else {
				table.addEmptyMessage(messages.noRecordsToShow());
			}
			adjustmentAccountCombo.select(getCompany().getAccount(
					data.getAdjustmentAccount()));
		}
	}

	private void getAssetValuesForItems() {

		AccounterAsyncCallback<Map<Long, Double>> callback = new AccounterAsyncCallback<Map<Long, Double>>() {

			@Override
			public void onException(AccounterException exception) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResultSuccess(Map<Long, Double> result) {
				for (Entry<Long, Double> entry : result.entrySet()) {
					ClientItem item = getCompany().getItem(entry.getKey());
					item.setAssetValue(entry.getValue());
				}
			}
		};

		Accounter.createGETService().getAssetValuesForInventories(callback);
	}

	@Override
	public void setFocus() {
		wareHouseCombo.setFocus();
	}

	private List<ClientTransactionItem> getStockAdjustmentItems() {
		List<ClientTransactionItem> rows = table.getAllRows();
		List<ClientTransactionItem> result = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : rows) {
			if (!item.isEmpty()) {
				item.setID(0);
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public void onEdit() {
		enableFormItems();
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		wareHouseCombo.setDisabled(!getCompany().getPreferences()
				.iswareHouseEnabled() || isInViewMode());
		table.setDisabled(isInViewMode());
		table.clear();
		table.setAllRows(data.getTransactionItems());
		addButton.setEnabled(!isInViewMode());
		adjustmentAccountCombo.setDisabled(isInViewMode());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		List<ClientTransactionItem> allRows = table.getAllRows();
		if (allRows.isEmpty()) {
			result.addError(this, messages.pleaseSelectInventoryItemToSave());
		}
		for (ClientTransactionItem item : allRows) {
			if (item.getQuantity().getUnit() <= 0) {
				result.addError(this,
						messages.pleaseSelectUnitForInventoryItem());
			}
		}
		result.add(table.validateGrid());
		result.add(form.validate());
		return result;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	public Double getAmountInBaseCurrency(Double amount) {
		return amount;
	}

	@Override
	public ClientCurrency getTransactionCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	@Override
	public Double getCurrencyFactor() {
		return 1.00D;
	}
}
