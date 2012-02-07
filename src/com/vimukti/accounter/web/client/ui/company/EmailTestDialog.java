package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientEmailAccount;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class EmailTestDialog extends BaseDialog {

	EmailField emailField;

	ClientEmailAccount emailAccount;

	public EmailTestDialog(ClientEmailAccount emailAccount) {
		super(messages.emailAddress());
		createControls();
		this.emailAccount = emailAccount;
	}

	private void createControls() {

		DynamicForm form = new DynamicForm();

		emailField = new EmailField(messages.emailAddress());
		emailField.setRequired(true);

		form.setFields(emailField);

		setBodyLayout(form);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(FormItem.validate(emailField));
		if (emailField.getValue() != null
				&& !UIUtils.isValidEmail(emailField.getValue())) {
			result.addError(emailField, messages.pleaseEnterValidEmailId());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		Accounter.createHomeService().sendTestMail(emailAccount,
				emailField.getValue(), new AccounterAsyncCallback<Boolean>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError("Messages sending failed");
					}

					@Override
					public void onResultSuccess(Boolean result) {
						sendSuccess();
					}
				});
		return false;
	}

	private void sendSuccess() {
		this.removeFromParent();
		Accounter
				.showInformation("A Test Email is sent to given email id. Kindly check your email and proceed if you got.");
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
