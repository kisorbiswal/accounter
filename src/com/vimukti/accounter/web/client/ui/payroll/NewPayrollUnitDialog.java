package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewPayrollUnitDialog extends BaseDialog<ClientPayrollUnit> {

	private TextItem symbolItem, formalNameItem, noOfDecimalsItem;
	private DynamicForm form;

	public NewPayrollUnitDialog(String string) {
		super(string);
		this.getElement().setId("NewPayrollUnitDialog");
		createControls();
	}

	public void createControls() {

		symbolItem = new TextItem(messages.symbol(), "symbolItem");
		symbolItem.setRequired(true);
		formalNameItem = new TextItem(messages.formalName(), "formalNameItem");
		formalNameItem.setRequired(true);
		noOfDecimalsItem = new TextItem(messages.noOfDecimalPlaces(),
				"noOfDecimalsItem");
		noOfDecimalsItem.setRequired(true);
		form = new DynamicForm("form");
		form.add(symbolItem, formalNameItem, noOfDecimalsItem);

		bodyLayout.add(form);

	}

	@Override
	protected boolean onOK() {
		updateData();
		return true;
	}

	private void updateData() {
		ClientPayrollUnit unit = new ClientPayrollUnit();
		unit.setSymbol(symbolItem.getValue());
		unit.setFormalname(formalNameItem.getValue());
		unit.setNoofDecimalPlaces(Integer.valueOf(noOfDecimalsItem.getValue()));
		saveOrUpdate(unit);
		ActionCallback<ClientPayrollUnit> callback = getCallback();
		if (callback != null) {
			callback.actionResult(unit);
		}
	}

	@Override
	public void setFocus() {
		symbolItem.setFocus();
	}

	public void setData(ClientPayrollUnit data) {
		if (data != null) {
			symbolItem.setValue(data.getSymbol());
			formalNameItem.setValue(data.getFormalname());
			noOfDecimalsItem.setValue("" + data.getNoofDecimalPlaces());
		}
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		String value = noOfDecimalsItem.getValue();
		try {
			Integer valueOf = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			result.addError(noOfDecimalsItem, messages.pleaseEnterValidNumber());
		}
		return result;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		super.saveSuccess(object);
		if (getCallback() != null) {
			getCallback().actionResult((ClientPayrollUnit) object);
		}
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
