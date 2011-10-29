package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class NewQuantityColumn extends TextEditColumn<ClientTransactionItem> {

	PopupPanel popup = new PopupPanel(true);

	@Override
	public int getWidth() {
		return 150;
	}

	protected String getValue(ClientTransactionItem row) {
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		if (item == null) {
			return String.valueOf(row.getQuantity().getValue());
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
				data.append(" (W: ");
				ClientWarehouse warehouse = Accounter.getCompany()
						.getWarehouse(row.getWareHouse());
				if (warehouse != null)
					data.append(warehouse.getWarehouseCode());
				data.append(")");
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
	public IsWidget getWidget(RenderContext<ClientTransactionItem> context) {
		IsWidget widget = super.getWidget(context);
		final ClientTransactionItem row = context.getRow();
		if (widget instanceof TextBox) {
			((TextBox) widget).addFocusListener(new FocusListener() {

				@Override
				public void onLostFocus(Widget sender) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFocus(Widget sender) {
					ClientItem item = Accounter.getCompany().getItem(
							row.getItem());
					if (item != null
							&& item.getType() == ClientItem.TYPE_INVENTORY_PART) {
						showPopUp(row);
					}
				}
			});
		}
		return widget;
	}

	private void showPopUp(final ClientTransactionItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		ClientUnit unit = Accounter.getCompany().getUnitById(
				row.getQuantity().getUnit());
		ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
				row.getWareHouse());
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		HorizontalPanel panel = new HorizontalPanel();
		final TextBox valueBox = new TextBox();
		valueBox.setText(String.valueOf(row.getQuantity().getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
		}
		unitBox.setComboItem(unit);
		unitBox.setShowTitle(false);
		final WarehouseCombo whCombo = new WarehouseCombo("");
		whCombo.setComboItem(wareHouse);
		whCombo.setShowTitle(false);

		panel.add(valueBox);
		panel.add(unitBox.getMainWidget());
		if (getPreferences().iswareHouseEnabled()) {
			panel.add(whCombo.getMainWidget());
		}
		popup.add(panel);
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
		Widget widget = getTable().getTable().getWidget(
				getTable().getAllRows().indexOf(row),
				getTable().getColumns().indexOf(this));
		popup.showRelativeTo(widget);
	}

	protected void setQuantity(ClientTransactionItem row,
			ClientQuantity quantity) {
		row.setQuantity(quantity);
		double lt = quantity.getValue() * row.getUnitPrice();
		double disc = row.getDiscount();
		row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
		getTable().update(row);
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().quantity();
	}

	@Override
	protected void setValue(ClientTransactionItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientQuantity quantity = row.getQuantity();
			quantity.setValue(DataUtils.getAmountStringAsDouble(value));
			setQuantity(row, quantity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
