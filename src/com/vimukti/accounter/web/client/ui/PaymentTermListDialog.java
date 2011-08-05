package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.company.AddPaymentTermDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Mandeep Singh
 * @param <T>
 * 
 */
public class PaymentTermListDialog extends GroupDialog<ClientPaymentTerms> {

	private GroupDialogButtonsHandler dialogButtonsHandler;

	List<ClientPaymentTerms> paymentTerms;
	ClientPaymentTerms paymentTerm;
	private AddPaymentTermDialog dialog;

	public PaymentTermListDialog() {
		super(Accounter.constants().managePaymentTerm(), Accounter.constants()
				.paymentTermDescription());
		createControls();
		center();
	}

	private void createControls() {
		listGridView.setType(AccounterCoreType.PAYMENT_TERM);
		setSize("400", "250");

		listGridView.setSize("400", "300");
		listGridView.addRecordClickHandler(new GridRecordClickHandler() {

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
				showAddEditTermDialog(null);

			}

			public void onSecondButtonClick() {
				showAddEditTermDialog((ClientPaymentTerms) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {
				deleteObject(getSelectedPaymentTerms());
				if (paymentTerms == null)
					enableEditRemoveButtons(false);

			}
		};
		addGroupButtonsHandler(dialogButtonsHandler);

	}

	protected ClientPaymentTerms getSelectedPaymentTerms() {

		return (ClientPaymentTerms) listGridView.getSelection();
	}

	public void createPaymentTerms() {
		if (Utility.isObjectExist(getCompany().getPaymentsTerms(),
				dialog.payTermText.getValue().toString())) {
			Accounter.showError(Accounter.constants().paytermsAlreadyExists());
		} else {
			ClientPaymentTerms clientPaymentTerms = getPaymentTerms();
			createObject(clientPaymentTerms);
		}
	}

	public void showAddEditTermDialog(ClientPaymentTerms rec) {
		dialog = new AddPaymentTermDialog(Accounter.constants()
				.addPaymentTermTitle(), Accounter.constants()
				.addPaymentTermTitleDesc());

		paymentTerm = rec;
		if (paymentTerm != null) {

			dialog.payTermText.setValue(paymentTerm.getName());
			dialog.descText.setValue(paymentTerm.getDescription());
			if (paymentTerm.getDue() != 0) {
				dialog.dueSelect
						.setValue(dialog.dueValues[paymentTerm.getDue() - 1]);
			}
			dialog.dayText.setValue(paymentTerm.getDueDays());
			dialog.discText.setValue(paymentTerm.getDiscountPercent());
			dialog.discDayText.setValue(paymentTerm.getIfPaidWithIn());

		}
		dialog.addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {

				if (dialog.nameDescForm.validate(true)) {

					if (paymentTerm != null) {
						editPaymentTerms();
					} else
						createPaymentTerms();
				} else {
					// Accounter.showError(FinanceApplication
					// .constants()
					// .detailsHighlightedInRedMustBeEntered());
					return false;
				}
				return true;
			}

		});
		dialog.show();
	}

	protected ClientPaymentTerms getPaymentTerms() {
		ClientPaymentTerms clientPaymentTerms;
		if (paymentTerm != null)
			clientPaymentTerms = paymentTerm;
		else
			clientPaymentTerms = new ClientPaymentTerms();

		clientPaymentTerms
				.setName(dialog.payTermText.getValue() != null ? dialog.payTermText
						.getValue().toString() : "");
		clientPaymentTerms
				.setDescription(dialog.descText.getValue() != null ? dialog.descText
						.getValue().toString() : "");
		clientPaymentTerms.setIfPaidWithIn(UIUtils.toInt(dialog.discDayText
				.getNumber() != null ? dialog.discDayText.getNumber() : "0"));
		clientPaymentTerms.setDiscountPercent(UIUtils.toDbl(dialog.discText
				.getPercentage() != null ? dialog.discText.getPercentage()
				: "0"));

		for (int i = 0; i < dialog.dueValues.length; i++) {
			if (dialog.dueSelect.getValue() != null) {
				if (dialog.dueSelect.getValue().toString()
						.equals(dialog.dueValues[i]))
					clientPaymentTerms.setDue(i + 1);
			}

		}
		clientPaymentTerms
				.setDueDays(UIUtils.toInt(dialog.dayText.getNumber() != null ? dialog.dayText
						.getNumber() : "0"));

		return clientPaymentTerms;

	}

	/*
	 * If the payment term is a new one,check whether is there any otherone with
	 * same name. If editing already exited one,check wheder the given name
	 * changed or not,if changed,check whether new name existed in the list or
	 * not.
	 * 
	 * @return true -if the payment term is already exists
	 */
	private boolean validateName(String name) {
		return (paymentTerm == null
				&& Utility.isObjectExist(company.getPaymentsTerms(), name) ? true
				: false)
				|| (paymentTerm != null && !(paymentTerm.getName()
						.equalsIgnoreCase(name) ? true
						: (Utility.isObjectExist(company.getPaymentsTerms(),
								name) ? false : true)));
	}

	protected void editPaymentTerms() {
		if (validateName(dialog.payTermText.getValue() != null ? dialog.payTermText
				.getValue().toString() : "")) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			ClientPaymentTerms clientPaymentTerms = getPaymentTerms();
			alterObject(clientPaymentTerms);
		}
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientPaymentTerms paymentTerms = (ClientPaymentTerms) obj;
		if (paymentTerms != null) {
			switch (index) {
			case 0:

				return paymentTerms.getName();
			case 1:
				return paymentTerms.getDescription();
			case 2:
				return paymentTerms.getIfPaidWithIn();

			case 3:
				return DataUtils.getNumberAsPercentString(paymentTerms
						.getDiscountPercent() + "");
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { Accounter.constants().name(),
				Accounter.constants().description(),
				Accounter.constants().paidWithin(),
				Accounter.constants().cashDiscount() };
	}

	@Override
	protected List<ClientPaymentTerms> getRecords() {
		return (List<ClientPaymentTerms>) getCompany().getPaymentsTerms();
	}

}
