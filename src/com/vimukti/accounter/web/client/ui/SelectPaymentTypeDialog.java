package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */
@SuppressWarnings("unchecked")
public class SelectPaymentTypeDialog extends BaseDialog {
	RadioGroupItem typeRadio;

	public SelectPaymentTypeDialog() {
		super(Accounter.getFinanceUIConstants().selectPaymentType(),
				Accounter.getFinanceUIConstants().selectPaymentType());
		createControls();
		center();

	}

	private void createControls() {
		setText(Accounter.getFinanceUIConstants().selectPaymentType());

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);
		String paymentType;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			paymentType = Accounter.getFinanceUIConstants()
					.vendorPayment();
		} else {
			paymentType = Accounter.getFinanceUIConstants()
					.supplierPayment();
		}

		typeRadio.setValueMap(paymentType, Accounter
				.getFinanceUIConstants().customerRefund());

		final DynamicForm typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		typeForm.setIsGroup(true);
		typeForm.setGroupTitle(Accounter.getFinanceUIConstants()
				.paymentDocuments());
		typeForm.setFields(typeRadio);

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {
				HistoryTokenUtils.setPresentToken(MainFinanceWindow
						.getViewManager().getCurrentView().getAction(),
						MainFinanceWindow.getViewManager().getCurrentView()
								.getData());
			}

			public boolean onOkClick() {
				if (!typeForm.validate(true)) {
					// Accounter.showError(FinanceApplication
					// .getFinanceUIConstants().pleaseSelecPaymentType());
					return false;
				}
				@SuppressWarnings("unused")
				ItemView itemView;

				if (typeRadio.getValue() != null) {
					String radio = typeRadio.getValue().toString();
					String paymentType;
					if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
						paymentType = Accounter
								.getFinanceUIConstants().vendorPayment();
					} else {
						paymentType = Accounter
								.getFinanceUIConstants().supplierPayment();
					}

					if (radio.equals(paymentType)) {
						try {
							HistoryTokenUtils.setPresentToken(
									VendorsActionFactory
											.getNewVendorPaymentAction(), null);
							VendorsActionFactory.getNewVendorPaymentAction()
									.run(null, false);
							;
						} catch (Throwable e) {
							// //UIUtils.logError("Failed...", e);

						}
					} else if (radio.equals(Accounter
							.getFinanceUIConstants().customerRefund())) {

						try {
							HistoryTokenUtils.setPresentToken(
									CustomersActionFactory
											.getCustomerRefundAction(), null);
							CustomersActionFactory.getCustomerRefundAction()
									.run(null, false);
							;
						} catch (Throwable e) {
							// //UIUtils.logError("Failed...", e);
						}
					}
				}
				return true;
			}

		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		okbtn.setWidth("60px");
		cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);
		setWidth("300");
		show();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getFinanceUIConstants().selectPaymentType();
	}
}
