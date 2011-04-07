package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class CreateUserDialog extends AbstractBaseDialog {

	TextItem fullName, legalName, street1Text, street2Text, cityText,
			stateText, zipText, faxText, websiteText;
	EmailField emailText;
	IntegerField phoneText;
	PasswordItem pwdText;
	DynamicForm userForm;
	DateItem createDate, lastLogin;

	public CreateUserDialog(AbstractBaseView parent) {
		super(parent);
		createControls();
		show();
	}

	private void getUser() {
		@SuppressWarnings("unused")
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());

			}

			@SuppressWarnings("null")
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

					ClientAddress a = null;

					street1Text.setValue(a.getStreet());
					street2Text.setValue(a.getStreet());
					cityText.setValue(a.getCity());
					stateText.setValue(a.getStateOrProvinence());
					zipText.setValue(a.getZipOrPostalCode());

					ClientContact c = null;

					phoneText.setValue(c.getBusinessPhone());

				} else {
					// //UIUtils.log("Get user Came But Failed!");
				}
			}

		};
		// getService.getUser(emailText.getValue().toString(), getUserCallBack);
		if (emailText.getValue() == null) {
			// //UIUtils.log("Please enter user email.");
		} else {
			// FinanceApplication.createGETService().getUser(
			// emailText.getValue().toString(), getUserCallBack);
		}
	}

	private void createUser() {
		// Window.alert("Creating User...");

		final IAccounterCRUDServiceAsync crudService = (IAccounterCRUDServiceAsync) GWT
				.create(IAccounterCRUDService.class);

		((ServiceDefTarget) crudService)
				.setServiceEntryPoint(FinanceApplication.CRUD_SERVICE_ENTRY_POINT);

		@SuppressWarnings("unused")
		final AsyncCallback<IsSerializable> createUserCallback = new AsyncCallback<IsSerializable>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
			}

			public void onSuccess(IsSerializable result) {
				if (result != null) {
					// //UIUtils.log("User created successfully!");
					FinanceApplication.setUser((ClientUser) result);
					removeFromParent();
					// getUser();
				}
			}
		};

		@SuppressWarnings("unused")
		ClientUser user = getUserObject();
		// crudService.createUser(user, createUserCallback);
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

		if (pwdText.getValue() != null) {
			user.setPasswordSha1Hash(pwdText.getValue().toString());
		}

		// Contact c = new Contact();
		// if (phoneText.getValue() != null) {
		// c.setBusinessPhone(phoneText.getValue().toString());
		// }
		if (emailText.getValue() != null) {
			// c.setEmail(emailText.getValue().toString());
			user.setEmail(emailText.getValue().toString());
		}
		//
		// user.setContact(null);// c);
		//
		//
		// if (createDate.getValue() != null) {
		// Object obj = createDate.getValue();
		// if (obj instanceof java.util.Date) {
		// java.util.Date date = (java.util.Date) obj;
		//
		// }
		// }

		// if (createDate.getValue() != null) {
		// user.setCreatedDate((java.util.Date) lastLogin.getValue());
		// }
		//
		// if (lastLogin.getValue() != null) {
		// Object obj = lastLogin.getValue();
		// if (obj instanceof java.util.Date) {
		// java.util.Date date = (java.util.Date) obj;
		//
		// }
		// }

		// if (lastLogin.getValue() != null) {
		// user.setLastLogin((java.util.Date) lastLogin.getValue());
		// }

		// Address a = new Address();
		//
		// if (street1Text.getValue() != null && street2Text.getValue() != null)
		// {
		// a.setStreet(street1Text.getValue().toString() + " "
		// + street2Text.getValue().toString());
		// }
		//
		// if (cityText.getValue() != null) {
		// a.setCity(cityText.getValue().toString());
		// }
		// if (stateText.getValue() != null) {
		// a.setStateOrProvinence(stateText.getValue().toString());
		// }
		// if (zipText.getValue() != null) {
		// a.setZipOrPostalCode(zipText.getValue().toString());
		// }
		//
		// user.setAddress(null);// a);

		return user;
	}

	private void createControls() {
		setTitle(FinanceApplication.getFinanceUIConstants().createNewUser());

		fullName = new TextItem(FinanceApplication.getFinanceUIConstants()
				.fullName());
		// fullName.setWidth("*");
		fullName.setColSpan(3);

		emailText = new EmailField(FinanceApplication.getFinanceUIConstants()
				.email());
		// emailText.setWidth("*");
		emailText.setRequired(true);

		pwdText = new PasswordItem(FinanceApplication.getFinanceUIConstants()
				.password());
		// pwdText.setWidth("*");
		pwdText.setRequired(true);

		legalName = new TextItem(FinanceApplication.getFinanceUIConstants()
				.legalName());
		// legalName.setWidth("*");

		lastLogin = new DateItem();
		lastLogin
				.setName(FinanceApplication.getCustomersMessages().lastLogin());
		// lastLogin.setUseTextField(true);

		createDate = new DateItem();
		createDate.setName(FinanceApplication.getCustomersMessages()
				.createDate());
		// createDate.setUseTextField(true);

		street1Text = new TextItem(FinanceApplication.getFinanceUIConstants()
				.streetAddress1());
		// street1Text.setWidth("*");
		street2Text = new TextItem(FinanceApplication.getFinanceUIConstants()
				.streetAddress2());
		// street2Text.setWidth("*");

		cityText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.city());
		// cityText.setWidth("*");
		stateText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.state());
		// stateText.setWidth("*");
		zipText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.postalCode());
		// zipText.setWidth("*");
		phoneText = new IntegerField(FinanceApplication.getFinanceUIConstants()
				.phone());
		// phoneText.setWidth("*");

		faxText = new TextItem(FinanceApplication.getFinanceUIConstants().fax());
		// faxText.setWidth("*");

		websiteText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.website());
		// websiteText.setWidth("*");

		userForm = new DynamicForm();
		// userForm.setSize("100%", "*");

		// userForm.setMargin(19);
		// userForm.setWrapItemTitles(false);
		userForm.setNumCols(4);
		userForm.setFields(fullName, emailText, pwdText, street1Text,
				street2Text, cityText, stateText, zipText, phoneText, faxText,
				websiteText);

		Button createUser = new Button(FinanceApplication
				.getFinanceUIConstants().createUser());
		// createUser.setLayoutAlign(Alignment.LEFT);
		Button getUser = new Button(FinanceApplication.getFinanceUIConstants()
				.getUser());
		// getUser.setLayoutAlign(Alignment.RIGHT);
		// createUser.setAlign(Alignment.LEFT);

		Button canButt = new Button(FinanceApplication.getFinanceUIConstants()
				.cancel());
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
				removeFromParent();
			}
		});

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setMembersMargin(290);
		// buttHLay.setSize("50%", "*");
		buttHLay.add(canButt);
		buttHLay.add(createUser);
		// buttHLay.setLayoutTopMargin(30);
		// buttHLay.setAutoHeight();
		// buttHLay.setAlign(Alignment.CENTER);

		VerticalPanel vLay = new VerticalPanel();
		// vLay.setMargin(20);
		vLay.setSize("100%", "100%");
		vLay.add(userForm);
		vLay.add(buttHLay);

		vLay.setTitle(FinanceApplication.getFinanceUIConstants().userDialog());
		vLay.addStyleName("User Dialog");

		// vLay.setTop(50);
		setSize("600", "200");
		// setAutoSize(true);
		add(vLay);
		show();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveFailed(Throwable exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
