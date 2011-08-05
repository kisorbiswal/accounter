package com.vimukti.accounter.web.client.ui.banking;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class SelectPayeeDialog extends BaseDialog {

	FormItem actionSource;
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

	public SelectPayeeDialog(AbstractBaseView parent, FormItem actionSource) {
		super(Accounter.constants().selectPayeeType(), Accounter.constants()
				.selectOneOfFollowingPayee());
		this.actionSource = actionSource;
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

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {
				removeFromParent();
				// Action.cancle();
			}

			public boolean onOkClick() {
				if (typeRadio.getValue() != null) {
					String radio = typeRadio.getValue().toString();
					// FIXME--an action is required here
					// okClick();
					if (radio.equals(Accounter.constants().supplier())) {
						// new VendorPaymentsAction("Not Issued").run();

						try {
							Action action = ActionFactory.getNewVendorAction();
							action.setActionSource(actionSource);

							action.run(null, true);

						} catch (Throwable e) {
							// //UIUtils.logError("Failed to Load Vendor View",
							// e);
						}

					} else if (radio.equals(Accounter.constants().customer())) {
						try {
							Action action = ActionFactory
									.getNewCustomerAction();
							action.setActionSource(actionSource);

							action.run(null, true);

							hide();

						} catch (Throwable e) {
							// //UIUtils.logError("Failed to Load Customer View",
							// e);
						}

					}
				} else {
					Accounter.showError(Accounter.constants()
							.pleaseSelecPaymentType());
				}
				return true;
			}

		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setSize("350", "220");
	}

}
