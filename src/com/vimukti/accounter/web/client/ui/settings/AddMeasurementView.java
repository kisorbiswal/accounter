package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.UnitsTable;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddMeasurementView extends BaseView<ClientMeasurement> {

	private TextItem nameItem, description;
	private UnitsTable unitsTable;
	private DynamicForm addMeasurmentForm;
	private AccounterConstants settingsMessages = Accounter.constants();

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

	@Override
	public void initData() {
		super.initData();
		if (data == null) {
			data = new ClientMeasurement();
		}
	}

	private void createControls() {

		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		panel.setWidth("100%");
		addMeasurmentForm = new DynamicForm();
		nameItem = new TextItem(settingsMessages.measurementName());
		nameItem.setRequired(true);

		description = new TextItem(settingsMessages.measurementDescription());

		unitsTable = new UnitsTable();

		Button addUnitButton = new Button();
		addUnitButton.setText(settingsMessages.addUnitButton());
		addUnitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientUnit clientUnit = new ClientUnit();
				unitsTable.setDisabled(false);
				if (unitsTable.getRecords().isEmpty()) {
					clientUnit.setDefault(true);
				}
				unitsTable.add(clientUnit);
			}
		});

		addMeasurmentForm.setFields(nameItem, description);
		panel.add(addMeasurmentForm);
		panel.add(unitsTable);
		panel.add(addUnitButton);
		this.add(panel);
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
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		data.setName(nameItem.getValue());
		data.setDesctiption(description.getValue());
		data.setUnits(unitsTable.getRecords());
	}

	@Override
	public void setFocus() {
		this.nameItem.setFocus();

	}

	// public void updateUnitsTable() {
	// if (addUnitsTable.getAllRows() != null) {
	// for (ClientUnit unit : addUnitsTable.getAllRows()) {
	// data.addUnit(unit.getType(), unit.getFactor());
	// }
	// }
	// defaultItem.initCombo(addUnitsTable.getAllRows());
	// if ((addUnitsTable.getAllRows().get(0) != null)
	// && (addUnitsTable.getAllRows().get(0).getType() != null))
	// defaultItem.setComboItem(addUnitsTable.getAllRows().get(0));
	// else
	// defaultItem.setSelected("");
	// }

}
