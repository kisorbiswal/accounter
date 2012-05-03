package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewPayStructureView extends BaseView<ClientPayStructure> {

	private EmployeesAndGroupsCombo empsAndGroups;
	private PayStructureTable grid;
	private AddNewButton itemTableButton;

	public NewPayStructureView() {
		this.getElement().setId("NewPayStructureView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label lab1 = new Label(messages.payStructure());
		lab1.setStyleName("label-title");

		empsAndGroups = new EmployeesAndGroupsCombo(messages.employeeOrGroup(),
				"empsAndGroups");
		empsAndGroups.setEnabled(!isInViewMode());
		empsAndGroups.setRequired(true);

		grid = new PayStructureTable();
		grid.setEnabled(!isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(empsAndGroups);
		mainVLay.add(grid);
		mainVLay.add(itemTableButton);

		this.add(mainVLay);
	}

	protected void addItem() {
		ClientPayStructureItem transactionItem = new ClientPayStructureItem();
		grid.add(transactionItem);

	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayStructure());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientPayStructure data) {
		long employee = data.getEmployee();
		if (employee == 0) {
			employee = data.getEmployeeGroup();
		}
		empsAndGroups.setEmpGroup(employee);

		grid.setAllRows(data.getItems());
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
		return messages.newPayee(messages.payStructure());
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		empsAndGroups.setFocus();
	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = data.getObjectType();
		this.rpcDoSerivce.canEdit(type, data.getID(), editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);

		empsAndGroups.setEnabled(!isInViewMode());
		grid.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue instanceof ClientEmployee) {
			data.setEmployee(selectedValue.getID());
		} else {
			data.setEmployeeGroup(selectedValue.getID());
		}

		data.setItems(grid.getRows());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult validate = new ValidationResult();
		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue == null) {
			validate.addError(empsAndGroups,
					messages.shouldNotBeNull(messages.employeeGroup()));
			return validate;
		}

		validate = grid.validate(validate);
		List<ClientPayStructureItem> rows = grid.getRows();
		if (rows.isEmpty()) {
			validate.addError(grid, messages.noItemSelected());
		}
		return validate;

	}

}
