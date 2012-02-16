package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class SelectItemTypeDialog extends BaseDialog<ClientItem> {
	RadioGroupItem typeRadio;
	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_NON_INVENTORY_PART = 3;
	// private ViewConfiguration configuration;
	boolean forCustomer;
	private DynamicForm typeForm;
	private boolean isDependent = true;
	private String itemname;

	public SelectItemTypeDialog(boolean isGeneratedFromCustomer) {
		super(messages.selectItemType(), messages.selectOneOfItem());
		this.forCustomer = isGeneratedFromCustomer;
		createControls();
		center();
	}

	private void createControls() {

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		// LinkedHashMap<String, String> typeMap = new LinkedHashMap<String,
		// String>();
		// typeMap.put("service", "Service");
		// typeMap.put("non-inventory", "Non-Inventory&nbsp;Item");
		boolean sellServices = company.getPreferences().isSellServices();
		boolean sellProducts = company.getPreferences().isSellProducts();
		if (getPreferences().isInventoryEnabled()) {
			if (sellProducts && sellServices) {
				typeRadio.setValue(messages.serviceItem(),
						messages.inventoryItem(), messages.nonInventoryItem(),
						messages.inventoryAssembly());
				typeRadio.setDefaultValue(messages.serviceItem());
			} else if (sellProducts) {
				typeRadio.setValueMap(messages.inventoryItem(),
						messages.nonInventoryItem());
				typeRadio.setDefaultValue(messages.inventoryItem());
			}
		} else {
			typeRadio.setValueMap(messages.serviceItem(),
					messages.productItem());
			typeRadio.setDefaultValue(messages.serviceItem());
		}

		typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("320px");
		show();
	}

	@Override
	protected ValidationResult validate() {
		return typeForm.validate();
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();

			if (radio.equals(messages.serviceItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_SERVICE);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.inventoryItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.nonInventoryItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_NON_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.productItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_NON_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.inventoryAssembly())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_INVENTORY_ASSEMBLY);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			}
			// UIUtils.setCanvas(itemView, configuration);
		}

		return true;
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
