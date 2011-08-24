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
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem;

public class EditProfileDialog extends BaseDialog<ClientUserInfo> {
	private TextBoxItem firstNameTextItem;
	private TextBoxItem lastNameTextItem;
	private VerticalPanel mainPanel;
	private ClientUser clientUser;

	public EditProfileDialog(String editProfile, String desc) {
		super(editProfile, desc);
		clientUser = Accounter.getUser();
		createControls();
	}

	private void createControls() {
		firstNameTextItem = new TextBoxItem();
		firstNameTextItem.setValue(clientUser.getFirstName());
		lastNameTextItem = new TextBoxItem();
		lastNameTextItem.setValue(clientUser.getLastName());
		mainPanel = new VerticalPanel();
		mainPanel.add(firstNameTextItem);
		mainPanel.add(lastNameTextItem);
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
								Header.userName.setText("Hello "
										+ userInfo.getFullName());
								Header.userName.setWidth(((Accounter.messages()
										.userName(
												Accounter.getUser()
														.getFullName())
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
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected boolean onOK() {
		return saveEditedProfile();
	}

}
