package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author vimukti5
 * @Implementation Fernandez
 * 
 */

public class WriteOffDialog extends BaseDialog<ClientAccount> {

	List<ClientAccount> allAccounts;
	private ClientAccount selectedWriteOffAccount;
	private AmountField discAmtText;
	private Double writeOffAmount;

	private ClientTransactionReceivePayment record;
	private static AccounterConstants customerConstants = Accounter.constants();
	public DynamicForm form;
	private boolean canEdit;
	OtherAccountsCombo discAccSelect;
	private ICurrencyProvider currencyProvider;

	public void setCashDiscountValue(Double cashDiscountValue) {
		if (cashDiscountValue == null)
			cashDiscountValue = 0.0D;
		this.writeOffAmount = cashDiscountValue;
		discAmtText.setAmount(currencyProvider
				.getAmountInTransactionCurrency(cashDiscountValue));
	}

	public Double getCashDiscountValue() {
		writeOffAmount = currencyProvider.getAmountInBaseCurrency(discAmtText
				.getAmount());
		return writeOffAmount;
	}

	public WriteOffDialog(List<ClientAccount> allAccounts,
			ClientTransactionReceivePayment record, boolean canEdit,
			ClientAccount clientAccount, ICurrencyProvider currencyProvider) {
		super(customerConstants.writeOff(), Accounter.constants()
				.writeOffPleaseAddDetails());
		this.currencyProvider = currencyProvider;
		this.record = record;
		this.allAccounts = allAccounts;
		this.setSelectedWriteOffAccount(clientAccount);
		this.canEdit = canEdit;

		createControls();
		center();
		setCashDiscountValue(record.getWriteOff());

	}

	public WriteOffDialog() {
		super(customerConstants.cashDiscount(), Accounter.constants()
				.writeOffPleaseAddDetails());

		createControls();
	}

	private void createControls() {

		discAccSelect = new OtherAccountsCombo(Accounter.messages()
				.writeOffAccount(Global.get().Account()), false);
		discAccSelect.initCombo(allAccounts);
		discAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setSelectedWriteOffAccount(selectItem);
					}

				});
		discAccSelect.setRequired(true);
		discAccSelect.setDisabled(!canEdit);
		if (getSelectedWriteOffAccount() != null)
			discAccSelect.setComboItem(getSelectedWriteOffAccount());

		discAmtText = new AmountField(customerConstants.writeOffAmount(), this);
		discAmtText.setDisabled(!canEdit);
		setCashDiscountValue(writeOffAmount);

		discAmtText.addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent event) {
				double amount = writeOffAmount != null ? writeOffAmount
						.doubleValue() : 0.0D;
				discAmtText.setAmount(currencyProvider
						.getAmountInTransactionCurrency(amount));
				discAmtText.focusInItem();

			}

		});

		if (!canEdit) {

			// okbtn.hide();
			okbtn.setVisible(false);
			cancelBtn.setTitle(customerConstants.close());

		}

		form = new DynamicForm();
		form.setWidth("100%");
		// form.setWrapItemTitles(false);
		form.setFields(discAccSelect, discAmtText);

		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(30);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(form);

		setBodyLayout(mainVLay);
		setWidth("350px");
		show();
	}

	public ValidationResult validate() {
		// if (getSelectedWriteOffAccount() == null) {
		// AccounterValidator.validateForm(form, true);
		// }
		return form.validate();
	}

	@Override
	public Object getGridColumnValue(ClientAccount obj, int index) {

		return null;
	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	public void setSelectedWriteOffAccount(ClientAccount selectedWriteOffAccount) {
		this.selectedWriteOffAccount = selectedWriteOffAccount;
	}

	public ClientAccount getSelectedWriteOffAccount() {
		return selectedWriteOffAccount;
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

}
