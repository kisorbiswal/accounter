package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.DialogBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
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
		// setWidth("400px");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.SHIPPING_METHOD);
		getGrid().setCellsWidth(new Integer[] { 140, 190 });
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientShippingMethod>() {

					@Override
					public boolean onRecordClick(
							ClientShippingMethod clientShippingMethod,
							int column) {
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
		this.okbtn.setVisible(false);
	}

	public void createShippingMethod() {

		ClientShippingMethod method = new ClientShippingMethod();
		method.setName(inputDlg.getTextValueByIndex(0));
		method.setDescription(inputDlg.getTextValueByIndex(1));
		saveOrUpdate(method);
	}

	public void deleteRecord() {

		deleteObject(getSelectedShippingMethod());
	}

	public ClientShippingMethod getSelectedShippingMethod() {
		return (ClientShippingMethod) listGridView.getSelection();
	}

	public void showAddEditTermDialog(ClientShippingMethod rec) {
		String arr[] = new String[2];
		arr[0] = messages.shippingMethod();
		arr[1] = messages.description();
		inputDlg = new InputDialog(this, messages.shippingMethod(), "", arr) {
		};
		inputDlg.getTextItems().get(1).setRequired(false);
		inputDlg.setWidth("325px");
		shippingMethod = rec;

		if (shippingMethod != null) {
			inputDlg.setTextItemValue(0, shippingMethod.getName());
			inputDlg.setTextItemValue(1, shippingMethod.getDescription());
		}
		inputDlg.show();
	}

	protected void editShippingMethod() {
		shippingMethod.setName(inputDlg.getTextValueByIndex(0));
		shippingMethod.setDescription(inputDlg.getTextValueByIndex(1));
		saveOrUpdate(shippingMethod);
	}

	@Override
	public Object getGridColumnValue(ClientShippingMethod shippingMethod,
			int index) {
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
		return new String[] { messages.name(), messages.description() };
	}

	@Override
	public String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";
		case 1:
			return "description";
		default:
			break;

		}
		return "";
	}

	@Override
	public String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "nameValue";
		case 1:
			return "descriptionValue";
		default:
			break;

		}
		return "";
	}

	@Override
	protected List<ClientShippingMethod> getRecords() {
		return getCompany().getShippingMethods();
	}

	@Override
	public String toString() {
		return messages.shippingMethodListDialog();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String methodName = inputDlg.getTextValueByIndex(0).toString();
			ClientShippingMethod shippingMethodByName = company
					.getShippingMethodByName(methodName);

			if (shippingMethod == null) {
				if (shippingMethodByName != null) {
					result.addError(this,
							messages.shippingMethodAlreadyExists());
				}
			} else {
				if (!(shippingMethod.getName().equalsIgnoreCase(methodName) ? true
						: shippingMethodByName != null ? false : true)) {
					result.addError(this, messages.alreadyExist());
				}
			}
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (inputDlg != null) {
			if (shippingMethod == null) {
				createShippingMethod();
				inputDlg = null;
			} else {
				editShippingMethod();
				inputDlg = null;
			}
		}
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
