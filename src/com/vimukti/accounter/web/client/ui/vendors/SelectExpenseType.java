package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

@SuppressWarnings("unchecked")
public class SelectExpenseType extends BaseDialog {
	RadioGroupItem typeRadio;
	private final String CHECK = Accounter.getVendorsMessages().check();
	private final String CREDIT_CARD = Accounter.getVendorsMessages()
			.creditCard();

	private final String CASH = Accounter.getVendorsMessages().cash();
	private final String EMPLOYEE = Accounter.getVendorsMessages().employee();
	// private ViewConfiguration configuration;
	private VendorsMessages vendorsConstants = GWT
			.create(VendorsMessages.class);

	public SelectExpenseType() {
		super(Accounter.getVendorsMessages().recordExpenses(), "");
		setText(Accounter.getVendorsMessages().recordExpenses());
		createControls();
		center();
	}

	public SelectExpenseType(AsyncCallback<IAccounterCore> callBack) {
		super(Accounter.getVendorsMessages().recordExpenses(), "");
		this.callBack = callBack;
		setText(Accounter.getVendorsMessages().recordExpenses());
		createControls();
		center();
	}

	private void createControls() {
		mainPanel.setSpacing(3);
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setValue(EMPLOYEE, CREDIT_CARD, CASH);
		DynamicForm typeForm = new DynamicForm();
		typeForm.setWidth("100%");
		typeForm.setIsGroup(true);

		typeForm.setGroupTitle(vendorsConstants.selectHowYouPaidForExpense());
		typeForm.setFields(typeRadio);

		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				if (typeRadio.getValue() != null) {
					String radio = typeRadio.getValue().toString();
					if (radio.equals(EMPLOYEE)) {
						VendorsActionFactory.EmployeeExpenseAction().run(null,
								false);
					} else if (radio.equals(CHECK)) {
						try {
							BankingActionFactory.getWriteChecksAction().run(
									null, false);
						} catch (Throwable e) {
							Accounter.showError(Accounter.getVendorsMessages()
									.failedToloadWriteCheck()

							);
							e.printStackTrace();
						}

					} else if (radio.equals(CREDIT_CARD)) {
						try {
							VendorsActionFactory.CreditCardExpenseAction().run(
									null, false);
						} catch (Throwable e) {
							Accounter.showError(Accounter.getVendorsMessages()
									.failedToLoadCreditCardCharg());
							e.printStackTrace();

						}

					} else if (radio.equals(CASH)) {
						try {
							VendorsActionFactory.CashExpenseAction().run(null,
									false);
						} catch (Throwable e) {
							Accounter.showError(Accounter.getVendorsMessages()
									.failedToLoadCashPurchase());
							e.printStackTrace();
						}
					} else if (radio.equals(EMPLOYEE)) {
						try {
							VendorsActionFactory.EmployeeExpenseAction().run(
									null, false);
						} catch (Throwable e) {
							Accounter.showError(Accounter.getVendorsMessages()
									.failedToLoadCashPurchase());
							e.printStackTrace();
						}
					} else {
						Accounter.showError(Accounter.getVendorsMessages()
								.pleaseSelectExpenseType());
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

		// okbtn.setWidth("60px");
		// cancelBtn.setWidth("60px");

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

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getVendorsMessages().recordExpenses();
	}

	// setTitle(vendorsConstants.selectExpenseType());
}
