package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VendorMergeDialog extends BaseDialog<ClientCustomer> implements
		AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private VendorCombo vendorCombo;
	private VendorCombo vendorCombo1;
	private TextItem vendorIDTextItem;
	private TextItem vendorIDTextItem1;

	private CheckboxItem status;
	private CheckboxItem status1;
	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;

	private ClientVendor fromclientVendor;
	private ClientVendor toClientVendor;

	public VendorMergeDialog(String title, String descript) {
		super(title, descript);
		setWidth("650px");
		okbtn.setText(messages.merge());
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
		vendorCombo = createVendorCombo();
		vendorCombo1 = createVendorCombo1();

		vendorIDTextItem = new TextItem(messages.payeeID(Global.get().Vendor()));
		vendorIDTextItem.setHelpInformation(true);

		vendorIDTextItem1 = new TextItem(
				messages.payeeID(Global.get().Vendor()));
		vendorIDTextItem1.setHelpInformation(true);
		vendorIDTextItem.setDisabled(true);
		vendorIDTextItem1.setDisabled(true);

		status = new CheckboxItem(messages.active());
		status.setValue(false);

		status.setHelpInformation(true);

		status1 = new CheckboxItem(messages.active());
		status1.setValue(false);
		status1.setHelpInformation(true);

		balanceTextItem = new TextItem(messages.balance());
		balanceTextItem.setHelpInformation(true);

		balanceTextItem1 = new TextItem(messages.balance());
		balanceTextItem1.setHelpInformation(true);
		balanceTextItem.setDisabled(true);
		balanceTextItem1.setDisabled(true);

		form.setItems(vendorCombo, vendorIDTextItem, status, balanceTextItem);
		form1.setItems(vendorCombo1, vendorIDTextItem1, status1,
				balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);

	}

	private VendorCombo createVendorCombo1() {
		vendorCombo1 = new VendorCombo(messages.payeeTo(Global.get().Vendor()),
				false);
		vendorCombo1.setHelpInformation(true);
		vendorCombo1.setRequired(true);
		vendorCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						toClientVendor = selectItem;
						customerSelected1(selectItem);

					}

				});

		return vendorCombo1;
	}

	private VendorCombo createVendorCombo() {
		vendorCombo = new VendorCombo(
				messages.payeeFrom(Global.get().Vendor()), false);
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						fromclientVendor = selectItem;
						customerSelected(selectItem);

					}

				});

		return vendorCombo;
	}

	private void customerSelected(ClientVendor selectItem) {

		vendorIDTextItem.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem.setValue(String.valueOf(selectItem.getBalance()));
		status.setValue(selectItem.isActive());

	}

	private void customerSelected1(ClientVendor selectItem) {
		vendorIDTextItem1.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem1.setValue(String.valueOf(selectItem.getBalance()));
		status1.setValue(selectItem.isActive());
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if (fromclientVendor != null && toClientVendor != null) {
			if (fromclientVendor.getID() == toClientVendor.getID()) {
				result.addError(fromclientVendor,
						messages.notMove(Global.get().vendors()));
				return result;
			}
		}

		result.add(form1.validate());

		return result;

	}

	@Override
	protected boolean onOK() {

		if (fromclientVendor != null && toClientVendor != null) {
			if (fromclientVendor.getID() == toClientVendor.getID()) {
				return false;
			}
		}
		ClientCurrency currency1 = getCompany().getCurrency(
				fromclientVendor.getCurrency());
		ClientCurrency currency2 = getCompany().getCurrency(
				toClientVendor.getCurrency());
		if (!currency1.equals(currency2)) {
			Accounter.showError(messages
					.currenciesOfTheBothCustomersMustBeSame(Global.get()
							.vendors()));
		} else {
			Accounter.createHomeService().mergeVendor(fromclientVendor,
					toClientVendor, this);
			com.google.gwt.user.client.History.back();
			return true;
		}
		return false;
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		vendorCombo.setFocus();

	}
}
