package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreditCardChargeView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class CreditCardExpenseView extends CreditCardChargeView {

	VendorCombo Ccard;
	private int viewFrom = 119;

	public CreditCardExpenseView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

	}

	@Override
	protected void initViewType() {

		titlelabel.setText(Accounter.constants().creditCardExpense());

		vendorForm.clear();
		termsForm.clear();
		Ccard = new VendorCombo(Accounter.constants().ccCompany(), true) {
			@Override
			public void initCombo(List<ClientVendor> list) {
				Iterator<ClientVendor> iterator = list.iterator();
				while (iterator.hasNext()) {
					ClientVendor vdr = iterator.next();
					if (vdr.getVendorGroup() != 0) {
						ClientVendorGroup vendorGrougp = Accounter.getCompany()
								.getVendorGroup(vdr.getVendorGroup());
						if (!vendorGrougp.getName().equals(
								AccounterConstants.CREDIT_CARD_COMPANIES)) {
							iterator.remove();
						}
					} else {
						iterator.remove();
					}

				}
				super.initCombo(list);
			}

			@Override
			public void onAddNew() {
				NewVendorAction action = ActionFactory.getNewVendorAction();

				action.setActionSource(this);
				action.setOpenedFrom(viewFrom);

				action.run(null, true);

			}
		};
		Ccard.setHelpInformation(true);
		Ccard.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

			@Override
			public void selectedComboBoxItem(ClientVendor selectItem) {
				selectedVendor = selectItem;
				Ccard.setComboItem(selectItem);
				addPhonesContactsAndAddress();
			}
		});

		Ccard.setRequired(true);
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
		payMethSelect.initCombo(selectedComboList);

		termsForm.setFields(payMethSelect, payFrmSelect, cheqNoText, delivDate);
		HorizontalPanel hPanel = (HorizontalPanel) termsForm.getParent();
		termsForm.removeFromParent();
		termsForm.setWidth("100%");
		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");
		hPanel.add(termsForm);

		if (isEdit) {
			ClientCreditCardCharge creditCardCharge = (ClientCreditCardCharge) transaction;
			Ccard.setComboItem(getCompany().getVendor(
					creditCardCharge.getVendor()));
			Ccard.setDisabled(true);
		}
		vendorForm.setFields(Ccard, contactNameSelect, phoneSelect,
				billToAreaItem);
		vendorForm.getCellFormatter().setWidth(0, 0, "180px");
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
		vendorForm.removeFromParent();
		verticalPanel.add(vendorForm);
		// verticalPanel.setSpacing(10);

	}

	@Override
	protected void updateTransaction() {

		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		// setting date
		if (transactionDateItem != null)

			transaction.setDate((transactionDateItem.getValue()).getDate());
		// setting number
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());
		ClientVendor vendor = Ccard.getSelectedValue();
		if (vendor != null)
			transaction.setVendor(vendor.getID());
		// setting contact
		if (contact != null) {
			transaction.setContact(contact);
		}
		// if (contactNameSelect.getValue() != null) {
		// // ClientContact contact = getContactBasedOnId(contactNameSelect
		// // .getValue().toString());
		// transaction
		// .setContact(getContactBasedOnId(contactNameSelect
		// .getValue().toString()));
		//
		// }

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		transaction.setPaymentMethod(paymentMethod);

		// Setting pay from
		payFromAccount = payFrmSelect.getSelectedValue().getID();
		if (payFromAccount != 0)
			transaction.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());

		// setting check no
		if (cheqNoText.getValue() != null)
			transaction.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}

		// setting delivery date
		transaction.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// Setting transactions
		transaction.setTransactionItems(vendorTransactionGrid
				.getallTransactions(transaction));

		// setting total
		transaction.setTotal(vendorTransactionGrid.getTotal());
		// setting memo
		transaction.setMemo(getMemoTextAreaItem());
		// setting ref
		// transaction.setReference(UIUtils.toStr(refText.getValue()));

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (!AccounterValidator.validateTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					AccounterErrorType.InvalidTransactionDate);
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					AccounterErrorType.InvalidTransactionDate);
		}

		result.add(vendorForm.validate());
		result.add(termsForm.validate());
		if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
			result.addError(vendorTransactionGrid,
					AccounterErrorType.blankTransaction);
		}
		result.add(vendorTransactionGrid.validateGrid());
		return result;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
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

	public void enableFormItems() {
		super.enableFormItems();
		Ccard.setDisabled(false);
	}

	@Override
	protected void showMenu(AccounterButton button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.constants().accounts(), Accounter
					.constants().serviceItem());
		else
			setMenuItems(button, Accounter.constants().accounts(), Accounter
					.constants().serviceItem());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().creditCardExpense();
	}
}
