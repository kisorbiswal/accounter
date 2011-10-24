package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CurrencyListCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * this dialog is used to display all the currencies list in a combo box
 */
public class NewCurrencyListDialog extends BaseDialog {

	private AccounterConstants constants = Global.get().constants();
	private ClientCurrency clientCurrency;
	private CurrencyListCombo listCombo;
	private CurrencyGroupListDialog parent;

	public NewCurrencyListDialog(CurrencyGroupListDialog parent, String text,
			ClientCurrency clientCurrency) {
		super(text, "");
		this.parent = parent;
		this.clientCurrency = clientCurrency;
		setWidth("100px");
		createControl();
	}

	private void createControl() {
		VerticalPanel panel = new VerticalPanel();
		DynamicForm form = new DynamicForm();

		listCombo = new CurrencyListCombo(constants.addCurrency());
		if (clientCurrency != null) {
			listCombo.setComboItem(clientCurrency);
		} else {
			clientCurrency = new ClientCurrency();
		}
		listCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {
					public void selectedComboBoxItem(ClientCurrency item) {

						clientCurrency.setName(item.getName());
						clientCurrency.setFormalName(item.getFormalName());
						clientCurrency.setSymbol(item.getSymbol());

					};
				});
		form.setFields(listCombo);
		panel.add(form);

		okbtn.setText(constants.addCurrency());
		okbtn.setWidth("150px");

		setBodyLayout(panel);

	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (clientCurrency == null) {
			result.addError(this, Accounter.constants().selectcurrency());

		} else {
			String value = clientCurrency.getName();
			for (ClientCurrency currency : company.getCurrencies()) {
				if (currency.getName().equalsIgnoreCase(value)
						&& currency.getID() != clientCurrency.getID()) {
					result.addError(this, Accounter.constants()
							.CurrencyAlreadyExists());
				}
			}

		}
		return result;
	}

	@Override
	protected boolean onOK() {

		return parent.onOK();

	}

	public ClientCurrency createOrEditItemGroup() {
		if (listCombo.getSelectedValue() != null) {
			// saveOrUpdate(clientCurrency);
			return clientCurrency;
		}
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
