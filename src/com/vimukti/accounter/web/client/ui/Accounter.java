package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewService;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.externalization.ActionsConstants;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.externalization.FinanceMessages;
import com.vimukti.accounter.web.client.images.FinanceImages;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.theme.ThemeImages;
import com.vimukti.accounter.web.client.ui.banking.BankingMessages;
import com.vimukti.accounter.web.client.ui.combo.AccounterComboConstants;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterExecute;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
import com.vimukti.accounter.web.client.ui.fixedassets.FixedAssetConstants;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.reports.ReportsMessages;
import com.vimukti.accounter.web.client.ui.settings.SettingsMessages;
import com.vimukti.accounter.web.client.ui.vat.VATMessages;
import com.vimukti.accounter.web.client.ui.vendors.VendorsMessages;

/**
 * 
 * @author kumar kasimala
 * 
 */
public class Accounter extends VerticalPanel implements EntryPoint {

	private MainFinanceWindow mainWindow;
	private static ClientFinanceDate startDate;
	private static ClientFinanceDate endDate;
	protected Widget loadingDialog;
	protected ValueCallBack<Accounter> callback;
	private VerticalPanel mainPanel;
	private HorizontalPanel helpPanel, corners;
	private HTML vimukti, feedback, leftcorner, midrepeat, rightcorner,
			footerleft, footerright;

	private static ClientUser user = null;
	private static ClientCompany company = null;
	public final static String CRUD_SERVICE_ENTRY_POINT = "/do/accounter/crud/rpc/service";
	public final static String GET_SERVICE_ENTRY_POINT = "/do/accounter/get/rpc/service";
	public final static String HOME_SERVICE_ENTRY_POINT = "/do/accounter/home/rpc/service";
	public final static String REPORT_SERVICE_ENTRY_POINT = "/do/accounter/report/rpc/service";
	public final static String USER_MANAGEMENT_ENTRY_POINT = "/do/accounter/user/rpc/service";

	private static IAccounterCRUDServiceAsync crudService;
	private static IAccounterGETServiceAsync getService;
	private static IAccounterHomeViewServiceAsync homeViewService;
	private static IAccounterReportServiceAsync reportService;

	private static CompanyMessages companyMessages;
	private static FinanceMessages financeMessages;
	private static VendorsMessages vendorsMessages;
	private static CustomersMessages customersMessages;
	private static FinanceConstants financeConstants;
	private static ActionsConstants actionsConstants;
	private static FinanceImages financeImages;
	private static FinanceMenuImages financeMenuImages;
	private static FinanceUIConstants financeUIConstants;
	private static BankingMessages bankingMessages;
	private static VATMessages vatMessages;
	private static SettingsMessages settingsMessages;

	private static AccounterComboConstants accounterComboConstants;
	private static FixedAssetConstants fixedAssetConstants;
	private static ReportsMessages reportsMessages;
	private static boolean isSales;
	private static boolean isPurchases;
	private static ThemeImages themeImages;

	// public FinanceApplication(String email,
	// ValueCallBack<FinanceApplication> callback) {
	// this.callback = callback;
	// MainFinanceWindow.makeAllViewsStaticstoNull();
	//
	// getCompany("");
	// }

	public Accounter(String email, ClientUser user,
			ValueCallBack<Accounter> callback) {
		Accounter.user = user;
		this.callback = callback;
		MainFinanceWindow.makeAllViewsStaticstoNull();

		getCompany("");
	}

	public Accounter(final boolean isSales,
			final ValueCallBack<Accounter> callback) {
		this.callback = callback;
		MainFinanceWindow.makeAllViewsStaticstoNull();
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);

		final AsyncCallback<ClientCompany> getCompanyCallback = new AsyncCallback<ClientCompany>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
				loadingDialog.removeFromParent();
			}

			public void onSuccess(ClientCompany company) {
				if (company != null) {
					// We got the company, set it for all further references.
					Accounter.setCompany(company);
					// Close the startup dialog...
					loadingDialog.removeFromParent();
					// and, now we are ready to start the application.
					initGUI();
					mainPanel = new VerticalPanel();

					mainPanel.add(mainWindow = new MainFinanceWindow(isSales));
					add(mainPanel);
					add(bottomCorners());
					add(helpPanel());
					setWidth("100%");

					// add(FinanceApplication.this);
					if (callback != null) {
						callback.execute(Accounter.this);
					}

				} else {
					// //UIUtils.log("Company: null!");
				}
			}

		};
		getService.getCompany(getClientIdentity().getID(), getCompanyCallback);
		// this.hide();
		if (isSales)
			loadingDialog = UIUtils.getLoadingDialog(Accounter
					.getFinanceUIConstants().loadingSalesPleaseWait());

		else
			loadingDialog = UIUtils.getLoadingDialog(Accounter
					.getFinanceUIConstants().loadingPurchasePleaseWait());

	}

	public HorizontalPanel helpPanel() {
		helpPanel = new HorizontalPanel();
		helpPanel.setStyleName("XTRA_HELP_PANEL");

		footerleft = new HTML();
		footerleft.addStyleName("footer-left");
		feedback = new HTML(
				"<div class='vimukti-name'>Vimukti Technologies Pvt Ltd. All rights reserved</div><div class='feedback-name'>Send your feedback to: <a href=''>support@accounterlive.com</a></div>");
		feedback.addStyleName("feedback-option");
		footerright = new HTML();
		footerright.addStyleName("footer-right");
		helpPanel.add(footerleft);
		helpPanel.add(feedback);
		helpPanel.setCellWidth(feedback, "99%");
		helpPanel.add(footerright);
		return helpPanel;
	}

	public HorizontalPanel bottomCorners() {
		corners = new HorizontalPanel();
		corners.setStyleName("bottom-corners");
		leftcorner = new HTML();
		leftcorner.addStyleName("left-corner");
		midrepeat = new HTML();
		midrepeat.addStyleName("mid-repeat");
		rightcorner = new HTML();
		rightcorner.addStyleName("right-corner");
		corners.add(leftcorner);
		corners.add(midrepeat);
		corners.setCellWidth(midrepeat, "98%");
		corners.add(rightcorner);
		return corners;
	}

	public Accounter() {
		MainFinanceWindow.makeAllViewsStaticstoNull();
		initGUI();
		mainPanel = new VerticalPanel();
		mainPanel.add(mainWindow = new MainFinanceWindow(null));
		add(mainPanel);
		add(bottomCorners());
		add(helpPanel());
		setWidth("100%");
	}

	@SuppressWarnings("unused")
	private void getUserByEmail(String mail) {
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
			}

			public void onSuccess(ClientUser user) {
				if (user != null) {
					Accounter.setUser(user);
					getCompany("DefBiz");
				} else {
					// //UIUtils.log("Get User Came But Failed!");
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.EMAIL,
				mail, getUserCallBack);
	}

	public void getCompany(String name) {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);

		final AsyncCallback<ClientCompany> getCompanyCallback = new AsyncCallback<ClientCompany>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
				loadingDialog.removeFromParent();
				caught.printStackTrace();
			}

			public void onSuccess(ClientCompany company) {
				if (company != null) {
					// We got the company, set it for all further references.
					// company.setAccountingType(ClientCompany.ACCOUNTING_TYPE_US);
					Accounter.setCompany(company);
					Accounter.setUser(company.getUser(Accounter
							.getClientIdentity().getID()));
					// Close the startup dialog...

					// and, now we are ready to start the application.
					initGUI();
					/*
					 * The timer is used here becoz,the rpc's response used for
					 * getting the initialDates in initDates() is coming after
					 * the view is loaded,but while loading the view we need
					 * startdate in dashboard's widgets,so it is giving
					 * nullpointer exception(since dates are not yet initialized
					 * becoz of delay in rpc response
					 */
					Timer timer = new Timer() {
						@Override
						public void run() {
							mainPanel = new VerticalPanel();
							mainPanel.add(mainWindow = new MainFinanceWindow(
									null) {
								public void onLoad() {
									super.onLoad();
									loadingDialog.removeFromParent();
								};
							});
							add(mainPanel);
							add(bottomCorners());
							add(helpPanel());
							setWidth("100%");
							if (callback != null) {
								callback.execute(Accounter.this);
							}
						}
					};
					timer.schedule(200);

					// add(FinanceApplication.this);

				} else {
					// //UIUtils.log("Company: null!");
				}
			}

		};
		getService.getCompany(getClientIdentity().getID(), getCompanyCallback);
		// this.hide();
		if (!GWT.isScript())
			loadingDialog = UIUtils.getLoadingDialog(Accounter
					.getFinanceUIConstants().loadingFinancePleaseWait());

	}

	public static ClientUser getUser() {
		return user;
	}

	public static ClientCompany getCompany() {
		return company;
	}

	public static void setUser(ClientUser u) {
		user = u;
	}

	public static void setCompany(ClientCompany c) {
		company = c;
	}

	private void initGUI() {
		initDates();
	}

	private void initDates() {
		createReportService().getMinimumAndMaximumTransactionDate(
				new AsyncCallback<List<ClientFinanceDate>>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(List<ClientFinanceDate> result) {
						if (result != null) {
							startDate = result.get(0) == null ? new ClientFinanceDate()
									: result.get(0);
							endDate = result.get(1) == null ? new ClientFinanceDate()
									: result.get(1);
							if (WidgetCreator.getDebitorsWidget() != null)
								WidgetCreator.getDebitorsWidget()
										.refreshClicked();
						}
					}

				});

	}

	public MainFinanceWindow getBackgroundCanvas() {
		return mainWindow;
	}

	public static IAccounterCRUDServiceAsync createCRUDService() {
		if (crudService == null) {
			crudService = (IAccounterCRUDServiceAsync) GWT
					.create(IAccounterCRUDService.class);
			((ServiceDefTarget) crudService)
					.setServiceEntryPoint(Accounter.CRUD_SERVICE_ENTRY_POINT);
		}

		return crudService;

	}

	public static IAccounterGETServiceAsync createGETService() {
		if (getService == null) {
			getService = (IAccounterGETServiceAsync) GWT
					.create(IAccounterGETService.class);
			((ServiceDefTarget) getService)
					.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);
		}
		return getService;
	}

	public static IAccounterHomeViewServiceAsync createHomeService() {
		if (homeViewService == null) {
			homeViewService = (IAccounterHomeViewServiceAsync) GWT
					.create(IAccounterHomeViewService.class);
			((ServiceDefTarget) homeViewService)
					.setServiceEntryPoint(Accounter.HOME_SERVICE_ENTRY_POINT);
		}
		return homeViewService;
	}

	public static IAccounterReportServiceAsync createReportService() {
		if (reportService == null) {
			reportService = (IAccounterReportServiceAsync) GWT
					.create(IAccounterReportService.class);
			((ServiceDefTarget) reportService)
					.setServiceEntryPoint(Accounter.REPORT_SERVICE_ENTRY_POINT);
		}
		return reportService;
	}

	public static ClientFinanceDate getStartDate() {
		return startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public static String getAppName() {
		return Accounter.getCompanyMessages().accounter();
	}

	public static CompanyMessages getCompanyMessages() {
		if (companyMessages == null) {
			companyMessages = (CompanyMessages) GWT
					.create(CompanyMessages.class);
		}
		return companyMessages;
	}

	public static FinanceMessages getFinanceMessages() {
		if (financeMessages == null) {
			financeMessages = (FinanceMessages) GWT
					.create(FinanceMessages.class);
		}
		return financeMessages;
	}

	public static VendorsMessages getVendorsMessages() {
		if (vendorsMessages == null) {
			vendorsMessages = (VendorsMessages) GWT
					.create(VendorsMessages.class);
		}
		return vendorsMessages;
	}

	public static CustomersMessages getCustomersMessages() {
		if (customersMessages == null) {
			customersMessages = (CustomersMessages) GWT
					.create(CustomersMessages.class);
		}
		return customersMessages;
	}

	public static SettingsMessages getSettingsMessages() {
		if (settingsMessages == null) {
			settingsMessages = (SettingsMessages) GWT
					.create(SettingsMessages.class);
		}
		return settingsMessages;
	}

	public static FixedAssetConstants getFixedAssetConstants() {
		if (fixedAssetConstants == null) {
			fixedAssetConstants = (FixedAssetConstants) GWT
					.create(FixedAssetConstants.class);
		}
		return fixedAssetConstants;
	}

	public static FinanceConstants getFinanceConstants() {
		if (financeConstants == null) {
			financeConstants = (FinanceConstants) GWT
					.create(FinanceConstants.class);
		}
		return financeConstants;
	}

	public static ActionsConstants getActionsConstants() {
		if (actionsConstants == null) {
			actionsConstants = (ActionsConstants) GWT
					.create(ActionsConstants.class);
		}
		return actionsConstants;
	}

	public static FinanceUIConstants getFinanceUIConstants() {
		if (financeUIConstants == null) {
			financeUIConstants = (FinanceUIConstants) GWT
					.create(FinanceUIConstants.class);
		}
		return financeUIConstants;
	}

	public static AccounterComboConstants getAccounterComboConstants() {
		if (accounterComboConstants == null) {
			accounterComboConstants = (AccounterComboConstants) GWT
					.create(AccounterComboConstants.class);
		}
		return accounterComboConstants;

	}

	public static ReportsMessages getReportsMessages() {
		if (reportsMessages == null) {
			reportsMessages = (ReportsMessages) GWT
					.create(ReportsMessages.class);
		}
		return reportsMessages;
	}

	public static VATMessages getVATMessages() {
		if (vatMessages == null) {
			vatMessages = (VATMessages) GWT.create(VATMessages.class);
		}
		return vatMessages;
	}

	public static FinanceImages getFinanceImages() {
		if (financeImages == null) {
			financeImages = (FinanceImages) GWT.create(FinanceImages.class);
		}
		return financeImages;
	}

	public static FinanceMenuImages getFinanceMenuImages() {
		if (financeMenuImages == null) {
			financeMenuImages = (FinanceMenuImages) GWT
					.create(FinanceMenuImages.class);
		}
		return financeMenuImages;
	}

	public static ThemeImages getThemeImages() {
		if (themeImages == null) {
			themeImages = (ThemeImages) GWT.create(ThemeImages.class);
		}
		return themeImages;
	}

	public static BankingMessages getBankingsMessages() {

		if (bankingMessages == null) {
			bankingMessages = (BankingMessages) GWT
					.create(BankingMessages.class);
		}
		return bankingMessages;
	}

	public void makeAllStaticInstancesNull() {
		endDate = null;
		user = null;
		company = null;
		crudService = null;
		getService = null;
		homeViewService = null;
		reportService = null;
		companyMessages = null;
		financeMessages = null;
		vendorsMessages = null;
		customersMessages = null;
		financeConstants = null;
		actionsConstants = null;
		financeImages = null;
		financeMenuImages = null;
		financeUIConstants = null;
		bankingMessages = null;
		vatMessages = null;
		accounterComboConstants = null;
		fixedAssetConstants = null;
		reportsMessages = null;
	}

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub

	}

	public String getUserDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCompanyName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLoggedInFromDomain() {
		// TODO Auto-generated method stub
		return false;
	}

	private static CustomDialog expireDialog;

	public enum AccounterType {
		ERROR, WARNING, WARNINGWITHCANCEL, INFORMATION;
	}

	/**
	 * 
	 * @param mesg
	 *            Default value:"Warning"
	 * @param mesgeType
	 *            Default value:"Warning"
	 * @param dialogType
	 *            Default OK
	 */
	public static void showError(String msg) {
		new AccounterDialog(msg, AccounterType.ERROR);
	}

	public static void showWarning(String mesg, AccounterType typeOfMesg) {

		new AccounterDialog(mesg, typeOfMesg).show();

	}

	public static void showWarning(String msg, AccounterType typeOfMesg,
			ErrorDialogHandler handler) {

		new AccounterDialog(msg, typeOfMesg, handler).show();
	}

	public static void showInformation(String msg) {

		new AccounterDialog(msg, AccounterType.INFORMATION).show();
	}

	public static void stopExecution() {
		if (timerExecution != null)
			timerExecution.stop();
	}

	private static AccounterExecute timerExecution;

	public static void setTimer(AccounterExecute execute) {
		timerExecution = execute;
	}

	public static void showMessage(String message) {
		if (expireDialog != null) {
			expireDialog.removeFromParent();
		}
		expireDialog = new CustomDialog();
		expireDialog.setText("Session Expired");
		VerticalPanel vPanel = new VerticalPanel();
		HTML data = new HTML("<p>" + message + "</p");
		data.getElement().getStyle().setMargin(10, Unit.PX);
		data.getElement().getStyle().setFontSize(14, Unit.PX);
		vPanel.add(data);
		AccounterButton loginBtn = new AccounterButton("Login");
		loginBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Window.Location.assign("/site/login");
			}
		});
		vPanel.add(loginBtn);
		loginBtn.enabledButton();
		loginBtn.getElement().getParentElement().addClassName("expiredButton");
		expireDialog.add(vPanel);
		expireDialog.center();
	}
}
