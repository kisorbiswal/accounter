package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreateClassDialog extends BaseDialog<ClientAccounterClass> {

	DynamicForm trackClassForm;

	TextItem createClassTextItem;

	private ValueCallBack<ClientAccounterClass> successCallback;

	private ClientAccounterClass accounterClass;

	public CreateClassDialog(ClientAccounterClass accounterClass, String title,
			String desc) {
		super(title, desc);
		setWidth("300px");
		this.accounterClass = accounterClass;
		initTrackClassDialog();
	}

	private void initTrackClassDialog() {
		VerticalPanel verticalPanel = new VerticalPanel();

		trackClassForm = new DynamicForm();

		createClassTextItem = new TextItem(Accounter.constants().className());

		if (accounterClass != null && !accounterClass.getClassName().isEmpty()) {
			createClassTextItem.setValue(accounterClass.getClassName());
		}

		trackClassForm.setFields(createClassTextItem);

		verticalPanel.add(trackClassForm);

		setBodyLayout(verticalPanel);

		this.center();

		show();

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (createClassTextItem.getValue().equals("")
				|| createClassTextItem.getValue() == null) {
			result.addError(createClassTextItem, Accounter.messages()
					.pleaseEnter(createClassTextItem.getTitle()));
		}
		return result;
	};

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createAccounterClass());
		}
		return true;
	}

	private ClientAccounterClass createAccounterClass() {
		if (this.accounterClass == null) {
			accounterClass = new ClientAccounterClass();
		}
		accounterClass.setClassName(createClassTextItem.getValue());
		return accounterClass;
	}

	public void addSuccessCallback(
			ValueCallBack<ClientAccounterClass> newClassHandler) {
		this.successCallback = newClassHandler;
	}

	@Override
	public void setFocus() {
		createClassTextItem.setFocus();

	}

}
