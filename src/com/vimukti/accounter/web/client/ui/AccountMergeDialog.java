package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 *
 */
public class AccountMergeDialog extends BaseDialog<ClientAccount> {

	private DynamicForm form;
	private DynamicForm form1;
	private ValueCallBack<ClientContact> successCallback;
	private PayFromAccountsCombo accountCombo;
	private PayFromAccountsCombo accountCombo1;
	private TextItem accountIDTextItem;
	private TextItem accountIDTextItem1;
	private TextItem status;
	private TextItem status1;
	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;

	public AccountMergeDialog(String title, String descript) {
		super(title, descript);
		setWidth("650px");
		okbtn.setText("Merge");
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm();
		form1 = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");
		form1.setHeight("100%");
		form1.setWidth("100%");
		VerticalPanel layout = new VerticalPanel();
		VerticalPanel layout1 = new VerticalPanel();
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		accountCombo = createCustomerCombo();
		accountCombo1 = createCustomerCombo1();

		accountIDTextItem = new TextItem("CustomerID");
		accountIDTextItem.setHelpInformation(true);

		accountIDTextItem1 = new TextItem("CustomerID");
		accountIDTextItem1.setHelpInformation(true);

		status = new TextItem("Status");
		status.setHelpInformation(true);

		status1 = new TextItem("Status");
		status1.setHelpInformation(true);

		balanceTextItem = new TextItem("Balance");
		balanceTextItem.setHelpInformation(true);

		balanceTextItem1 = new TextItem("Balance");
		balanceTextItem1.setHelpInformation(true);

		form.setItems(accountCombo, accountIDTextItem, status, balanceTextItem);
		form1.setItems(accountCombo1, accountIDTextItem1, status1,
				balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);
	}

	private PayFromAccountsCombo createCustomerCombo1() {
		accountCombo1 = new PayFromAccountsCombo("TO Account");
		accountCombo1.setHelpInformation(true);
		accountCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						customerSelected1(selectItem);

					}

				});

		return accountCombo1;
	}

	private PayFromAccountsCombo createCustomerCombo() {
		accountCombo = new PayFromAccountsCombo("From Account");
		accountCombo.setHelpInformation(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						customerSelected(selectItem);

					}

				});

		return accountCombo;
	}

	private void customerSelected(ClientAccount selectItem) {

		accountIDTextItem.setValue(String.valueOf(selectItem.getID()));
		// balanceTextItem.setValue(String.valueOf(selectItem.getBalance()));
		status.setValue("active");

	}

	private void customerSelected1(ClientAccount selectItem) {
		accountIDTextItem1.setValue(String.valueOf(selectItem.getID()));
		// balanceTextItem1.setValue(String.valueOf(selectItem.getBalance()));
		status1.setValue("active");
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		result = form1.validate();
		return result;

	}

	/**
	 * @return
	 */
	private ClientContact createContact() {
		ClientContact contact = new ClientContact();
		// contact.setName(nameItem.getValue());
		// contact.setTitle(titleItem.getValue());
		// contact.setBusinessPhone(businessPhoneItem.getValue());
		// contact.setEmail(emailItem.getValue());
		return contact;
	}

	/**
	 * @param newContactHandler
	 */
	public void addSuccessCallback(
			ValueCallBack<ClientContact> newContactHandler) {
		this.successCallback = newContactHandler;
	}

	@Override
	protected boolean onOK() {

		return true;
	}

}
