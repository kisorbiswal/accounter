package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientStockAdjustmentItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.StockAdjustmentTable;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class StockAdjustmentView extends BaseView<ClientStockAdjustment> {

	private VerticalPanel mainPanel;
	private WarehouseCombo wareHouseCombo;
	private StockAdjustmentTable table;
	private AddNewButton addButton;
	private DynamicForm form;
	private List<DynamicForm> listForms;
	private ClientWarehouse wareHouse;

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		listForms = new ArrayList<DynamicForm>();

		wareHouseCombo = new WarehouseCombo(Accounter.messages().wareHouse());
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

		table = new StockAdjustmentTable() {

			@Override
			protected String getAvailableQuantity(long item) {
				return StockAdjustmentView.this.getAvailableQuantity(item);
			}

			@Override
			protected boolean isInViewMode() {
				return StockAdjustmentView.this.isInViewMode();
			}
		};
		table.setDisabled(isInViewMode());

		addButton = new AddNewButton();
		addButton.setEnabled(!isInViewMode());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				table.add(new ClientStockAdjustmentItem());
			}
		});

		form = new DynamicForm();
		form.setFields(wareHouseCombo);
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
				for (ClientItemStatus is : warehouse.getItemStatuses()) {
					if (is.getItem() == itemId) {
						itemStatus = is;
						break;
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
		return Accounter.messages().stockAdjustment();
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
		data.setStockAdjustmentItems(getStockAdjustmentItems());
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientStockAdjustment());
			table.addEmptyMessage(messages.noRecordsToShow());
		} else {
			ClientWarehouse warehouse = getCompany().getWarehouse(
					data.getWareHouse());
			wareHouseCombo.setComboItem(warehouse);
			List<ClientStockAdjustmentItem> stockAdjustmentItems = data
					.getStockAdjustmentItems();
			if (!stockAdjustmentItems.isEmpty()) {
				table.setAllRows(stockAdjustmentItems);
			} else {
				table.addEmptyMessage(messages.noRecordsToShow());
			}
		}
	}

	@Override
	public void setFocus() {
		wareHouseCombo.setFocus();
	}

	private List<ClientStockAdjustmentItem> getStockAdjustmentItems() {
		List<ClientStockAdjustmentItem> rows = table.getAllRows();
		List<ClientStockAdjustmentItem> result = new ArrayList<ClientStockAdjustmentItem>();
		for (ClientStockAdjustmentItem item : rows) {
			if (!item.isEmpty()) {
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
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (table.getAllRows().isEmpty()) {
			result.addError(this, messages.noInventoryItemsAvilable());
		}
		for (ClientStockAdjustmentItem item : table.getAllRows()) {
			if (item.getAdjustmentQty().getValue() <= 0
					|| item.getAdjustmentQty().getUnit() <= 0) {
				result.addError(this, messages.noRecordsSelected());
			}
		}
		return result;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
