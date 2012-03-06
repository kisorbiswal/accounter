package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomerMergeDialog extends BaseView<ClientCustomer> {

	private DynamicForm form;
	private DynamicForm form1;

	private CustomerCombo customerCombo;
	private CustomerCombo customerCombo1;
	private TextItem customerIDTextItem;
	private TextItem customerIDTextItem1;

	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;
	private CheckboxItem status;
	private CheckboxItem status1;

	private ClientCustomer clientCustomer;
	private ClientCustomer clientCustomer1;

	public CustomerMergeDialog() {

	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("accountMergeDialog");
		createControls();
		saveAndNewButton.setVisible(false);
		saveAndCloseButton.setText(messages.merge());
		setSize("100%", "100%");
		clientCustomer1 = null;
		clientCustomer = null;
	}

	private void createControls() {

		form = new DynamicForm("firstForm");
		form1 = new DynamicForm("secondForm");

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		customerCombo = createCustomerCombo();
		customerCombo1 = createCustomerCombo1();

		customerIDTextItem = new TextItem(messages.payeeID(Global.get()
				.Customer()), "customerIDTextItem");

		customerIDTextItem1 = new TextItem(messages.payeeID(Global.get()
				.Customer()), "customerIDTextItem");

		status = new CheckboxItem(messages.active(), "status");
		status.setValue(false);

		status1 = new CheckboxItem(messages.active(), "status");
		status1.setValue(false);

		balanceTextItem = new TextItem(messages.balance(), "balanceTextItem");

		balanceTextItem1 = new TextItem(messages.balance(), "balanceTextItem");
		customerCombo.setRequired(true);
		customerCombo1.setRequired(true);
		form.add(customerCombo, customerIDTextItem, status, balanceTextItem);
		form1.add(customerCombo1, customerIDTextItem1, status1,
				balanceTextItem1);

		horizontalPanel.add(form);
		horizontalPanel.add(form1);
		this.add(horizontalPanel);

	}

	private CustomerCombo createCustomerCombo1() {

		customerCombo1 = new CustomerCombo(messages.payeeTo(Global.get()
				.Customer()), false);
		customerCombo1.setRequired(true);

		customerCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						clientCustomer1 = selectItem;
						customerSelected1(selectItem);

					}

				});

		return customerCombo1;
	}

	private CustomerCombo createCustomerCombo() {

		customerCombo = new CustomerCombo(messages.payeeFrom(Global.get()
				.Customer()), false);

		customerCombo = new CustomerCombo(messages.payeeFrom(Global.get()
				.Customer()), false);

		customerCombo.setRequired(true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						clientCustomer = selectItem;
						customerSelected(selectItem);

					}

				});

		return customerCombo;
	}

	private void customerSelected(ClientCustomer selectItem) {

		customerIDTextItem.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem.setValue(String.valueOf(selectItem.getBalance()));
		status.setValue(selectItem.isActive());

	}

	private void customerSelected1(ClientCustomer selectItem) {
		customerIDTextItem1.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem1.setValue(String.valueOf(selectItem.getBalance()));
		status1.setValue(selectItem.isActive());
	}

	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (clientCustomer != null && clientCustomer1 != null) {
			if (clientCustomer1.getID() == clientCustomer.getID()) {
				result.addError(clientCustomer,
						messages.notMove(Global.get().customers()));
				return result;
			}
		}
		result = form.validate();
		result = form1.validate();
		return result;
	}

	@Override
	public void saveAndUpdateView() {

		validate();

		if (clientCustomer1 != null && clientCustomer != null) {
			if (clientCustomer1.getID() == clientCustomer.getID()) {
			}
		}
		ClientCurrency currency1 = getCompany().getCurrency(
				clientCustomer1.getCurrency());
		ClientCurrency currency2 = getCompany().getCurrency(
				clientCustomer.getCurrency());
		if (!currency1.equals(currency2)) {
			Accounter.showError(messages
					.currenciesOfTheBothCustomersMustBeSame(Global.get()
							.customers()));
		} else {

			Accounter.createHomeService().mergeCustomer(clientCustomer,
					clientCustomer1, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							Accounter.showInformation("Merge Sucessful");

						}
					});
		}

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
	public String getViewTitle() {
		return "Customer Merge View";
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
