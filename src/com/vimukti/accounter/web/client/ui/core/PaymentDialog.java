package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

@SuppressWarnings("unchecked")
public class PaymentDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);
	private final String RECEIVE_PAYMENT = customerConstants.receivePayment();
	private final String CUSTOMER_PREPAYMENT = customerConstants
			.customerPrePayment();

	public PaymentDialog() {
		super(FinanceApplication.getCustomersMessages().payments(), "");
		setText(FinanceApplication.getCustomersMessages().payments());
		createControls();
		center();
	}

	public PaymentDialog(AsyncCallback<IAccounterCore> callBack) {
		super(FinanceApplication.getCustomersMessages().payments(), "");
		setText(FinanceApplication.getCustomersMessages().payments());
		createControls();
		center();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	public void createControls() {
		mainPanel.setSpacing(3);
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setValue(RECEIVE_PAYMENT, CUSTOMER_PREPAYMENT);
		DynamicForm typeForm = new DynamicForm();
		typeForm.setWidth("100%");
		typeForm.setIsGroup(true);

		typeForm.setGroupTitle(customerConstants.setPaymentType());
		typeForm.setFields(typeRadio);

		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				if (typeRadio.getValue() != null) {
					String radio = typeRadio.getValue().toString();
					if (radio.equals(RECEIVE_PAYMENT)) {
						try {
							HistoryTokenUtils.setPresentToken(
									CustomersActionFactory
											.getReceivePaymentAction(), null);
							CustomersActionFactory.getReceivePaymentAction()
									.run(null, false);
						} catch (Throwable e) {
							Accounter.showError(FinanceApplication
									.getVendorsMessages()
									.failedToloadWriteCheck()

							);
							e.printStackTrace();
						}

					} else if (radio.equals(CUSTOMER_PREPAYMENT)) {
						try {
							HistoryTokenUtils.setPresentToken(
									CustomersActionFactory
											.getNewCustomerPaymentAction(),
									null);
							CustomersActionFactory
									.getNewCustomerPaymentAction().run(null,
											false);
						} catch (Throwable e) {
							Accounter.showError(FinanceApplication
									.getVendorsMessages()
									.failedToLoadCreditCardCharg());
							e.printStackTrace();

						}

					}
				}
				removeFromParent();
				return true;
			}

			@Override
			public void onCancelClick() {
				removeFromParent();
			}
		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("300");

	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	// setTitle(customerConstants.selectPaymentType());

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getCustomersMessages().payments();
	}
}
