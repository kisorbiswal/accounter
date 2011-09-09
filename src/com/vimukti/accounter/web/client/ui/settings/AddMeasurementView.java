package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class AddMeasurementView extends BaseView<ClientMeasurement> {

	private TextItem nameItem, description;
	private SelectCombo defaultItem;
	private AddUnitsGrid addUnitsGrid;
	private DynamicForm addMeasurmentForm, defaultForm;
	private AccounterConstants settingsMessages = Accounter.constants();
	// private ClientMeasurement measurment;
	private List<ClientUnit> defaultList;

	// private AddUnitsListGridData addUnitsListGridData;

	public AddMeasurementView() {
		// init();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
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
		defaultList = new ArrayList<ClientUnit>();
		panel.setSpacing(10);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		addMeasurmentForm = new DynamicForm();
		defaultForm = new DynamicForm();
		initGrid();
		nameItem = new TextItem(settingsMessages.measurementName());
		nameItem.setRequired(true);
		description = new TextItem(settingsMessages.measurementDescription());
		Button addUnitButton = new Button();
		addUnitButton.setText(settingsMessages.getAddUnitButton());
		addUnitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientUnit unitData = new ClientUnit();
				addUnitsGrid.setDisabled(false);
				addUnitsGrid.addData(unitData);
			}
		});
		defaultItem = new SelectCombo(settingsMessages.getdefaultUnit());
		defaultItem.setDisabled(false);
		defaultItem.setTitleStyleName(settingsMessages.getdefaultUnit());
		// Button saveButton = new Button();
		// saveButton.setText(settingsMessages.getAddMesurementSaveButton());
		// saveButton.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// }
		// });
		addMeasurmentForm.setFields(nameItem, description);
		// Button cancelButton = new Button();
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
		defaultItem.setDisabled(isInViewMode());
		panel.add(defaultForm);
		// horizontalPanel.add(saveButton);
		// horizontalPanel.setSpacing(20);
		// horizontalPanel.add(cancelButton);
		// panel.add(horizontalPanel);
		this.add(panel);
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

	public void setDefaultComboValue(ClientUnit unitData) {
		defaultList.add(unitData);
		if ((defaultList.get(0)).getType().equals(unitData.getType())) {
			defaultItem.setComboItem(unitData.getType());
			return;
		}
		// defaultItem.addComboItem(unitData.getType());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// validate measurement name?
		// validate units grid
		if (nameItem.getValue().toString() == null
				|| nameItem.getValue().toString().isEmpty()) {
			result.addError(nameItem, Accounter.constants()
					.pleaseEnteraValidMeasurementName());
		}
		if (addUnitsGrid.getRecords().isEmpty()) {
			result.addError(addUnitsGrid, Accounter.constants()
					.unitsMustnotbeNull());
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		data.setName(nameItem.getValue().toString());
		data.setDesctiption(description.getValue().toString());
		for (ClientUnit unit : defaultList) {
			data.addUnit(unit.getType(), unit.getFactor());
		}
	}

	@Override
	public void setFocus() {
		this.nameItem.setFocus();

	}
}
