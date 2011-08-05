package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class TaxDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	private final String TAXGROUP = Accounter.constants().taxGroup();
	private final String TAXITEM = Accounter.constants().taxItem();
	private FormItem actionSource;
	// private ViewConfiguration configuration;
	private AccounterConstants vendorsConstants = Accounter.constants();

	public TaxDialog() {
		super(Accounter.constants().tax(), Accounter.constants()
				.selectOneType());
		setText(Accounter.constants().tax());
		createControls();
		center();
	}

	public <T> TaxDialog(AccounterAsyncCallback<T> callBack) {
		super(Accounter.constants().tax(), Accounter.constants()
				.selectOneType());
		setText(Accounter.constants().tax());
		createControls();
		center();
	}

	public <T> TaxDialog(AccounterAsyncCallback<T> callBack,
			FormItem actionSource) {
		super(Accounter.constants().tax(), Accounter.constants()
				.selectOneType());
		this.actionSource = actionSource;
		setText(Accounter.constants().tax());
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
							Action action = ActionFactory
									.getManageSalesTaxGroupsAction();
							action.setActionSource(actionSource);

							action.run(null, true);
						} catch (Throwable e) {
							Accounter.showError(Accounter.constants()
									.failedToloadTaxGroup()

							);
							e.printStackTrace();
						}

					} else if (radio.equals(TAXITEM)) {
						try {
							Action action = ActionFactory.getNewVatItemAction();
							action.setActionSource(actionSource);

							action.run(null, true);
							// ActionFactory.getNewVatItemAction().run(null,
							// true);
						} catch (Throwable e) {
							Accounter.showError(Accounter.constants()
									.failedToloadTaxItem());
							e.printStackTrace();

						}

					} else {
						Accounter.showError(Accounter.constants()
								.pleaseSelectTaxType());
					}

				}
				removeFromParent();
				return true;
			}

			@Override
			public void onCancelClick() {
				removeFromParent();
				// Action.cancle();
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

}
