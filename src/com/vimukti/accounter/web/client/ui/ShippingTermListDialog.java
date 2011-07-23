package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
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
public class ShippingTermListDialog extends GroupDialog<ClientShippingTerms> {
	TextItem methodText, descText;
	boolean isClose;
	ClientShippingTerms shippingTerm;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	List<ClientShippingTerms> shippingTerms;
	private InputDialog inputDlg;

	public ShippingTermListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.SHIPPING_TERM);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientShippingTerms clientShippingTerms = (ClientShippingTerms) core;
				if (clientShippingTerms != null)
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
				showAddEditTermDialog((ClientShippingTerms) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {
				deleteObject(getSelectedShippingTerms());
				if (shippingTerms == null)
					enableEditRemoveButtons(false);
			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
	}

	public void createShippingTerms() {
		if (Utility.isObjectExist(Accounter.getCompany()
				.getShippingTerms(), inputDlg.getTextValueByIndex(0))) {
			Accounter.showError("ShippingTerm  Already Exists");
		} else {
			ClientShippingTerms shippingTerm = new ClientShippingTerms();
			shippingTerm.setName(inputDlg.getTextValueByIndex(0));
			shippingTerm.setDescription(inputDlg.getTextValueByIndex(1));
			createObject(shippingTerm);
		}
	}

	public ClientShippingTerms getSelectedShippingTerms() {
		return (ClientShippingTerms) listGridView.getSelection();
	}

	private void EditShippingTerms() {
		if (!(shippingTerm.getName().equalsIgnoreCase(
				UIUtils.toStr(inputDlg.getTextValueByIndex(0).toString())) ? true
				: (Utility.isObjectExist(company.getShippingTerms(), UIUtils
						.toStr(inputDlg.getTextValueByIndex(0).toString()))) ? false
						: true)) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			shippingTerm.setName(inputDlg.getTextValueByIndex(0));
			shippingTerm.setDescription(inputDlg.getTextValueByIndex(1));
			alterObject(shippingTerm);
		}

	}

	public void showAddEditTermDialog(ClientShippingTerms rec) {
		String arr[] = new String[2];
		arr[0] = "Shipping Term";
		arr[1] = "Description";
		inputDlg = new InputDialog(Accounter.getCompanyMessages()
				.shippingTerm(), "", arr) {
			@Override
			protected String getViewTitle() {
				return Accounter.getCompanyMessages().shippingTerm();
			}
		};
		inputDlg.getTextItems().get(1).setRequired(false);

		shippingTerm = rec;

		if (shippingTerm != null) {
			inputDlg.setTextItemValue(0, shippingTerm.getName());
			inputDlg.setTextItemValue(1, shippingTerm.getDescription());
		}
		InputDialogHandler dialogHandler = new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (inputDlg.getForm().validate(true)) {
					if (shippingTerm != null)
						EditShippingTerms();
					else
						createShippingTerms();
				} else {
					// Accounter.showError(FinanceApplication
					// .getCustomersMessages()
					// .detailsHighlightedInRedMustBeEntered());
					return false;
				}
				return true;
			}

		};
		inputDlg.addInputDialogHandler(dialogHandler);
		inputDlg.show();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientShippingTerms shippingTerms = (ClientShippingTerms) obj;
		if (shippingTerms != null) {
			switch (index) {
			case 0:
				return shippingTerms.getName();
			case 1:
				return shippingTerms.getDescription();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] {
				Accounter.getFinanceUIConstants().Name(),
				Accounter.getFinanceUIConstants().description() };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List getRecords() {
		return Accounter.getCompany().getShippingTerms();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getCompanyMessages().manageShippingTermList();
	}

}
