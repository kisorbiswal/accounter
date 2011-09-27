package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ClientGlobal;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationService;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewService;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterComboMessges;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.images.FinanceImages;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.theme.ThemeImages;
import com.vimukti.accounter.web.client.ui.core.AccounterDialog;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.uibinder.setup.SetupWizard;

/**
 * 
 * 
 */
public class Accounter implements EntryPoint {

	private static MainFinanceWindow mainWindow;
	protected ValueCallBack<Accounter> callback;
	private static ClientFinanceDate endDate;
	private static PlaceController placeController;
	public static SetupWizard setupWizard;
	public static Header header;
	public static SimplePanel vpanel;

	private static ClientUser user = null;
	private static ClientCompany company = null;

	public final static String CI_SERVICE_ENTRY_POINT = "/do/accounter/ci/rpc/service";
	public final static String CRUD_SERVICE_ENTRY_POINT = "/do/accounter/crud/rpc/service";
	public final static String GET_SERVICE_ENTRY_POINT = "/do/accounter/get/rpc/service";
	public final static String HOME_SERVICE_ENTRY_POINT = "/do/accounter/home/rpc/service";
	public final static String REPORT_SERVICE_ENTRY_POINT = "/do/accounter/report/rpc/service";
	public final static String USER_MANAGEMENT_ENTRY_POINT = "/do/accounter/user/rpc/service";

	private static IAccounterCRUDServiceAsync crudService;
	private static IAccounterCompanyInitializationServiceAsync cIService;
	private static IAccounterGETServiceAsync getService;
	private static IAccounterHomeViewServiceAsync homeViewService;
	private static IAccounterReportServiceAsync reportService;

	private static AccounterMessages messages;
	private static AccounterConstants constants;
	private static AccounterComboMessges comboMessages;
	private static FinanceImages financeImages;
	private static FinanceMenuImages financeMenuImages;

	private static ThemeImages themeImages;
	private static ClientFinanceDate startDate;
	private static boolean isMacApp;

	public static void loadCompany() {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);

		final AccounterAsyncCallback<ClientCompany> getCompanyCallback = new AccounterAsyncCallback<ClientCompany>() {
			public void onException(AccounterException caught) {
				showError(constants.unableToLoadCompany());
				// //UIUtils.log(caught.toString());
				caught.printStackTrace();
			}

			public void onResultSuccess(ClientCompany company) {
				if (company == null) {

				}

				// We got the company, set it for all further references.
				// company.setAccountingType(ClientCompany.ACCOUNTING_TYPE_US);
				Accounter.setCompany(company);

				Accounter.setUser(company.getLoggedInUser());
				startDate = company.getTransactionStartDate();
				endDate = company.getTransactionStartDate();

				// and, now we are ready to start the application.
				removeLoadingImage();

				if (!company.isConfigured()) {
					header = new Header();
					vpanel = new SimplePanel();
					vpanel.addStyleName("empty_menu_bar");
					setupWizard = new SetupWizard(new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								RootPanel.get("mainWindow").remove(setupWizard);
								RootPanel.get("mainWindow").remove(header);
								RootPanel.get("mainWindow").remove(vpanel);
								loadCompany();
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Accounter.showError("Accounter Loading Failed");
						}
					});
					RootPanel.get("mainWindow").add(header);
					RootPanel.get("mainWindow").add(vpanel);
					RootPanel.get("mainWindow").add(setupWizard);
				} else {
					initGUI();
				}

			}

		};
		getService.getCompany(getCompanyCallback);

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

	private static void initGUI() {
		mainWindow = new MainFinanceWindow();
		RootPanel.get("mainWindow").add(mainWindow);
	}

	public static void reset() {
		HorizontalMenuBar menubar = new HorizontalMenuBar();
		if (!isMacApp()) {
			mainWindow.remove(1);
			mainWindow.insert(menubar, 1);
		}else{
			reloadMacMenu();
		}
	}

	private native static void reloadMacMenu() /*-{
	  $wnd.MacReload();
	}-*/;
	
	public static MainFinanceWindow getMainFinanceWindow()
	{
		return mainWindow;
	}

	public static IAccounterCompanyInitializationServiceAsync createCompanyInitializationService() {
		if (cIService == null) {
			cIService = (IAccounterCompanyInitializationServiceAsync) GWT
					.create(IAccounterCompanyInitializationService.class);
			((ServiceDefTarget) cIService)
					.setServiceEntryPoint(Accounter.CI_SERVICE_ENTRY_POINT);
		}

		return cIService;

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
		return Accounter.constants().accounter();
	}

	public static AccounterMessages messages() {
		if (messages == null) {
			messages = (AccounterMessages) GWT.create(AccounterMessages.class);
		}
		return messages;
	}

	public static AccounterConstants constants() {
		if (constants == null) {
			constants = (AccounterConstants) GWT
					.create(AccounterConstants.class);
		}
		return constants;
	}

	public static AccounterComboMessges comboMessages() {
		if (comboMessages == null) {
			comboMessages = (AccounterComboMessges) GWT
					.create(AccounterComboMessges.class);
		}
		return comboMessages;
	}

	public static AccounterConstants getFinanceConstants() {
		if (constants == null) {
			constants = (AccounterConstants) GWT
					.create(AccounterConstants.class);
		}
		return constants;
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

	@Override
	public void onModuleLoad() {

		boolean isAdmin = JNSI.getIsAdmin("isAdmin");
		IGlobal global = GWT.create(ClientGlobal.class);
		Global.set(global);
		if (isAdmin) {
			return;
		}

		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
		initializeIsMacApp();
		loadCompany();
	}

	public String getUserDisplayName() {
		return Accounter.getCompany().getDisplayName();
	}

	public String getCompanyName() {
		return Accounter.getCompany().getName();
	}

	public static boolean isLoggedInFromDomain() {
		// TODO Auto-generated method stub
		return false;
	}

	private static CustomDialog expireDialog;

	public enum AccounterType {
		ERROR, WARNING, WARNINGWITHCANCEL, INFORMATION;
	}

	private static native void removeLoadingImage() /*-{
		var parent = $wnd.document.getElementById('loadingWrapper');
		var footer = $wnd.document.getElementById('mainFooter');
		var feedbackimg = $wnd.document.getElementById('contact');
		feedbackimg.style.visibility = 'visible';
		//var header = $wnd.document.getElementById('mainHeader');
		parent.style.visibility = 'hidden';
		footer.style.visibility = 'visible';
		//header.style.visibility = 'visible';
	}-*/;

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

	private static EventBus eventBus;

	public static void showMessage(String message) {
		if (expireDialog != null) {
			expireDialog.removeFromParent();
		}
		expireDialog = new CustomDialog();
		expireDialog.setText(message);
		VerticalPanel vPanel = new VerticalPanel();
		HTML data = new HTML("<p>" + message + "</p");
		data.getElement().getStyle().setMargin(10, Unit.PX);
		data.getElement().getStyle().setFontSize(14, Unit.PX);
		vPanel.add(data);
		Button loginBtn = new Button(constants().logIn());
		loginBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign("/main/login");
			}
		});
		vPanel.add(loginBtn);
		loginBtn.setEnabled(true);
		loginBtn.getElement().getParentElement().addClassName("expiredButton");
		expireDialog.add(vPanel);
		expireDialog.center();
	}

	public static EventBus getEventBus() {
		if (eventBus == null) {
			eventBus = new SimpleEventBus();
		}
		return eventBus;
	}

	public static <D extends IAccounterCore> void inviteUser(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> inviteUserCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}
		};
		if (coreObj.getID() == 0) {
			Accounter.createCRUDService().inviteUser((IAccounterCore) coreObj,
					inviteUserCallBack);
		} else {
			Accounter.createCRUDService().updateUser((IAccounterCore) coreObj,
					inviteUserCallBack);
		}
		// } else {
		// Accounter.createCRUDService().updateUser((IAccounterCore) coreObj,
		// transactionCallBack);
		// }
	}

	public static <D extends IAccounterCore> void createOrUpdate(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {
				if (coreObj.getID() != 0) {
					coreObj.setVersion(coreObj.getVersion() + 1);
				}
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}

		};
		if (coreObj.getID() == 0) {
			Accounter.createCRUDService().create((IAccounterCore) coreObj,
					transactionCallBack);
		} else {
			Accounter.createCRUDService().update((IAccounterCore) coreObj,
					transactionCallBack);
		}
	}

	public static <D extends IAccounterCore> void deleteUser(
			final IDeleteCallback source, final D data) {
		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException exception) {
				source.deleteFailed(exception);
			}

			public void onResultSuccess(Boolean result) {
				getCompany().processDeleteObject(data.getObjectType(),
						data.getID());
				source.deleteSuccess(data);
			}

		};
		Accounter.createCRUDService().deleteUser(data,
				Accounter.getUser().getEmail(), transactionCallBack);
	}

	public static <D extends IAccounterCore> void deleteObject(
			final IDeleteCallback source, final D data) {
		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException exception) {
				source.deleteFailed(exception);
			}

			public void onResultSuccess(Boolean result) {
				getCompany().processDeleteObject(data.getObjectType(),
						data.getID());
				source.deleteSuccess(data);
			}

		};
		Accounter.createCRUDService().delete(data.getObjectType(),
				data.getID(), transactionCallBack);
	}

	public static void voidTransaction(final ISaveCallback source,
			final AccounterCoreType coreType, final long transactionsID) {

		// currentrequestedWidget = widget;
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				getCompany().processCommand(exception);
				source.saveFailed(exception);
			}

			@Override
			public void onResultSuccess(Boolean result) {

				if (result) {
					AccounterCommand cmd = new AccounterCommand();
					cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
					cmd.setData(null);
					cmd.setID(transactionsID);
					cmd.setObjectType(coreType);
					getCompany().processUpdateOrCreateObject(cmd);
					// FIXME We may need to pass the actual object or we need
					// the interface to know that we will not give any object
					source.saveSuccess(cmd);
				}

			}
		};
		Accounter.createCRUDService().voidTransaction(coreType, transactionsID,
				callback);
	}

	public static void updateCompany(final ISaveCallback callback,
			final ClientCompany clientCompany) {
		boolean useAccountNumbers = clientCompany.getPreferences()
				.getUseAccountNumbers();
		AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {

				if (caught instanceof AccounterException) {
					callback.saveFailed(caught);
				}
			}

			public void onResultSuccess(Long result) {

				if (result != null) {
					getCompany().processUpdateOrCreateObject(clientCompany);
				}
				callback.saveSuccess(clientCompany);
			}

		};
		Accounter.createCRUDService().updateCompany(clientCompany,
				transactionCallBack);

	}

	public static PlaceController placeController() {
		return placeController;
	}

	public static <D extends IAccounterCore> void updateUser(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}
		};
		Accounter.createCRUDService().updateUser((IAccounterCore) coreObj,
				transactionCallBack);
	}

	private void initializeIsMacApp() {
		String cookie = Cookies.getCookie("Nativeapp");
		if (cookie != null) {
			setMacApp(true);
		}
	}

	public static boolean isMacApp() {
		return isMacApp;
	}

	public void setMacApp(boolean isMacApp) {
		Accounter.isMacApp = isMacApp;
	}

	public static void get1099FormInformation(
			AsyncCallback<ArrayList<Client1099Form>> myCallback, int selected) {

		Accounter.createCRUDService().get1099Vendors(selected, myCallback);

	}

	public static void get1099InformationByVendor(
			AsyncCallback<Client1099Form> myCallback, long vendorId) {
		Accounter.createCRUDService().get1099InformationByVendor(vendorId,
				myCallback);
	}
}
