package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientStockAdjustmentItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.edittable.QuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class StockAdjustmentQuantityColumn extends
		QuantityColumn<ClientStockAdjustmentItem> {

	PopupPanel popup = new PopupPanel(true);

	protected ClientQuantity getQuantity(ClientStockAdjustmentItem row) {
		return row.getAdjustmentQty();
	}

	protected void setQuantity(ClientStockAdjustmentItem row, ClientQuantity d) {
		row.setAdjustmentQty(d);
		getTable().update(row);
	}

	@Override
	public IsWidget getWidget(RenderContext<ClientStockAdjustmentItem> context) {
		final IsWidget widget = super.getWidget(context);
		final ClientStockAdjustmentItem row = context.getRow();
		if (widget instanceof TextBox) {
			((TextBox) widget).addFocusListener(new FocusListener() {

				@Override
				public void onLostFocus(Widget sender) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFocus(Widget sender) {
					showPopUp(row);
					((TextBox) widget).setFocus(false);
				}
			});
		}
		return widget;
	}

	private void showPopUp(final ClientStockAdjustmentItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		FlexTable table = new FlexTable();
		Label valueLabel = new Label(messages.value());
		Label unitLabel = new Label(messages.unit());

		ClientUnit unit = Accounter.getCompany().getUnitById(
				row.getAdjustmentQty().getUnit());
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		final TextBox valueBox = new TextBox();
		valueBox.setFocus(true);
		valueBox.setText(String.valueOf(row.getAdjustmentQty().getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		ClientUnit itemUnit = null;
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
			itemUnit = measurement.getDefaultUnit();
		}
		if (unit != null) {
			unitBox.setComboItem(unit);
		} else {
			unitBox.setComboItem(itemUnit);
		}
		unitBox.setShowTitle(false);

		table.setWidget(0, 0, valueLabel);
		table.setWidget(1, 0, valueBox);
		table.setWidget(0, 1, unitLabel);
		table.setWidget(1, 1, unitBox.getMainWidget());
		popup.add(table);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {

				String value = valueBox.getValue();
				ClientUnit unit = unitBox.getSelectedValue();
				if (value.isEmpty()) {
					value = "1";
				}
				ClientQuantity quantity = row.getAdjustmentQty();
				try {
					quantity.setValue(DataUtils.getAmountStringAsDouble(value));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (unit != null)
					quantity.setUnit(unit.getId());
				setQuantity(row, quantity);
				getTable().update(row);
			}
		});
		Widget widget = getTable().getWidget(row,this);
		popup.setAutoHideEnabled(true);
		popup.showRelativeTo(widget);
	}

	protected String getValue(ClientStockAdjustmentItem row) {
		if (row.getAdjustmentQty() == null) {
			return "";
		} else {
			ClientUnit unit = Accounter.getCompany().getUnitById(
					row.getAdjustmentQty().getUnit());
			StringBuffer data = new StringBuffer();
			data.append(String.valueOf(row.getAdjustmentQty().getValue()));
			data.append(" ");
			if (unit != null) {
				data.append(unit.getType());
			}
			return data.toString();
		}
	}

	@Override
	protected void setValue(ClientStockAdjustmentItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientQuantity quantity = row.getAdjustmentQty();
			quantity.setValue(DataUtils.getAmountStringAsDouble(value));
			setQuantity(row, quantity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getColumnName() {
		return messages.adjustmentQty();
	}

	@Override
	public int getWidth() {
		return 200;
	}

}
