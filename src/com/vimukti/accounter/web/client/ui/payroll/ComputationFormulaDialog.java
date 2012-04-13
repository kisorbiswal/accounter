package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientComputationFormulaFunction;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class ComputationFormulaDialog extends
		BaseDialog<ClientComputationFormulaFunction> {

	private DynamicForm mainForm;

	private ValueCallBack<List<ClientComputationFormulaFunction>> successCallback;
	private ComputationFormulaTable table;
	private AddNewButton itemTableButton;

	public ComputationFormulaDialog(String string) {
		super(string);
		this.getElement().setId("ComputationFormulaDialog");
		createControls();
	}

	public void createControls() {
		mainForm = new DynamicForm("mainForm");

		table = new ComputationFormulaTable();
		itemTableButton = new AddNewButton();
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientComputationFormulaFunction row = new ClientComputationFormulaFunction();
				table.add(row);
			}
		});

		mainForm.add(table);
		mainForm.add(itemTableButton);

		bodyLayout.add(mainForm);
	}

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createFunctions());
		}
		return true;
	}

	public void addCallback(
			ValueCallBack<List<ClientComputationFormulaFunction>> successCallback) {
		this.successCallback = successCallback;
	}

	private List<ClientComputationFormulaFunction> createFunctions() {
		List<ClientComputationFormulaFunction> functions = new ArrayList<ClientComputationFormulaFunction>();
		functions = table.getAllRows();
		return functions;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
