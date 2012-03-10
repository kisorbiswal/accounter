package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class CurrencyGroupListDialog extends GroupDialog<ClientCurrency> {
	private GroupDialogButtonsHandler buttonsHandler;
	private NewCurrencyListDialog currencyListDialog;
	private ClientCurrency clientCurrency;

	public CurrencyGroupListDialog(String title, String desc) {
		super(title, desc);
//		setWidth("400px");
		createLayout();
		center();
	}

	private void createLayout() {
		getGrid().setType(AccounterCoreType.CURRENCY);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientCurrency>() {
					@Override
					public boolean onRecordClick(ClientCurrency core, int column) {

						ClientCurrency currency = (ClientCurrency) core;
						if (currency != null)
							enableEditRemoveButtons(true);
						else
							enableEditRemoveButtons(false);
						return false;
					}
				});

		buttonsHandler = new GroupDialogButtonsHandler() {

			@Override
			public void onThirdButtonClick() {
				if (getDeleteObject() == null) {
					return;
				}
				deleteObject(getDeleteObject());
			}

			@Override
			public void onSecondButtonClick() {
				ClientCurrency selection = (ClientCurrency) listGridView
						.getSelection();
				ShowAddEditDialog(selection);
			}

			@Override
			public void onFirstButtonClick() {
				ShowAddEditDialog(null);
			}

			@Override
			public void onCloseButtonClick() {

			}
		};
		addGroupButtonsHandler(buttonsHandler);
		okbtn.setVisible(false);
	}

	private ClientCurrency getDeleteObject() {
		ClientCurrency currency = (ClientCurrency) listGridView.getSelection();
		String primaryCurrencyString = getCompany().getPrimaryCurrency()
				.getFormalName();

		if (currency.getFormalName().toLowerCase()
				.contains(primaryCurrencyString.toLowerCase())) {
			Accounter.showError(messages.CannotDeletePrimaryCurrency());
		} else {
			return (ClientCurrency) listGridView.getSelection();
		}
		return null;
	}

	public void ShowAddEditDialog(ClientCurrency currency) {
		clientCurrency = currency;
		currencyListDialog = new NewCurrencyListDialog(this,
				messages.addCurrency(), clientCurrency);
		currencyListDialog.show();
		currencyListDialog.center();

	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.currency() };
	}

	@Override
	protected List<ClientCurrency> getRecords() {
		return new ArrayList<ClientCurrency>(getCompany().getCurrencies());
	}

	@Override
	public Object getGridColumnValue(ClientCurrency obj, int index) {
		ClientCurrency currency = (ClientCurrency) obj;
		if (currency != null) {
			switch (index) {
			case 0:
				return currency.getName();
			}
		}
		return super.getGridColumnValue(obj, index);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		clientCurrency = (ClientCurrency) listGridView.getSelection();
		ClientCurrency currency = getCompany().getPrimaryCurrency();
		if (currency != null && clientCurrency.getID() == currency.getID()) {
			result.addError(this, messages.CannotDeletePrimaryCurrency());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		saveOrUpdate(currencyListDialog.createOrEditItemGroup());
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
