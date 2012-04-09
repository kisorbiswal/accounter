package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class EmployeeGroupListDialog extends GroupDialog<ClientEmployeeGroup> {

	private InputDialog inputDlg;
	private ClientEmployeeGroup employeeGroup;
	List<ClientEmployeeGroup> employeeGroups;
	private GroupDialogButtonsHandler dialogButtonsHandler;

	public EmployeeGroupListDialog(String title, String descript) {
		super(title, descript);
		setWidth("380px");
		initialise();
		center();
	}

	public void initialise() {
		this.button1.setFocus(true);
		getGrid().setType(AccounterCoreType.EMPLOYEE_GROUP);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientEmployeeGroup>() {

					@Override
					public boolean onRecordClick(ClientEmployeeGroup core,
							int column) {
						enableEditRemoveButtons(true);
						return true;
					}

				});

		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientEmployeeGroup) getSelectedRecord());
			}

			public void onThirdButtonClick() {
				if (listGridView != null) {
					ClientEmployeeGroup selectedGroup = (ClientEmployeeGroup) listGridView
							.getSelection();
					if (selectedGroup != null) {
						return;
					}

					deleteObject((IAccounterCore) listGridView.getSelection());
					if (employeeGroups == null) {
						enableEditRemoveButtons(false);
					}
				}
			}

		};

		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	protected void showAddEditGroupDialog(ClientEmployeeGroup rec) {
		employeeGroup = rec;
		inputDlg = new InputDialog(this, messages.employeeGroup(), "",
				messages.employeeGroup()) {
		};

		if (employeeGroup != null) {
			inputDlg.setTextItemValue(0, employeeGroup.getName());
		}
		inputDlg.show();
	}

	@Override
	public Object getGridColumnValue(ClientEmployeeGroup group, int index) {
		switch (index) {
		case 0:
			if (group != null)
				return group.getName();
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.name() };
	}

	@Override
	protected List<ClientEmployeeGroup> getRecords() {
		return getCompany().getEmployeeGroups();
	}

	@Override
	protected boolean onOK() {
		if (inputDlg != null) {
			if (employeeGroup != null) {
				editCustomerGroups();
				inputDlg = null;
			} else {
				createCustomerGroups();
				inputDlg = null;
			}
		}
		return true;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String value = inputDlg.getTextItems().get(0).getValue().toString();
			ClientEmployeeGroup employeeGroupByName = company
					.getEmployeeGroupByName(UIUtils.toStr(value));
			if (employeeGroup != null) {
				if (!(employeeGroup.getName().equalsIgnoreCase(value) ? true
						: employeeGroupByName == null)) {
					result.addError(this, messages.alreadyExist());
				}
			}
		} else {
			ClientEmployeeGroup employeeGroupByName2 = getCompany()
					.getEmployeeGroupByName(
							inputDlg.getTextItems().get(0).getValue()
									.toString());
			if (employeeGroupByName2 != null) {
				result.addError(this,
						messages.payeeGroupAlreadyExists(messages.employee()));
			}
		}
		return result;
	}

	private void editCustomerGroups() {
		employeeGroup.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(employeeGroup);
	}

	private void createCustomerGroups() {
		ClientEmployeeGroup group = new ClientEmployeeGroup();
		group.setName(inputDlg.getTextItems().get(0).getValue().toString());
		saveOrUpdate(group);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";

		default:
			break;
		}
		return null;
	}

	@Override
	public String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "name";

		default:
			break;
		}
		return null;
	}

}
