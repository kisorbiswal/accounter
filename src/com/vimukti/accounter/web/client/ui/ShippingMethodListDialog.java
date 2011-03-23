package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.DialogBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class ShippingMethodListDialog extends GroupDialog<ClientShippingMethod> {
	TextItem methodText, descText;
	boolean isClose;
	DialogBox dlg;
	ClientShippingMethod shippingMethod;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	List<ClientShippingMethod> shippingMethods;

	private InputDialog inputDlg;

	public ShippingMethodListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.SHIPPING_METHOD);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientShippingMethod clientShippingMethod = (ClientShippingMethod) core;
				if (clientShippingMethod != null)
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
				showAddEditTermDialog((ClientShippingMethod) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {
				deleteRecord();
				if (shippingMethods == null)
					enableEditRemoveButtons(false);
			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
	}

	public void createShippingMethod() {
		if (Utility.isObjectExist(FinanceApplication.getCompany()
				.getShippingMethods(), inputDlg.getTextValueByIndex(0))) {
			Accounter.showError("Shipping Method  Already Exists");
		} else {

			ClientShippingMethod method = new ClientShippingMethod();
			method.setName(inputDlg.getTextValueByIndex(0));
			method.setDescription(inputDlg.getTextValueByIndex(1));
			createObject(method);
		}
	}

	public void deleteRecord() {

		deleteObject(getSelectedShippingMethod());
	}

	public ClientShippingMethod getSelectedShippingMethod() {
		return (ClientShippingMethod) listGridView.getSelection();
	}

	public void showAddEditTermDialog(ClientShippingMethod rec) {
		String arr[] = new String[2];
		arr[0] = FinanceApplication.getFinanceUIConstants().shippingMethod();
		arr[1] = FinanceApplication.getFinanceUIConstants().descriptioN();
		inputDlg = new InputDialog(FinanceApplication.getCustomersMessages()
				.shippingMethod(), "", arr);
		inputDlg.getTextItems().get(1).setRequired(false);
		inputDlg.setWidth("320");
		shippingMethod = rec;

		if (shippingMethod != null) {
			inputDlg.setTextItemValue(0, shippingMethod.getName());
			inputDlg.setTextItemValue(1, shippingMethod.getDescription());
		}
		InputDialogHandler dialogHandler = new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (inputDlg.getForm().validate()) {
					if (shippingMethod == null)
						createShippingMethod();
					else
						editShippingMethod();
				} else
					return false;

				return true;
			}

		};
		inputDlg.addInputDialogHandler(dialogHandler);
		inputDlg.show();
	}

	protected void editShippingMethod() {
		if (!(shippingMethod.getName().equalsIgnoreCase(
				UIUtils.toStr(inputDlg.getTextValueByIndex(0).toString())) ? true
				: (Utility.isObjectExist(company.getShippingMethods(), UIUtils
						.toStr(inputDlg.getTextValueByIndex(0).toString()))) ? false
						: true)) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			shippingMethod.setName(inputDlg.getTextValueByIndex(0));
			shippingMethod.setDescription(inputDlg.getTextValueByIndex(1));
			alterObject(shippingMethod);
		}
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientShippingMethod shippingMethod = (ClientShippingMethod) obj;
		if (shippingMethod != null) {
			switch (index) {
			case 0:
				return shippingMethod.getName();
			case 1:
				return shippingMethod.getDescription();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] {
				FinanceApplication.getFinanceUIConstants().Name(),
				FinanceApplication.getFinanceUIConstants().description() };
	}

	@Override
	protected List<ClientShippingMethod> getRecords() {
		return (List<ClientShippingMethod>) FinanceApplication.getCompany()
				.getShippingMethods();
	}

	@Override
	public String toString() {
		return FinanceApplication.getFinanceUIConstants()
				.shippingMethodListDialog();
	}
}
