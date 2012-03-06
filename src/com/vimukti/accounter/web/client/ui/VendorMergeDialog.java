package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VendorMergeDialog extends BaseView<ClientCustomer> implements
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

	public VendorMergeDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("accountMergeDialog");
		createControls();
		saveAndNewButton.setVisible(false);
		saveAndCloseButton.setText(messages.merge());
		setSize("100%", "100%");
	}

	private void createControls() {
		form = new DynamicForm("firstForm");
		form1 = new DynamicForm("secondForm");
		StyledPanel styledPanel = new StyledPanel("mainPanel");
		vendorCombo = createVendorCombo();
		vendorCombo1 = createVendorCombo1();

		vendorIDTextItem = new TextItem(
				messages.payeeID(Global.get().Vendor()), "vendorIDTextItem");

		vendorIDTextItem1 = new TextItem(
				messages.payeeID(Global.get().Vendor()), "vendorIDTextItem");
		vendorIDTextItem.setEnabled(false);
		vendorIDTextItem1.setEnabled(false);

		status = new CheckboxItem(messages.active(), "status");
		status.setValue(false);

		status1 = new CheckboxItem(messages.active(), "status");
		status1.setValue(false);

		balanceTextItem = new TextItem(messages.balance(), "balanceTextItem");

		balanceTextItem1 = new TextItem(messages.balance(), "balanceTextItem");
		balanceTextItem.setEnabled(false);
		balanceTextItem1.setEnabled(false);
		form.add(vendorCombo);
		form.add(vendorIDTextItem);
		form.add(status);
		form.add(balanceTextItem);
		form1.add(vendorCombo1);
		form1.add(vendorIDTextItem1);
		form1.add(status1);
		form1.add(balanceTextItem1);
		// form.setItems(getTextItems());
		styledPanel.add(form);
		styledPanel.add(form1);
		this.add(styledPanel);

	}

	private VendorCombo createVendorCombo1() {
		vendorCombo1 = new VendorCombo(messages.payeeTo(Global.get().Vendor()),
				false);
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

	public ValidationResult validate() {
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
	public void saveAndUpdateView() {
		validate();
		if (fromclientVendor != null && toClientVendor != null) {
			if (fromclientVendor.getID() == toClientVendor.getID()) {
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
		}
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
		return messages.mergeVendors(Global.get().Vendor());
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}
}
