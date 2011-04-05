package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */
@SuppressWarnings("unchecked")
public class CustomerGroupListDialog extends GroupDialog<ClientCustomerGroup> {

	private GroupDialogButtonsHandler dialogButtonsHandler;
	ClientCustomerGroup customerGroup;
	List<ClientCustomerGroup> customterGroups;
	private InputDialog inputDlg;
	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);

	public CustomerGroupListDialog(String title, String descript) {
		super(title, descript);
		// setSize("380", "300");
		setWidth("380");
		initialise();
		center();
	}

	private void initialise() {
		this.button1.setFocus(true);
		getGrid().setType(AccounterCoreType.CUSTOMER_GROUP);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
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
					Accounter.showError(FinanceApplication
							.getCustomersMessages().selectTaxGroup());
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
	}

	public void createCustomerGroups() {
		ClientCustomerGroup customerGroup = new ClientCustomerGroup();
		customerGroup.setName(inputDlg.getTextItems().get(0).getValue()
				.toString());
		if (Utility.isObjectExist(FinanceApplication.getCompany()
				.getCustomerGroups(), customerGroup.getName())) {
			Accounter.showError("Customer Group Already Exists");
		} else {
			createObject(customerGroup);
		}
	}

	// public long getSelectedCustomerId() {
	// return ((ClientCustomerGroup) listGridView.getSelection()).getStringID();
	// }

	public void showAddEditGroupDialog(ClientCustomerGroup rec) {
		customerGroup = rec;
		inputDlg = new InputDialog(customerConstants.customerGroup(), "",
				FinanceApplication.getCustomersMessages().customeRGroup());

		if (customerGroup != null) {
			inputDlg.setTextItemValue(0, customerGroup.getName());
		}
		InputDialogHandler dialogHandler = new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (inputDlg.getForm().validate()) {
					if (customerGroup != null) {
						editCustomerGroups();
					} else {
						createCustomerGroups();
					}
				} else {
					// Accounter.showError(FinanceApplication
					// .getCustomersMessages().detailsHighlightedInRedMustBeEntered());
					return false;
				}
				return true;
			}

		};
		inputDlg.addInputDialogHandler(dialogHandler);
		// inputDlg.setWidth(350);
		inputDlg.show();
	}

	protected void editCustomerGroups() {

		if (!(customerGroup.getName().equalsIgnoreCase(
				UIUtils.toStr(inputDlg.getTextItems().get(0).getValue()
						.toString())) ? true : (Utility.isObjectExist(company
				.getCustomerGroups(), UIUtils.toStr(inputDlg.getTextItems()
				.get(0).getValue().toString())) ? false : true))) {
			Accounter.showError("Customer Group Already Exists");
		} else {
			customerGroup.setName(inputDlg.getTextValueByIndex(0));
			alterObject(customerGroup);
		}
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientCustomerGroup group = (ClientCustomerGroup) obj;
		switch (index) {
		case 0:
			if (group != null)
				return group.getName();
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { FinanceApplication.getCustomersMessages().name() };
	}

	@Override
	protected List getRecords() {
		return (List) FinanceApplication.getCompany().getCustomerGroups();
	}

	public void deleteCallBack() {
		ClientCustomerGroup custGrp = (ClientCustomerGroup) listGridView
				.getSelection();
		if (custGrp != null) {
			deleteObject(custGrp);
		}

	}

}