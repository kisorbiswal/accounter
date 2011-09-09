package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Header;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class EditProfileDialog extends BaseDialog<ClientUserInfo> {
	private TextItem firstNameTextItem;
	private TextItem lastNameTextItem;
	private VerticalPanel mainPanel;
	private ClientUser clientUser;

	public EditProfileDialog(String editProfile, String desc) {
		super(editProfile, desc);
		clientUser = Accounter.getUser();
		createControls();
	}

	private void createControls() {
		firstNameTextItem = new TextItem(Accounter.constants().firstName());
		firstNameTextItem.setValue(clientUser.getFirstName());
		lastNameTextItem = new TextItem(Accounter.constants().lastName());
		lastNameTextItem.setValue(clientUser.getLastName());
		mainPanel = new VerticalPanel();
		DynamicForm form = new DynamicForm();
		form.setStyleName("edit_user_profile");
		form.setNumCols(2);
		form.setFields(firstNameTextItem, lastNameTextItem);
		mainPanel.add(form);
		okbtn.setText(Accounter.constants().saveButton());
		setBodyLayout(mainPanel);

	}

	protected boolean saveEditedProfile() {
		// Send request to edit profile.
		String firstName = firstNameTextItem.getValue();
		String lastName = lastNameTextItem.getValue();
		if (firstName.isEmpty() || firstName == null || lastName.isEmpty()
				|| lastName == null) {
			addError(this, Accounter.constants().nameShouldnotbeempty());
			return false;
		} else if (firstName.equals(clientUser.getFirstName())
				&& lastName.equals(clientUser.getLastName())) {
			addError(this, Accounter.constants().bothnamessameasprevious());
			return false;
		} else {
			final ClientUserInfo userInfo = clientUser.toUserInfo();
			userInfo.setFirstName(firstName);
			userInfo.setLastName(lastName);
			userInfo.setFullName(firstName + " " + lastName);
			Accounter.createCRUDService().updateUser(userInfo,
					new AccounterAsyncCallback<Long>() {
						@Override
						public void onException(AccounterException caught) {

						}

						@Override
						public void onResultSuccess(Long result) {
							if (result != null) {
								removeFromParent();
								Accounter.showInformation(Accounter.constants()
										.updatedSuccessfully());
								Header.userName.setText(Accounter.messages()
										.userName(userInfo.getFullName()));
								Header.userName.setWidth(((Accounter.messages()
										.userName(userInfo.getFullName())
										.length() * 6))
										+ "px");
								getCallback().actionResult(userInfo);
							} else {
								addError(this, Accounter.constants()
										.yourPresentPasswordisWrong());
							}
						}

					});

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
	protected boolean onOK() {
		return saveEditedProfile();
	}

	@Override
	public void setFocus() {
		firstNameTextItem.setFocus();

	}

}
