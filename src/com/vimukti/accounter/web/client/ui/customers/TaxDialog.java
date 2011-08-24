package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;

public class TaxDialog extends BaseDialog<ClientTAXCode> {
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

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		// okbtn.setWidth("60px");
		// cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);
		setWidth("300px");

	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String radio = typeRadio.getValue().toString();
		if (!radio.equals(TAXGROUP) && !radio.equals(TAXITEM)) {
			result.addError(this, Accounter.constants().pleaseSelectTaxType());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();

			if (radio.equals(TAXGROUP)) {
				// try {
				ManageSalesTaxGroupsAction action = ActionFactory
						.getManageSalesTaxGroupsAction();
				action.setCallback(new ActionCallback<ClientTAXGroup>() {

					@Override
					public void actionResult(ClientTAXGroup result) {
						setResult(getCompany().getTAXCode(result.getID()));
					}
				});

				action.run(null, true);

			} else if (radio.equals(TAXITEM)) {
				// try {
				NewVatItemAction action = ActionFactory.getNewVatItemAction();
				action.setCallback(new ActionCallback<ClientTAXItem>() {

					@Override
					public void actionResult(ClientTAXItem result) {
						setResult(getCompany().getTAXCode(result.getID()));
					}
				});

				action.run(null, true);

			}

		}
		hide();
		return true;
	}

}
