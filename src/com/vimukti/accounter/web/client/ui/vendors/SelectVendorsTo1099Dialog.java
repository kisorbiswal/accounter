package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class SelectVendorsTo1099Dialog extends BaseDialog {

	protected DialogGrid availVendorsGrid;
	protected DialogGrid selectVendorsGrid;
	protected Button addButton, removeButton;

	private ArrayList<ClientVendor> tempAvailVendorsList;
	private ArrayList<ClientVendor> tempSelectedVendorsList;
	public DynamicForm form1;
	private static boolean flag = false;

	public SelectVendorsTo1099Dialog(String title, String desc) {
		super(title, desc);
		createControls();

		fillSelectedVendors();
		fillAvailableVendors();
		mainPanel.setSpacing(3);
		center();
	}

	private void fillSelectedVendors() {
		for (ClientVendor codeInternal : getSelectedVendors()) {
			selectVendorsGrid.addData(codeInternal);
		}
	}

	private ArrayList<ClientVendor> getSelectedVendors() {
		ArrayList<ClientVendor> vendors = getCompany().getVendors();
		tempSelectedVendorsList = new ArrayList<ClientVendor>();

		tempSelectedVendorsList.addAll(Utility
				.filteredList(new ListFilter<ClientVendor>() {

					@Override
					public boolean filter(ClientVendor e) {
						return e.isTrackPaymentsFor1099();
					}
				},vendors));

		return tempSelectedVendorsList;
	}

	private void fillAvailableVendors() {
		for (ClientVendor vendor : getAvailableVendors())
			availVendorsGrid.addData(vendor);
	}

	private ArrayList<ClientVendor> getAllVendors() {
		return getCompany().getVendors();
	}

	private ArrayList<ClientVendor> getAvailableVendors() {
		boolean isFound = false;
		tempAvailVendorsList = new ArrayList<ClientVendor>();
		for (ClientVendor codeInternal : getAllVendors()) {
			if (tempSelectedVendorsList != null)
				for (ClientVendor internal : tempSelectedVendorsList) {
					if (codeInternal.getID() == internal.getID()) {
						isFound = true;
						break;
					} else
						isFound = false;
				}
			if (!isFound)
				tempAvailVendorsList.add(codeInternal);
		}
		return tempAvailVendorsList;

	}

	private void createControls() {
		setWidth("570px");
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setWidth("100%");

		addButton = new Button(Accounter.constants().add());
		addButton.setWidth("80px");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (availVendorsGrid.getSelection() != null) {
					ClientVendor gridRecord = (ClientVendor) availVendorsGrid
							.getSelection();
					selectVendorsGrid.addData(gridRecord);
					tempSelectedVendorsList.add(gridRecord);

					availVendorsGrid.deleteRecord(gridRecord);
					if (tempAvailVendorsList != null)
						tempAvailVendorsList.remove(gridRecord);
					if (availVendorsGrid.getRecords() == null
							|| availVendorsGrid.getRecords().size() == 0
							|| availVendorsGrid.getSelection() == null) {
						addButton.setEnabled(false);
					}

				}
			}
		});
		addButton.setEnabled(false);

		removeButton = new Button(Accounter.constants().remove());
		removeButton.setWidth("80px");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectVendorsGrid.getSelection() != null) {
					ClientVendor gridRecord = (ClientVendor) selectVendorsGrid
							.getSelection();
					selectVendorsGrid.deleteRecord(gridRecord);
					tempSelectedVendorsList.remove(gridRecord);
					int selectedIndex = availVendorsGrid
							.getSelectedRecordIndex();
					availVendorsGrid.addData(gridRecord);
					availVendorsGrid.selectRecord(selectedIndex);
					if (tempAvailVendorsList != null)
						tempAvailVendorsList.add(gridRecord);
					if (selectVendorsGrid.getRecords() == null
							|| selectVendorsGrid.getRecords().size() == 0
							|| selectVendorsGrid.getSelection() == null) {
						removeButton.setEnabled(false);
					}
				}
			}
		});
		removeButton.setEnabled(false);

		availVendorsGrid = new DialogGrid(false);

		availVendorsGrid.setName(Accounter.constants().available());
		setAvailVendorsGridFields();
		setAvalableVendorsGridCellWidths();
		availVendorsGrid.setView(SelectVendorsTo1099Dialog.this);
		availVendorsGrid.addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				addButton.setEnabled(true);
				return true;
			}
		});
		availVendorsGrid.init();

		// Buttons Layout
		VerticalPanel buttonsLayout = new VerticalPanel();
		buttonsLayout.setWidth("100px");
		buttonsLayout.setSpacing(3);

		buttonsLayout.add(addButton);
		buttonsLayout.add(removeButton);

		selectVendorsGrid = new DialogGrid(false);
		selectVendorsGrid.setName(Accounter.constants().selected());
		setSelectedVendorsGridFields();
		setSelectedVendorGridCellWidths();
		selectVendorsGrid.setView(SelectVendorsTo1099Dialog.this);
		selectVendorsGrid.addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				removeButton.setEnabled(true);
				return true;
			}
		});
		selectVendorsGrid.init();

		mainPanel.setCellHorizontalAlignment(availVendorsGrid,
				HasHorizontalAlignment.ALIGN_LEFT);
		mainPanel.add(availVendorsGrid);
		mainPanel.setCellHorizontalAlignment(buttonsLayout,
				HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonsLayout);
		mainPanel.setCellHorizontalAlignment(selectVendorsGrid,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.add(selectVendorsGrid);

		setBodyLayout(mainPanel);
		center();
	}

	private void setSelectedVendorsGridFields() {
		selectVendorsGrid.setCellsWidth(new Integer[] { 200 });

	}

	private void setSelectedVendorGridCellWidths() {
		selectVendorsGrid.addColumns(new String[] { Accounter.constants()
				.trackPaymentsFor1099() });

	}

	private void setAvalableVendorsGridCellWidths() {
		availVendorsGrid.setCellsWidth(new Integer[] { 200 });

	}

	private void setAvailVendorsGridFields() {
		availVendorsGrid.addColumns(new String[] { Accounter.constants()
				.selectFromList() });

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		if (!flag) {
			flag = true;
			return getAvailableGridItemsColumnValue(obj, index);
		} else {
			flag = false;
			return getSelectedGridItemsColumnValue(obj, index);
		}

	}

	private Object getSelectedGridItemsColumnValue(IsSerializable obj, int index) {
		ClientVendor vendor = (ClientVendor) obj;
		switch (index) {
		case 0:
			return vendor.getName();

		default:
			return null;
		}
	}

	private Object getAvailableGridItemsColumnValue(IsSerializable obj,
			int index) {
		ClientVendor vendor = (ClientVendor) obj;
		switch (index) {
		case 0:
			return vendor.getName();

		default:
			return null;
		}
	}

	@Override
	protected boolean onOK() {
		for (ClientVendor vendor : tempSelectedVendorsList) {
			vendor.setTrackPaymentsFor1099(true);
			saveOrUpdate(vendor);
		}
		for (ClientVendor vendor : tempAvailVendorsList) {
			vendor.setTrackPaymentsFor1099(false);
			saveOrUpdate(vendor);
		}
		return true;
	}

}
