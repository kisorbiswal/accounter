package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ItemMergeDialog extends BaseDialog {

	private DynamicForm form;
	private DynamicForm form1;
	private ValueCallBack<ClientContact> successCallback;
	private ItemCombo itemCombo;
	private ItemCombo itemCombo1;
	private TextItem customerIDTextItem;
	private TextItem customerIDTextItem1;
	private TextItem status;
	private TextItem status1;
	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;

	public ItemMergeDialog(String title, String descript) {
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
		itemCombo = createCustomerCombo();
		itemCombo1 = createCustomerCombo1();

		customerIDTextItem = new TextItem("CustomerID");
		customerIDTextItem.setHelpInformation(true);

		customerIDTextItem1 = new TextItem("CustomerID");
		customerIDTextItem1.setHelpInformation(true);

		status = new TextItem("Status");
		status.setHelpInformation(true);

		status1 = new TextItem("Status");
		status1.setHelpInformation(true);

		balanceTextItem = new TextItem("Balance");
		balanceTextItem.setHelpInformation(true);

		balanceTextItem1 = new TextItem("Balance");
		balanceTextItem1.setHelpInformation(true);

		form.setItems(itemCombo, customerIDTextItem, status,
				balanceTextItem);
		form1.setItems(itemCombo1, customerIDTextItem1, status1,
				balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);
	}

	private ItemCombo createCustomerCombo1() {
		itemCombo1 = new ItemCombo("ItemTO",1);
		itemCombo1.setHelpInformation(true);
		itemCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						customerSelected1(selectItem);

					}

				});

		return itemCombo1;
	}

	private ItemCombo createCustomerCombo() {
		itemCombo = new ItemCombo("ItemFrom",1);
		itemCombo.setHelpInformation(true);
		itemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						customerSelected(selectItem);

					}

				});

		return itemCombo;
	}

	private void customerSelected(ClientItem selectItem) {

		customerIDTextItem.setValue(String.valueOf(selectItem.getID()));
		//balanceTextItem.setValue(String.valueOf(selectItem.getBalance()));
		status.setValue("active");

	}

	private void customerSelected1(ClientItem selectItem) {
		customerIDTextItem1.setValue(String.valueOf(selectItem.getID()));
		//balanceTextItem1.setValue(String.valueOf(selectItem.getBalance()));
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
