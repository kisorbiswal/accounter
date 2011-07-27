package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class AddMeasurementView extends BaseView {

	private TextItem nameItem, description;
	private SelectCombo defaultItem;
	private AddUnitsGrid addUnitsGrid;
	private DynamicForm addMeasurmentForm, defaultForm;
	private SettingsMessages settingsMessages = GWT
			.create(SettingsMessages.class);
	private Measurement measurment;
	private List defaultList;
	private AddUnitsListGridData addUnitsListGridData;

	public AddMeasurementView() {
		// init();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		createControls();

	}

	private void createControls() {
		VerticalPanel panel = new VerticalPanel();
		defaultList = new ArrayList();
		panel.setSpacing(10);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		addMeasurmentForm = new DynamicForm();
		defaultForm = new DynamicForm();
		initGrid();
		nameItem = new TextItem(settingsMessages.getMeasurmentName());
		nameItem.setRequired(true);
		description = new TextItem(settingsMessages.getMeasurmentDescription());
		AccounterButton addUnitButton = new AccounterButton();
		addUnitButton.setText(settingsMessages.getAddUnitButton());
		addUnitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Unit unitData = new Unit();
				addUnitsGrid.setDisabled(false);
				addUnitsGrid.addData(unitData);
			}
		});
		defaultItem = new SelectCombo(settingsMessages.getdefaultUnit());
		defaultItem.setDisabled(false);
		defaultItem.setTitleStyleName(settingsMessages.getdefaultUnit());
		// AccounterButton saveButton = new AccounterButton();
		// saveButton.setText(settingsMessages.getAddMesurementSaveButton());
		// saveButton.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// }
		// });
		addMeasurmentForm.setFields(nameItem, description);
		// AccounterButton cancelButton = new AccounterButton();
		// cancelButton.setText(settingsMessages.getCancelButton());
		panel.add(addMeasurmentForm);
		panel.add(addUnitsGrid);
		horizontalPanel.setCellHorizontalAlignment(addUnitButton, ALIGN_RIGHT);
		panel.add(addUnitButton);
		defaultForm.setFields(defaultItem);
		defaultItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (defaultItem.getSelectedValue() != null)
							defaultItem.setComboItem(defaultItem
									.getSelectedValue());
					}
				});
		defaultItem.setRequired(true);
		defaultItem.setDisabled(isEdit);
		panel.add(defaultForm);
		// horizontalPanel.add(saveButton);
		// horizontalPanel.setSpacing(20);
		// horizontalPanel.add(cancelButton);
		// panel.add(horizontalPanel);
		buttonLayout.setVisible(true);
		mainPanel.add(panel);
	}

	private void initGrid() {
		addUnitsGrid = new AddUnitsGrid(false);
		addUnitsGrid.setDisabled(true);
		addUnitsGrid.setCanEdit(true);
		addUnitsGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		addUnitsGrid.init();
		addUnitsGrid.setView(this);
	}

	public String getName() {
		return nameItem.getValueField();
	}

	public String getDescription() {
		return description.getValueField();
	}

	public void setDefaultComboValue(Unit unitData) {
		defaultList.add(unitData);
		if (((Unit) (defaultList.get(0))).getType().equals(unitData.getType())) {
			defaultItem.setComboItem(unitData.getType());
			return;
		}
		defaultItem.addComboItem(unitData.getType());
	}

	private void addSelectedItemsToList() {
		measurment = new Measurement();
		measurment.setName(nameItem.getValue().toString());
		measurment.setDesctiption(description.getValue().toString());
		for (Object iterable_element : defaultList) {
			Unit unit = (Unit) iterable_element;
			measurment.addUnit(unit.getType(), unit.getFactor());
		}
	}

	@Override
	public boolean validate() throws Exception {
		if (nameItem.getValue().toString() == null
				|| nameItem.getValue().toString().isEmpty()) {
			MainFinanceWindow.getViewManager().showError(
					"Please Enter a Valid Measurement Name");
			return false;
		}
		List<Unit> unitRecords = addUnitsGrid.getRecords();
		if (unitRecords.size() == 0) {
			MainFinanceWindow.getViewManager().showError(
					"Units Must not be Null");
			return false;
		}
		// List<Unit> unitRecords = addUnitsGrid.getRecords();
		// if (unitRecords != null) {
		// for (Unit unit : unitRecords) {
		// if (unit.getType().toString().isEmpty()) {
		// MainFinanceWindow.getViewManager().showError(
		// "Units Must not be Null");
		// return false;
		// }
		// }
		// } else {
		// MainFinanceWindow.getViewManager().showError(
		// "Units Must not be Null");
		// return false;
		// }
		// }
		return true;
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		addSelectedItemsToList();
		super.saveAndUpdateView();
		MainFinanceWindow.getViewManager().createObject(measurment, this);
	}
}
