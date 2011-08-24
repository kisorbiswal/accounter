package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class ItemMergeDialog extends BaseDialog implements AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private ItemCombo itemCombo;
	private ItemCombo itemCombo1;
	private CheckboxItem status;
	private CheckboxItem status1;

	private TextItem itemType;
	private TextItem itemType1;
	private TextItem price1;
	private TextItem price;

	private ClientItem fromClientItem;
	private ClientItem toClientItem;

	public ItemMergeDialog(String title, String descript) {
		super(title, descript);
		setWidth("650px");
		okbtn.setText(Accounter.constants().merge());
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

		status = new CheckboxItem(Accounter.constants().active());
		status.setValue(false);

		status.setHelpInformation(true);

		status1 = new CheckboxItem(Accounter.constants().active());
		status1.setValue(false);

		itemType = new TextItem(Accounter.constants().itemType());
		itemType.setHelpInformation(true);
		itemType.setDisabled(true);

		itemType1 = new TextItem(Accounter.constants().itemType());
		itemType1.setHelpInformation(true);
		itemType1.setDisabled(true);
		price = new TextItem(Accounter.constants().price());
		price.setHelpInformation(true);
		price.setDisabled(true);

		price1 = new TextItem(Accounter.constants().price());
		price1.setHelpInformation(true);
		price1.setDisabled(true);

		form.setItems(itemCombo, itemType, status, price);
		form1.setItems(itemCombo1, itemType1, status1, price1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);
	}

	private ItemCombo createCustomerCombo1() {
		itemCombo1 = new ItemCombo(Accounter.constants().itemTo(), 2, false);
		itemCombo1.setRequired(true);
		itemCombo1.setHelpInformation(true);
		itemCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						toClientItem = selectItem;
						customerSelected1(selectItem);

					}

				});

		return itemCombo1;
	}

	private ItemCombo createCustomerCombo() {
		itemCombo = new ItemCombo(Accounter.constants().itemFrom(), 2, false);
		itemCombo.setHelpInformation(true);
		itemCombo.setRequired(true);
		itemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						fromClientItem = selectItem;
						customerSelected(selectItem);

					}

				});

		return itemCombo;
	}

	private void customerSelected(ClientItem selectItem) {

		status.setValue(selectItem.isActive());
		if (selectItem.getType() == 1) {
			itemType.setValue(Accounter.constants().service());
		} else if (selectItem.getType() == 3) {
			itemType.setValue(Accounter.constants().product());
		}
		price.setValue(String.valueOf(selectItem.getSalesPrice()));

	}

	private void customerSelected1(ClientItem selectItem) {
		status1.setValue(selectItem.isActive());
		if (selectItem.getType() == 1) {
			itemType1.setValue(Accounter.constants().service());
		} else if (selectItem.getType() == 3) {
			itemType1.setValue(Accounter.constants().service());
		}
		price1.setValue(String.valueOf(selectItem.getSalesPrice()));
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if (fromClientItem.getID() == toClientItem.getID()) {
			result.addError(fromClientItem, Accounter.constants().notMove());
			return result;
		}
		result = form1.validate();
		result = form.validate();
		return result;

	}

	@Override
	protected boolean onOK() {

		Accounter.createHomeService().mergeItem(fromClientItem, toClientItem,
				this);

		return true;
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

}
