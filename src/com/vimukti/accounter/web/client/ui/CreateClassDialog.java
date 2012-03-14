package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

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
		this.getElement().setId("CreateClassDialog");
//		setWidth("300px");
		this.accounterClass = accounterClass;
		initTrackClassDialog();
	}

	private void initTrackClassDialog() {
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");

		createClassTextItem = new TextItem(messages.className(),
				"createClassTextItem");

		if (accounterClass != null && !accounterClass.getClassName().isEmpty()) {
			createClassTextItem.setValue(accounterClass.getClassName());
		}

		trackClassForm.add(createClassTextItem);

		verticalPanel.add(trackClassForm);

		setBodyLayout(verticalPanel);

		this.center();

		show();

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String className = createClassTextItem.getValue();
		if (className == null || className.trim().isEmpty()) {
			result.addError(createClassTextItem,
					messages.pleaseEnter(createClassTextItem.getTitle()));
		}

		ArrayList<ClientAccounterClass> accounterClasses = getCompany()
				.getAccounterClasses();
		for (ClientAccounterClass clientAccounterClass : accounterClasses) {
			if (accounterClass != null
					&& clientAccounterClass.getID() == this.accounterClass
							.getID()) {
				continue;
			}
			if (clientAccounterClass.getName().toLowerCase()
					.equalsIgnoreCase(className.toLowerCase())) {
				result.addError(createClassTextItem, messages.alreadyExist());
				break;
			}
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

	@Override
	protected boolean onCancel() {
		return true;
	}
}
