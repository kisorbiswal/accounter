package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
import com.vimukti.accounter.web.client.data.ClientIdentity;
import com.vimukti.accounter.web.client.externalization.ActionsConstants;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.externalization.FinanceMessages;
import com.vimukti.accounter.web.client.images.FinanceImages;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.services.UserManagmentService;
import com.vimukti.accounter.web.client.services.UserManagmentServiceAsync;
import com.vimukti.accounter.web.client.theme.ThemeImages;
import com.vimukti.accounter.web.client.ui.banking.BankingMessages;
import com.vimukti.accounter.web.client.ui.combo.AccounterComboConstants;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
import com.vimukti.accounter.web.client.ui.fixedassets.FixedAssetConstants;
import com.vimukti.accounter.web.client.ui.reports.ReportsMessages;
import com.vimukti.accounter.web.client.ui.settings.SettingsMessages;
import com.vimukti.accounter.web.client.ui.vat.VATMessages;
import com.vimukti.accounter.web.client.ui.vendors.VendorsMessages;

/**
 * 
 * @author kumar kasimala
 * 
 */
public class FinanceApplication extends VerticalPanel {

	private MainFinanceWindow mainWindow;
	private static ClientFinanceDate startDate;
	private static ClientFinanceDate endDate;
	protected Widget loadingDialog;
	protected ValueCallBack<FinanceApplication> callback;

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
	private static UserManagmentServiceAsync userService;
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
	public static ClientIdentity clientIdentity;
	private static ThemeImages themeImages;

	public FinanceApplication(String email,
			ValueCallBack<FinanceApplication> callback) {
		this.callback = callback;
		MainFinanceWindow.makeAllViewsStaticstoNull();

		getCompany("");
	}

	public FinanceApplication(String email, ClientIdentity identity,
			ValueCallBack<FinanceApplication> callback) {
		setClientIdentity(identity);
		this.callback = callback;
		MainFinanceWindow.makeAllViewsStaticstoNull();

		getCompany("");
	}

	public FinanceApplication(final boolean isSales,
			final ValueCallBack<FinanceApplication> callback) {
		this.callback = callback;
		MainFinanceWindow.makeAllViewsStaticstoNull();
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(FinanceApplication.GET_SERVICE_ENTRY_POINT);

		final AsyncCallback<ClientCompany> getCompanyCallback = new AsyncCallback<ClientCompany>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
				loadingDialog.removeFromParent();
			}

			public void onSuccess(ClientCompany company) {
				if (company != null) {
					// We got the company, set it for all further references.
					FinanceApplication.setCompany(company);
					// Close the startup dialog...
					loadingDialog.removeFromParent();
					// and, now we are ready to start the application.
					initGUI();
					add(mainWindow = new MainFinanceWindow(isSales));
					// add(FinanceApplication.this);
					if (callback != null) {
						callback.execute(FinanceApplication.this);
					}

				} else {
					// //UIUtils.log("Company: null!");
				}
			}

		};
		getService.getCompany(getClientIdentity().getId(), getCompanyCallback);
		// this.hide();
		if (isSales)
			loadingDialog = UIUtils.getLoadingDialog(FinanceApplication
					.getFinanceUIConstants().loadingSalesPleaseWait());

		else
			loadingDialog = UIUtils.getLoadingDialog(FinanceApplication
					.getFinanceUIConstants().loadingPurchasePleaseWait());

	}

	public FinanceApplication() {
		MainFinanceWindow.makeAllViewsStaticstoNull();
		initGUI();
		add(mainWindow = new MainFinanceWindow(null));
	}

	@SuppressWarnings("unused")
	private void getUserByEmail(String mail) {
		final AsyncCallback<ClientUser> getUserCallBack = new AsyncCallback<ClientUser>() {
			public void onFailure(Throwable caught) {
				// //UIUtils.log(caught.toString());
			}

			public void onSuccess(ClientUser user) {
				if (user != null) {
					FinanceApplication.setUser(user);
					getCompany("DefBiz");
				} else {
					// //UIUtils.log("Get User Came But Failed!");
				}
			}

		};
		FinanceApplication.createGETService().getObjectById(
				AccounterCoreType.EMAIL, mail, getUserCallBack);
	}

	public void getCompany(String name) {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(FinanceApplication.GET_SERVICE_ENTRY_POINT);

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
					FinanceApplication.setCompany(company);
					FinanceApplication.setUser(company
							.getUser(FinanceApplication.getClientIdentity()
									.getId()));
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
							add(mainWindow = new MainFinanceWindow(null) {
								public void onLoad() {
									super.onLoad();
									loadingDialog.removeFromParent();
								};
							});
							if (callback != null) {
								callback.execute(FinanceApplication.this);
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
		getService.getCompany(getClientIdentity().getId(), getCompanyCallback);
		// this.hide();
		if (!GWT.isScript())
			loadingDialog = UIUtils.getLoadingDialog(FinanceApplication
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
					.setServiceEntryPoint(FinanceApplication.CRUD_SERVICE_ENTRY_POINT);
		}

		return crudService;

	}

	public static UserManagmentServiceAsync creatUserService() {
		if (userService == null) {
			userService = (UserManagmentServiceAsync) GWT
					.create(UserManagmentService.class);
			((ServiceDefTarget) userService)
					.setServiceEntryPoint(FinanceApplication.USER_MANAGEMENT_ENTRY_POINT);
		}

		return userService;

	}

	public static IAccounterGETServiceAsync createGETService() {
		if (getService == null) {
			getService = (IAccounterGETServiceAsync) GWT
					.create(IAccounterGETService.class);
			((ServiceDefTarget) getService)
					.setServiceEntryPoint(FinanceApplication.GET_SERVICE_ENTRY_POINT);
		}
		return getService;
	}

	public static IAccounterHomeViewServiceAsync createHomeService() {
		if (homeViewService == null) {
			homeViewService = (IAccounterHomeViewServiceAsync) GWT
					.create(IAccounterHomeViewService.class);
			((ServiceDefTarget) homeViewService)
					.setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
		}
		return homeViewService;
	}

	public static IAccounterReportServiceAsync createReportService() {
		if (reportService == null) {
			reportService = (IAccounterReportServiceAsync) GWT
					.create(IAccounterReportService.class);
			((ServiceDefTarget) reportService)
					.setServiceEntryPoint(FinanceApplication.REPORT_SERVICE_ENTRY_POINT);
		}
		return reportService;
	}

	public static ClientFinanceDate getStartDate() {
		return startDate;
	}

	public static ClientFinanceDate getEndDate() {
		return endDate;
	}

	public static String getAppName() {
		return FinanceApplication.getCompanyMessages().accounter();
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

	public static boolean isSales() {
		return isSales;
	}

	public static boolean isPurchases() {
		return isPurchases;
	}

	public static void setPurchases(boolean isPurchases) {
		FinanceApplication.isPurchases = isPurchases;
	}

	public static void setSales(boolean isSales) {
		FinanceApplication.isSales = isSales;
	}

	public static void makeAllStaticInstancesNull() {
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

	public static void setClientIdentity(ClientIdentity clientIdentity) {
		FinanceApplication.clientIdentity = clientIdentity;
	}

	public static ClientIdentity getClientIdentity() {
		return clientIdentity;
	}
}
