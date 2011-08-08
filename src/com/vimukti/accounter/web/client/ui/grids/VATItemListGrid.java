package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordDoubleClickHandler;
import com.vimukti.accounter.web.client.ui.vat.VATGroupView;

public class VATItemListGrid extends ListGrid<ClientTAXItem> {

	private VATItemCombo vatitemCombo;
	private RecordDoubleClickHandler<ClientTAXItem> doubleClickHandler;
	private double groupRate = 0.0;
	private VATGroupView view;

	public VATItemListGrid() {
		super(false, true);

	}

	@Override
	public ValidationResult validateGrid() {

		for (ClientTAXItem item : objects) {
			Object value = this.getColumnValue(item, 0);
			if (value == null || value == "") {
				new ValidationResult().addError(
						0 + ',' + objects.indexOf(item), Accounter.constants()
								.pleaseselectVATIteminTransGrid());
			}
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().vatItem(),
				Accounter.constants().rate(),
				Accounter.constants().vatAgency(),
				Accounter.constants().description(), " " };
	}

	@Override
	public void init() {
		super.init();
		// addFooterValues("", FinanceApplication.constants().groupRate()
		// + groupRate + "%");
		createControls();
	}

	private void createControls() {

		vatitemCombo = new VATItemCombo("");

		filterVATItems(view.salesTypeRadio.getDefaultValue());

		vatitemCombo.setGrid(this);
		vatitemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem vitem) {
						if (vitem != null) {
							selectedObject.setDescription(vitem
									.getDescription());
							selectedObject.setID(vitem.getID());
							selectedObject.setName(vitem.getName());
							selectedObject.setTaxAgency(vitem.getTaxAgency());
							selectedObject.setTaxRate(vitem.getTaxRate());
							selectedObject.setTaxAgency(vitem.getTaxAgency());
							selectedObject.setTaxRate(vitem.getTaxRate());
							updateGroupRate();
						}

					}

				});

		addRecordClickHandler(new RecordClickHandler<ClientTAXItem>() {

			public boolean onRecordClick(ClientTAXItem core, int column) {

				switch (column) {
				case 0:
					if (core.getName() != null)
						vatitemCombo.setComboItem(core);
					else
						vatitemCombo.setValue("");
				}

				return true;
			}
		});

		doubleClickHandler = new RecordDoubleClickHandler<ClientTAXItem>() {

			@Override
			public void OnCellDoubleClick(ClientTAXItem core, int column) {
				if (column == 0)
					vatitemCombo.setComboItem(core);

			}

		};

	}

	public void updateGroupRate() {

		List<ClientTAXItem> vatItems = getRecords();
		groupRate = 0.0;
		for (ClientTAXItem vatItem : vatItems) {
			groupRate += vatItem.getTaxRate();
		}
		// updateFooterValues(FinanceApplication.constants().groupRate()
		// + groupRate + "%", 1);
		updateData(selectedObject);

	}

	@Override
	protected int getColumnType(int index) {

		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;

		default:
			break;
		}
		return -1;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 4)
			return 15;
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientTAXItem obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return obj.getTaxRate() + " %";
		case 2:
			return obj.getTaxAgency() != 0 ? Accounter.getCompany()
					.getTaxAgency(obj.getTaxAgency()).getName() : "";

		case 3:
			return obj.getDescription();
		case 4:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";

		default:
			break;
		}
		return "";

	}

	@Override
	protected String[] getSelectValues(ClientTAXItem obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientTAXItem obj, int row, int index) {
		if (index == 0)
			return true;
		else
			return false;
	}

	@Override
	protected void onClick(ClientTAXItem obj, int row, int index) {

		if (index == 4) {
			deleteRecord(obj);
			return;
		}
		if (recordClickHandler != null) {
			recordClickHandler.onRecordClick(obj, index);
		}
	}

	@Override
	public void onDoubleClick(ClientTAXItem obj) {

	}

	@Override
	protected void onValueChange(ClientTAXItem obj, int index, Object value) {

		updateGroupRate();
	}

	@Override
	protected int sort(ClientTAXItem obj1, ClientTAXItem obj2, int index) {
		return 0;
	}

	@Override
	protected void addOrEditSelectBox(ClientTAXItem obj, Object value) {
		VATItemCombo box = vatitemCombo;
		if (box != null) {
			Widget widget = box.getMainWidget();
			this.widgetsMap.put(currentCol, widget);
			this.setWidget(currentRow, currentCol, widget);
		} else
			super.addOrEditSelectBox(obj, value);
	}

	protected void onDoubleClick(ClientTAXItem obj, int row, int col) {
		if (this.doubleClickHandler != null)
			this.doubleClickHandler.OnCellDoubleClick(obj, col);
		super.onDoubleClick(obj, row, col);
	}

	@Override
	public void editComplete(ClientTAXItem item, Object value, int col) {
		super.editComplete(item, value, col);
	}

	public void filterVATItems(String value) {
		if (value != null
				&& value.equalsIgnoreCase(Accounter.constants().purchaseType())) {
			vatitemCombo.initCombo(vatitemCombo.getPurchaseWithPrcntVATItems());
		} else {
			vatitemCombo.initCombo(vatitemCombo.getSalesWithPrcntVATItems());
		}

	}

	public void setView(VATGroupView view) {
		this.view = view;
	}

}
