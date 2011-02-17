package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.Accounter;
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
		super(FinanceApplication.getFinanceUIConstants().selectItemType(),
				FinanceApplication.getFinanceUIConstants().selectOneOfItem());
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

		typeRadio.setValueMap(FinanceApplication.getFinanceUIConstants()
				.service(), FinanceApplication.getFinanceUIConstants()
				.product());
		typeRadio.setDefaultValue(FinanceApplication.getFinanceUIConstants()
				.service());

		final DynamicForm typeForm = new DynamicForm();
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (!typeForm.validate()) {
					Accounter.showError(FinanceApplication
							.getFinanceUIConstants().pleaseSelectItemType());
					return false;
				}
				@SuppressWarnings("unused")
				ItemView itemView;

				if (typeRadio.getValue() != null) {
					String radio = typeRadio.getValue().toString();
					if (radio.equals(FinanceApplication.getFinanceUIConstants()
							.service())) {
						try {
							ItemView view = new ItemView(null, TYPE_SERVICE,
									isGeneratedFromCustomer);
							MainFinanceWindow.getViewManager().showView(view,
									null, false, action);
						} catch (Throwable e) {
							// //UIUtils.logError("Failed...", e);

						}
					} else if (radio.equals(FinanceApplication
							.getFinanceUIConstants().product())) {

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
		// TODO Auto-generated method stub

	}
}
