package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class CashExpenseView extends CashPurchaseView {

	// private ClientAccount pettycash;
	protected List<String> selectedComboList;
	// AccountCombo petycash;
	AccounterConstants accounterConstants = Accounter.constants();

	public CashExpenseView() {
		super(ClientTransaction.TYPE_CASH_EXPENSE);
	}

	@Override
	protected void initViewType() {
		titlelabel.setText(Accounter.constants().cashExpense());
		vendorForm.clear();
		vendorForm.removeFromParent();
		termsForm.clear();
		// petycash = new AccountCombo(Accounter.constants().cashExpense()) {
		// @Override
		// protected List<ClientAccount> getAccounts() {
		// List<ClientAccount> accounts = getCompany().getAccounts();
		// for (ClientAccount acct : accounts) {
		// if (acct.getName().equals(
		// AccounterClientConstants.PETTY_CASH)) {
		// pettycash = acct;
		// return Arrays.asList(acct);
		// }
		// }
		// return null;
		// }
		// };
		// petycash.setHelpInformation(true);
		// petycash.setComboItem(pettycash);
		// petycash.setRequired(true);
		try {
			String listString[] = new String[] {
					Accounter.constants().cash(),
					UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
							.constants().check()),
					Accounter.constants().creditCard(),
					Accounter.constants().directDebit(),
					Accounter.constants().masterCard(),
					Accounter.constants().onlineBanking(),
					Accounter.constants().standingOrder(),
					Accounter.constants().switchMaestro() };
			selectedComboList = new ArrayList<String>();
			for (int i = 0; i < listString.length; i++) {
				selectedComboList.add(listString[i]);
			}
			paymentMethodCombo.initCombo(selectedComboList);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		// payFromCombo.setComboItem(pettycash);

		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		VerticalPanel vPanel = (VerticalPanel) termsForm.getParent();
		termsForm.removeFromParent();
		vPanel.add(termsForm);
		termsForm.getCellFormatter().setWidth(0, 0, "180px");

		// vendorForm.setFields(petycash);
		// VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
		// vendorForm.removeFromParent();
		// verticalPanel.add(vendorForm);

		if (isInViewMode()) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
			// petycash.setComboItem(cashPurchase.getCashExpenseAccount());
			// petycash.setDisabled(true);
			deliveryDateItem.setValue(new ClientFinanceDate(cashPurchase
					.getDeliveryDate()));

		}

		settabIndexes();
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CASH_EXPENSE);

		// transaction.setCashExpenseAccount(petycash.getSelectedValue());

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			transaction.setPhone(phoneNo);

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethod);

		// Setting Pay From Account
		if (payFromAccount != null)
			transaction.setPayFrom(payFromAccount.getID());

		// Setting Check number
		transaction.setCheckNumber(checkNo.getValue().toString());
		// transaction
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		// Setting Total
		transaction.setTotal(vendorTransactionTable.getTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// accounterConstants.invalidateTransactionDate());
		// }
		//
		// if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate))
		// {
		// result.addError(transactionDate,
		// accounterConstants.invalidateDate());
		// }

		if (!payFromCombo.validate()) {
			result.addError(payFromCombo, Accounter.messages()
					.pleaseSelectVendor(Accounter.constants().payFrom()));
		}
		//
		// if (!AccounterValidator.isValidDueOrDelivaryDates(
		// deliveryDateItem.getEnteredDate(), this.transactionDate)) {
		// result.addError(deliveryDateItem, Accounter.constants().the()
		// + " "
		// + Accounter.constants().deliveryDate()
		// + " "
		// + " "
		// + Accounter.constants()
		// .cannotbeearlierthantransactiondate());
		// }
		//
		// if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
		// result.addError(vendorTransactionGrid,
		// accounterConstants.blankTransaction());
		// }
		// result.add(vendorTransactionGrid.validateGrid());

		return result;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
	}

	@Override
	public void enableFormItems() {
		super.enableFormItems();
		// petycash.setDisabled(false);
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button,
				Accounter.messages().accounts(Global.get().account()),
				Accounter.constants().serviceItem());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().cashExpense();
	}

	private void settabIndexes() {
		paymentMethodCombo.setTabIndex(1);
		payFromCombo.setTabIndex(2);
		checkNo.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		memoTextAreaItem.setTabIndex(6);

	}
}
