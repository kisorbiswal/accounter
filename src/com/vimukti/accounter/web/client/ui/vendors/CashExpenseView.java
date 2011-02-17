package com.vimukti.accounter.web.client.ui.vendors;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class CashExpenseView extends CashPurchaseView {

	private ClientAccount pettycash;

	AccountCombo petycash;

	public CashExpenseView() {
		super(ClientTransaction.TYPE_CASH_EXPENSE);
		this.validationCount = 6;
	}

	@Override
	protected void initViewType() {
		titlelabel.setText(FinanceApplication.getVendorsMessages()
				.cashExpense());
		vendorForm.clear();
		termsForm.clear();
		petycash = new AccountCombo(FinanceApplication.getVendorsMessages()
				.cashExpense()) {

			@Override
			public SelectItemType getSelectItemType() {
				return SelectItemType.ACCOUNT;
			}

			@Override
			protected List<ClientAccount> getAccounts() {
				List<ClientAccount> accounts = FinanceApplication.getCompany()
						.getAccounts();
				for (ClientAccount acct : accounts) {
					if (acct.getName().equals(AccounterConstants.PETTY_CASH)) {
						pettycash = acct;
						return Arrays.asList(acct);
					}

				}
				return null;
			}

		};
		petycash.setComboItem(pettycash);
		petycash.setRequired(true);

		paymentMethodCombo.setValueMap(FinanceApplication.getVendorsMessages()
				.cash(), UIUtils
				.getpaymentMethodCheckBy_CompanyType(FinanceApplication
						.getCustomersMessages().check()), FinanceApplication
				.getVendorsMessages().creditCard(), FinanceApplication
				.getVendorsMessages().directDebit(), FinanceApplication
				.getVendorsMessages().masterCard(), FinanceApplication
				.getVendorsMessages().onlineBanking(), FinanceApplication
				.getVendorsMessages().standingOrder(), FinanceApplication
				.getVendorsMessages().switchMaestro());
		payFromCombo.setComboItem(pettycash);
		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo);
		VerticalPanel vPanel = (VerticalPanel) termsForm.getParent();
		termsForm.removeFromParent();
		termsForm.setWidth("50%");
		vPanel.add(termsForm);

		// vendorForm.setFields(petycash);
		// VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
		// vendorForm.removeFromParent();
		// verticalPanel.add(vendorForm);

		if (transactionObject != null) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transactionObject;
			petycash.setComboItem(cashPurchase.getCashExpenseAccount());
			petycash.setDisabled(true);
			deliveryDateItem.setValue(new ClientFinanceDate(cashPurchase
					.getDeliveryDate()));

		}
	}

	@Override
	protected ClientCashPurchase prepareObject() {
		ClientCashPurchase cashPurchase = transactionObject != null ? (ClientCashPurchase) transactionObject
				: new ClientCashPurchase();

		// Setting Type
		cashPurchase.setType(ClientTransaction.TYPE_CASH_EXPENSE);

		cashPurchase.setCashExpenseAccount(petycash.getSelectedValue());

		// Setting Contact
		if (contact != null)
			cashPurchase.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			cashPurchase.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			cashPurchase.setPhone(phoneNo);

		// Setting Payment Methods
		cashPurchase.setPaymentMethod(paymentMethod);

		// Setting Pay From Account
		cashPurchase.setPayFrom(payFromAccount.getStringID());

		// Setting Check number
		cashPurchase.setCheckNumber(checkNo.getValue().toString());
		// cashPurchase
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			cashPurchase.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getTime());

		// Setting Total
		cashPurchase.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		cashPurchase.setMemo(getMemoTextAreaItem());
		// Setting Reference
		cashPurchase.setReference(getRefText());
		return cashPurchase;
	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (this.validationCount) {
		case 6:
			return (petycash != null && !petycash.equals(""));
		case 5:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 4:
			return AccounterValidator.validateFormItem(payFromCombo);
		case 3:
			return AccounterValidator.validate_dueOrDelivaryDates(
					deliveryDateItem.getEnteredDate(), this.transactionDate,
					FinanceApplication.getVendorsMessages().deliverydate());
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;
		}

	}

	@Override
	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);
	}

	@Override
	public void enableFormItems() {
		super.enableFormItems();
		petycash.setDisabled(false);
	}

	@Override
	protected void showMenu() {
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(FinanceApplication.getVendorsMessages()
					.nominalCodeItem(), FinanceApplication.getVendorsMessages()
					.service());
		else
			setMenuItems(FinanceApplication.getVendorsMessages()
					.nominalCodeItem(), FinanceApplication.getVendorsMessages()
					.service());
	}
}
