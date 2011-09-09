package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyView extends BaseView<ClientCurrency> {
	TextItem currencyNameText, formalNameText;
	private AccounterConstants currencyConstants;
	private DynamicForm currencyForm;
	private boolean wait;
	private ClientCompany company = getCompany();
	AccounterConstants accounterConstants = Accounter.constants();

	public CurrencyView() {
		super();

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {

			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}

	}

	/*
	 * private VerticalPanel getGeneralTab() {
	 * 
	 * currencyNameText = new TextItem(currencyConstants.currencyName());
	 * currencyNameText.setHelpInformation(true);
	 * currencyNameText.setWidth(100); currencyNameText.setRequired(true);
	 * 
	 * formalNameText = new TextItem(currencyConstants.currencyFormalName());
	 * formalNameText.setHelpInformation(true); formalNameText.setWidth(100);
	 * formalNameText.setRequired(true);
	 * 
	 * currencyForm = UIUtils.form(currencyConstants.currency());
	 * currencyForm.setFields(currencyNameText, formalNameText);
	 * currencyForm.setWidth("100%");
	 * currencyForm.getCellFormatter().setWidth(0, 0, "205");
	 * 
	 * if (existCurrency != null) {
	 * currencyNameText.setValue(existCurrency.getName());
	 * formalNameText.setValue(existCurrency.getFormalName());
	 * 
	 * } mainPanel.add(currencyForm); return mainPanel;
	 * 
	 * }
	 */

	private void updateData() {
		data.setName(currencyNameText.getValue().toString());
		data.setFormalName(formalNameText.getValue().toString());
	}

	public void saveAndUpdateView() {

		if (!wait) {
			updateData();
			// List<ClientCurrency> list = new ArrayList<ClientCurrency>(
			// company.getCurrencies());
			saveOrUpdate(data);
		}

	}

	@Override
	public ValidationResult validate() {
		ClientCurrency currency = company.getCurrency(currencyNameText
				.getValue().toString());
		ValidationResult result = new ValidationResult();

		// checks whether the currency is already available or not?
		if (currency != null) {
			result.addError(currencyNameText, accounterConstants.alreadyExist());
		}
		return result;
	}

	private void createControls() {
		currencyNameText = new TextItem(currencyConstants.currencyName());
		currencyNameText.setHelpInformation(true);
		currencyNameText.setWidth(100);
		currencyNameText.setRequired(true);

		formalNameText = new TextItem(currencyConstants.currencyFormalName());
		formalNameText.setHelpInformation(true);
		formalNameText.setWidth(100);
		formalNameText.setRequired(true);

		currencyForm = UIUtils.form(currencyConstants.currency());
		currencyForm.setFields(currencyNameText, formalNameText);
		currencyForm.setWidth("100%");
		currencyForm.getCellFormatter().setWidth(0, 0, "205");

		if (getData() != null) {
			currencyNameText.setValue(data.getName());
			formalNameText.setValue(data.getFormalName());
		} else {
			setData(new ClientCurrency());
		}

		VerticalPanel panel = new VerticalPanel();
		panel.add(currencyForm);
		this.add(panel);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
