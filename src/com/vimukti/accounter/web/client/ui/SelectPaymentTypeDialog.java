package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class SelectPaymentTypeDialog extends BaseDialog {
	RadioGroupItem typeRadio;

	public SelectPaymentTypeDialog() {
		super(messages.selectPaymentType(), messages.selectPaymentType());
		createControls();
		center();

	}

	private void createControls() {
		setText(messages.selectPaymentType());

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);
		String paymentType;
		paymentType = messages.payeePayment(Global.get().Vendor());

		typeRadio.setValueMap(paymentType,
				messages.customerRefund(Global.get().Customer()));

		typeRadio.setDefaultValue(paymentType);
		final DynamicForm typeForm = new DynamicForm("typeForm");
		typeForm.add(typeRadio);
		typeForm.setWidth("100%");

		typeForm.add(typeRadio);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(typeForm);

		okbtn.setWidth("60px");
		cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);
		setWidth("300px");
		show();
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();
			String paymentType;
			paymentType = messages.payeePayment(Global.get().Vendor());
			if (radio.equals(paymentType)) {
				ActionFactory.getNewVendorPaymentAction().run(null, false);
			} else if (radio.equals(messages.customerRefund(Global.get()
					.Customer()))) {

				ActionFactory.getCustomerRefundAction().run(null, false);
			}
		}
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
