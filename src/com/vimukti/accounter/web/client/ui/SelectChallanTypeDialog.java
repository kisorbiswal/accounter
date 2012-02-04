package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.vat.TDSChalanDetailsAction;

public class SelectChallanTypeDialog extends BaseDialog<ClientTDSChalanDetail> {
	RadioGroupItem typeRadio;
	public static final int Form26Q = 1;
	public static final int Form27Q = 2;
	public static final int Form27EQ = 3;
	private DynamicForm typeForm;
	private boolean isDependent = true;

	public SelectChallanTypeDialog() {
		super(messages.selectItemType(), messages.selectOneOfItem());
		createControls();
		center();
	}

	private void createControls() {

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		typeRadio.setValueMap(messages.form26Q(), messages.form27Q());
		// ,messages.form27EQ());
		typeRadio.setDefaultValue(messages.form26Q());

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

			TDSChalanDetailsAction action = new TDSChalanDetailsAction();
			action.setDependent(isDependent);
			if (radio.equals(messages.form26Q())) {
				action.setType(ClientTDSChalanDetail.Form26Q);
			} else if (radio.equals(messages.form27Q())) {
				action.setType(ClientTDSChalanDetail.Form27Q);
			} else if (radio.equals(messages.form27EQ())) {
				action.setType(ClientTDSChalanDetail.Form27EQ);
			}
			action.setCallback(getCallback());
			action.run();
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
}
