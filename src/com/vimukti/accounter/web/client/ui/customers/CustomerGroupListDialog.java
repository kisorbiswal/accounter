package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */

public class CustomerGroupListDialog extends GroupDialog<ClientCustomerGroup> {

	private GroupDialogButtonsHandler dialogButtonsHandler;
	ClientCustomerGroup customerGroup;
	List<ClientCustomerGroup> customterGroups;
	private InputDialog inputDlg;
	private AccounterConstants customerConstants = Accounter.constants();

	public CustomerGroupListDialog(String title, String descript) {
		super(title, descript);
		// setSize("380", "300");
		setWidth("380px");
		initialise();
		center();
	}

	private void initialise() {
		this.button1.setFocus(true);
		getGrid().setType(AccounterCoreType.CUSTOMER_GROUP);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientCustomerGroup>() {

					@Override
					public boolean onRecordClick(ClientCustomerGroup core,
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
				if (listGridView.getSelection() != null) {
					showAddEditGroupDialog((ClientCustomerGroup) listGridView
							.getSelection());
				} else {
					Accounter.showError(Accounter.constants().selectTaxGroup());
					new Exception();
				}

			}

			public void onThirdButtonClick() {
				deleteObject((IAccounterCore) listGridView.getSelection());
				if (customterGroups == null)
					enableEditRemoveButtons(false);
			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	public void createCustomerGroups() {
		ClientCustomerGroup customerGroup = new ClientCustomerGroup();
		customerGroup.setName(inputDlg.getTextItems().get(0).getValue()
				.toString());

		saveOrUpdate(customerGroup);
	}

	// public long getSelectedCustomerId() {
	// return ((ClientCustomerGroup) listGridView.getSelection()).getID();
	// }

	public void showAddEditGroupDialog(ClientCustomerGroup rec) {
		customerGroup = rec;
		inputDlg = new InputDialog(this, Accounter.messages().customerGroup(
				Global.get().Customer()), "", Accounter.messages()
				.customerGroup(Global.get().Customer())) {
		};

		if (customerGroup != null) {
			inputDlg.setTextItemValue(0, customerGroup.getName());
		}
		// inputDlg.setWidth(350);
		inputDlg.show();
	}

	protected void editCustomerGroups() {

		customerGroup.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(customerGroup);
	}

	@Override
	public Object getGridColumnValue(ClientCustomerGroup group, int index) {
		switch (index) {
		case 0:
			if (group != null)
				return group.getName();
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { Accounter.constants().name() };
	}

	@Override
	protected List<ClientCustomerGroup> getRecords() {
		return getCompany().getCustomerGroups();
	}

	public void deleteCallBack() {
		ClientCustomerGroup custGrp = (ClientCustomerGroup) listGridView
				.getSelection();
		if (custGrp != null) {
			deleteObject(custGrp);
		}

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String value = inputDlg.getTextItems().get(0).getValue().toString();
			ClientCustomerGroup customerGroupByName = company
					.getCustomerGroupByName(UIUtils.toStr(value));
			if (customerGroupByName != null) {
				result.addError(this, Accounter.messages()
						.customerGroupAlreadyExists(Global.get().Customer()));
			}
			// if (customerGroup != null) {
			// if (!(customerGroup.getName().equalsIgnoreCase(
			// UIUtils.toStr(value)) ? true
			// : (Utility.isObjectExist(company.getCustomerGroups()
			// ) ? false : true))) {
			// result.addError(this, Accounter.constants()
			// .customerGroupAlreadyExists());
			// }
		} else {
			ClientCustomerGroup customerGroupByName2 = getCompany()
					.getCustomerGroupByName(
							inputDlg.getTextItems().get(0).getValue()
									.toString());
			if (customerGroupByName2 != null) {
				result.addError(this, Accounter.messages()
						.customerGroupAlreadyExists(Global.get().Customer()));
			}
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (inputDlg != null) {
			if (customerGroup != null) {
				editCustomerGroups();
				inputDlg = null;
			} else {
				createCustomerGroups();
				inputDlg = null;
			}
		}
		return true;
	}
}