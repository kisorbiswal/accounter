package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
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

public class SelectItemTypeDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_NON_INVENTORY_PART = 3;
	// private ViewConfiguration configuration;
	private NewItemAction action;
	boolean isGeneratedFromCustomer;

	public SelectItemTypeDialog(NewItemAction action,
			boolean isGeneratedFromCustomer) {
		super(Accounter.constants().selectItemType(), Accounter.constants()
				.selectOneOfItem());
		this.action = action;
		this.isGeneratedFromCustomer = isGeneratedFromCustomer;
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

		typeRadio.setValueMap(Accounter.constants().service(), Accounter
				.constants().product());
		typeRadio.setDefaultValue(Accounter.constants().service());

		final DynamicForm typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("320");
		show();
	}

	@Override
	protected ValidationResult validate() {
		if (!typeForm.validate(true)) {
			// Accounter.showError(FinanceApplication
			// .constants().pleaseSelectItemType());
			return null;
		}
		return super.validate();
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();
			if (radio.equals(Accounter.constants().service())) {
				try {
					ItemView view = new ItemView(null, TYPE_SERVICE,
							isGeneratedFromCustomer);
					MainFinanceWindow.getViewManager().showView(view, null,
							false, action);
				} catch (Throwable e) {
					return false;
				}
			} else if (radio.equals(Accounter.constants().product())) {
				ItemView view = new ItemView(null, TYPE_NON_INVENTORY_PART,
						isGeneratedFromCustomer);
				try {
					MainFinanceWindow.getViewManager().showView(view, null,
							false, action);
				} catch (Exception e) {
					return false;
				}
				// UIUtils.setCanvas(itemView, configuration);
			}
		}

		return true;
	}
}
