package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author vimukti5
 * @Implementation Fernandez
 * 
 */

public class WriteOffDialog extends BaseDialog {

	List<ClientAccount> allAccounts;
	private ClientAccount selectedWriteOffAccount;
	private AmountField discAmtText;
	private Double writeOffAmount;
	
	private ClientTransactionReceivePayment record;
	private static AccounterConstants customerConstants = Accounter.constants();
	public DynamicForm form;
	private boolean canEdit;
	OtherAccountsCombo discAccSelect;

	public void setCashDiscountValue(Double cashDiscountValue) {
		if (cashDiscountValue == null)
			cashDiscountValue = 0.0D;
		this.writeOffAmount = cashDiscountValue;
		// StringBuffer buffer = new StringBuffer("" +
		// UIUtils.getCurrencySymbol()
		// + "");
		// buffer.append(cashDiscountValue != null ? String
		// .valueOf(cashDiscountValue) : "0.00");
		discAmtText.setAmount(cashDiscountValue);
	}

	public Double getCashDiscountValue() {
		writeOffAmount = discAmtText.getAmount();
		return writeOffAmount;
	}

	public WriteOffDialog(List<ClientAccount> allAccounts,
			ClientTransactionReceivePayment record, boolean canEdit,
			ClientAccount clientAccount) {
		super(customerConstants.writeOff(), Accounter.constants()
				.writeOffPleaseAddDetails());
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

		discAccSelect = new OtherAccountsCombo(
				customerConstants.writeOffAccount(), false);
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

		discAmtText = new AmountField(customerConstants.writeOffAmount());
		discAmtText.setDisabled(!canEdit);
		setCashDiscountValue(writeOffAmount);

		discAmtText.addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent event) {

				discAmtText.setAmount(writeOffAmount != null ? writeOffAmount
						.doubleValue() : 0.0D);
				discAmtText.focusInItem();

			}

		});
		// discAmtText.addBlurHandler(new BlurHandler() {
		// public void onBlur(BlurEvent event) {
		// try {
		//
		// String valueStr = discAmtText.getValue().toString()
		// .replace("" + UIUtils.getCurrencySymbol() + "", "");
		//
		// Double amount = Double.parseDouble(valueStr);
		//
		// setCashDiscountValue(amount);
		//
		// } catch (Exception e) {
		// setCashDiscountValue(null);
		// }
		//
		// }
		// });

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
		setWidth("350");
		show();
	}

	public boolean validate() throws InvalidEntryException {
		if (getSelectedWriteOffAccount() == null) {
			AccounterValidator.validateForm(form, true);
		}
		return true;
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {

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
		if (core.getID() == this.discAccSelect.getSelectedValue().getID()) {
			this.discAccSelect.addItemThenfireEvent((ClientAccount) core);
		}
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().writeOff();
	}
}
