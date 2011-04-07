package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.data.ClientIdentity;
import com.vimukti.accounter.web.client.services.IdentityService;
import com.vimukti.accounter.web.client.services.IdentityServiceAsync;
import com.vimukti.accounter.web.client.ui.company.CompanySetupDialog;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

public class StartupDialog extends DialogBox {

	private EmailField userEmailText;
	private TextItem userPassText;
	private DialogGrid companyGrid;
	DynamicForm form;
	private ValueCallBack<FinanceApplication> defbizcallback;
	// protected BaseWidget loadingDialog;
	public static StartupDialog startUp;

	public StartupDialog() {
		createControls();
	}

	public StartupDialog(ValueCallBack<FinanceApplication> callback) {
		super();
		this.defbizcallback = callback;
		// getUserByEmail(email);
		initCompany();
	}

	private void initCompany() {

	}

	private void createControls() {
		setText(FinanceApplication.getFinanceUIConstants().logIn());

		userEmailText = new EmailField(FinanceApplication
				.getFinanceUIConstants().email());
		userEmailText.setRequired(true);
		// userEmailText.setWidth("*");
		userEmailText.setValue(FinanceApplication.getCompanyMessages()
				.admindefbizcom());

		userPassText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.password());
		userPassText.setRequired(true);
		// userPassText.setWidth("*");
		userPassText.setValue(FinanceApplication.getCompanyMessages().defbiz());
		// usrPassText.sett

		form = new DynamicForm();
		form.setFields(userEmailText, userPassText);

		Button createButt = UIUtils.Button(FinanceApplication
				.getCompanyMessages().createUser(), "U");
		// createButt.setAutoFit(true);

		Button loginButt = UIUtils.Button(FinanceApplication
				.getCompanyMessages().login(), "L");
		// loginButt.setAutoFit(true);

		Button createCompButt = UIUtils.Button(FinanceApplication
				.getCompanyMessages().createCompany(), "C");
		// createCompButt.setWidth("*");
		// createCompButt.setAutoFit(true);
		// createCompButt.setAlign(Alignment.CENTER);

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setLayoutMargin(20);
		// buttHLay.setAutoHeight();
		// buttHLay.setAlign(Alignment.RIGHT);
		// buttHLay.setMembersMargin(20);
		buttHLay.add(loginButt);
		buttHLay.add(createButt);

		createButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// if (form.validate()) {
				createUser();
				// }
			}
		});

		loginButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (validateForm()) {
					checkUserLogin();
					// getUser();
				}
			}
		});

		createCompButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (FinanceApplication.getUser() == null) {
					UIUtils.say("Please login or create a user first!");
				} else {
					new CompanySetupDialog(null);
				}
			}
		});

		companyGrid = new DialogGrid(false);
		// companyGrid.hide();
		companyGrid.addColumns(new String[] {
				FinanceApplication.getCustomersMessages().Id(),
				FinanceApplication.getCustomersMessages().name(),
				FinanceApplication.getCustomersMessages().legalName() });

		Button closeButt = new Button(FinanceApplication.getCustomersMessages()
				.close());
		// closeButt.setLayoutAlign(Alignment.RIGHT);

		closeButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// cancelClick();
			}
		});

		companyGrid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientCompany>() {

					@Override
					public void OnCellDoubleClick(ClientCompany core, int column) {
						ClientCompany company = core;
						String name = company.getName();

						getCompany(name);

					}
				});
		companyGrid.init();

		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setMargin(20);
		// mainVLay.setSize("100%", "100%");
		mainVLay.add(form);
		mainVLay.add(buttHLay);
		mainVLay.add(createCompButt);
		mainVLay.add(companyGrid);

		setSize("450", "450");
		add(mainVLay);
		center();
		show();
		checkUserLogin();
	}

	public void getCompany(String name) {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(FinanceApplication.GET_SERVICE_ENTRY_POINT);

		final AsyncCallback<ClientCompany> getCompanyCallback = new AsyncCallback<ClientCompany>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
				// loadingDialog.destroy();
				StartupDialog.this.show();
			}

			public void onSuccess(ClientCompany company) {
				if (company != null) {
					// We got the company, set it for all further references.
					FinanceApplication.setCompany(company);
					// Close the startup dialog...
					// destroy();
					StartupDialog.this.removeFromParent();
					// loadingDialog.destroy();
					// and, now we are ready to start the application.

					FinanceApplication financeApplication = new FinanceApplication();
					if (defbizcallback != null)
						defbizcallback.execute(financeApplication);
					add(financeApplication);
					// else

				} else {
					UIUtils.say("Company: null!");
				}
			}

		};
		getService.getCompany(null, getCompanyCallback);

		this.hide();
		// loadingDialog = UIUtils
		// .getLoadingMessageDialog("Loading Data <br>&nbsp&nbsp Please Wait...");

	}

	private boolean validateForm() {
		if (!form.validate())
			return false;
		if (!UIUtils.isValidEmail(userEmailText.getValue().toString())) {
			UIUtils.say("Invalid email!");
			return false;
		}
		return true;
	}

	private void checkUserLogin() {

		final AsyncCallback<ClientIdentity> checkLoginCallback = new AsyncCallback<ClientIdentity>() {
			public void onFailure(Throwable caught) {
				UIUtils.say("Could not authenticate!");
			}

			public void onSuccess(ClientIdentity result) {
				if (result != null) {
					FinanceApplication application = new FinanceApplication("",result,
							new ValueCallBack<FinanceApplication>() {

								@Override
								public void execute(FinanceApplication value) {
									RootPanel.get().add(value);
									StartupDialog.this.removeFromParent();
								}
							});
				} else {
					
					
					UIUtils.say("Login failed!");
				}
			}

		};
		IdentityServiceAsync service = GWT.create(IdentityService.class);
		((ServiceDefTarget) service)
				.setServiceEntryPoint("/do/bizantra/identity");
		
		service.getUserIdentity(userEmailText.getValue().toString(),
				userPassText.getValue().toString(), false, 11,
				checkLoginCallback);
		


	}

	private void createUser() {
		new CreateUserDialog(null);
	}

	private void getUser() {
		@SuppressWarnings("unused")
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				Accounter.showError("Unable to login");
			}

			public void onSuccess(ClientUser user) {
				if (user != null) {
					FinanceApplication.setUser(user);
					// getCompanyList();

					FinanceApplication.setCompany(user.getClientCompany());
					// Close the startup dialog...
					StartupDialog.this.removeFromParent();
					// loadingDialog.destroy();
					// and, now we are ready to start the application.

					FinanceApplication financeApplication = new FinanceApplication();
					if (defbizcallback != null)
						defbizcallback.execute(financeApplication);
					// FIXME
					// financeApplication.draw();
					// else

					// destroy();
					// new CompanySetupDialog();
				} else {
					UIUtils.say("Get User Came But Failed!");
				}
			}

		};
		// FinanceApplication.createGETService().getUser(
		// userEmailText.getValue().toString(), getUserCallBack);
	}

	@SuppressWarnings("unused")
	private void getUserByEmail(String mail) {
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				UIUtils.say(caught.toString());
			}

			public void onSuccess(ClientUser user) {
				if (user != null) {
					FinanceApplication.setUser(user);
					getCompany();
				} else {
					UIUtils.say("Get User Came But Failed!");
				}
			}

		};
		// FinanceApplication.createGETService().getUser(mail, getUserCallBack);
	}

	@SuppressWarnings("unused")
	private void getCompanyList() {
		AsyncCallback<List<ClientCompany>> getCompanyListCallback = new AsyncCallback<List<ClientCompany>>() {

			public void onFailure(Throwable caught) {
				// //UIUtils.log("Get Company List Failed!");
			}

			public void onSuccess(List<ClientCompany> result) {
				if (result != null) {
					fillGrid(result);
					getCompany(result.get(0).getName());
					// companyGrid.show();
				} else {
					UIUtils.say("Result: null!");
				}
			}
		};

		// FinanceApplication.createGETService().getCompanies(
		// getCompanyListCallback);
	}

	private void getCompany() {
		@SuppressWarnings("unused")
		AsyncCallback<List<ClientCompany>> getCompanyListCallback = new AsyncCallback<List<ClientCompany>>() {

			public void onFailure(Throwable caught) {
				UIUtils.say("Get Company List Failed!");
			}

			public void onSuccess(List<ClientCompany> result) {
				if (result != null) {
					getCompany(result.get(0).getName());
				} else {
					UIUtils.say("Result: null!");
				}
			}
		};
		// FinanceApplication.createGETService().getCompanies(
		// getCompanyListCallback);
	}

	@SuppressWarnings("unchecked")
	private void fillGrid(List<ClientCompany> result) {
		// ListGridRecord[] records = new ListGridRecord[result.size()];
		// ClientCompany c;
		// for (int recordIndex = 0; recordIndex < records.length;
		// ++recordIndex) {
		// c = result.get(recordIndex);
		// records[recordIndex] = new ListGridRecord();
		// records[recordIndex].setAttribute("comp_id", c.getStringID() + "");
		// records[recordIndex].setAttribute("name", c.getName());
		// records[recordIndex].setAttribute("legal_name", c.getLegalName());
		// }
		List<IsSerializable> list = (ArrayList) result;
		companyGrid.setRecords(list);
	}
}
