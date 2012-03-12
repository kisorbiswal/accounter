package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.company.AddPaymentTermDialog;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
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
		super(messages.managePaymentTerm(), messages.paymentTermDescription());
		createControls();
		center();
	}

	private void createControls() {
		listGridView.setType(AccounterCoreType.PAYMENT_TERM);
		listGridView.setCellsWidth(new Integer[] { 120, 140, 130 /* , 135 */});
//		listGridView.setSize("515px", "500px");
		listGridView
				.addRecordClickHandler(new GridRecordClickHandler<ClientPaymentTerms>() {

					@Override
					public boolean onRecordClick(ClientPaymentTerms core,
							int column) {
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
				ClientPaymentTerms selectedPaymentTerms = getSelectedPaymentTerms();
				if (!selectedPaymentTerms.isDefault()) {
					showAddEditTermDialog(selectedPaymentTerms);
				} else {
					Accounter.showError(messages
							.youcannotEditDeleteDefaultTerms());
				}

			}

			public void onThirdButtonClick() {
				ClientPaymentTerms selectedPaymentTerms = getSelectedPaymentTerms();
				if (!selectedPaymentTerms.isDefault()) {
					deleteObject(selectedPaymentTerms);
				} else {
					Accounter.showError(messages
							.youcannotEditDeleteDefaultTerms());
				}
				if (paymentTerms == null)
					enableEditRemoveButtons(false);

			}
		};
		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);

	}

	protected ClientPaymentTerms getSelectedPaymentTerms() {

		return (ClientPaymentTerms) listGridView.getSelection();
	}

	public void showAddEditTermDialog(ClientPaymentTerms rec) {
		dialog = new AddPaymentTermDialog(this, messages.addPaymentTermTitle(),
				messages.addPaymentTermTitleDesc());
		this.paymentTerm = rec;
		if (rec != null) {
			dialog.payTermText.setValue(rec.getName());
			dialog.dateDriven.setValue(paymentTerm.isDateDriven());
			dialog.disableForms(paymentTerm.isDateDriven());
			if (!paymentTerm.isDateDriven()) {
				dialog.netDueIn.setNumber(UIUtils.toLong(paymentTerm
						.getDueDays()));
				dialog.discountField
						.setAmount(paymentTerm.getDiscountPercent());
				dialog.discountDue.setNumber(UIUtils.toLong(paymentTerm
						.getIfPaidWithIn()));
			} else {
				dialog.netDueBefore.setNumber(UIUtils.toLong(paymentTerm
						.getDueDays()));
				dialog.discountPerField.setAmount(paymentTerm
						.getDiscountPercent());
				dialog.discountPaidBefore.setNumber(UIUtils.toLong(paymentTerm
						.getIfPaidWithIn()));

			}

			// dialog.descText.setValue(rec.getDescription());
			// if (rec.getDue() != 0) {
			// dialog.dueSelect.setValue(dialog.dueValues[rec.getDue() - 1]);
			// }
			// dialog.dayText.setValue(String.valueOf(rec.getDueDays()));
			// dialog.discText.setValue(String.valueOf(rec.getDiscountPercent()));
			// dialog.discDayText.setValue(String.valueOf(rec.getIfPaidWithIn()));
		}

		dialog.setCallback(new ActionCallback<ClientPaymentTerms>() {
			@Override
			public void actionResult(ClientPaymentTerms result) {
				setResult(result);
			}
		});

		dialog.show();
	}

	protected ClientPaymentTerms getPaymentTerms() {
		ClientPaymentTerms clientPaymentTerms;
		if (paymentTerm != null) {
			clientPaymentTerms = paymentTerm;
			clientPaymentTerms
					.setName(dialog.payTermText.getValue() != null ? dialog.payTermText
							.getValue().toString() : "");
			paymentTerm.setDateDriven(dialog.dateDriven.getValue());
			if (dialog.fixedDays.getValue()) {
				paymentTerm.setDueDays(UIUtils.toInt((this.dialog.netDueIn
						.getNumber() != null ? this.dialog.netDueIn.getNumber()
						: 0)));
				paymentTerm.setDiscountPercent(this.dialog.discountField
						.getAmount() != null ? this.dialog.discountField
						.getAmount() : 0);
				paymentTerm
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountDue
								.getNumber() != null ? this.dialog.discountDue
								.getNumber() : 0)));
			} else {
				paymentTerm.setDueDays(UIUtils.toInt((this.dialog.netDueBefore
						.getNumber() != null ? this.dialog.netDueBefore
						.getNumber() : 0)));
				paymentTerm.setDiscountPercent(this.dialog.discountPerField
						.getAmount() != null ? this.dialog.discountPerField
						.getAmount() : 0);
				paymentTerm
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountPaidBefore
								.getNumber() != null ? this.dialog.discountPaidBefore
								.getNumber() : 0)));
			}
			// clientPaymentTerms
			// .setDescription(dialog.descText.getValue() != null ?
			// dialog.descText
			// .getValue().toString() : "");
			// clientPaymentTerms.setIfPaidWithIn(UIUtils.toInt(dialog.discDayText
			// .getValue() != null ? dialog.discDayText.getValue() : "0"));
			// clientPaymentTerms.setDiscountPercent(UIUtils
			// .toDbl(dialog.discText.getValue().toString()
			// .replaceAll("%", "") != null ? dialog.discText
			// .getValue().toString().replaceAll("%", "") : "0"));

			// for (int i = 0; i < dialog.dueValues.length; i++) {
			// if (dialog.dueSelect.getValue() != null) {
			// if (dialog.dueSelect.getValue().toString()
			// .equals(dialog.dueValues[i]))
			// clientPaymentTerms.setDue(i + 1);
			// }
			// }
			// clientPaymentTerms.setDueDays(UIUtils.toInt(dialog.dayText
			// .getValue() != null ? dialog.dayText.getValue() : "0"));
		} else {
			clientPaymentTerms = new ClientPaymentTerms();
			clientPaymentTerms
					.setName(dialog.payTermText.getValue() != null ? dialog.payTermText
							.getValue().toString() : "");

			clientPaymentTerms.setDateDriven(dialog.dateDriven.getValue());
			if (dialog.fixedDays.getValue()) {
				clientPaymentTerms
						.setDueDays(UIUtils.toInt((this.dialog.netDueIn
								.getNumber() != null ? this.dialog.netDueIn
								.getNumber() : 0)));
				clientPaymentTerms.setDiscountPercent(this.dialog.discountField
						.getAmount() != null ? this.dialog.discountField
						.getAmount() : 0);
				clientPaymentTerms
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountDue
								.getNumber() != null ? this.dialog.discountDue
								.getNumber() : 0)));
			} else {
				clientPaymentTerms
						.setDueDays(UIUtils.toInt((this.dialog.netDueBefore
								.getNumber() != null ? this.dialog.netDueBefore
								.getNumber() : 0)));
				clientPaymentTerms
						.setDiscountPercent(this.dialog.discountPerField
								.getAmount() != null ? this.dialog.discountPerField
								.getAmount() : 0);
				clientPaymentTerms
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountPaidBefore
								.getNumber() != null ? this.dialog.discountPaidBefore
								.getNumber() : 0)));
			}
			// clientPaymentTerms
			// .setDescription(dialog.descText.getValue() != null ?
			// dialog.descText
			// .getValue().toString() : "");
			// clientPaymentTerms
			// .setIfPaidWithIn(UIUtils.toInt(dialog.discDayText
			// .getNumber() != null ? dialog.discDayText
			// .getNumber() : "0"));
			// clientPaymentTerms.setDiscountPercent(UIUtils
			// .toDbl(dialog.discText.getPercentage().toString()
			// .replaceAll("%", "") != null ? dialog.discText
			// .getPercentage().toString().replaceAll("%", "")
			// : "0"));
			//
			// for (int i = 0; i < dialog.dueValues.length; i++) {
			// if (dialog.dueSelect.getValue() != null) {
			// if (dialog.dueSelect.getValue().toString()
			// .equals(dialog.dueValues[i]))
			// clientPaymentTerms.setDue(i + 1);
			// }
			// }
			// clientPaymentTerms.setDueDays(UIUtils.toInt(dialog.dayText
			// .getNumber() != null ? dialog.dayText.getNumber() : "0"));
		}
		/*
		 * all these are modified cause after editing clientPaymentTerms was not
		 * getting the values from text field. Now its working
		 */

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
		ClientPaymentTerms paymentTermsByName = company
				.getPaymentTermsByName(name);
		return (paymentTerm == null && paymentTermsByName == null)
				|| (paymentTerm != null && !(paymentTerm.getName()
						.equalsIgnoreCase(name) ? true
						: paymentTermsByName == null));
	}

	@Override
	public Object getGridColumnValue(ClientPaymentTerms paymentTerms, int index) {
		if (paymentTerms != null) {
			switch (index) {
			case 0:
				return paymentTerms.getName();
			case 1:
				return paymentTerms.getDescription();
			case 2:
				return paymentTerms.getDueDays();
				// case 3:
				// return DataUtils.getNumberAsPercentString(paymentTerms
				// .getDiscountPercent()
				// + "");
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.name(), messages.description(),
				messages.dueDays(),
		/* messages.cashDiscount() */};
	}

	@Override
	protected List<ClientPaymentTerms> getRecords() {
		return getCompany().getPaymentsTerms();
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (paymentTerm != null) {
			if (validateName(dialog.payTermText.getValue() != null ? dialog.payTermText
					.getValue().toString() : "")) {
				result.addError(this, messages.paytermsAlreadyExists());
			}
		} else {
			String value = dialog.payTermText.getValue();
			ClientPaymentTerms clientPaymentTerms = company
					.getPaymentTermsByName(value);
			if (clientPaymentTerms != null) {
				result.addError(this, messages.paytermsAlreadyExists());
			}
		}
		return result;
	}

	@Override
	public boolean onOK() {
		ClientPaymentTerms paymentTerms = getPaymentTerms();
		if (paymentTerms != null) {
			saveOrUpdate(paymentTerms);
		} else {
			saveOrUpdate(paymentTerms);
		}
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
