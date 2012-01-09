package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CurrencyListCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * this dialog is used to display all the currencies list in a combo box
 */
public class NewCurrencyListDialog extends BaseDialog<ClientCurrency> {

	private ClientCurrency clientCurrency;
	private CurrencyListCombo listCombo;
	private final CurrencyGroupListDialog parent;
	private TextItem currencySymbolItem;

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
		currencySymbolItem = new TextItem(messages.currencySymbol());
		listCombo = new CurrencyListCombo(messages.addCurrency());
		if (clientCurrency != null) {
			listCombo.setComboItem(clientCurrency);
			listCombo.setDisabled(true);
			okbtn.setText(messages.editCurrency());
		} else {
			clientCurrency = new ClientCurrency();
			okbtn.setText(messages.addCurrency());
		}
		if (clientCurrency.getSymbol() != null) {
			currencySymbolItem.setValue(clientCurrency.getSymbol());
		}
		listCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {
					@Override
					public void selectedComboBoxItem(ClientCurrency item) {

						clientCurrency.setName(item.getName());
						clientCurrency.setFormalName(item.getFormalName());
						clientCurrency.setSymbol(item.getSymbol());
						currencySymbolItem.setValue(clientCurrency.getSymbol());
					};
				});
		form.setFields(listCombo, currencySymbolItem);
		panel.add(form);

		okbtn.setWidth("150px");

		setBodyLayout(panel);

	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (clientCurrency == null) {
			result.addError(this, messages.selectcurrency());

		} else {
			String value = clientCurrency.getName();

			for (ClientCurrency currency : company.getCurrencies()) {
				if (currency.getName() != null)
					if (currency.getName().equalsIgnoreCase(value)
							&& currency.getID() != clientCurrency.getID()) {
						result.addError(this, messages.CurrencyAlreadyExists());
					}
			}

		}
		return result;
	}

	@Override
	protected boolean onOK() {
		clientCurrency.setSymbol(currencySymbolItem.getValue());
		return parent.onOK();

	}

	public ClientCurrency createOrEditItemGroup() {
		if (listCombo.getSelectedValue() != null) {
			// saveOrUpdate(clientCurrency);
			if (clientCurrency.getFormalName().equals(
					getCompany().getPreferences().getPrimaryCurrency()
							.getFormalName())) {
				Accounter.getCompany().getPreferences()
						.setPrimaryCurrency(clientCurrency);
				Accounter.updateCompany(NewCurrencyListDialog.this,
						Accounter.getCompany());
			}
			return clientCurrency;
		}
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
