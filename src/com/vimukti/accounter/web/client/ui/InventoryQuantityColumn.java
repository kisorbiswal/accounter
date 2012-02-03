package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class InventoryQuantityColumn extends
		TextEditColumn<ClientInventoryAssemblyItem> {

	PopupPanel popup = new PopupPanel(true);

	@Override
	public int getWidth() {
		return 80;
	}

	protected String getValue(ClientInventoryAssemblyItem row) {

		ClientItem item = null;
		if (row.getInventoryItem() != null) {
			item = Accounter.getCompany().getItem(
					row.getInventoryItem().getID());
		}
		if (item == null) {
			ClientQuantity quantity = row.getQuantity();
			return quantity != null ? String.valueOf(quantity.getValue()) : "";
		} else {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART) {
				ClientUnit unit = Accounter.getCompany().getUnitById(
						row.getQuantity().getUnit());
				StringBuffer data = new StringBuffer();
				data.append(String.valueOf(row.getQuantity().getValue()));
				data.append(" ");
				if (unit != null) {
					data.append(unit.getType());
				}
				if (getPreferences().iswareHouseEnabled()) {
					data.append(" (W: ");
					ClientWarehouse warehouse = Accounter.getCompany()
							.getWarehouse(row.getWarehouse());
					if (warehouse != null)
						data.append(warehouse.getWarehouseCode());
					data.append(")");
				}

				return data.toString();
			} else {
				ClientQuantity value = row.getQuantity();
				if (value != null)
					return String.valueOf(value.getValue());
				else
					return "";
			}
		}
	}

	@Override
	public IsWidget getWidget(RenderContext<ClientInventoryAssemblyItem> context) {
		final IsWidget widget = super.getWidget(context);
		final ClientInventoryAssemblyItem row = context.getRow();
		if (widget instanceof TextBox) {
			((TextBox) widget).addFocusListener(new FocusListener() {

				@Override
				public void onLostFocus(Widget sender) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFocus(Widget sender) {
					ClientItem item = Accounter.getCompany().getItem(
							row.getInventoryItem().getID());
					if (item != null
							&& item.getType() == ClientItem.TYPE_INVENTORY_PART) {
						showPopUp(row);
						((TextBox) widget).setFocus(false);
					}
				}
			});
		}
		return widget;
	}

	private void showPopUp(final ClientInventoryAssemblyItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		FlexTable table = new FlexTable();
		Label valueLabel = new Label(messages.value());
		Label unitLabel = new Label(messages.unit());
		Label wareHouseLabel = new Label(messages.wareHouse());

		ClientUnit unit = Accounter.getCompany().getUnitById(
				row.getQuantity().getUnit());
		ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
				row.getWarehouse());
		ClientItem item = Accounter.getCompany().getItem(
				row.getInventoryItem().getID());
		final TextBox valueBox = new TextBox();
		valueBox.setFocus(true);
		valueBox.setText(String.valueOf(row.getQuantity().getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
		}
		unitBox.setSelected(unit.getName());
		unitBox.setShowTitle(false);
		final WarehouseCombo whCombo = new WarehouseCombo("");
		whCombo.setComboItem(wareHouse);
		whCombo.setShowTitle(false);
		whCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientWarehouse>() {

			@Override
			public void selectedComboBoxItem(ClientWarehouse selectItem) {
				if (!popup.isShowing()) {
					if (selectItem != null) {
						row.setWareHouse(selectItem.getID());
						getTable().update(row);
					}
				}
			}
		});

		table.setWidget(0, 0, valueLabel);
		table.setWidget(1, 0, valueBox);
		table.setWidget(0, 1, unitLabel);
		table.setWidget(1, 1, unitBox.getMainWidget());
		table.getCellFormatter().addStyleName(1, 1, "quantity_unit_width");
		if (getPreferences().iswareHouseEnabled()) {
			table.setWidget(0, 2, wareHouseLabel);
			table.setWidget(1, 2, whCombo.getMainWidget());
		}
		popup.add(table);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {

				String value = valueBox.getValue();
				ClientUnit unit = unitBox.getSelectedValue();
				if (value.isEmpty()) {
					value = "1";
				}
				ClientQuantity quantity = row.getQuantity();
				try {
					quantity.setValue(DataUtils.getAmountStringAsDouble(value));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (unit != null)
					quantity.setUnit(unit.getId());
				setQuantity(row, quantity);
				if (whCombo.getSelectedValue() != null)
					row.setWareHouse(whCombo.getSelectedValue().getID());
				getTable().update(row);
			}
		});
		Widget widget = getTable().getWidget(row, this);
		popup.setAutoHideEnabled(true);
		popup.addStyleName("quantityPopup");
		popup.showRelativeTo(widget);
	}

	public void setQuantity(ClientInventoryAssemblyItem row,
			ClientQuantity quantity) {
		if (quantity != null) {
			row.setQuantity(quantity);
			double lt = quantity.getValue() * row.getUnitPrice();
			row.setLineTotal(lt);
			getTable().update(row);
		}
	}

	@Override
	protected String getColumnName() {
		return messages.quantity();
	}

	@Override
	protected void setValue(ClientInventoryAssemblyItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientItem item = Accounter.getCompany().getItem(
					row.getInventoryItem().getID());
			if (item != null
					&& item.getType() == ClientItem.TYPE_INVENTORY_PART) {
				return;
			}
			ClientQuantity quantity = row.getQuantity();
			if (quantity != null) {
				quantity.setValue(DataUtils.getAmountStringAsDouble(JNSI
						.getCalcultedAmount(value)));
			}
			setQuantity(row, quantity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
