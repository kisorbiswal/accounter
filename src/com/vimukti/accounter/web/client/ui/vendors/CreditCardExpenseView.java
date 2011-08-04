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
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreditCardChargeView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class CreditCardExpenseView extends CreditCardChargeView {

	VendorCombo Ccard;
	private int viewFrom = 119;

	public CreditCardExpenseView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		this.validationCount = 5;

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

		if (transactionObject != null) {
			ClientCreditCardCharge creditCardCharge = (ClientCreditCardCharge) transactionObject;
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
	protected ClientCreditCardCharge prepareObject() throws Exception {

		ClientCreditCardCharge creditCardCharge = transactionObject != null ? (ClientCreditCardCharge) transactionObject
				: new ClientCreditCardCharge();
		if (creditCardChargeTaken != null)
			creditCardCharge = creditCardChargeTaken;
		else
			creditCardCharge = new ClientCreditCardCharge();

		// Setting Type
		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		// setting date
		if (transactionDateItem != null)

			creditCardCharge
					.setDate((transactionDateItem.getValue()).getDate());
		// setting number
		if (transactionNumber != null)
			creditCardCharge.setNumber(transactionNumber.getValue().toString());
		ClientVendor vendor = Ccard.getSelectedValue();
		if (vendor != null)
			creditCardCharge.setVendor(vendor.getID());
		// setting contact
		if (contact != null) {
			creditCardCharge.setContact(contact);
		}
		// if (contactNameSelect.getValue() != null) {
		// // ClientContact contact = getContactBasedOnId(contactNameSelect
		// // .getValue().toString());
		// creditCardCharge
		// .setContact(getContactBasedOnId(contactNameSelect
		// .getValue().toString()));
		//
		// }

		// Setting Address
		if (billingAddress != null)
			creditCardCharge.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			creditCardCharge.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		creditCardCharge.setPaymentMethod(paymentMethod);

		// Setting pay from
		payFromAccount = payFrmSelect.getSelectedValue().getID();
		if (payFromAccount != 0)
			creditCardCharge.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());

		// setting check no
		if (cheqNoText.getValue() != null)
			creditCardCharge.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}

		// setting delivery date
		creditCardCharge.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// Setting transactions
		creditCardCharge.setTransactionItems(vendorTransactionGrid
				.getallTransactions(creditCardCharge));

		// setting total
		creditCardCharge.setTotal(vendorTransactionGrid.getTotal());
		// setting memo
		creditCardCharge.setMemo(getMemoTextAreaItem());
		// setting ref
		// creditCardCharge.setReference(UIUtils.toStr(refText.getValue()));

		transactionObject = creditCardCharge;

		return creditCardCharge;
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		switch (this.validationCount) {
		case 5:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 4:
			return AccounterValidator.validateForm(vendorForm, false);
		case 3:
			return AccounterValidator.validateForm(termsForm, false);
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
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
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
