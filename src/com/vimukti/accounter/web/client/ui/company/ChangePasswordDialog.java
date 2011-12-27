package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;

public class ChangePasswordDialog extends BaseDialog {

	private PasswordItem oldPasswordTextItem, newPasswordTextItem,
			confirmNewPasswordTextItem;
	private DynamicForm textItemsForm;
	private VerticalPanel mainPanel;
	private String oldPassword, newPassword, confirmNewPassword;

	public ChangePasswordDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	private void createControls() {

		oldPasswordTextItem = new PasswordItem(Accounter.messages()
				.oldPassword());
		newPasswordTextItem = new PasswordItem(Accounter.messages()
				.newPassword());
		confirmNewPasswordTextItem = new PasswordItem(Accounter.messages()
				.confirmNewPassword());

		oldPasswordTextItem.setRequired(true);
		newPasswordTextItem.setRequired(true);
		confirmNewPasswordTextItem.setRequired(true);

		textItemsForm = new DynamicForm();

		mainPanel = new VerticalPanel();

		textItemsForm.setNumCols(2);
		textItemsForm.setCellSpacing(10);
		textItemsForm.setFields(oldPasswordTextItem, newPasswordTextItem,
				confirmNewPasswordTextItem);

		mainPanel.add(textItemsForm);
		okbtn.setText(Accounter.messages().save());

		setBodyLayout(mainPanel);

	}

	protected boolean savePassword() {

		oldPassword = oldPasswordTextItem.getValue().toString();
		newPassword = newPasswordTextItem.getValue().toString();
		confirmNewPassword = confirmNewPasswordTextItem.getValue().toString();
		String emailID = Accounter.getUser().getEmail();

		if (!(newPassword.toString().length() < 6)) {
			if (newPassword.equals(confirmNewPassword)) {
				Accounter.createHomeService().changePassWord(emailID,
						oldPassword, newPassword,
						new AccounterAsyncCallback<Boolean>() {

							@Override
							public void onException(AccounterException caught) {

							}

							@Override
							public void onResultSuccess(Boolean result) {
								if (result) {
									removeFromParent();
									Accounter.showInformation(Accounter
											.messages()
											.passwordSuccessfullyChanged());
								} else {
									Accounter.showError(Accounter.messages()
											.youHaveEnteredWrongPassword());
								}
							}

						});
			} else {
				addError(this, Accounter.messages().passwordsnotmatched());
				return false;
			}
		} else {
			addError(this, Accounter.messages()
					.passwordshouldcontainminimum6characters());
			return false;
		}
		return true;

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public ValidationResult validate() {
		return textItemsForm.validate();
	}

	@Override
	protected boolean onOK() {
		return savePassword();
	}

	@Override
	public void setFocus() {
		oldPasswordTextItem.setFocus();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
