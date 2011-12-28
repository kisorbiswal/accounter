package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class PaymentDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	private final String RECEIVE_PAYMENT = messages.receivePayment();
	private final String CUSTOMER_PREPAYMENT = messages
			.payeePrePayment(Global.get().Customer());

	public PaymentDialog() {
		super(messages.payments(), "");
		setText(messages.payments());
		createControls();
		center();
	}

	public PaymentDialog(AccounterAsyncCallback<IAccounterCore> callBack) {
		super(messages.payments(), "");
		setText(messages.payments());
		createControls();
		center();
	}

	public void createControls() {
		mainPanel.setSpacing(3);
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);

		typeRadio.setValue(RECEIVE_PAYMENT, CUSTOMER_PREPAYMENT);
		typeRadio.setDefaultValue(RECEIVE_PAYMENT);
		DynamicForm typeForm = new DynamicForm();
		typeForm.setWidth("100%");
		typeForm.setIsGroup(true);

		typeForm.setGroupTitle(messages.setPaymentType());
		typeForm.setFields(typeRadio);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("300px");

	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();
			if (radio.equals(RECEIVE_PAYMENT)) {
				ActionFactory.getReceivePaymentAction().run(null, false);
			} else if (radio.equals(CUSTOMER_PREPAYMENT)) {
				ActionFactory.getNewCustomerPaymentAction().run(null, false);
			}
		}
		removeFromParent();
		return true;
	}
}
