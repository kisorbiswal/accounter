package com.vimukti.accounter.web.client.ui.banking;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class SelectPayeeDialog extends BaseDialog {

	RadioGroupItem typeRadio;
	// private ClientCompany company;

	private final String VENDOR_PAY = UIUtils.getVendorString(Accounter
			.constants().supplierPay(), Accounter.constants().vendorpay());

	private final String CUST_REFUND = Accounter.constants().custRefund();

	private final String EMP_REIMB = Accounter.constants().empreimb();

	public SelectPayeeDialog(AbstractBaseView parent) {
		super(Accounter.constants().selectPayeeType(), Accounter.constants()
				.selectOneOfFollowingPayee());

		// company = FinanceApplication.getCompany();
		createControls();
		center();

	}


	private void createControls() {

		mainPanel.setSpacing(15);

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();

		typeRadio.setValueMap(Accounter.constants().customer(), Accounter
				.constants().supplier());
		typeRadio.setDefaultValue(Accounter.constants().customer());

		DynamicForm typeForm = new DynamicForm();
		// typeForm.setIsGroup(true);
		// typeForm.setGroupTitle("Account Type");
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setSize("350", "220");
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (typeRadio.getValue() != null) {
			result.addError(this, Accounter.constants()
					.pleaseSelecPaymentType());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		String radio = typeRadio.getValue().toString();
		// FIXME--an action is required here
		// okClick();
		if (radio.equals(Accounter.constants().supplier())) {
			// new VendorPaymentsAction("Not Issued").run();
			Action action = ActionFactory.getNewVendorAction();
			action.setActionSource(actionSource);

			action.run(null, true);

		} else if (radio.equals(Accounter.constants().customer())) {
			Action action = ActionFactory.getNewCustomerAction();
			action.setActionSource(actionSource);

			action.run(null, true);

			hide();

		}
		return true;
	}

}
