package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class AddNewContactDialog extends GroupDialog<ClientContact> {
	private GroupDialogButtonsHandler dialogButtonsHandler;
	private InputDialog inputDlg;
	private ClientContact contacts;

	public AddNewContactDialog(String title, String descript) {
		super(title, descript);
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.CONTACT);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {
			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientContact clientContact = (ClientContact) core;
				if (clientContact != null)
					enableEditRemoveButtons(true);
				else
					enableEditRemoveButtons(false);
				return true;
			}
		});
		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
			
				showAddEditTermDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditTermDialog((ClientContact) listGridView
						.getSelection());
			}

			@Override
			public void onThirdButtonClick() {

			}
		};
		addGroupButtonsHandler(dialogButtonsHandler);

	}

	public void showAddEditTermDialog(ClientContact contact) {
		String arr[] = new String[4];
		arr[0] = Accounter.constants().name();
		arr[1] = Accounter.constants().title();
		arr[2] = Accounter.constants().businessPhone();
		arr[3] = Accounter.constants().email();

		inputDlg = new InputDialog(this, Accounter.constants().contact(), "",
				arr);

		inputDlg.getTextItems().get(1).setRequired(false);
		inputDlg.setWidth("320");
		contacts = contact;

		if (contacts != null) {
			inputDlg.setTextItemValue(0, contacts.getName());
			inputDlg.setTextItemValue(1, contacts.getTitle());
			inputDlg.setTextItemValue(2, contacts.getBusinessPhone());
			inputDlg.setTextItemValue(3, contacts.getEmail());
		}
		InputDialogHandler dialogHandler = new InputDialogHandler() {

			public boolean onOkClick() {
				// if (inputDlg.getForm().validate()) {
				// if (contacts == null)
				// createContactobject();
				// else
				// editContactObject();
				// } else {
				// return false;
				// }

				return true;
			}

			@Override
			public void onCancel() {
				System.out.println("Cancel");

			}

			@Override
			public boolean onOK() {
				System.out.println("Ok");
				return false;
			}

		};
		inputDlg.addInputDialogHandler(dialogHandler);
		inputDlg.show();
	}

	private void createContactobject() {
		// if (Utility.isObjectExist(Accounter.getCompany().getClientContacts(),
		// inputDlg.getTextValueByIndex(0))) {
		// Accounter.showError("Contact  Already Exists");
		// } else {

		ClientContact method = new ClientContact();
		method.setPrimary(true);
		method.setName(inputDlg.getTextValueByIndex(0));
		method.setTitle(inputDlg.getTextValueByIndex(1));
		method.setBusinessPhone(inputDlg.getTextValueByIndex(2));
		method.setEmail(inputDlg.getTextValueByIndex(3));
		// createObject(method);
	}

	// }

	private void editContactObject() {
		// if (!(contacts.getName().equalsIgnoreCase(
		// UIUtils.toStr(inputDlg.getTextValueByIndex(0).toString())) ? true
		// : (Utility.isObjectExist(company.getClientContacts(), UIUtils
		// .toStr(inputDlg.getTextValueByIndex(0).toString()))) ? false
		// : true)) {
		// Accounter.showError(AccounterErrorType.ALREADYEXIST);
		// } else {
		// contacts.setPrimary(true);
		// contacts.setName(inputDlg.getTextValueByIndex(0));
		// contacts.setTitle(inputDlg.getTextValueByIndex(1));
		// contacts.setBusinessPhone(inputDlg.getTextValueByIndex(2));
		// contacts.setEmail(inputDlg.getTextValueByIndex(3));
		// alterObject(contacts);
		// }
	}

	@Override
	public String[] setColumns() {
		return new String[] { Accounter.constants().name(),
				Accounter.constants().title(),
				Accounter.constants().businessPhone(),
				Accounter.constants().email() };
	}

	// @Override
	protected List<ClientContact> getRecords() {
		// return (List<ClientContact>)
		// Accounter.getCompany().getClientContacts();
		return null;
	}

	// @Override
	// protected String getViewTitle() {
	// return "Contacts List Dialog";
	// }

	public void deleteRecord() {

		deleteObject(getSelectedShippingMethod());
	}

	public ClientContact getSelectedShippingMethod() {
		return (ClientContact) listGridView.getSelection();
	}

	@Override
	protected boolean onOK() {
		System.out.println("ok112");
		return false;
	}

}
