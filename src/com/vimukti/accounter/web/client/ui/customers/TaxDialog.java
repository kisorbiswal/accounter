package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;
import com.vimukti.accounter.web.client.ui.vendors.VendorsMessages;

@SuppressWarnings("unchecked")
public class TaxDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	private final String TAXGROUP = FinanceApplication.getCustomersMessages()
			.taxGroup();
	private final String TAXITEM = FinanceApplication.getCustomersMessages()
			.taxItem();
	private FormItem actionSource;
	// private ViewConfiguration configuration;
	private VendorsMessages vendorsConstants = GWT
			.create(VendorsMessages.class);

	public TaxDialog() {
		super(FinanceApplication.getCustomersMessages().tax(),
				FinanceApplication.getCompanyMessages().selectOneType());
		setText(FinanceApplication.getCustomersMessages().tax());
		createControls();
		center();
	}

	public <T> TaxDialog(AsyncCallback<T> callBack) {
		super(FinanceApplication.getCustomersMessages().tax(),
				FinanceApplication.getCompanyMessages().selectOneType());
		this.callBack = callBack;
		setText(FinanceApplication.getCustomersMessages().tax());
		createControls();
		center();
	}

	public <T> TaxDialog(AsyncCallback<T> callBack, FormItem actionSource) {
		super(FinanceApplication.getCustomersMessages().tax(),
				FinanceApplication.getCompanyMessages().selectOneType());
		this.callBack = callBack;
		this.actionSource = actionSource;
		setText(FinanceApplication.getCustomersMessages().tax());
		createControls();
		center();
	}

	private void createControls() {
		mainPanel.setSpacing(3);
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setValue(TAXGROUP, TAXITEM);
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
					if (radio.equals(TAXGROUP)) {
						try {
							Action action = CompanyActionFactory
									.getManageSalesTaxGroupsAction();
							action.setActionSource(actionSource);
							action.run(null, true);
						} catch (Throwable e) {
							Accounter.showError(FinanceApplication
									.getCustomersMessages()
									.failedToloadTaxGroup()

							);
							e.printStackTrace();
						}

					} else if (radio.equals(TAXITEM)) {
						try {
							Action action = VatActionFactory
									.getNewVatItemAction();
							action.setActionSource(actionSource);
							action.run(null, true);
							// VatActionFactory.getNewVatItemAction().run(null,
							// true);
						} catch (Throwable e) {
							Accounter.showError(FinanceApplication
									.getCustomersMessages()
									.failedToloadTaxItem());
							e.printStackTrace();

						}

					} else {
						Accounter.showError(FinanceApplication
								.getCustomersMessages().pleaseSelectTaxType());
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
		return FinanceApplication.getCustomersMessages().tax();
	}

}
