package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;

@SuppressWarnings("unchecked")
public class ChangePasswordView extends AbstractBaseView {

	private PasswordItem oldPasswordTextItem, newPasswordTextItem,
			confirmNewPasswordTextItem;
	private DynamicForm textItemsForm;
	private Button saveButton, closeButton;
	private HorizontalPanel buttonPanel;
	private VerticalPanel mainPanel;
	private String oldPassword, newPassword, confirmNewPassword;
	private boolean isMatch;

	@Override
	public void init() {
		this.validationCount = 1;
		super.init();
		createControls();

	}

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
		mainPanel.setCellHorizontalAlignment(buttonPanel, ALIGN_RIGHT);

		mainPanel.setWidth("100%");
		mainPanel.setStyleName("change_password_view");
		add(mainPanel);

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
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

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
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() throws Exception {
		switch (this.validationCount) {
		case 1:
			return AccounterValidator.validateForm(textItemsForm, false);
		default:
			return true;
		}
	}

}
