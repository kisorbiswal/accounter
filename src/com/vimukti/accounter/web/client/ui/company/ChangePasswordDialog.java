package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;

@SuppressWarnings("unchecked")
public class ChangePasswordDialog extends BaseDialog {

	public ChangePasswordDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	private PasswordItem oldPasswordTextItem, newPasswordTextItem,
			confirmNewPasswordTextItem;
	private DynamicForm textItemsForm;
	private Button saveButton, closeButton;
	private HorizontalPanel buttonPanel;
	private VerticalPanel mainPanel;
	private String oldPassword, newPassword, confirmNewPassword;
	private boolean isMatch;


	private void createControls() {
		oldPasswordTextItem = new PasswordItem(FinanceApplication
				.getCompanyMessages().oldPassword());
		newPasswordTextItem = new PasswordItem(FinanceApplication
				.getCompanyMessages().newPassword());
		confirmNewPasswordTextItem = new PasswordItem(FinanceApplication
				.getCompanyMessages().confirmNewPassword());

		oldPasswordTextItem.setRequired(true);
		newPasswordTextItem.setRequired(true);
		confirmNewPasswordTextItem.setRequired(true);

		saveButton = new Button(FinanceApplication.getSettingsMessages()
				.saveButton());
		closeButton = new Button(FinanceApplication.getCustomersMessages()
				.close());

		textItemsForm = new DynamicForm();

		buttonPanel = new HorizontalPanel();
		mainPanel = new VerticalPanel();

		textItemsForm.setNumCols(2);
		textItemsForm.setCellSpacing(10);
		textItemsForm.setFields(oldPasswordTextItem, newPasswordTextItem,
				confirmNewPasswordTextItem);

		buttonPanel.add(saveButton);
		buttonPanel.add(closeButton);

		if (saveButton.isEnabled()) {
			saveButton.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(saveButton, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (closeButton.isEnabled()) {
			closeButton.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(closeButton, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}

		mainPanel.add(textItemsForm);
		mainPanel.add(buttonPanel);
		mainPanel.setCellHorizontalAlignment(buttonPanel,
				HasAlignment.ALIGN_RIGHT);

		mainPanel.setWidth("100%");
		mainPanel.setStyleName("change_password_view");

		setBodyLayout(mainPanel);
		okbtn.setVisible(false);
		cancelBtn.setVisible(false);
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					MainFinanceWindow.getViewManager().restoreErrorBox();
					validate();
				} catch (Exception e) {
				}
				savePassword();
			}
		});

		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				MainFinanceWindow.getViewManager().closeCurrentView();
				removeFromParent();
			}
		});

	}

	protected void savePassword() {
		oldPassword = oldPasswordTextItem.getValue().toString();
		newPassword = newPasswordTextItem.getValue().toString();
		confirmNewPassword = confirmNewPasswordTextItem.getValue().toString();

		if (newPassword.equals(confirmNewPassword)) {
			isMatch = true;
		} else {
			isMatch = false;
		}

		FinanceApplication.creatUserService().changePassWord(oldPassword,
				newPassword, isMatch, confirmNewPassword,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							if (isMatch) {
								// TODO Display Password Updated Successfully
								// Message
							} else {
								// TODO Display New Password and Confirm New
								// Password should be same message
							}
						}
					}

				});

	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// switch (this.validationCount) {
		// case 1:
		try {
			return AccounterValidator.validateForm(textItemsForm, true);
		} catch (InvalidEntryException e) {
			e.printStackTrace();
		}
		// default:
		return true;
		// }
	}

}
