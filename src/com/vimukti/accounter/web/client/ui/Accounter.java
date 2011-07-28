package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
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
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.images.FinanceImages;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.theme.ThemeImages;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterExecute;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

/**
 * 
 * 
 */
public class Accounter implements EntryPoint {

	private MainFinanceWindow mainWindow;
	protected Widget loadingDialog;
	protected ValueCallBack<Accounter> callback;
	private ClientFinanceDate endDate;

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

	private static AccounterMessages messages;
	private static AccounterConstants constants;
	private static FinanceImages financeImages;
	private static FinanceMenuImages financeMenuImages;

	private static ThemeImages themeImages;
	private static ClientFinanceDate startDate;


	


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

					// and, now we are ready to start the application.
					initGUI();

				} else {
					// //UIUtils.log("Company: null!");
				}
			}

		};
		getService.getCompany(getCompanyCallback);
		// this.hide();
		if (!GWT.isScript())
			loadingDialog = UIUtils.getLoadingDialog(constants().loadingFinancePleaseWait());

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
		//TODO remove the loading screen
		this.mainWindow=new MainFinanceWindow();
		RootPanel.get("accounter").add(this.mainWindow);
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
			messages = (AccounterMessages) GWT
					.create(AccounterMessages.class);
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

	}

	public String getUserDisplayName() {
		return Accounter.getCompany().getDisplayName();
	}

	public String getCompanyName() {
		return Accounter.getCompany().getName();
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
	private static EventBus eventBus;

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
				Window.Location.assign("/site/login");
			}
		});
		vPanel.add(loginBtn);
		loginBtn.enabledButton();
		loginBtn.getElement().getParentElement().addClassName("expiredButton");
		expireDialog.add(vPanel);
		expireDialog.center();
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

}
