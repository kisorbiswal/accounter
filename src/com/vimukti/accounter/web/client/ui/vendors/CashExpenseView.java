package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class CashExpenseView extends CashPurchaseView {

	private ClientAccount pettycash;
	protected List<String> selectedComboList;
	AccountCombo petycash;

	public CashExpenseView() {
		super(ClientTransaction.TYPE_CASH_EXPENSE);
		this.validationCount = 6;
	}

	@Override
	protected void initViewType() {
		titlelabel.setText(Accounter.constants().cashExpense());
		vendorForm.clear();
		vendorForm.removeFromParent();
		termsForm.clear();
		petycash = new AccountCombo(Accounter.constants()
				.cashExpense()) {

			@Override
			public SelectItemType getSelectItemType() {
				return SelectItemType.ACCOUNT;
			}

			@Override
			protected List<ClientAccount> getAccounts() {
				List<ClientAccount> accounts = getCompany().getAccounts();
				for (ClientAccount acct : accounts) {
					if (acct.getName().equals(AccounterConstants.PETTY_CASH)) {
						pettycash = acct;
						return Arrays.asList(acct);
					}

				}
				return null;
			}

		};
		petycash.setHelpInformation(true);
		petycash.setComboItem(pettycash);
		petycash.setRequired(true);
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

		payFromCombo.setComboItem(pettycash);
		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo);
		VerticalPanel vPanel = (VerticalPanel) termsForm.getParent();
		termsForm.removeFromParent();
		vPanel.add(termsForm);
		termsForm.getCellFormatter().setWidth(0, 0, "180px");

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
		cashPurchase.setPayFrom(payFromAccount.getID());

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
		// cashPurchase.setReference(getRefText());
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
			return AccounterValidator.validateFormItem(payFromCombo, false);
		case 3:
			return AccounterValidator.validate_dueOrDelivaryDates(
					deliveryDateItem.getEnteredDate(), this.transactionDate,
					Accounter.constants().deliverydate());
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
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);
	}

	@Override
	public void enableFormItems() {
		super.enableFormItems();
		petycash.setDisabled(false);
	}

	@Override
	protected void showMenu(AccounterButton button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.constants().accounts(),
					Accounter.constants().service());
		else
			setMenuItems(button, Accounter.constants().accounts(),
					Accounter.constants().service());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().cashExpense();
	}
}
