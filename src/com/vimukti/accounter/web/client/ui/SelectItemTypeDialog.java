package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */
@SuppressWarnings("unchecked")
public class SelectItemTypeDialog extends BaseDialog {
	RadioGroupItem typeRadio;
	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_NON_INVENTORY_PART = 3;
	// private ViewConfiguration configuration;
	private NewItemAction action;
	boolean isGeneratedFromCustomer;

	public SelectItemTypeDialog(NewItemAction action,
			boolean isGeneratedFromCustomer) {
		super(Accounter.constants().selectItemType(), Accounter
				.constants().selectOneOfItem());
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

		typeRadio.setValueMap(Accounter.constants().service(),
				Accounter.constants().product());
		typeRadio.setDefaultValue(Accounter.constants().service());

		final DynamicForm typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {
				removeFromParent();
				// Action.cancle();
			}

			public boolean onOkClick() {
				if (!typeForm.validate(true)) {
					// Accounter.showError(FinanceApplication
					// .constants().pleaseSelectItemType());
					return false;
				}
				@SuppressWarnings("unused")
				ItemView itemView;

				if (typeRadio.getValue() != null) {
					String radio = typeRadio.getValue().toString();
					if (radio.equals(Accounter.constants()
							.service())) {
						try {
							ItemView view = new ItemView(null, TYPE_SERVICE,
									isGeneratedFromCustomer);
							MainFinanceWindow.getViewManager().showView(view,
									null, false, action);
						} catch (Throwable e) {
							// //UIUtils.logError("Failed...", e);

						}
					} else if (radio.equals(Accounter.constants()
							.product())) {

						try {
							ItemView view = new ItemView(null,
									TYPE_NON_INVENTORY_PART,
									isGeneratedFromCustomer);
							MainFinanceWindow.getViewManager().showView(view,
									null, false, action);
							// UIUtils.setCanvas(itemView, configuration);
						} catch (Throwable e) {
							// //UIUtils.logError("Failed...", e);
						}

					}
				}

				return true;
			}

		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("320");
		show();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().selectItemType();
	}
}
