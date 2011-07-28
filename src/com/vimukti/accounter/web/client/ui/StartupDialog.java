package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.company.CompanySetupDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
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
	private ValueCallBack<Accounter> defbizcallback;
	// protected BaseWidget loadingDialog;
	public static StartupDialog startUp;

	public StartupDialog() {
		createControls();
	}

	public StartupDialog(ValueCallBack<Accounter> callback) {
		super();
		this.defbizcallback = callback;
		// getUserByEmail(email);
		initCompany();
	}

	private void initCompany() {

	}

	private void createControls() {
		setText(Accounter.constants().logIn());

		userEmailText = new EmailField(Accounter.constants()
				.email());
		userEmailText.setRequired(true);
		// userEmailText.setWidth("*");
		userEmailText.setValue("admin@accounter.com");

		userPassText = new TextItem(Accounter.constants()
				.password());
		userPassText.setRequired(true);
		// userPassText.setWidth("*");
		userPassText.setValue(Accounter.constants().defbiz());
		// usrPassText.sett

		form = new DynamicForm();
		form.setFields(userEmailText, userPassText);

		AccounterButton createButt = UIUtils.AccounterButton(Accounter
				.constants().createUser(), "U");
		// createButt.setAutoFit(true);

		AccounterButton loginButt = UIUtils.AccounterButton(Accounter
				.constants().login(), "L");
		// loginButt.setAutoFit(true);

		AccounterButton createCompButt = UIUtils.AccounterButton(Accounter
				.constants().createCompany(), "C");
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
		loginButt.enabledButton();
		createButt.enabledButton();
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
				if (Accounter.getUser() == null) {
					UIUtils.say("Please login or create a user first!");
				} else {
					new CompanySetupDialog(null);
				}
			}
		});

		companyGrid = new DialogGrid(false);
		// companyGrid.hide();
		companyGrid.addColumns(new String[] {
				Accounter.constants().Id(),
				Accounter.constants().name(),
				Accounter.constants().legalName() });

		AccounterButton closeButt = new AccounterButton(Accounter
				.constants().close());
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
		createCompButt.enabledButton();
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
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);

		final AsyncCallback<ClientCompany> getCompanyCallback = new AsyncCallback<ClientCompany>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
				// loadingDialog.destroy();
				StartupDialog.this.show();
			}

			public void onSuccess(ClientCompany company) {
				if (company != null) {
					// We got the company, set it for all further references.
					Accounter.setCompany(company);
					// Close the startup dialog...
					// destroy();
					StartupDialog.this.removeFromParent();
					// loadingDialog.destroy();
					// and, now we are ready to start the application.

					Accounter financeApplication = new Accounter();
					if (defbizcallback != null)
						defbizcallback.execute(financeApplication);
					add(financeApplication);
					// else

				} else {
					UIUtils.say("Company: null!");
				}
			}

		};
		getService.getCompany(getCompanyCallback);

		this.hide();
		// loadingDialog = UIUtils
		// .getLoadingMessageDialog("Loading Data <br>&nbsp&nbsp Please Wait...");

	}

	private boolean validateForm() {
		if (!form.validate(true))
			return false;
		if (!UIUtils.isValidEmail(userEmailText.getValue().toString())) {
			UIUtils.say("Invalid email!");
			return false;
		}
		return true;
	}

	private void checkUserLogin() {

		final AsyncCallback<ClientUser> checkLoginCallback = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				UIUtils.say("Could not authenticate!");
			}

			public void onSuccess(ClientUser result) {
				if (result != null) {
					Accounter application = new Accounter("", result,
							new ValueCallBack<Accounter>() {

								@Override
								public void execute(Accounter value) {
									StartupDialog.this.removeFromParent();
									RootPanel.get().add(value);

								}
							});
				} else {

					UIUtils.say("Login failed!");
				}
			}

		};
		IAccounterGETServiceAsync service = GWT
				.create(IAccounterGETService.class);

		((ServiceDefTarget) service)
				.setServiceEntryPoint("/do/bizantra/identity");

		service.getUser(userEmailText.getValue().toString(), userPassText
				.getValue().toString(), false, 11, checkLoginCallback);

	}

	private void createUser() {
		new CreateUserDialog(null);
	}

	// private void getUser() {
	// final AsyncCallback<ClientUser> getUserCallBack = new
	// AsyncCallback<ClientUser>() {
	// public void onFailure(Throwable caught) {
	// Accounter.showError("Unable to login");
	// }
	//
	// public void onSuccess(ClientUser user) {
	// if (user != null) {
	// Accounter.setUser(user);
	// // getCompanyList();
	//
	// Accounter.setCompany(user.getClientCompany());
	// // Close the startup dialog...
	// StartupDialog.this.removeFromParent();
	// // loadingDialog.destroy();
	// // and, now we are ready to start the application.
	//
	// Accounter financeApplication = new Accounter();
	// if (defbizcallback != null)
	// defbizcallback.execute(financeApplication);
	// XXX NOT USED
	// // financeApplication.draw();
	// // else
	//
	// // destroy();
	// // new CompanySetupDialog();
	// } else {
	// UIUtils.say("Get User Came But Failed!");
	// }
	// }
	//
	// };
	// // FinanceApplication.createGETService().getUser(
	// // userEmailText.getValue().toString(), getUserCallBack);
	// }

	@SuppressWarnings("unused")
	private void getUserByEmail(String mail) {
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				UIUtils.say(caught.toString());
			}

			public void onSuccess(ClientUser user) {
				if (user != null) {
					Accounter.setUser(user);
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

	
	private void fillGrid(List<ClientCompany> result) {
		// ListGridRecord[] records = new ListGridRecord[result.size()];
		// ClientCompany c;
		// for (int recordIndex = 0; recordIndex < records.length;
		// ++recordIndex) {
		// c = result.get(recordIndex);
		// records[recordIndex] = new ListGridRecord();
		// records[recordIndex].setAttribute("comp_id", c.getID() + "");
		// records[recordIndex].setAttribute("name", c.getName());
		// records[recordIndex].setAttribute("legal_name", c.getLegalName());
		// }
		List<IsSerializable> list = (ArrayList) result;
		companyGrid.setRecords(list);
	}
}
