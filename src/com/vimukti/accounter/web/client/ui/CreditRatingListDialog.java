package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
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
public class CreditRatingListDialog extends GroupDialog<ClientCreditRating> {

	private GroupDialogButtonsHandler dialogButtonsHandler;
	ClientCreditRating creditRating;
	List<ClientCreditRating> creditRatings;
	private InputDialog inputDlg;

	public CreditRatingListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.CREDIT_RATING);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientCreditRating clientCreditRating = (ClientCreditRating) core;
				if (clientCreditRating != null)
					enableEditRemoveButtons(true);
				else
					enableEditRemoveButtons(false);
				return false;
			}
		});
		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientCreditRating) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {

				deleteObject(getSelectedCreditGroup());
				if (creditRatings == null)
					enableEditRemoveButtons(false);

			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
	}

	public void createCreditRatings() {
		if (Utility.isObjectExist(FinanceApplication.getCompany()
				.getCreditRatings(), inputDlg.getTextItems().get(0).getValue()
				.toString())) {
			Accounter.showError("CreditRating  Already Exists");
		} else {
			ClientCreditRating creditRating = new ClientCreditRating();
			creditRating.setName(inputDlg.getTextItems().get(0).getValue()
					.toString());

			createObject(creditRating);
		}
	}

	public String getSelectedCreditGroupId() {

		return ((ClientCreditRating) listGridView.getSelection()).getStringID();
	}

	public ClientCreditRating getSelectedCreditGroup() {

		return (ClientCreditRating) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientCreditRating rec) {
		creditRating = rec;
		String creditRateString = FinanceApplication.getFinanceUIConstants()
				.creditRating();
		inputDlg = new InputDialog(FinanceApplication.getCustomersMessages()
				.creditRating(), "", creditRateString);

		if (creditRating != null) {
			inputDlg.setTextItemValue(0, creditRating.getName());
		}

		InputDialogHandler dialogHandler = new InputDialogHandler() {
			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (inputDlg.getForm().validate()) {
					if (creditRating != null) {
						EditCreditRatings();
					} else
						createCreditRatings();
				} else {
					return false;
				}
				return true;
			}

		};
		inputDlg.addInputDialogHandler(dialogHandler);
		inputDlg.show();
	}

	protected void EditCreditRatings() {
		if (!(creditRating.getName().equalsIgnoreCase(
				UIUtils.toStr(inputDlg.getTextItems().get(0).getValue()
						.toString())) ? true : (Utility.isObjectExist(company
				.getItemGroups(), UIUtils.toStr(inputDlg.getTextItems().get(0)
				.getValue().toString()))) ? false : true)) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			creditRating.setName(inputDlg.getTextValueByIndex(0));
			alterObject(creditRating);
		}

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientCreditRating creditRating = (ClientCreditRating) obj;
		if (creditRating != null) {
			switch (index) {
			case 0:
				return creditRating.getName();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { FinanceApplication.getFinanceUIConstants().name() };
	}

	@Override
	protected List<ClientCreditRating> getRecords() {
		return (List<ClientCreditRating>) FinanceApplication.getCompany()
				.getCreditRatings();
	}

}
