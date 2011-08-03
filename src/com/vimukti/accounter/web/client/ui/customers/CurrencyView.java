package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyView extends BaseView<ClientCurrency> {
	TextItem currencyNameText, formalNameText;
	private ClientCurrency currency;
	private ClientCurrency existCurrency;
	private AccounterConstants currencyConstants;
	private DynamicForm currencyForm;
	private boolean wait;
	private ClientCompany company = getCompany();

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
			saveFailed(new Exception());
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

	private ClientCurrency getCurrencyObject() {

		if (existCurrency != null) {
			currency = existCurrency;

		} else {
			currency = new ClientCurrency();
			currency.setName(currencyNameText.getValue().toString());
			currency.setFormalName(formalNameText.getValue().toString());

		}
		return currency;

	}

	public void saveAndUpdateView() throws Exception {

		if (!wait) {
			try {
				ClientCurrency currency = getCurrencyObject();
				if (existCurrency == null) {
					List<ClientCurrency> list = new ArrayList<ClientCurrency>(
							company.getCurrencies());

					if (!isObjectExist(list, currency))
						createObject(currency);
				} else
					alterObject(currency);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	}

	private boolean isObjectExist(List<ClientCurrency> list,
			ClientCurrency currency2) {

		if (list == null || list.isEmpty())
			return false;
		for (ClientCurrency s : list) {
			if (s.getName() != null
					&& s.getName().toLowerCase()
							.equals(currency2.getName().toLowerCase())) {
				return true;
			}

		}
		return false;
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

		if (existCurrency != null) {
			currencyNameText.setValue(existCurrency.getName());
			formalNameText.setValue(existCurrency.getFormalName());

		}

		VerticalPanel panel = new VerticalPanel();
		panel.add(currencyForm);
		mainPanel.add(panel);

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

}
