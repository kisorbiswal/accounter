package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
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
		super(Accounter.constants().selectPaymentType(), Accounter.constants()
				.selectPaymentType());
		createControls();
		center();

	}

	private void createControls() {
		setText(Accounter.constants().selectPaymentType());

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);
		String paymentType;
		paymentType = Accounter.messages().vendorPayment(Global.get().Vendor());

		typeRadio.setValueMap(paymentType, Accounter.messages().customerRefund(
				Global.get().Customer()));

		final DynamicForm typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		typeForm.setIsGroup(true);
		typeForm.setGroupTitle(Accounter.constants().paymentDocuments());
		typeForm.setFields(typeRadio);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
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
			paymentType = Accounter.messages().vendorPayment(
					Global.get().Vendor());
			if (radio.equals(paymentType)) {
				ActionFactory.getNewVendorPaymentAction().run(null, false);
			} else if (radio.equals(Accounter.messages().customerRefund(
					Global.get().Customer()))) {

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
