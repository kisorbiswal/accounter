package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class UserInformationDialog extends AbstractBaseDialog {

	TextItem fullName, legalName, street1Text, street2Text, cityText,
			stateText, zipText, faxText, websiteText;
	IntegerField phoneText;
	EmailField emailText;
	DynamicForm userForm;
	DateItem createDate, lastLogin;

	public UserInformationDialog(AbstractBaseView parent) {
		super(parent);
		createControls();
		show();
	}

	private void getUser() {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);

		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);

		@SuppressWarnings("unused")
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				Accounter.showInformation(Accounter
						.getFinanceUIConstants().getUserFailed());
			}

			public void onSuccess(ClientUser user) {
				if (user != null) {

					fullName.setValue(user.getFullName());
					emailText.setValue(user.getEmail());
					ClientFinanceDate d = user.getCreatedDate();
					// DateItem createDate = new DateItem();
					createDate.setValue(d);

					// ClientFinanceDate d1 = user.getLastLogin();
					ClientFinanceDate d1 = new ClientFinanceDate();
					// DateItem lastLogin = new DateItem();
					lastLogin.setValue(d1);
					legalName.setValue(user.getDefaultCompany()
							.getTradingName());

					// ClientAddress a = user.getAddress();
					//
					// street1Text.setValue(a.getStreet());
					// street2Text.setValue(a.getStreet());
					// cityText.setValue(a.getCity());
					// stateText.setValue(a.getStateOrProvinence());
					// zipText.setValue(a.getZipOrPostalCode());
					//
					// ClientContact c = user.getContact();

					// phoneText.setValue(c.getBusinessPhone());

				} else {
					// //UIUtils.log("Get user Came But Failed!");
				}
			}

		};
		// getService.getUser(emailText.getValue().toString(), getUserCallBack);
		if (emailText.getValue() == null) {
			UIUtils.say(Accounter.getFinanceUIConstants()
					.pleaseEnterUserEmail());
		} else {
			// getService
			// .getUser(emailText.getValue().toString(), getUserCallBack);
		}
	}

	private void createUser() {
		// //UIUtils.log("Creating User...");

		final IAccounterCRUDServiceAsync crudService = (IAccounterCRUDServiceAsync) GWT
				.create(IAccounterCRUDService.class);

		((ServiceDefTarget) crudService)
				.setServiceEntryPoint(Accounter.CRUD_SERVICE_ENTRY_POINT);

		final AsyncCallback<String> createUserCallback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());

			}

			public void onSuccess(String result) {
				if (result != null) {
					// //UIUtils.log("user created successfully!");
					getUser();

				}
			}
		};

		ClientUser user = getUserObject();
		crudService.create(user, createUserCallback);
	}

	private ClientUser getUserObject() {
		ClientUser user = new ClientUser();

		// TextItem fullName, legalName, street1Text, street2Text, cityText,
		// stateText, zipText, phoneText, faxText, emailText, websiteText;
		// DynamicForm userForm;
		// DateItem createDate, lastLogin;

		if (fullName.getValue() != null) {
			user.setFullName(fullName.getValue().toString());
		}

		if (createDate.getValue() != null) {
			Object obj = createDate.getValue();
			if (obj instanceof java.util.Date) {
				@SuppressWarnings("unused")
				java.util.Date date = (java.util.Date) obj;

			}
		}

		if (lastLogin.getValue() != null) {
			Object obj = lastLogin.getValue();
			if (obj instanceof java.util.Date) {
				@SuppressWarnings("unused")
				java.util.Date date = (java.util.Date) obj;

			}
		}

		ClientAddress a = new ClientAddress();

		if (street1Text.getValue() != null && street2Text.getValue() != null) {
			a.setStreet(street1Text.getValue().toString() + " "
					+ street2Text.getValue().toString());
		}

		if (cityText.getValue() != null) {
			a.setCity(cityText.getValue().toString());
		}
		if (stateText.getValue() != null) {
			a.setStateOrProvinence(stateText.getValue().toString());
		}
		if (zipText.getValue() != null) {
			a.setZipOrPostalCode(zipText.getValue().toString());
		}

		ClientContact c = new ClientContact();
		if (phoneText.getValue() != null) {
			c.setBusinessPhone(phoneText.getValue().toString());
		}
		if (emailText.getValue() != null) {
			c.setEmail(emailText.getValue().toString());
			user.setEmail(emailText.getValue().toString());
		}

		return user;
	}

	private void createControls() {
		fullName = new TextItem(Accounter.getFinanceUIConstants()
				.fullName());
		emailText = new EmailField(Accounter.getFinanceUIConstants()
				.email());
		legalName = new TextItem(Accounter.getFinanceUIConstants()
				.legalName());

		lastLogin = new DateItem();
		lastLogin.setName(Accounter.getFinanceUIConstants()
				.lastLogin());
		// lastLogin.setUseTextField(true);

		createDate = new DateItem();
		createDate.setName(Accounter.getFinanceUIConstants()
				.createDate());
		// createDate.setUseTextField(true);

		street1Text = new TextItem(Accounter.getFinanceUIConstants()
				.streetAddress1());
		street2Text = new TextItem(Accounter.getFinanceUIConstants()
				.streetAddress2());

		cityText = new TextItem(Accounter.getFinanceUIConstants()
				.city());
		stateText = new TextItem(Accounter.getFinanceUIConstants()
				.state());
		zipText = new TextItem(Accounter.getFinanceUIConstants()
				.postalCode());
		phoneText = new IntegerField(Accounter.getFinanceUIConstants()
				.phone());

		faxText = new TextItem(Accounter.getFinanceUIConstants().fax());

		websiteText = new TextItem(Accounter.getFinanceUIConstants()
				.website());

		userForm = new DynamicForm();
		// userForm.setSize("80%", "*");

		// userForm.setMargin(19);
		// userForm.setWrapItemTitles(false);
		userForm.setNumCols(4);
		userForm.setFields(fullName, emailText, lastLogin, createDate,
				street1Text, street2Text, cityText, stateText, zipText,
				phoneText, faxText, legalName, websiteText);

		AccounterButton createUser = new AccounterButton(Accounter
				.getFinanceUIConstants().createUser());
		AccounterButton getUser = new AccounterButton(Accounter
				.getFinanceUIConstants().getUser());
		// createUser.setAlign(Alignment.LEFT);

		AccounterButton canButt = new AccounterButton(Accounter
				.getFinanceUIConstants().close());
		// canButt.setAlign(Alignment.RIGHT);

		createUser.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createUser();
			}
		});
		getUser.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getUser();
			}
		});

		canButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// cancelClick();
			}
		});

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setMembersMargin(290);
		// buttHLay.setSize("50%", "*");
		buttHLay.add(createUser);

		createUser.enabledButton();
		getUser.enabledButton();
		// buttHLay.setMargin(60);
		// buttHLay.setAlign(Alignment.CENTER);

		VerticalPanel vLay = new VerticalPanel();
		vLay.setSize("100%", "100%");
		vLay.add(userForm);
		vLay.add(buttHLay);
		vLay.setTitle(Accounter.getFinanceUIConstants().userDialog());
		vLay.addStyleName(Accounter.getFinanceUIConstants()
				.userDialog());

		setSize("600", "430");
		add(vLay);
		show();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
